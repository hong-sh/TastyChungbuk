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
import android.widget.Spinner;
import android.widget.Toast;

import common.WebServiceUtil;

import dto.UserInfo;

public class DialogLogin extends DialogFragment {

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

	// SharedPreferences pref = getSharedPreferences("pref",
	// getActivity().MODE_PRIVATE);;

	public static DialogLogin newInstance() {
		DialogLogin login_dialog = new DialogLogin();
		return login_dialog;
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
		rootView = inflater.inflate(R.layout.dialog_login, container, false);

		init();

		return rootView;
	}

	private void init() {

		user_info = new UserInfo(getActivity());
		progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar);

		((Button) rootView.findViewById(R.id.button_close))
				.setOnClickListener(buttonClickListener);
		((Button) rootView.findViewById(R.id.button_login))
				.setOnClickListener(buttonClickListener);
	}

	View.OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.button_close:
				dismiss();
				break;
			case R.id.button_login:
				progressbar.setVisibility(View.VISIBLE);
				new LoginTask().execute();
				break;
			}
		}
	};

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
						getActivity());
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

				request.addProperty("id", ((EditText) rootView
						.findViewById(R.id.editText_EmailAddress)).getText()
						.toString());
				request.addProperty("pass", ((EditText) rootView
						.findViewById(R.id.editText_Password)).getText()
						.toString());

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
				Toast.makeText(getActivity(), "로그인이 완료되었습니다.",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
			} else {
				progressbar.setVisibility(View.GONE);
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(
						getActivity());
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

				request.addProperty("id", ((EditText) rootView
						.findViewById(R.id.editText_EmailAddress)).getText()
						.toString());
				request.addProperty("pass", ((EditText) rootView
						.findViewById(R.id.editText_Password)).getText()
						.toString());

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
