package com.cerri.foodshop.batch.services;

import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cerri.foodshop.data.db.DayMealDAO;
import com.cerri.foodshop.data.db.DbManager;
import com.cerri.foodshop.data.model.DayMeal;
import com.cerri.foodshop.ui.activities.TabsFragmentActivity;
import com.cerri.foodshop.util.constants.Constants;
import com.cerri.foodshop.util.exceptions.EmptyResultException;
import com.cerri.foodshop.ws.DayMealsFacade;
import com.cerri.foodshop.R;

public class DayMealUpdateService extends IntentService {

	private static final String TAG = "DayMealUpdateService";

	public DayMealUpdateService() {
		super("DayMealUpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.v(TAG, "Starting day meal update process");

		/*
		 * Get connection to the local db
		 */
		DbManager db = new DbManager(this);
		db.open();
		
		try {

			/*
			 * Check if connection is available
			 */
			ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

			if(activeNetwork != null && activeNetwork.isConnected()) {

				Log.v(TAG, "Device is connected to internet");

				/*
				 * Get the latest day meals
				 */
				long lastUpdate = DayMealDAO.findLastUpdate(db);
				/*
				 * Trick to handle first access
				 */
				if(lastUpdate == 0) {
					lastUpdate = 1;
				}
				List<DayMeal> dayMeals = DayMealsFacade.getDayMeals(getApplicationContext(), lastUpdate);
				if(dayMeals != null && dayMeals.size() != 0) {

					/*
					 * Save the new day meals
					 */
//					DayMealDAO.updateDayMeals(db, dayMeals);
					DayMealDAO.deleteAllDayMeals(db);
					for(DayMeal m: dayMeals) {
						DayMealDAO.persist(db, m);
					}

					/*
					 * Display a notification to the user
					 */
					sendDayMealNotification();
				}
			}
			Log.v(TAG, "Device is not connected to internet. Cannot update day meals");

		} catch (EmptyResultException ere) {
			Log.v(TAG, "No new day meals downloaded");
		} catch (Exception e) {
			Log.e(TAG, "An error occurred while updating day meals: " + e.getMessage());
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	private void sendDayMealNotification() {

		Log.v(TAG, "Generating day meal notification");
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

		/*
		 * Set title and text
		 */
		notificationBuilder.setContentTitle("Gastronomia Manzoni");
		notificationBuilder.setContentText("Nuovi piatti del giorno disponibili");

		/*
		 * Set text showed when the notification is viewed
		 */
		notificationBuilder.setTicker("Gastronomia Manzoni: Nuovi piatti del giorno");

		/*
		 * Notification icon
		 */
		notificationBuilder.setSmallIcon(R.drawable.ic_launcher);

		/*
		 * Pending intent launched when the notification is pressed
		 */
		Intent intent = new Intent(this, TabsFragmentActivity.class);
		intent.putExtra(Constants.FROM_NOTIFICATION_DAY_MEAL, true);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		notificationBuilder.setContentIntent(pIntent);

		notificationBuilder.setAutoCancel(true);

		/*
		 * Set sound, light and vibration as default
		 */
		notificationBuilder.setDefaults(Notification.DEFAULT_SOUND
				| Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

		((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(
				Constants.NOTIFICATION_ID_DAY_MEAL, notificationBuilder.build());

		Log.v(TAG, "Day meal notification sent");
	}

}
