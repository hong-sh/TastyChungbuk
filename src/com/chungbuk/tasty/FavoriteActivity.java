package com.chungbuk.tasty;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import adapter.FavoriteAdapter;
import adapter.StoreInfoAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import common.ActivityManager;
import common.WebServiceUtil;
import dto.FavoriteInfo;
import dto.StoreInfo;
import dto.UserInfo;

public class FavoriteActivity extends ActionBarActivity implements OnItemClickListener{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	/*
	 * The {@link ViewPager} that will host the section contents.
	 */

	private ActivityManager act = ActivityManager.getInstance();
	private ListView listview_favorite;
	UserInfo user_info = new UserInfo(this);
	String[] info;
	private ProgressBar progressbar;
	ArrayAdapter<FavoriteInfo> adapter;
	ArrayList<FavoriteInfo> favor_list = new ArrayList<FavoriteInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		act.addlist(this);
		
		init();
		setupActionBar();
		progressbar.setVisibility(View.VISIBLE);
		new GetFavoriteTask().execute();
	}
	
	private void init()
	{
		info = user_info.loadLoginData();
		listview_favorite = (ListView)findViewById(R.id.listView_favor);
		listview_favorite.setOnItemClickListener(this);
		progressbar = (ProgressBar)findViewById(R.id.progressBar);
	}
	

	/**
	 * Set up the {@link android.support.v7.app.ActionBarActivity.getSupportActionBar}.
	 */
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), DetailPageActivity.class);
		intent.putExtra("ipx", favor_list.get(position).get_ipx());
		startActivity(intent);
	}
	
	
	class GetFavoriteTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return GetFavoriteSoap();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {

				adapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.listview_favor, favor_list, Integer.parseInt(info[5]));
				listview_favorite.setAdapter(adapter);
				
			}else if(result.equals("nodata"))
			{
				Toast.makeText(FavoriteActivity.this, "즐겨찾기 목록이 없습니다.", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(FavoriteActivity.this, "목록을 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
			}

		}

		private String GetFavoriteSoap() {
			String result = "";
			
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_get_favorites_method_name);

				request.addProperty("user_idx", Integer.parseInt(info[5]));
				//request.addProperty("user_idx", 2);
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_get_favorites_soap_action, envelope);

				SoapObject table = (SoapObject) envelope.getResponse();

				for (int i = 0; i < table.getPropertyCount(); i++) {

					SoapObject temp = (SoapObject) table.getProperty(i);
					
					if(temp.getProperty("idx").toString().equals("0"))
					{
						return "nodata";
					}
					
					favor_list.add(new FavoriteInfo(Integer.parseInt(temp.getProperty("store_ipx").toString()), 
							temp.getProperty("store_name").toString(), temp.getProperty("store_type").toString() , 
							temp.getProperty("store_address").toString() , Integer.parseInt(temp.getProperty("store_review_count").toString()), Integer.parseInt(temp.getProperty("store_rate").toString())));
					

				}
				result = "true";

			} catch (Exception e) {
				result = "false";
				Log.e("MyTag", "getInfoSoap Error : " + e.toString());
			}

			return result;
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
//			NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.search, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	//
	// if (item.getItemId() == R.id.action_search) {
	//
	//
	// } else {
	// Intent intent = new Intent(FavoriteActivity.this,
	// MainActivity.class);
	// startActivity(intent);
	// }
	//
	// return super.onOptionsItemSelected(item);
	// }

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			act.removeAct(this);
			startActivity(intent);
			break;
		}
		return false;
	}

	

}
