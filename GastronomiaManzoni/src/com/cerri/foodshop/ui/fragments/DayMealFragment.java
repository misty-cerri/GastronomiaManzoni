package com.cerri.foodshop.ui.fragments;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.cerri.foodshop.data.db.DayMealDAO;
import com.cerri.foodshop.data.db.DbManager;
import com.cerri.foodshop.data.model.DayMeal;
import com.cerri.foodshop.ui.activities.DayMealDetailActivity;
import com.cerri.foodshop.ui.activities.TabsFragmentActivity;
import com.cerri.foodshop.ui.adapters.DayMealListAdapter;
import com.cerri.foodshop.util.constants.Constants;
import com.cerri.foodshop.util.exceptions.CommunicationException;
import com.cerri.foodshop.util.exceptions.EmptyResultException;
import com.cerri.foodshop.ws.DayMealsFacade;
import com.cerri.foodshop.R;

public class DayMealFragment extends ListFragment {

	public static final String TAG = "DayMealFragment";

	ListView dayMealListView;
	DayMealListAdapter dayMealAdapter;
	DayMealDownloadTask downloadTask;
	DbManager db;
	private int screenWidth;
	private int screenHeight;

	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		/*
		 * Retain fragment across activity recreation
		 */
		setRetainInstance(true);

		/*
		 * Get screen dimension	
		 */
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		screenWidth =  dm.widthPixels;
		screenHeight = dm.heightPixels; 

		/*
		 * Assign the adapter to populate the list view
		 */
		dayMealAdapter = new DayMealListAdapter(this.getActivity(), 
				R.layout.day_meal_layout, new ArrayList<DayMeal>(), screenWidth, screenHeight);
		//		dayMealAdapter = new DayMealAdapter(this.getActivity(), 
		//				R.layout.day_meal_layout, new ArrayList<DayMeal>());

		setListAdapter(dayMealAdapter);

		db = ((TabsFragmentActivity)this.getActivity()).getDbManager();

		Log.v(TAG, "Day Meal fragment created");

		/*
		 * Start the async task that will load the items of the list view
		 */
		downloadTask = new DayMealDownloadTask();
		downloadTask.execute(getActivity().getIntent().getBooleanExtra(
				Constants.FROM_NOTIFICATION_DAY_MEAL,false));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Intent intent = new Intent(getActivity(), DayMealDetailActivity.class);
		intent.putExtra(Constants.INTENT_KEY_DAY_MEAL, dayMealAdapter.getItem(position));
		startActivity(intent);

	}
	
	private ProgressDialog progressDialog;
	
	private void showConnectionDialog() {
		progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.waiting_connection), 
				true, true, new OnCancelListener(){
			public void onCancel(DialogInterface dialog) {
                /*
                 * Check the status, status can be RUNNING, FINISHED and PENDING
                 * It can be only cancelled if it is not in FINISHED state
                 * Finally close the activity 
                 * (and the application, as this dialog is showed only at application start)
                 */
                if (downloadTask != null && downloadTask.getStatus() != AsyncTask.Status.FINISHED){
                	downloadTask.cancel(true);
                	getActivity().finish();
                }
            }
		});
	}

	/**
	 * Asynchronously populate the list view
	 * 
	 * @author cerri
	 */
	private class DayMealDownloadTask extends AsyncTask<Boolean, DayMeal, Void> {

		@Override
		protected void onPreExecute() {
			/*
			 * Before inserting elements, must empty the adapter
			 */
			dayMealAdapter.clear();
			super.onPreExecute();
			/*
			 * Show a progress dialog
			 */
			showConnectionDialog();
		}

		@Override
		protected Void doInBackground(Boolean... params) {

			Log.v(TAG, "Starting day meal background worker");

			if(params[0] == false) {

				Log.v(TAG, "Fragment not opened from a notification. Downloading day meals");

				long lastUpdate = DayMealDAO.findLastUpdate(db);
				/*
				 * Trick to handle first access
				 */
				if(lastUpdate == 0) {
					lastUpdate = 1;
				}

				/*
				 * Check if connection is available
				 */
				ConnectivityManager cm = (ConnectivityManager)getActivity().getApplicationContext().
						getSystemService(Context.CONNECTIVITY_SERVICE);

				NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

				if(activeNetwork != null && activeNetwork.isConnected()) {

					Log.v(TAG, "Device is connected to internet");

					/*
					 * Get day meals. If server call fails warn the user with a toast
					 */
					List<DayMeal> dayMeals;
					try {
						dayMeals = DayMealsFacade.getDayMeals(getActivity(), lastUpdate);

						// DayMealDAO.updateDayMeals(db, dayMeals);

						DayMealDAO.deleteAllDayMeals(db);
						for(DayMeal m: dayMeals) {
							DayMealDAO.persist(db, m);
						}
					} catch (SocketTimeoutException e) {
						Log.e(TAG, getString(R.string.error_day_meal_time_out));
						CharSequence text = getString(R.string.error_day_meal_time_out);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					} catch (EmptyResultException e) {
						Log.i(TAG, getString(R.string.error_no_day_meal_downloaded));
						CharSequence text = getString(R.string.error_no_day_meal_downloaded);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					} catch (ClientProtocolException e) {
						Log.e(TAG, e.getMessage());
						CharSequence text = getString(R.string.error_downloading_day_meals);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
						CharSequence text = getString(R.string.error_downloading_day_meals);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					} catch (CommunicationException e) {
						Log.e(TAG, e.getMessage());
						CharSequence text = getString(R.string.error_downloading_day_meals);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage());
						CharSequence text = getString(R.string.error_downloading_day_meals);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
						CharSequence text = getString(R.string.error_downloading_day_meals);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					}
				} 
				else {
					if(lastUpdate != 0) {
						/*
						 * If device is not connected and database is not empty warn the user with a toast
						 */
						CharSequence text = getString(R.string.not_connected_message);
						Message message = Message.obtain();  
						message.obj = text;  
						mHandler.sendMessage(message);
					}
				}
			}

			/*
			 * Load day meal elements from the database
			 */
			DayMeal[] dayMeals = DayMealDAO.findAllDayMeals(db);

			for (int i = 0; i < dayMeals.length; i++) {
				publishProgress(dayMeals[i]);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(DayMeal... values) {
			/*
			 * Publish progress to the adapter 
			 */
			dayMealAdapter.add(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			Log.v(TAG, "Background task has completed is job");
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
}
