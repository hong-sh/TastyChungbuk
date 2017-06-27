package com.chungbuk.tasty;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class UserInfoActivity extends Activity {

	SharedPreferences pref;
	private String str_sex = "남자";
	
	private ArrayAdapter<CharSequence> adspin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		
		init();

	}

	private void init() {

		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		final SharedPreferences.Editor editor = pref.edit();

		((RadioGroup) findViewById(R.id.radioGroup))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						int id = ((RadioGroup) findViewById(R.id.radioGroup))
								.getCheckedRadioButtonId();
						if (id == R.id.radio_male) {
							str_sex = "남자";
						} else {
							str_sex = "여자";
						}
					}
				});

		((Spinner) findViewById(R.id.spinner_age)).setPrompt("연령대를 선택하세요.");
		adspin = ArrayAdapter.createFromResource(this, R.array.age_array,
				android.R.layout.simple_spinner_dropdown_item);
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		((Spinner) findViewById(R.id.spinner_age)).setAdapter(adspin);
		

		((Button) findViewById(R.id.button_insert))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						editor.putString("name",
								((EditText) findViewById(R.id.editText_name))
										.getText().toString());

						editor.putString("age", ((Spinner) findViewById(R.id.spinner_age)).getSelectedItem().toString());
						editor.putString("sex", str_sex);
						editor.commit();

						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(intent);
					}
				});

	}
}
