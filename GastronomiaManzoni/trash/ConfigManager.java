package com.cerri.foodshop.util;

import com.cerri.foodshop.util.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class ConfigManager {

	public static final String TAG = "ConfigManager";
	
	/**
	 * Store all the configuration properties in Shared Preferences
	 * @param context
	 */
	public static void setUpConfiguration(Context context){
		if(getBooleanValue(context, Constants.FIRST_RUN)) {
			Log.v(TAG, "Setting up app configuration");
			setStringValue(context, Constants.WS_DAY_MEAL_GET_BY_DATE, 
					Constants.WS_DAY_MEAL_GET_BY_DATE_VALUE);
			setBooleanValue(context, Constants.FIRST_RUN, false);
		}
	}
	
	private static void setStringValue(Context context, String key, String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putString(key, value);
		prefsEditor.commit();
	}
	
	private static void setBooleanValue(Context context, String key, boolean value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putBoolean(key, value);
		prefsEditor.commit();
	}
	
	public static String getStringValue(Context context, String configKey) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(configKey, "DEFAULT VALUE");
	}
	
	public static Boolean getBooleanValue(Context context, String configKey) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(configKey, true);
	}

}
