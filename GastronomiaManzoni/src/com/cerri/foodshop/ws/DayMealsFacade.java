package com.cerri.foodshop.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.cerri.foodshop.data.model.DayMeal;
import com.cerri.foodshop.util.constants.Constants;
import com.cerri.foodshop.util.exceptions.CommunicationException;
import com.cerri.foodshop.util.exceptions.EmptyResultException;

public class DayMealsFacade {

	public static final String TAG = "DayMealsFacade";
	
	/**
	 * Manage calls to day meals web service 
	 * 
	 * @param context
	 * @param lastUpdate
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws EmptyResultException
	 * @throws CommunicationException
	 * @throws JSONException
	 */
	public static List<DayMeal> getDayMeals(Context context, long lastUpdate) 
			throws ClientProtocolException, IOException, EmptyResultException, 
			CommunicationException, JSONException {

		List<DayMeal> dayMeals = new ArrayList<DayMeal>();
		/*
		 * Create HTTP GET request
		 */
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		List<NameValuePair> httpParams = new LinkedList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("date", String.valueOf(lastUpdate)));
		
		String httpUri = Constants.WS_DAY_MEAL_GET_BY_DATE + URLEncodedUtils.format(httpParams, "utf-8");
		HttpGet httpGet = new HttpGet(httpUri);
		
		HttpResponse response = httpClient.execute(httpGet);

		int statusCode = response.getStatusLine().getStatusCode();
		/*
		 * Check HTTP response status
		 */
		if(statusCode == 204) {
			/*
			 * No content: day meals are up to date
			 */
			throw new EmptyResultException();

		} else if (statusCode == 200) {
			
			/*
			 * Convert JSON result to model objects
			 */
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			inputStream.close();
			Log.v(TAG, stringBuilder.toString());
			JSONObject result = new JSONObject(stringBuilder.toString());
			JSONArray dayMealsArray = null;
			/*
			 * Try to cast to array. 
			 * If JSONException is thrown try to cast to JSONObject.
			 * If JSONException is thrown again re-throw it to be handled in the outer block.  
			 */
			try {
				dayMealsArray = result.getJSONArray("meal");
			} catch (JSONException e1) {
				try {
					result = result.getJSONObject("meal");
				} catch (JSONException e2) {
					throw e2;
				}
				dayMealsArray = new JSONArray();
				dayMealsArray.put(result);
			}
//			JSONArray dayMealsArray= new JSONArray(stringBuilder.toString());
			for(int i = 0; i < dayMealsArray.length(); i++) {
				JSONObject dayMealObject = dayMealsArray.getJSONObject(i);
//				DayMeal dayMeal = new DayMeal(dayMealObject.getLong("id_meal"), dayMealObject.getLong("date"), dayMealObject.getString("name"),
//						dayMealObject.getString("caption"), dayMealObject.getString("description"),
//						dayMealObject.getString("pic"), dayMealObject.getString("ingredients"),
//						dayMealObject.getInt("vegan") == 1 ? true : false,
//						dayMealObject.getInt("vegetarian") == 1 ? true : false,
//						dayMealObject.getInt("glutenfree") == 1 ? true : false);
				String categories = dayMealObject.has("categories") ? dayMealObject.getString("categories") : "";
				DayMeal dayMeal = new DayMeal(
						dayMealObject.getLong("idMeal"), dayMealObject.getLong("ts"), dayMealObject.getString("name"),
						dayMealObject.has("caption") ? dayMealObject.getString("caption") : null, 
						dayMealObject.has("description") ? dayMealObject.getString("description") : null,
						dayMealObject.has("pic") ? dayMealObject.getString("pic") : null,
						dayMealObject.has("ingredients") ? dayMealObject.getString("ingredients") : null,
						categories.contains("vegan"), categories.contains("vegetarian"),
						categories.contains("glutenfree"));
				dayMeals.add(dayMeal);
			}
			return dayMeals;

		} else {
			/*
			 * Something went wrong
			 */
			throw new CommunicationException(statusCode);
		}
	}
}
