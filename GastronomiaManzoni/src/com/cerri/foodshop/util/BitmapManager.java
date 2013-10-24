package com.cerri.foodshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

/**
 * Singleton class that manage the download and the caching of bitmaps to be displayed 
 * in multiple instances of ImageView. 
 * Doesn't optimize downloading if there are image views that must display the same url
 * (it may happens that two downloading operation of the same image are started)   
 * 
 * @author cerri
 *
 */
public enum BitmapManager {
	
    INSTANCE;  

    public static final String TAG = "BitmapManager";
    
    /*
	 *  Get max available VM memory, exceeding this amount will throw an OutOfMemory exception. 
	 *  Stored in kilobytes as LruCache takes an int in its constructor.
	 */
	final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	final int cacheSize = maxMemory / 16;

    LruCache<String, Bitmap> mMemoryCache;
    
    private final ExecutorService pool;  
    private Map<ImageView, String> imageViews = 
    		Collections.synchronizedMap(new WeakHashMap<ImageView, String>());  
    private Bitmap placeholder;
    
    /*
     * List of url for which a download operation has been started.
     * Used to avoid multiple downloads of the same images.   
     */
    private List<String> urls = Collections.synchronizedList(new ArrayList<String>());
  
    BitmapManager() {  
    	mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
//    	{
//    		@SuppressLint("NewApi")
//			@Override
//    		protected int sizeOf(String key, Bitmap bitmap) {
//    			/*
//    			 *  The cache size will be measured in kilobytes rather than number of items.
//    			 */
//    			if(android.os.Build.VERSION.SDK_INT < 12) {
//    				/*
//    				 * This is valid as long as RGB_565 configuration is used
//    				 * If changed to ARGB_8888 change to
//    				 * bitmap.getHeight() * bitmap.getWidth() * 4; 
//    				 */
//    				return bitmap.getHeight() * bitmap.getWidth() * 2;
//    			} else {
//    				return bitmap.getByteCount() / 1024;
//    			}
//    		}
//    	};
    	pool = Executors.newFixedThreadPool(5);  
    }  
  
    public void setPlaceholder(Bitmap bmp) {  
        placeholder = bmp;  
    }  

    public Bitmap getBitmapFromCache(String url) {  
    	synchronized (mMemoryCache) {
    		return mMemoryCache.get(url);
    	}
    }
    
    public Bitmap getBitmapOrPlaceholderFromCache(String url) {
    	if(url == null) {
    		return placeholder;
    	}
    	Bitmap b = null;
    	synchronized (mMemoryCache) {
    		if(mMemoryCache.get(url) != null) {
    			b = mMemoryCache.get(url);
    		}
    	}
    	if(b == null) {
			return placeholder;
		}
    	return b;
//		return mMemoryCache.get(url);
    }
  
    @SuppressLint("HandlerLeak")
	public void queueJob(final String url, final ImageView imageView,  
            final int width, final int height) {  
        /* 
         * Create handler in UI thread. 
         */  
        final Handler handler = new Handler() {  
            @Override  
            public void handleMessage(Message msg) {  
                String tag = imageViews.get(imageView);  
                Log.v(TAG, "Tag: " + tag + " - Url: " + url);
                if (tag != null && tag.equals(url)) {  
                    if (msg.obj != null) {  
                        imageView.setImageBitmap((Bitmap) msg.obj);  
                    } else {  
                        imageView.setImageBitmap(placeholder);  
                        Log.e(TAG, "fail " + url);  
                    }  
                }  
            }  
        };  
        if(!urls.contains(url)) {
        	urls.add(url);
        	pool.submit(new Runnable() {  
                @Override  
                public void run() {  
                	Log.v(TAG, "Downloading bitmap: " + url);  
                    final Bitmap bmp = downloadBitmap(url, width, height);  
                    Message message = Message.obtain();  
                    message.obj = bmp;  
                    Log.v(TAG, "Item downloaded: " + url);  
      
                    handler.sendMessage(message);  
                }  
            });
        }
    }  
  
    public void loadBitmap(final String url, final ImageView imageView,  
            final int width, final int height) {  
        imageViews.put(imageView, url);  
        Bitmap bitmap = getBitmapFromCache(url);  
  
        /*
         * Check in UI thread, so no concurrency issues  
         */
        if (bitmap != null) {
            Log.v(TAG, "Item loaded from cache: " + url);  
            imageView.setImageBitmap(bitmap);  
        } else {
            imageView.setImageBitmap(placeholder);  
            queueJob(url, imageView, width, height);  
        }  
    }  
  
    private Bitmap downloadBitmap(String url, int width, int height) {  
        try {
        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url.replaceAll(" ", "%20")).getContent());
            bitmap = Bitmap.createScaledBitmap(bitmap, 
            		width / 2, ((width / 2)*bitmap.getHeight())/bitmap.getWidth(), 
            		true);
            mMemoryCache.put(url, bitmap);
            return bitmap;  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
}  