package com.chungbuk.tasty;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import adapter.ReviewInfoAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import common.ActivityManager;
import common.WebServiceUtil;

import dto.ReviewInfo;

public class ReviewMoreActivity extends ActionBarActivity {

	ListView listview;
	ProgressBar progressbar;
	int store_idx;

	private ActivityManager act = ActivityManager.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_more);

		act.addlist(this);
		
		Intent intent = getIntent();
		store_idx = intent.getIntExtra("ipx", 0);
		
		setupActionBar();
		init();
		progressbar.setVisibility(View.VISIBLE);
		new getReviewSoapAsyncTask().execute();
	}
	
	/**
	 * Set up the
	 * {@link android.support.v7.app.ActionBarActivity.getSupportActionBar}.
	 */
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	private void init() {
		listview = (ListView) findViewById(R.id.listView);
		progressbar = (ProgressBar) findViewById(R.id.progressBar);
		((Button)findViewById(R.id.button_ok)).setOnClickListener(buttonClickListener);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
		case android.R.id.home:
			
			Intent intent = new Intent(getApplicationContext(),
					DetailPageActivity.class);
			intent.putExtra("ipx", store_idx);
			act.removeAct(this);
			startActivity(intent);
			
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
				Intent intent = new Intent(getApplicationContext(),
						DetailPageActivity.class);
				intent.putExtra("ipx", store_idx);
				act.removeAct(this);
				startActivity(intent);
			
			break;
		}
		return false;
	}
	
	View.OnClickListener buttonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.button_ok)
			{
				Intent intent = new Intent(getApplicationContext(),
						DetailPageActivity.class);
				intent.putExtra("ipx", store_idx);
				act.removeAct(ReviewMoreActivity.this);
				startActivity(intent);
			}
		}
	};

	class getReviewSoapAsyncTask extends
			AsyncTask<String, String, ArrayList<ReviewInfo>> {

		@Override
		protected ArrayList<ReviewInfo> doInBackground(String... params) {

			return getReviewSoap(store_idx);
		}

		@Override
		protected void onPostExecute(ArrayList<ReviewInfo> result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);
			
			ReviewInfoAdapter adapter = new ReviewInfoAdapter(ReviewMoreActivity.this, R.layout.listview_review, result);
			listview.setAdapter(adapter);
			

			// if (result.size() <= 5) {
			// ((Button) findViewById(R.id.button_more))
			// .setVisibility(View.GONE);
			// }

		}

		private ArrayList<ReviewInfo> getReviewSoap(int store_idx) {

			ArrayList<ReviewInfo> reviewList = new ArrayList<ReviewInfo>();

			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_get_review_method_name);
				request.addProperty("store_idx", store_idx);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_get_review_soap_action, envelope);

				SoapObject table = (SoapObject) envelope.getResponse();
				reviewList.clear();

				for (int i = 0; i < table.getPropertyCount(); i++) {
					SoapObject element = (SoapObject) table.getProperty(i);

					ReviewInfo item = new ReviewInfo();

					item.put("idx", element.getProperty("idx").toString());
					item.put("store_idx", element.getProperty("store_idx")
							.toString());
					item.put("user_idx", element.getProperty("user_idx")
							.toString());
					item.put("user_name", element.getProperty("user_name")
							.toString());
					item.put("user_age", element.getProperty("user_age")
							.toString());
					item.put("user_sex", element.getProperty("user_sex")
							.toString());
					item.put("comment", element.getProperty("comment")
							.toString());
					item.put("rate", element.getProperty("rate").toString());
					item.put("sympathy", element.getProperty("sympathy")
							.toString());
					item.put("mod_date", element.getProperty("mod_date")
							.toString());

					reviewList.add(item);
				}

			} catch (Exception e) {
				reviewList.clear();
				Log.e("MyTag", "getCurrentDriversSoap Error : " + e.toString());
			}

			return reviewList;
		}
	}

}
