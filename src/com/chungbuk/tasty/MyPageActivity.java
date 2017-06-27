package com.chungbuk.tasty;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import common.ActivityManager;
import common.WebServiceUtil;
import dto.UserInfo;

public class MyPageActivity extends ActionBarActivity {

	SharedPreferences pref;
	private ArrayAdapter<CharSequence> adspin;
	private String str_sex;

	private ActivityManager act = ActivityManager.getInstance();
	UserInfo user_info;
	String[] info;

	private ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypage);

		act.addlist(this);
		user_info = new UserInfo(this);

		init_user_info();
		setupActionBar();
		init();

	}

	private void init_user_info() {

		info = user_info.loadLoginData();
		str_sex = info[3];
		pref = getSharedPreferences("user_info", MODE_PRIVATE);

		((EditText) findViewById(R.id.editText_name)).setText(info[2]);
		if (info[3].equals("0")) {
			((RadioButton) findViewById(R.id.radio_male)).setChecked(true);
		} else if (info[3].equals("1")) {
			((RadioButton) findViewById(R.id.radio_female)).setChecked(true);
		}

		((RadioGroup) findViewById(R.id.radioGroup_sex))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						int id = ((RadioGroup) findViewById(R.id.radioGroup_sex))
								.getCheckedRadioButtonId();
						if (id == R.id.radio_male) {
							str_sex = "0";
						} else {
							str_sex = "1";
						}
					}
				});

		int position = Integer.parseInt(info[4]);
		((Spinner) findViewById(R.id.spinner_age)).setPrompt("연령대를 선택하세요.");
		adspin = ArrayAdapter.createFromResource(this, R.array.age_array,
				android.R.layout.simple_spinner_dropdown_item);
		((Spinner) findViewById(R.id.spinner_age)).setAdapter(adspin);
		((Spinner) findViewById(R.id.spinner_age)).setSelection(position);

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
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// NavUtils.navigateUpFromSameTask(this);
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			act.removeAct(this);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {

		progressbar = (ProgressBar) findViewById(R.id.progressBar);

		((Button) findViewById(R.id.button_edit))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder ab = new AlertDialog.Builder(
								MyPageActivity.this);
						ab.setTitle("개인정보 수정");
						ab.setMessage("변경사항을 수정하시겠습니까?");

						ab.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										final SharedPreferences.Editor editor = pref
												.edit();

										editor.putString(
												"name",
												((EditText) findViewById(R.id.editText_name))
														.getText().toString());

										editor.putString(
												"age",
												String.valueOf(((Spinner) findViewById(R.id.spinner_age))
														.getSelectedItemPosition()));

										editor.putString("gender", str_sex);

										if (((EditText) findViewById(R.id.editText_current_pass))
												.getText().toString()
												.equals(info[1])
												&& (!((EditText) findViewById(R.id.editText_pass))
														.getText().toString()
														.equals(""))) {
											editor.putString(
													"pass",
													((EditText) findViewById(R.id.editText_pass))
															.getText()
															.toString());

										} else if (!((EditText) findViewById(R.id.editText_current_pass))
												.getText().toString()
												.equals(info[1])
												&& (!((EditText) findViewById(R.id.editText_pass))
														.getText().toString()
														.equals(""))) {
											AlertDialog.Builder ab = new AlertDialog.Builder(
													MyPageActivity.this);
											ab.setTitle("개인정보 수정");
											ab.setMessage("현재 비밀번호가 일치하지 않습니다.");
											ab.setPositiveButton(
													"확인",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
														}
													}).show();

										}
										editor.commit();

										info = user_info.loadLoginData();

										progressbar.setVisibility(View.VISIBLE);
										new EditTask().execute();
										init_user_info();

									}
								}).setNegativeButton("취소",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
						AlertDialog alert = ab.create();
						alert.show();
					}
				});
	}

	class EditTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return EditSoap();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (result.equals("true")) {
				Toast.makeText(MyPageActivity.this, "개인정보를 수정하였습니다.",
						Toast.LENGTH_SHORT).show();

				// user_info
				// .setUser_name(((EditText) findViewById(R.id.editText_name))
				// .getText().toString());
				// user_info.setUser_age(Integer.parseInt(String
				// .valueOf(((Spinner) findViewById(R.id.spinner_age))
				// .getSelectedItemPosition())));
				// user_info.setUser_gender(Integer
				// .parseInt(str_sex));
				// if (((EditText) findViewById(R.id.editText_current_pass))
				// .getText().toString()
				// .equals(info[1])
				// && (!((EditText) findViewById(R.id.editText_pass))
				// .getText().toString()
				// .equals(""))) {
				//
				// user_info
				// .setUser_pass(((EditText) findViewById(R.id.editText_pass))
				// .getText()
				// .toString());
				// }
				// user_info.saveLoginData(info[5], info[0], info[1]);
			}

			else {
				Toast.makeText(MyPageActivity.this, "개인정보를 수정에 실패하였습니다.",
						Toast.LENGTH_SHORT).show();

			}

		}

		private String EditSoap() {
			String result = "";
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_set_user_info_method_name);

				request.addProperty("user_id", info[0]);
				request.addProperty("user_pass", info[1]);
				request.addProperty("user_name", info[2]);
				request.addProperty("user_sex", Integer.parseInt(info[3]));
				request.addProperty("user_age", Integer.parseInt(info[4]));
				request.addProperty("user_idx", Integer.parseInt(info[5]));

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_set_user_info_soap_action,
						envelope);

				result = envelope.getResponse().toString();

			} catch (Exception e) {
				Log.e("MyTag", "LoginSoap Error : " + e.toString());
			}

			return result;
		}

	}

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