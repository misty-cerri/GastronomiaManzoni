package com.cerri.foodshop.ui.fragments;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cerri.foodshop.util.CustomHttpClient;
import com.cerri.foodshop.R;

public class PersonalAreaFragment extends Fragment {

	public static final String TAG = "PersonalAreaFragment";

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
		return (LinearLayout)inflater.inflate(R.layout.personal_area_layout, container, false);
//		return (LinearLayout)inflater.inflate(R.layout.test_admob_layout, container, false);
	}

	private class Connection extends AsyncTask {
		@Override
		protected Object doInBackground(Object... arg0) {
			connect();
			return null;
		}
	}

	private void connect() {

		// Example send http request
		final String url = "https://cmijvmh.jvmhost.net:10007";
		
		// Instantiate the custom HttpClient
		DefaultHttpClient client = new CustomHttpClient(getActivity().getApplicationContext());
		HttpGet get = new HttpGet(url);
		// Execute the GET call and obtain the response
		Log.v(TAG, url);
		HttpResponse getResponse;
		try {
			getResponse = client.execute(get);
			Log.v(TAG, getResponse.getStatusLine().toString());
			HttpEntity responseEntity = getResponse.getEntity();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpGet httpGet = new HttpGet(url);
//		HttpPost httpPost = new HttpPost(url);
//		Log.v(TAG, "Get created");
//		try {
//			HttpResponse response = httpClient.execute(httpPost);
//			HttpResponse response = httpClient.execute(httpGet);
//			Log.v(TAG, response.getStatusLine().toString());
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		new Connection().execute();
	}
}
