 	package com.cerri.foodshop.ui.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerri.foodshop.data.model.DayMeal;
import com.cerri.foodshop.util.BitmapManager;
import com.cerri.foodshop.R;

public class DayMealListAdapter extends ArrayAdapter<DayMeal>{

	private int resource;
	private LayoutInflater inflater;
	private int screenWidth;
	private int screenHeight;
//	private int imgSize;

	public DayMealListAdapter(Context context, int resourceId,
			List<DayMeal> objects, int screenWidth, int screenHeight) {
		super(context, resourceId, objects);
		resource = resourceId;
		inflater = LayoutInflater.from(context);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
//		imgSize = Math.max(screenWidth, screenHeight);
//		imgSize = imgSize/3;
		
		BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(  
                context.getResources(), R.drawable.logo)); 
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		/*
		 * Get item to be returned at that position
		 */
		DayMeal dayMeal = getItem(position);

		ViewHolder holder;

		if (v == null) {
			v = inflater.inflate(resource, parent, false);
			holder = new ViewHolder();
			holder.titleTextView = (TextView) v.findViewById(R.id.dayMealTitle);
			holder.captionTextView = (TextView) v
					.findViewById(R.id.dayMealCaption);
			holder.dayMealImageView = (ImageView) v
					.findViewById(R.id.dayMealImage);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			FileInputStream inputStream;
//			try {
//				inputStream = new FileInputStream(new File(holder.dayMealImageView.getContext().getExternalFilesDir(
//						holder.dayMealImageView.getContext().getString(R.string.images_subfolder)), 
//						dayMeal.getPic()));
//				Bitmap dayMealBitmap = ImageUtil.decodeSampledBitmapFromResourceMemOpt(inputStream, 100, 100);
//				holder.dayMealImageView.setImageBitmap(dayMealBitmap);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
		
		holder.titleTextView.setText(dayMeal.getName());
		holder.captionTextView.setText(dayMeal.getCaption());
		
		/*
		 * Check if connection is available
		 */
		ConnectivityManager cm = (ConnectivityManager)getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(dayMeal.getPic() == null) {
			holder.dayMealImageView.setImageBitmap(
					BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo));
		} else {
			if(activeNetwork != null && activeNetwork.isConnected()) {	
				BitmapManager.INSTANCE.loadBitmap(
						dayMeal.getPic(), holder.dayMealImageView, screenWidth, screenHeight);
			} else {
				holder.dayMealImageView.setImageBitmap(
						BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo));
			}
		}
		return v;
	}

	/**
	 * Used to recycle list items
	 * 
	 * @author cerri
	 *
	 */
	private static class ViewHolder {
		TextView titleTextView;
		TextView captionTextView;
		ImageView dayMealImageView;
	}

}
