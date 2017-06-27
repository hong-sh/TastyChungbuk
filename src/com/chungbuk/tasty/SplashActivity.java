package com.chungbuk.tasty;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import common.ActivityManager;
import common.WebServiceUtil;
import dto.UserInfo;

public class SplashActivity extends FragmentActivity {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	PagerAdapter mPagerAdapter;
	UserInfo user_info;
	ProgressBar progressbar;
	
	private ActivityManager act = ActivityManager.getInstance();
	public static boolean page_flag = false;
	String[] Text = new String[2];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// getActionBar().hide();
		setContentView(R.layout.activity_splash);

		user_info = new UserInfo(this);
		String[] temp = user_info.loadLoginData();

		if ((!temp[0].equals("")) && (!temp[1].equals(""))) {
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			act.removeAct(this);
			startActivity(intent);
		}

		act.addlist(this);

		init();
	}

	private void init() {
		
		((Button) findViewById(R.id.button_join))
				.setOnClickListener(buttonClickListener);
		((Button) findViewById(R.id.button_login))
				.setOnClickListener(buttonClickListener);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		progressbar = (ProgressBar)findViewById(R.id.progressBar);

	}

	View.OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.button_join) {
				DialogFragment fragment = DialogJoin.newInstance();
				fragment.setStyle(
						DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
				fragment.show(getSupportFragmentManager(), "dialog");
			} else if (v.getId() == R.id.button_login) {
				if (!page_flag) {
					DialogFragment fragment = DialogLogin.newInstance();
					fragment.setStyle(
							DialogFragment.STYLE_NO_TITLE,
							android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
					fragment.show(getSupportFragmentManager(), "dialog");
				} else {
					Text = PageFragment.getText();
					progressbar.setVisibility(View.VISIBLE);
					new LoginTask().execute();
					page_flag = false;
				}
			}

		}
	};

	private class PagerAdapter extends FragmentStatePagerAdapter {

		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 해당하는 page의 Fragment를 생성합니다.
			
			return PageFragment.create(position);
		}

		@Override
		public int getCount() {
			return 6; // 총 5개의 page를 보여줍니다.
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			act.Allremove(this);
			finish();
			break;
		}
		return false;
	}
	
	class LoginTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return LoginSoap();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);
						
			if (result.equals("true")) {

				new GetUserInfoTask().execute();
			}

			else {

				progressbar.setVisibility(View.GONE);
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(
						SplashActivity.this);
				alt_bld.setMessage(
						"로그인이 되지않았습니다.\n아이디, 비밀번호 혹은 네트워크연결을 확인하여주세요.")
						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
				AlertDialog alert = alt_bld.create();
				alert.setTitle("로그인 알림");
				// alert.setIcon(icon);
				alert.show();
			}

		}

		private String LoginSoap() {
			String result = "";
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_user_info_login_method_name);
				
				request.addProperty("id", Text[0]);
				request.addProperty("pass", Text[1]);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_user_info_login_soap_action, envelope);

				result = envelope.getResponse().toString();

			} catch (Exception e) {
				Log.e("MyTag", "LoginSoap Error : " + e.toString());
			}

			return result;
		}

	}
	
	class GetUserInfoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return GetUserInfoSoap();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {

				progressbar.setVisibility(View.GONE);
				Toast.makeText(SplashActivity.this, "로그인이 완료되었습니다.",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
			} else {
				progressbar.setVisibility(View.GONE);
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(
						SplashActivity.this);
				alt_bld.setMessage(
						"로그인이 되지않았습니다.\n아이디, 비밀번호 혹은 네트워크연결을 확인하여주세요.")
						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
				AlertDialog alert = alt_bld.create();
				alert.setTitle("로그인 알림");
				// alert.setIcon(icon);
				alert.show();
			}

		}

		private String GetUserInfoSoap() {
			String result = "";
			
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_get_user_info_method_name);

				request.addProperty("id", Text[0]);
				request.addProperty("pass", Text[1]);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_get_user_info_soap_action, envelope);

				SoapObject table = (SoapObject) envelope.getResponse();

				for (int i = 0; i < table.getPropertyCount(); i++) {

					SoapObject temp = (SoapObject) table.getProperty(i);

					user_info.saveLoginData(Integer.parseInt(temp.getProperty("idx").toString()) , temp.getProperty("user_id").toString(), 
							temp.getProperty("user_pass").toString(),
							temp.getProperty("user_name").toString(),
							Integer.parseInt(temp.getProperty("user_sex").toString()), 
							Integer.parseInt(temp.getProperty("user_age").toString()));

				}
				result = "true";

			} catch (Exception e) {
				result = "false";
				Log.e("MyTag", "getInfoSoap Error : " + e.toString());
			}

			return result;
		}

	}


}
