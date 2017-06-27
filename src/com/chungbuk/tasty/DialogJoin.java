package com.chungbuk.tasty;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import common.WebServiceUtil;

import dto.UserInfo;

public class DialogJoin extends DialogFragment {

	/**
	 * Create a new instance of RequestDialog, providing "_address" as an
	 * argument.
	 */
	private View rootView;
	private ProgressBar progressbar;
	private int int_gender = -1;
	private Spinner spinner;
	private ArrayAdapter<CharSequence> adspin;
	UserInfo user_info;
	//SharedPreferences pref = getSharedPreferences("pref", getActivity().MODE_PRIVATE);;
	
	public static DialogJoin newInstance() {
		DialogJoin join_dialog = new DialogJoin();
		return join_dialog;
	}

	/**
	 * OnCreate of RequestDialog, get the save argument for address
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.dialog_join, container, false);

		init();

		return rootView;
	}

	private void init() {
		
		user_info = new UserInfo(getActivity());
		progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		spinner = (Spinner)rootView.findViewById(R.id.spinner_Age);
		spinner.setPrompt("연령대를 선택하세요.");
		adspin = ArrayAdapter.createFromResource(getActivity(), R.array.age_array, android.R.layout.simple_spinner_item);
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adspin);
		
		
		((RadioGroup) rootView.findViewById(R.id.radiogroup))
				.setOnCheckedChangeListener(radiogroupListener);
		
		((Button) rootView.findViewById(R.id.button_close))
				.setOnClickListener(buttonClickListener);
		((Button) rootView.findViewById(R.id.button_join))
				.setOnClickListener(buttonClickListener);
	}

	RadioGroup.OnCheckedChangeListener radiogroupListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == R.id.radioButton_Male)
				int_gender = 0;
			else
				int_gender = 1;
		}
	};

	View.OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.button_close:
				dismiss();
				break;
			case R.id.button_join:
				progressbar.setVisibility(View.VISIBLE);
				new IdCheckTask().execute();
				break;
			}
		}
	};
	
	class IdCheckTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return IdCheckSoap();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equals("false")) {
				new JoinTask().execute();
			}
			else
			{
				progressbar.setVisibility(View.GONE);
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
				alt_bld.setMessage("아이디가 중복됩니다.")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.setTitle("아이디 중복확인");
				//alert.setIcon(icon);
				alert.show();
			}

		}

		private String IdCheckSoap() {
			String result="";
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_user_info_id_check_method_name);

				request.addProperty("id", ((EditText) rootView
						.findViewById(R.id.editText_EmailAddress)).getText()
						.toString());
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_user_info_id_check_soap_action, envelope);

				result = envelope.getResponse().toString();
				
			} catch (Exception e) {
				Log.e("MyTag", "getCurrentDriversSoap Error : " + e.toString());
			}

			return result;
		}

	}
	

	class JoinTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return JoinSoap();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressbar.setVisibility(View.GONE);

			if (!result.equals("false")) {
				progressbar.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
				user_info.saveLoginData( Integer.parseInt(result) ,
						((EditText) rootView
						.findViewById(R.id.editText_EmailAddress)).getText()
						.toString(), ((EditText) rootView
						.findViewById(R.id.editText_Password)).getText()
						.toString(), ((EditText) rootView
						.findViewById(R.id.editText_name)).getText()
						.toString(), int_gender, spinner.getSelectedItemPosition());
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
			}
			else
			{
				progressbar.setVisibility(View.GONE);
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
				alt_bld.setMessage("회원가입이 되지않았습니다.\n네트워크연결을 확인하여주세요.")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.setTitle("회원가입 알림");
				//alert.setIcon(icon);
				alert.show();
			}

		}

		private String JoinSoap() {
			String result="";
			try {
				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
						WebServiceUtil.tasty_user_info_signup_method_name);

				request.addProperty("id", ((EditText) rootView
						.findViewById(R.id.editText_EmailAddress)).getText()
						.toString());
				request.addProperty("pass", ((EditText) rootView
						.findViewById(R.id.editText_Password)).getText()
						.toString());
				request.addProperty("name", ((EditText) rootView
						.findViewById(R.id.editText_name)).getText()
						.toString());
				request.addProperty("sex", int_gender);
				request.addProperty("age", spinner.getSelectedItemPosition());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						WebServiceUtil.URL);
				androidHttpTransport.call(
						WebServiceUtil.tasty_user_info_signup_soap_action, envelope);

				result = envelope.getResponse().toString();
				
			} catch (Exception e) {
				Log.e("MyTag", "getCurrentDriversSoap Error : " + e.toString());
			}

			return result;
		}

	}
}

/*
 * @Override public void onDismiss(android.content.DialogInterface dialog) {
 * RequestDialog.isAnimation_flag =2; DialogFragment fragment =
 * RequestDialog.newInstance("test");
 * //fragment.setStyle(DialogFragment.STYLE_NO_TITLE,
 * android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
 * fragment.setStyle(DialogFragment.STYLE_NO_TITLE,
 * android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
 * fragment.show(getFragmentManager(), "dialog"); CarTypeDialog.this.dismiss();
 * };
 */
