package com.chungbuk.tasty;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import common.ActivityManager;
import common.WebServiceUtil;

import dto.ReviewInfo;
import dto.StoreInfo;
import dto.UserInfo;

public class DetailPageActivity extends ActionBarActivity implements
		DialogRate.RateListener {

	private ArrayList<String> mArrays = null;
	private ListView lt_List;
	private Intent intent;
	private int ipx, what;
	private int store_rate;
	private StoreInfo item = new StoreInfo();
	private String TAG = "DetailPage";
	private ProgressBar progressbar;
	private View layout_review1;
	private View layout_review2;
	private View layout_review3;
	private View layout_review4;
	private View layout_review5;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;

	private boolean rate_dialog_call = false;
	public static int store_idx_detail = 0;
	private UserInfo user = new UserInfo(this);
	private String[] userItem;
	private StoreInfo storeItem = new StoreInfo();
	private int user_idx;
	private String user_name;

	private GoogleMap map;

	private ActivityManager act = ActivityManager.getInstance();
	private int position;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailpage);

		act.addlist(this);

		intent = getIntent();
		ipx = intent.getIntExtra("ipx", -1);
		what = intent.getIntExtra("what", 0);

		for (int i = 0; i < MainActivity.all_list.size(); i++) {
			if (MainActivity.all_list.get(i).get_ipx() == ipx) {
				item = MainActivity.all_list.get(i);
				position = i;
				break;
			}
		}
		setupActionBar();
		init();

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
			if(what == 0)
			{
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			act.removeAct(this);
			startActivity(intent);
			}else
			{
				Intent intent = new Intent(getApplicationContext(),
						ListViewActivity.class);
				intent.putExtra("what", what);
				act.removeAct(this);
				startActivity(intent);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (what == 0) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				act.removeAct(this);
				startActivity(intent);
			} else {
				Intent intent = new Intent(getApplicationContext(),
						ListViewActivity.class);
				intent.putExtra("what", what);
				act.removeAct(this);
				startActivity(intent);
			}
			break;
		}
		return false;
	}

	private void init() {

		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		
		button1.setOnClickListener(buttonListener);
		button2.setOnClickListener(buttonListener);
		button3.setOnClickListener(buttonListener);
		((Button)findViewById(R.id.button_favor)).setOnClickListener(buttonListener);
		((Button)findViewById(R.id.button_more)).setOnClickListener(buttonListener);
		layout_review1 = findViewById(R.id.layout_review1);
		layout_review2 = findViewById(R.id.layout_review2);
		layout_review3 = findViewById(R.id.layout_review3);
		layout_review4 = findViewById(R.id.layout_review4);
		layout_review5 = findViewById(R.id.layout_review5);

		userItem = user.loadLoginData();
		user_idx = Integer.parseInt(userItem[5]);
		user_name = userItem[2];

		setup_map();
		setup_storeDetail();

		new getReviewSoapAsyncTask().execute();

	}

	private OnClickListener buttonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.button1) {
				Intent callintent = new Intent(Intent.ACTION_DIAL);
				callintent.setData(Uri.parse("tel:" + item.get_tel()));
				startActivity(callintent);

			} else if (v.getId() == R.id.button2) {
				rate_dialog_call = true;
				DialogFragment fragment = DialogRate.newInstance();
				fragment.setStyle(
						DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
				fragment.show(getSupportFragmentManager(), "rate");

			} else if (v.getId() == R.id.button3) {
				Intent review = new Intent(DetailPageActivity.this,
						ReviewActivity.class);
				review.putExtra("store_idx", item.get_ipx());
				review.putExtra("store_name", String.valueOf(item.get_name()));
				act.removeAct(DetailPageActivity.this);
				startActivity(review);

			}
			else if(v.getId() == R.id.button_favor)
			{
				new FavoriteTask().execute();
			}
			else if(v.getId() == R.id.button_more)
			{
				Intent intent = new Intent(DetailPageActivity.this, ReviewMoreActivity.class);
				intent.putExtra("ipx", item.get_ipx());
				act.removeAct(DetailPageActivity.this);
				startActivity(intent);
			}
		}
	};

	public void onCallBackActivity() {
		// TODO Auto-generated method stub
		Log.d("MyTag", "enter onresume");
		if (rate_dialog_call) {
			store_rate = DialogRate.get_rate();
			rate_dialog_call = false;
			new RateSetTask().execute();
		}
	}

	private void setup_storeDetail() {
		((TextView) findViewById(R.id.textView_name1)).setText(item.get_name());
		((TextView) findViewById(R.id.textView_ceo1)).setText(item.get_ceo());
		// ((TextView)findViewById(R.id.textView_type1)).setText(item.get_type());
		((TextView) findViewById(R.id.textView_address1)).setText(item
				.get_address());
		((RatingBar) findViewById(R.id.ratingBar_rate1))
				.setRating((float) ((float) item.get_rate() / 2.0));
		((TextView) findViewById(R.id.textView_rate1)).setText(String
				.format("%.1f",((float) ((float) item.get_rate() / 2.0))));
		// ((RatingBar)findViewById(R.id.ratingBar_rate1)).(String.valueOf(item.get_rate()));
		((TextView) findViewById(R.id.textView_review_count1)).setText(String
				.valueOf(item.get_review_count()));
		((TextView) findViewById(R.id.textView7)).setText(String.valueOf(item
				.get_rate_count())+"명" );
		((TextView) findViewById(R.id.textView3)).setText(String.valueOf(item
				.get_favorite_count()));
		((TextView) findViewById(R.id.textView_tel1)).setText(item.get_tel());

		// ((TextView)findViewById(R.id.textView_type1)).setText(item.get_type());
		((TextView) findViewById(R.id.textView_detail1)).setText(item
				.get_detail());
		progressbar = (ProgressBar) findViewById(R.id.progressBar);

		if (item.get_type().equals("0")) {
			((ImageView) findViewById(R.id.imageView1))
					.setImageResource(R.drawable.listview_icon_kor);
		} else if (item.get_type().equals("1")) {
			((ImageView) findViewById(R.id.imageView1))
					.setImageResource(R.drawable.listview_icon_chinese);
		}

		else if (item.get_type().equals("2")) {

			((ImageView) findViewById(R.id.imageView1))
					.setImageResource(R.drawable.listview_icon_japan);
		}

		else if (item.get_type().equals("3")) {
			((ImageView) findViewById(R.id.imageView1))
					.setImageResource(R.drawable.listview_icon_eng);
		} else if (item.get_type().equals("4")) {
			((ImageView) findViewById(R.id.imageView1))
					.setImageResource(R.drawable.listview_icon_etc);
		}

	}

	private void setup_map() {
		if (map == null) {
			// Try to obtain the map from the SupportMapFragment.
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map_detail)).getMap();

			Log.d(TAG, "map : " + map.toString());
			// Check if we were successful in obtaining the map.
			if (map != null) {
				map.setMyLocationEnabled(true);
				map.getUiSettings().setMyLocationButtonEnabled(false);
				// map.setOnCameraChangeListener(mapCameraChangeListener);

				map.setInfoWindowAdapter(new InfoWindowAdapter() {

					@Override
					public View getInfoWindow(Marker marker) {

						return null;
					}

					public View getInfoContents(Marker marker) {

						View v = getLayoutInflater().inflate(R.layout.bullon,
								null);
						TextView name = (TextView) v
								.findViewById(R.id.textView_name);
						TextView review_count = (TextView) v
								.findViewById(R.id.textView_review_count);

						name.setText(item.get_name());
						review_count.setText(String.valueOf(item
								.get_review_count()));
						ImageView icon;
						switch (Integer.parseInt(item.get_type())) {
						case 0:
							((ImageView) v.findViewById(R.id.imageView_main))
									.setImageResource(R.drawable.ballon_kor);
							break;
						case 1:
							((ImageView) v.findViewById(R.id.imageView_main))
									.setImageResource(R.drawable.ballon_chinese);
							break;
						case 2:
							((ImageView) v.findViewById(R.id.imageView_main))
									.setImageResource(R.drawable.ballon_japan);
							break;
						case 3:
							((ImageView) v.findViewById(R.id.imageView_main))
									.setImageResource(R.drawable.ballon_eng);
							break;
						case 4:
							((ImageView) v.findViewById(R.id.imageView_main))
									.setImageResource(R.drawable.ballon_etc);
							break;
						}

						return v;
					}
				});
				// map.setOnMapClickListener(mapClickListener);
			}
		}

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(Double.parseDouble(item.get_lat()), Double
						.parseDouble(item.get_lng())),
				map.getCameraPosition().zoom), 800, null);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(Double.parseDouble(item.get_lat()), Double
						.parseDouble(item.get_lng())), 15));

		Log.d(TAG, "Lat: " + Double.parseDouble(item.get_lat()) + "Lng : "
				+ item.get_lng());

		switch (Integer.parseInt(item.get_type())) {
		case 0:
			map.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(item.get_lat()),
									Double.parseDouble(item.get_lng())))
					.snippet("")
					.title("store")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.fork_kor)));
			break;
		case 1:
			map.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(item.get_lat()),
									Double.parseDouble(item.get_lng())))
					.snippet("")
					.title("store")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mysetlocation)));
			break;
		case 2:
			map.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(item.get_lat()),
									Double.parseDouble(item.get_lng())))
					.snippet("")
					.title("store")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mysetlocation)));
			break;
		case 3:
			map.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(item.get_lat()),
									Double.parseDouble(item.get_lng())))
					.snippet("")
					.title("store")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.fork_eng)));
			break;
		case 4:
			map.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(item.get_lat()),
									Double.parseDouble(item.get_lng())))
					.snippet("")
					.title("store")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mysetlocation)));
			break;
		case 5:
			map.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(item.get_lat()),
									Double.parseDouble(item.get_lng())))
					.snippet("")
					.title("store")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mysetlocation)));
			break;
		}

	}

	class getReviewSoapAsyncTask extends
			AsyncTask<String, String, ArrayList<ReviewInfo>> {

		@Override
		protected ArrayList<ReviewInfo> doInBackground(String... params) {

			return getReviewSoap(item.get_ipx());
		}

		@Override
		protected void onPostExecute(ArrayList<ReviewInfo> result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);
			if(result.size() <= 5)
			{
				((Button)findViewById(R.id.button_more)).setVisibility(View.GONE);
			}
			Log.d(TAG, "size : " + result.size());
			switch (result.size()) {
			case 0:

				break;
			case 1:
				Log.d("MyTag", "result.get(0).get_user_sex() = "
						+ result.get(0).get_user_sex().toString());
				if (result.get(0).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_woman);

				}

				((TextView) findViewById(R.id.textView_name)).setText(result
						.get(0).get_user_name());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_age)).setText(result
						.get(0).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar1)).setRating(result
						.get(0).get_rate());
				((TextView) findViewById(R.id.textView_opinion)).setText(result
						.get(0).get_comment());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_date)).setText(result
						.get(0).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review1.setVisibility(View.VISIBLE);
				break;
			case 2:
				if (result.get(0).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_woman);

				}

				if (result.get(1).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_woman);

				}
				((TextView) findViewById(R.id.textView_name)).setText(result
						.get(0).get_user_name());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_age)).setText(result
						.get(0).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar1)).setRating(result
						.get(0).get_rate());
				((TextView) findViewById(R.id.textView_opinion)).setText(result
						.get(0).get_comment());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_date)).setText(result
						.get(0).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review1.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name2)).setText(result
						.get(1).get_user_name());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_age2)).setText(result
						.get(1).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar12)).setRating(result
						.get(1).get_rate());
				((TextView) findViewById(R.id.textView_opinion2))
						.setText(result.get(1).get_comment());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_date2)).setText(result
						.get(1).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review2.setVisibility(View.VISIBLE);
				break;
			case 3:
				if (result.get(0).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_woman);

				}

				if (result.get(1).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_woman);

				}
				if (result.get(2).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main3))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main3))
							.setImageResource(R.drawable.icon_woman);

				}
				((TextView) findViewById(R.id.textView_name)).setText(result
						.get(0).get_user_name());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_age)).setText(result
						.get(0).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar1)).setRating(result
						.get(0).get_rate());
				((TextView) findViewById(R.id.textView_opinion)).setText(result
						.get(0).get_comment());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_date)).setText(result
						.get(0).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review1.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name2)).setText(result
						.get(1).get_user_name());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_age2)).setText(result
						.get(1).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar12)).setRating(result
						.get(1).get_rate());
				((TextView) findViewById(R.id.textView_opinion2))
						.setText(result.get(1).get_comment());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_date2)).setText(result
						.get(1).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review2.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name3)).setText(result
						.get(2).get_user_name());
				((TextView) findViewById(R.id.textView_gender3)).setText(result
						.get(2).get_user_sex());
				((TextView) findViewById(R.id.textView_age3)).setText(result
						.get(2).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar13)).setRating(result
						.get(2).get_rate());
				((TextView) findViewById(R.id.textView_opinion3))
						.setText(result.get(2).get_comment());
				((TextView) findViewById(R.id.textView_gender3)).setText(result
						.get(2).get_user_sex());
				((TextView) findViewById(R.id.textView_date3)).setText(result
						.get(2).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review3.setVisibility(View.VISIBLE);
				break;
			case 4:
				if (result.get(0).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_woman);

				}

				if (result.get(1).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_woman);

				}
				if (result.get(2).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main3))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main3))
							.setImageResource(R.drawable.icon_woman);

				}
				if (result.get(3).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main4))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main4))
							.setImageResource(R.drawable.icon_woman);

				}
				((TextView) findViewById(R.id.textView_name)).setText(result
						.get(0).get_user_name());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_age)).setText(result
						.get(0).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar1)).setRating(result
						.get(0).get_rate());
				((TextView) findViewById(R.id.textView_opinion)).setText(result
						.get(0).get_comment());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_date)).setText(result
						.get(0).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review1.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name2)).setText(result
						.get(1).get_user_name());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_age2)).setText(result
						.get(1).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar12)).setRating(result
						.get(1).get_rate());
				((TextView) findViewById(R.id.textView_opinion2))
						.setText(result.get(1).get_comment());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_date2)).setText(result
						.get(1).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review2.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name3)).setText(result
						.get(2).get_user_name());
				((TextView) findViewById(R.id.textView_gender3)).setText(result
						.get(2).get_user_sex());
				((TextView) findViewById(R.id.textView_age3)).setText(result
						.get(2).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar13)).setRating(result
						.get(2).get_rate());
				((TextView) findViewById(R.id.textView_opinion3))
						.setText(result.get(2).get_comment());
				((TextView) findViewById(R.id.textView_gender3)).setText(result
						.get(2).get_user_sex());
				((TextView) findViewById(R.id.textView_date3)).setText(result
						.get(2).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review3.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name4)).setText(result
						.get(3).get_user_name());
				((TextView) findViewById(R.id.textView_gender4)).setText(result
						.get(3).get_user_sex());
				((TextView) findViewById(R.id.textView_age4)).setText(result
						.get(3).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar14)).setRating(result
						.get(3).get_rate());
				((TextView) findViewById(R.id.textView_opinion4))
						.setText(result.get(3).get_comment());
				((TextView) findViewById(R.id.textView_gender4)).setText(result
						.get(3).get_user_sex());
				((TextView) findViewById(R.id.textView_date4)).setText(result
						.get(3).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review4.setVisibility(View.VISIBLE);
				break;
			default:
				if (result.get(0).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main1))
							.setImageResource(R.drawable.icon_woman);

				}

				if (result.get(1).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main2))
							.setImageResource(R.drawable.icon_woman);

				}
				if (result.get(2).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main3))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main3))
							.setImageResource(R.drawable.icon_woman);

				}
				if (result.get(3).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main4))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main4))
							.setImageResource(R.drawable.icon_woman);

				}
				if (result.get(4).get_user_sex().equals("남")) {

					((ImageView) findViewById(R.id.imageView_main5))
							.setImageResource(R.drawable.icon_man);

				} else {
					((ImageView) findViewById(R.id.imageView_main5))
							.setImageResource(R.drawable.icon_woman);

				}
				((TextView) findViewById(R.id.textView_name)).setText(result
						.get(0).get_user_name());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_age)).setText(result
						.get(0).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar1)).setRating(result
						.get(0).get_rate());
				((TextView) findViewById(R.id.textView_opinion)).setText(result
						.get(0).get_comment());
				((TextView) findViewById(R.id.textView_gender)).setText(result
						.get(0).get_user_sex());
				((TextView) findViewById(R.id.textView_date)).setText(result
						.get(0).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review1.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name2)).setText(result
						.get(1).get_user_name());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_age2)).setText(result
						.get(1).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar12)).setRating(result
						.get(1).get_rate());
				((TextView) findViewById(R.id.textView_opinion2))
						.setText(result.get(1).get_comment());
				((TextView) findViewById(R.id.textView_gender2)).setText(result
						.get(1).get_user_sex());
				((TextView) findViewById(R.id.textView_date2)).setText(result
						.get(1).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review2.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name3)).setText(result
						.get(2).get_user_name());
				((TextView) findViewById(R.id.textView_gender3)).setText(result
						.get(2).get_user_sex());
				((TextView) findViewById(R.id.textView_age3)).setText(result
						.get(2).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar13)).setRating(result
						.get(2).get_rate());
				((TextView) findViewById(R.id.textView_opinion3))
						.setText(result.get(2).get_comment());
				((TextView) findViewById(R.id.textView_gender3)).setText(result
						.get(2).get_user_sex());
				((TextView) findViewById(R.id.textView_date3)).setText(result
						.get(2).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review3.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name4)).setText(result
						.get(3).get_user_name());
				((TextView) findViewById(R.id.textView_gender4)).setText(result
						.get(3).get_user_sex());
				((TextView) findViewById(R.id.textView_age4)).setText(result
						.get(3).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar14)).setRating(result
						.get(3).get_rate());
				((TextView) findViewById(R.id.textView_opinion4))
						.setText(result.get(3).get_comment());
				((TextView) findViewById(R.id.textView_gender4)).setText(result
						.get(3).get_user_sex());
				((TextView) findViewById(R.id.textView_date4)).setText(result
						.get(3).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review4.setVisibility(View.VISIBLE);

				((TextView) findViewById(R.id.textView_name5)).setText(result
						.get(4).get_user_name());
				((TextView) findViewById(R.id.textView_gender5)).setText(result
						.get(4).get_user_sex());
				((TextView) findViewById(R.id.textView_age5)).setText(result
						.get(4).get_user_age());
				((RatingBar) findViewById(R.id.ratingBar15)).setRating(result
						.get(4).get_rate());
				((TextView) findViewById(R.id.textView_opinion5))
						.setText(result.get(4).get_comment());
				((TextView) findViewById(R.id.textView_gender5)).setText(result
						.get(4).get_user_sex());
				((TextView) findViewById(R.id.textView_date5)).setText(result
						.get(4).get_mod_date());
				Log.d(TAG, "sympathy : " + result.get(0).get_sympathy());

				layout_review5.setVisibility(View.VISIBLE);
				break;
			}
		}

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
				item.put("user_idx", element.getProperty("user_idx").toString());
				item.put("user_name", element.getProperty("user_name")
						.toString());
				item.put("user_age", element.getProperty("user_age").toString());
				item.put("user_sex", element.getProperty("user_sex").toString());
				item.put("comment", element.getProperty("comment").toString());
				item.put("rate", element.getProperty("rate").toString());
				item.put("sympathy", element.getProperty("sympathy").toString());
				item.put("mod_date", element.getProperty("mod_date").toString());

				reviewList.add(item);
			}

		} catch (Exception e) {
			reviewList.clear();
			Log.e("MyTag", "getCurrentDriversSoap Error : " + e.toString());
		}

		return reviewList;
	}

	class RateSetTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return RateSetSoap(ipx, user_idx, user_name, store_rate);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {
				new getReviewSoapAsyncTask().execute();
				new ReplaceTask().execute();				
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

			return ReplaceSoap(item.get_ipx());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {
				setup_storeDetail();
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

					item.put("ipx", element.getProperty("ipx").toString());

					item.put("name", element.getProperty("name").toString());
					item.put("ceo", element.getProperty("ceo").toString());
					item.put("type", element.getProperty("type").toString());
					item.put("tel", element.getProperty("tel1").toString() + "-"
							+ element.getProperty("tel2").toString() + "-"
							+ element.getProperty("tel3").toString());

					item.put("detail", element.getProperty("detail").toString());
					item.put("address", element.getProperty("address").toString());
					item.put("review_count", element.getProperty("review_count")
							.toString());
					item.put("rate_count", element.getProperty("rate_count")
							.toString());
					item.put("rate", element.getProperty("rate").toString());
					item.put("lat", element.getProperty("lat").toString());
					item.put("lng", element.getProperty("lng").toString());
					item.put("distance", MainActivity.all_list.get(position).get_real_distance());
					item.put("rate_count", element.getProperty("rate_count")
							.toString());
					item.put("favorite_count",
							element.getProperty("favorites_count").toString());

					MainActivity.all_list.set(position, item);
					
					result = "true";
				}

			} catch (Exception e) {
				Log.e("MyTag", "LoginSoap Error : " + e.toString());
			}

			return result;
		}
	}
	
	class FavoriteTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return FavoriteSoap(item.get_ipx(), user.getUser_idx());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("add")) {
				Toast.makeText(DetailPageActivity.this, "해당 맛집을 즐겨찾기로 등록하였습니다.\n즐겨찾기 목록은 마이페이지에서 확인하실 수 있습니다.",
						Toast.LENGTH_SHORT).show();
			}
			else if(result.equals("delete"))
			{
				Toast.makeText(DetailPageActivity.this, "해당 맛집 즐겨찾기를 취소하였습니다.\n즐겨찾기 목록은 마이페이지에서 확인하실 수 있습니다.",
						Toast.LENGTH_SHORT).show();
			}

			else {
				progressbar.setVisibility(View.GONE);
			}

		}

		private String FavoriteSoap(int store_idx , int user_idx) {
			String result = "";
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_set_favorites_method_name);

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
						WebServiceUtil.tasty_set_favorites_soap_action, envelope);

				result = envelope.getResponse().toString();

			} catch (Exception e) {
				Log.e("MyTag", "LoginSoap Error : " + e.toString());
			}

			return result;
		}

	}
}