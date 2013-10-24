package com.cerri.foodshop.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cerri.foodshop.R;

public class ShopInfoImageAdapter extends BaseAdapter {
	private Context mContext;
	int sizeInDp;

	public ShopInfoImageAdapter(Context c, int sizeInDp) {
		mContext = c;
		this.sizeInDp = sizeInDp;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(sizeInDp, sizeInDp));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(1, 1, 1, 1);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mThumbIds[position]);

		return imageView;
	}

	// references to our images
	private Integer[] mThumbIds = {
			R.drawable.negozio_1, R.drawable.negozio_2,
			R.drawable.negozio_3, R.drawable.negozio_4,
			R.drawable.negozio_5, R.drawable.negozio_6,
			R.drawable.negozio_7, R.drawable.negozio_8,
//			R.drawable.negozio_9, 
			R.drawable.negozio_10, R.drawable.negozio_11
			
	};
}