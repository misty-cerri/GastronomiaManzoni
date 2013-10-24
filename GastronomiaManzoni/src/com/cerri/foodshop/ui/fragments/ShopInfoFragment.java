 package com.cerri.foodshop.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cerri.foodshop.R;
import com.cerri.foodshop.ui.adapters.ShopInfoImageAdapter;

public class ShopInfoFragment extends Fragment {

	public static final String TAG = "ShopInfoFragment";
	
	// Hold a reference to the current animator,
    // so that it can be canceled mid-way.
//    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
//    private int mShortAnimationDuration;

//	int downX,upX;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist.  The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed.  Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
//		return (LinearLayout)inflater.inflate(R.layout.shop_info_layout, container, false);
		return (FrameLayout)inflater.inflate(R.layout.shop_info_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Button for gps navigation to the shop
		 */
		Button navigateButton = (Button)getActivity().findViewById(R.id.shop_info_navigate_button);
		navigateButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try {
//					throw new Exception("Test");
					Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
							"google.navigation:ll=" + getString(R.string.geo_position)));
					startActivity(navIntent);
				} catch (Exception e) {
					Log.e(TAG, "Error while launching navigation activity: " + e.getMessage());
					CharSequence text = getString(R.string.error_starting_navigation_intent);
					Message message = Message.obtain();  
					message.obj = text;  
					mHandler.sendMessage(message);
				}
			}
		});

		GridView gridview = (GridView) getActivity().findViewById(R.id.shop_info_gallery_gridview);
		float scale = getResources().getDisplayMetrics().density;
		int sizeInDp;
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = 150;
		try {
			if(Build.VERSION.SDK_INT >= 13) {
				Point size = new Point();
				display.getSize(size);
				width = size.x / 5;
			} else {
				width = display.getWidth() / 5;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		sizeInDp = (int) (width*scale + 0.5f);
	    gridview.setAdapter(new ShopInfoImageAdapter(getActivity(), sizeInDp));
	    gridview.setColumnWidth(sizeInDp);
	    final ImageView expandedImageView = (ImageView) getActivity().findViewById(R.id.shop_info_expanded_image);
	    final FrameLayout expandedImageViewOuter = (FrameLayout) getActivity().findViewById(R.id.shop_info_expanded_image_outer);
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	expandedImageView.setImageDrawable(((ImageView)v).getDrawable());
	        	expandedImageView.setVisibility(View.VISIBLE);
	        	expandedImageViewOuter.setVisibility(View.VISIBLE);
	        }
	    });

	    expandedImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getVisibility() == View.VISIBLE) {
					expandedImageView.setVisibility(View.INVISIBLE);
					expandedImageViewOuter.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	@SuppressLint({ "HandlerLeak" })
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(getActivity(), (CharSequence)msg.obj, duration);
			toast.show();
		}
	};
}
	
