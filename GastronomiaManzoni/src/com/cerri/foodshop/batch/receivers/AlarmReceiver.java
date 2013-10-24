	package com.cerri.foodshop.batch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cerri.foodshop.batch.services.DayMealUpdateService;
import com.cerri.foodshop.util.constants.Constants;

/**
 * Receive clock alarms and start the proper service 
 * 
 * @author cerri
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "AlarmReceiver";
	
	public AlarmReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(Constants.ACTION_DAY_MEAL_UPDATE.equals(intent.getAction())) {
			
			Log.d(TAG, "Day meal alarm received. Starting day meal update service");
			
			/*
	         * Start the day meal update service
	         */
	        Intent updateService = new Intent(context, DayMealUpdateService.class);
	        context.startService(updateService);
			
		} else if (Constants.ACTION_NEWS_UPDATE.equals(intent.getAction())) {
			
			Log.d(TAG, "News alarm received. Starting news update service");
			
		} else if (Constants.ACTION_PROMO_UPDATE.equals(intent.getAction())) {
			
			Log.d(TAG, "Promo alarm received. Starting promo update service");
			
		} else if (intent.getAction() == null){
			
			Log.d(TAG, "Null action on intent! Should not occurs");
			
		}
	}
}
