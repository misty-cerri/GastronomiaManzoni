package com.cerri.foodshop.batch.receivers;

import java.util.Calendar;

import com.cerri.foodshop.util.constants.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Manage the starting of the application alarms 
 * 
 * @author cerri
 *
 */
public class AlarmStarterReceiver extends BroadcastReceiver {

	private static final String TAG = "AlarmStarterReceiver";

	public AlarmStarterReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		/*
		 * Start day meal alarm
		 */
		startDayMealAlarm(context);

		/*
		 * Start promo alarm
		 */

		/*
		 * Start news alarm
		 */

	}

	/**
	 * Start the day meal alarm	
	 * 
	 * @param context
	 */
	public static void startDayMealAlarm(Context context) {

		AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		/*
		 * Start alarm manager two hours from now
		 */
		Log.v(TAG, "Setting up day meal alarm");
		Calendar updateTime = Calendar.getInstance();
		updateTime.set(Calendar.HOUR, updateTime.get(Calendar.HOUR) + 2);
		
		Intent dayMealIntent = new Intent(context, AlarmReceiver.class);
		dayMealIntent.setAction(Constants.ACTION_DAY_MEAL_UPDATE);
		PendingIntent dayMealPendingIntent = PendingIntent.getBroadcast(
				context, 0, dayMealIntent, PendingIntent.FLAG_CANCEL_CURRENT);

//		alarms.setRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(),
//				AlarmManager.INTERVAL_FIFTEEN_MINUTES, dayMealPendingIntent);
	    alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(),
	            2 * AlarmManager.INTERVAL_HOUR, dayMealPendingIntent);
		Log.v(TAG, "Day meal alarm set up");
	}
}