//	private void zoomImageFromThumb(final View thumbView, int imageResId) {
//	    // If there's an animation in progress, cancel it
//	    // immediately and proceed with this one.
//	    if (mCurrentAnimator != null) {
//	        mCurrentAnimator.cancel();
//	    }
//
//	    // Load the high-resolution "zoomed-in" image.
//	    final ImageView expandedImageView = (ImageView) getActivity().findViewById(
//	            R.id.shop_info_expanded_image);
//	    expandedImageView.setImageResource(imageResId);
//
//	    // Calculate the starting and ending bounds for the zoomed-in image.
//	    // This step involves lots of math. Yay, math.
//	    final Rect startBounds = new Rect();
//	    final Rect finalBounds = new Rect();
//	    final Point globalOffset = new Point();
//
//	    // The start bounds are the global visible rectangle of the thumbnail,
//	    // and the final bounds are the global visible rectangle of the container
//	    // view. Also set the container view's offset as the origin for the
//	    // bounds, since that's the origin for the positioning animation
//	    // properties (X, Y).
//	    thumbView.getGlobalVisibleRect(startBounds);
////	    getActivity().findViewById(R.id.container)
////	            .getGlobalVisibleRect(finalBounds, globalOffset);
//	    startBounds.offset(-globalOffset.x, -globalOffset.y);
//	    finalBounds.offset(-globalOffset.x, -globalOffset.y);
//
//	    // Adjust the start bounds to be the same aspect ratio as the final
//	    // bounds using the "center crop" technique. This prevents undesirable
//	    // stretching during the animation. Also calculate the start scaling
//	    // factor (the end scaling factor is always 1.0).
//	    float startScale;
//	    if ((float) finalBounds.width() / finalBounds.height()
//	            > (float) startBounds.width() / startBounds.height()) {
//	        // Extend start bounds horizontally
//	        startScale = (float) startBounds.height() / finalBounds.height();
//	        float startWidth = startScale * finalBounds.width();
//	        float deltaWidth = (startWidth - startBounds.width()) / 2;
//	        startBounds.left -= deltaWidth;
//	        startBounds.right += deltaWidth;
//	    } else {
//	        // Extend start bounds vertically
//	        startScale = (float) startBounds.width() / finalBounds.width();
//	        float startHeight = startScale * finalBounds.height();
//	        float deltaHeight = (startHeight - startBounds.height()) / 2;
//	        startBounds.top -= deltaHeight;
//	        startBounds.bottom += deltaHeight;
//	    }
//
//	    // Hide the thumbnail and show the zoomed-in view. When the animation
//	    // begins, it will position the zoomed-in view in the place of the
//	    // thumbnail.
//	    thumbView.setAlpha(0f);
//	    expandedImageView.setVisibility(View.VISIBLE);
//
//	    // Set the pivot point for SCALE_X and SCALE_Y transformations
//	    // to the top-left corner of the zoomed-in view (the default
//	    // is the center of the view).
//	    expandedImageView.setPivotX(0f);
//	    expandedImageView.setPivotY(0f);
//
//	    // Construct and run the parallel animation of the four translation and
//	    // scale properties (X, Y, SCALE_X, and SCALE_Y).
//	    AnimatorSet set = new AnimatorSet();
//	    set
//	            .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
//	                    startBounds.left, finalBounds.left))
//	            .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
//	                    startBounds.top, finalBounds.top))
//	            .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
//	            startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
//	                    View.SCALE_Y, startScale, 1f));
//	    set.setDuration(mShortAnimationDuration);
//	    set.setInterpolator(new DecelerateInterpolator());
//	    set.addListener(new AnimatorListenerAdapter() {
//	        @Override
//	        public void onAnimationEnd(Animator animation) {
//	            mCurrentAnimator = null;
//	        }
//
//	        @Override
//	        public void onAnimationCancel(Animator animation) {
//	            mCurrentAnimator = null;
//	        }
//	    });
//	    set.start();
//	    mCurrentAnimator = set;
//
//	    // Upon clicking the zoomed-in image, it should zoom back down
//	    // to the original bounds and show the thumbnail instead of
//	    // the expanded image.
//	    final float startScaleFinal = startScale;
//	    expandedImageView.setOnClickListener(new View.OnClickListener() {
//	        @Override
//	        public void onClick(View view) {
//	            if (mCurrentAnimator != null) {
//	                mCurrentAnimator.cancel();
//	            }
//
//	            // Animate the four positioning/sizing properties in parallel,
//	            // back to their original values.
//	            AnimatorSet set = new AnimatorSet();
//	            set.play(ObjectAnimator
//	                        .ofFloat(expandedImageView, View.X, startBounds.left))
//	                        .with(ObjectAnimator
//	                                .ofFloat(expandedImageView, 
//	                                        View.Y,startBounds.top))
//	                        .with(ObjectAnimator
//	                                .ofFloat(expandedImageView, 
//	                                        View.SCALE_X, startScaleFinal))
//	                        .with(ObjectAnimator
//	                                .ofFloat(expandedImageView, 
//	                                        View.SCALE_Y, startScaleFinal));
//	            set.setDuration(mShortAnimationDuration);
//	            set.setInterpolator(new DecelerateInterpolator());
//	            set.addListener(new AnimatorListenerAdapter() {
//	                @Override
//	                public void onAnimationEnd(Animator animation) {
//	                    thumbView.setAlpha(1f);
//	                    expandedImageView.setVisibility(View.GONE);
//	                    mCurrentAnimator = null;
//	                }
//
//	                @Override
//	                public void onAnimationCancel(Animator animation) {
//	                    thumbView.setAlpha(1f);
//	                    expandedImageView.setVisibility(View.GONE);
//	                    mCurrentAnimator = null;
//	                }
//	            });
//	            set.start();
//	            mCurrentAnimator = set;
//	        }
//	    });
//	}
//}

/*
 * Set up image switcher
 */
//		btnNext = (Button)getActivity().findViewById(R.id.shop_info_next_image_button);
//		imageSwitcher = (ImageSwitcher)getActivity().findViewById(R.id.shop_info_image_switcher);
//
//		/*
//		 * Set the ViewFactory of the ImageSwitcher that will create ImageView object when asked
//		 */
//		imageSwitcher.setFactory(new ViewFactory() {
//
//			public View makeView() {
//
//				ImageView imageView = new ImageView(getActivity().getApplicationContext());
//				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
//						LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//				return imageView;
//			}
//		});
//
//		/*
//		 * Declare the animations and initialize them
//		 */
//		Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
//		Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
//
//		/*
//		 * Set the animation type to imageSwitcher
//		 */
//		imageSwitcher.setInAnimation(in);
//		imageSwitcher.setOutAnimation(out);
//		imageSwitcher.setImageResource(imageIds[0]);
//
//		/*
//		 * Set up touch listener to slide between images
//		 */
//		
//		imageSwitcher.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View arg0, MotionEvent event) {
//				
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					downX = (int) event.getX(); 
//					Log.i(TAG, "event.getX()" + " downX " + downX);
//					return true;
//				} 
//
//				else if (event.getAction() == MotionEvent.ACTION_UP) {
//					upX = (int) event.getX(); 
//					Log.i(TAG, "event.getX()" + " upX " + upX);
//					if (upX - downX > 10) {
//
//						currentIndex--;
//						if (currentIndex < 0) {
//							currentIndex = imageIds.length-1;
//						}
//
//						imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.slide_in_left));
//						imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.slide_out_right));
//
//						imageSwitcher.setImageResource(imageIds[currentIndex]);
//					} 
//
//					else if (upX - downX < -10) {
//
//						currentIndex++;
//						if (currentIndex > imageIds.length-1) {
//							currentIndex = 0;
//						}
//						
//						imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.slide_in_right));
//						imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.slide_out_left));
//
//						imageSwitcher.setImageResource(imageIds[currentIndex]);
//					}
//					return true;
//				}
//				return false;
//			}
//
//		});

/*
 * ClickListener for NEXT button
 * When clicked on Button ImageSwitcher will switch between Images
 * The current Image will go out and next Image  will come in with specified animation
 */
//		btnNext.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View v) {
//				currentIndex++;
//				/*
//				 *  If index reaches maximum reset it
//				 */
//				if(currentIndex==messageCount)
//					currentIndex=1;
//				imageSwitcher.setImageResource(imageIds[currentIndex]);
//			}
//		});