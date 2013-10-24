package com.cerri.foodshop.util.constants;

import java.util.ResourceBundle;

public class Constants {

	public static final int HTTP_TIMEOUT = 10000;
	
//	public static final String FIRST_RUN = "FIRST_RUN";
	
//	public static final String WS_DAY_MEAL_GET_BY_DATE = "WS_DAY_MEAL_GET_BY_DATE";
//	public static final String WS_DAY_MEAL_GET_BY_DATE_VALUE = "http://www.aleshoptest.altervista.org/api.php?rquest=dayMeals";
//	public static final String WS_DAY_MEAL_GET_BY_DATE = "http://192.168.43.135:8080/GastronomiaManzoni/rest/public/daymeals/daymeals?";
	public static final String WS_DAY_MEAL_GET_BY_DATE = ResourceBundle.getBundle("foodshop").getString("WS_DAY_MEAL_GET_BY_DATE");
	
	
	public static final String ACTION_DAY_MEAL_UPDATE = "ACTION_DAY_MEAL_UPDATE";
	public static final String ACTION_PROMO_UPDATE = "ACTION_PROMO_UPDATE";
	public static final String ACTION_NEWS_UPDATE = "ACTION_NEWS_UPDATE";
	
	public static final int NOTIFICATION_ID_DAY_MEAL = 1;
	public static final int NOTIFICATION_ID_PROMO = 2;
	public static final int NOTIFICATION_ID_NEWS = 3;

	/*
	 * Intent Extras
	 */
	public static final String FROM_NOTIFICATION_DAY_MEAL = "FROM_NOTIFICATION_DAY_MEAL";
	public static final String INTENT_KEY_DAY_MEAL = "INTENT_KEY_DAY_MEAL";
	

}
