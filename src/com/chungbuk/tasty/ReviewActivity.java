package com.chungbuk.tasty;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import common.ActivityManager;
import common.WebServiceUtil;
import dto.RateInfo;
import dto.StoreInfo;
import dto.UserInfo;

public class ReviewActivity extends ActionBarActivity implements
		DialogRate.RateListener {
	private int store_idx;
	private String store_name;
	private Intent intent;
	private TextView store_nameText;
	private RatingBar review_rating;
	private EditText EditTextcomment;
	private Button button1;
	private UserInfo user = new UserInfo(this);
	private String[] userItem;
	private StoreInfo storeItem = new StoreInfo();
	private int user_idx;
	private int user_age;
	private int user_sex;
	private String user_name;
	private ProgressBar progressbar;
	private boolean rate_check = false;
	private String TAG = "ReviewActivity";
	private String temp_comment;
	private String NO = "NO";

	private boolean rate_dialog_call = false;
	private int store_rate;
	private int position;

	private ActivityManager act = ActivityManager.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);

		act.addlist(this);

		intent = getIntent();
		store_idx = intent.getIntExtra("store_idx", 0);
		store_name = intent.getStringExtra("store_name");

		userItem = user.loadLoginData();
		user_idx = Integer.parseInt(userItem[5]);
		user_age = Integer.parseInt(userItem[4]);
		user_sex = Integer.parseInt(userItem[3]);
		user_name = userItem[2];
		
		for(int i=0; i<MainActivity.all_list.size(); i++)
		{
			if(MainActivity.all_list.get(i).get_ipx() == store_idx)
			{
				storeItem = MainActivity.all_list.get(i);
				position = i;
				break;
			}
		}

		init();
		setupActionBar();

		new getRateSoapAsyncTask().execute();

	}

	/**
	 * Set up the
	 * {@link android.support.v7.app.ActionBarActivity.getSupportActionBar}.
	 */
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

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

	public void init() {
		button1 = (Button) findViewById(R.id.button1);
		store_nameText = (TextView) findViewById(R.id.StoreName);
		review_rating = (RatingBar) findViewById(R.id.ReviewRate);
		EditTextcomment = (EditText) findViewById(R.id.editText1);
		progressbar = (ProgressBar) findViewById(R.id.progressBar);

		button1.setOnClickListener(buttonListener);
		setup_review();

	}

	public void setup_review() {
		Log.d(TAG, "store_name" + store_name);
		((TextView) findViewById(R.id.StoreName)).setText(store_name);

	}

	private OnClickListener buttonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.button1) {
				if (!((EditText) findViewById(R.id.editText1)).getText()
						.toString().equals("")) {
					if (rate_check) {
						temp_comment = ((EditText) findViewById(R.id.editText1))
								.getText().toString();
						new ReviewSetTask().execute();
						Log.d("MyTag", "edit = "
								+ ((EditText) findViewById(R.id.editText1))
										.getText().toString());
						Intent intent = new Intent(getApplicationContext(),
								DetailPageActivity.class);
						Log.d("MyTag", "store_idx = " + store_idx);
						intent.putExtra("ipx", store_idx);
						act.removeAct(ReviewActivity.this);
						startActivity(intent);
						/*
						 * Intent reivew = new Intent(ReviewActivity.this,
						 * DetailPageActivity.class);
						 * reivew.putExtra("store_idx", store_idx);
						 * startActivityForResult(reivew, 6);
						 */
					} else {

						AlertDialog.Builder alt_bld = new AlertDialog.Builder(
								ReviewActivity.this);
						alt_bld.setMessage("평점이 등록되지 않았습니다.\n평점을 먼저 입력해주세요.")
								.setPositiveButton("확인",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												// dialog.dismiss();

												rate_dialog_call = true;
												DialogFragment fragment = DialogRate
														.newInstance();
												fragment.setStyle(
														DialogFragment.STYLE_NO_TITLE,
														android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
												fragment.show(
														getSupportFragmentManager(),
														"rate");

											}
										});
						AlertDialog alert = alt_bld.create();
						alert.setTitle("리뷰 알림");
						// alert.setIcon(icon);
						alert.show();

					}
				} else {
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(
							ReviewActivity.this);
					alt_bld.setMessage("댓글이 입력되지 않았습니다.\n댓글을 입력해주세요.")
							.setPositiveButton("확인",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});
					AlertDialog alert = alt_bld.create();
					alert.setTitle("리뷰 알림");
					// alert.setIcon(icon);
					alert.show();
				}
			}
		}
	};

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.review, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	class getRateSoapAsyncTask extends
			AsyncTask<String, String, ArrayList<RateInfo>> {

		@Override
		protected ArrayList<RateInfo> doInBackground(String... params) {

			return getRateSoap(store_idx, user_idx);
		}

		@Override
		protected void onPostExecute(ArrayList<RateInfo> result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);
			Log.d("MyTag", "result = " + result.size());
			if (result.get(0).get_idx() == 0) {
				rate_check = false;
			} else {
				rate_check = true;

				float rate_temp = 0;

				rate_temp = (float) ((float) result.get(0).get_rate() / 2.0);

				// result.get(0).get_rate()
				Log.d("MyTag", "result = " + result.get(0).get_rate());
				((RatingBar) findViewById(R.id.ReviewRate))
						.setRating(rate_temp);
			}

		}

	}

	private ArrayList<RateInfo> getRateSoap(int store_idx, int user_idx) {

		ArrayList<RateInfo> rateList = new ArrayList<RateInfo>();

		try {
			SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
					WebServiceUtil.tasty_get_rate_method_name);

			request.addProperty("store_idx", store_idx);
			request.addProperty("user_idx", user_idx);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapSerializationEnvelope.XSD;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebServiceUtil.URL);
			androidHttpTransport.call(
					WebServiceUtil.tasty_get_rate_soap_action, envelope);

			SoapObject table = (SoapObject) envelope.getResponse();
			rateList.clear();

			Log.d("MyTag", "getPropertyCount" + table.getPropertyCount());

			for (int i = 0; i < table.getPropertyCount(); i++) {
				SoapObject element = (SoapObject) table.getProperty(i);

				RateInfo item = new RateInfo();

				item.put("idx", element.getProperty("idx").toString());
				item.put("store_idx", element.getProperty("store_idx")
						.toString());
				item.put("user_idx", element.getProperty("user_idx").toString());
				item.put("user_name", element.getProperty("user_name")
						.toString());
				item.put("rate", element.getProperty("rate").toString());

				rateList.add(item);

			}

		} catch (Exception e) {
			rateList.clear();
			Log.e("MyTag", "getCurrentDriversSoap Error : " + e.toString());
		}

		return rateList;
	}

	class ReviewSetTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return ReviewSetSoap(store_idx, user_idx, user_name, user_age,
					user_sex, temp_comment, NO);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {

				new ReplaceTask().execute();
				
			}

			else {

				progressbar.setVisibility(View.GONE);

			}

		}
	}

	private String ReviewSetSoap(int store_idx, int user_idx, String user_name,
			int user_age, int user_sex, String comment1, String comment_image2) {
		String result = "";
		try {
			SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
					WebServiceUtil.tasty_set_review_method_name);

			request.addProperty("store_idx", store_idx);
			request.addProperty("user_idx", user_idx);
			request.addProperty("user_name", user_name);
			request.addProperty("user_age", user_age);
			request.addProperty("user_sex", user_sex);
			request.addProperty("comment", comment1);
			request.addProperty("comment_image", comment_image2);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapSerializationEnvelope.XSD;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					WebServiceUtil.URL);
			androidHttpTransport.call(
					WebServiceUtil.tasty_set_review_soap_action, envelope);

			result = envelope.getResponse().toString();

		} catch (Exception e) {
			Log.e("MyTag", "LoginSoap Error : " + e.toString());
		}

		return result;
	}

	class RateSetTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return RateSetSoap(store_idx, user_idx, user_name, store_rate);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {
				temp_comment = ((EditText) findViewById(R.id.editText1)).getText()
						.toString();
				new ReviewSetTask().execute();

			}

			else {
				progressbar.setVisibility(View.GONE);
			}

		}

		private String RateSetSoap(int store_idx, int user_idx,
				String user_name, int store_rate) {
			String result = "";
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_set_rate_method_name);

				request.addProperty("store_idx", store_idx);
				request.addProperty("user_idx", user_idx);
				request.addProperty("user_name", user_name);
				request.addProperty("store_rate", store_rate);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_set_rate_soap_action, envelope);

				result = envelope.getResponse().toString();

			} catch (Exception e) {
				Log.e("MyTag", "LoginSoap Error : " + e.toString());
			}

			return result;
		}

	}
	
	class ReplaceTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return ReplaceSoap(storeItem.get_ipx());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {
				Intent intent = new Intent(getApplicationContext(),
						DetailPageActivity.class);
				Log.d("MyTag", "store_idx = " + store_idx);
				intent.putExtra("ipx", store_idx);
				act.removeAct(ReviewActivity.this);
				startActivity(intent);
			}

			else {
				progressbar.setVisibility(View.GONE);
			}

		}

		private String ReplaceSoap(int store_idx) {
			String result = "";
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_store_info_one_select_method_name);

				request.addProperty("store_idx", store_idx);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_store_info_one_select_soap_action, envelope);

				SoapObject table = (SoapObject) envelope.getResponse();

				for (int i = 0; i < table.getPropertyCount(); i++) {
					SoapObject element = (SoapObject) table.getProperty(i);

					//StoreInfo temp = new StoreInfo();

					storeItem.put("ipx", element.getProperty("ipx").toString());

					storeItem.put("name", element.getProperty("name").toString());
					storeItem.put("ceo", element.getProperty("ceo").toString());
					storeItem.put("type", element.getProperty("type").toString());
					storeItem.put("tel", element.getProperty("tel1").toString() + "-"
							+ element.getProperty("tel2").toString() + "-"
							+ element.getProperty("tel3").toString());

					storeItem.put("detail", element.getProperty("detail").toString());
					storeItem.put("address", element.getProperty("address").toString());
					storeItem.put("review_count", element.getProperty("review_count")
							.toString());
					storeItem.put("rate_count", element.getProperty("rate_count")
							.toString());
					storeItem.put("rate", element.getProperty("rate").toString());
					storeItem.put("lat", element.getProperty("lat").toString());
					storeItem.put("lng", element.getProperty("lng").toString());
					storeItem.put("distance", MainActivity.all_list.get(position).get_real_distance());
					storeItem.put("rate_count", element.getProperty("rate_count")
							.toString());
					storeItem.put("favorite_count",
							element.getProperty("favorites_count").toString());

					MainActivity.all_list.set(position, storeItem);
					
					result = "true";
				}

			} catch (Exception e) {
				Log.e("MyTag", "LoginSoap Error : " + e.toString());
			}

			return result;
		}
	}

	public void onCallBackActivity() {
		// TODO Auto-generated method stub
		Log.d("MyTag", "enter onresume");
		if (rate_dialog_call) {
			store_rate = DialogRate.get_rate();
			rate_dialog_call = false;
			new RateSetTask().execute();

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(getApplicationContext(),
					DetailPageActivity.class);
			Log.d("MyTag", "store_idx = " + store_idx);
			intent.putExtra("ipx", store_idx);
			act.removeAct(this);
			startActivity(intent);
			break;
		}
		return false;
	}

}