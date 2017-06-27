package dto;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
 
public class UserInfo {

	
	private Context mContext;
	private int idx;
	private String user_email;
	private String user_name;
	private String user_pass;
	private int user_gender;
	private int user_age;
	

	public UserInfo( Context context ) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	// 로그인 데이터 저장
	public void saveLoginData(int idx, String id, String pass, String name, int gender, int age){
		SharedPreferences pref = mContext.getSharedPreferences("user_info", mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("idx", String.valueOf(idx));
		editor.putString("id", id);
		editor.putString("pass", pass);
		editor.putString("name", name);
		editor.putString("gender", String.valueOf(gender));
		editor.putString("age", String.valueOf(age));
		editor.commit();
		
		this.idx = idx;
		this.user_email = id;
		this.user_pass = pass;
		this.user_gender = gender;
		this.user_age = age;
		this.user_name = name;
		
	}
	

	public String[] loadLoginData(  ){
		String[] temp = null;
		SharedPreferences pref =  mContext.getSharedPreferences("user_info", mContext.MODE_PRIVATE);
		temp = new String[6];
		temp[0] = pref.getString("id", "");
		temp[1] = pref.getString("pass", "");
		temp[2] = pref.getString("name", "");
		temp[3] = pref.getString("gender", "");
		temp[4] = pref.getString("age", "");
		temp[5] = pref.getString("idx", "");

		return temp;
	}


	public int getUser_age() {
		return user_age;
	}


	public int getUser_idx(){
		SharedPreferences pref =  mContext.getSharedPreferences("user_info", mContext.MODE_PRIVATE);
		return Integer.parseInt(pref.getString("idx", "0"));
	}

	public void setUser_age(int user_age) {
		this.user_age = user_age;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(int user_gender) {
		this.user_gender = user_gender;
	}

	public String getUser_pass() {
		return user_pass;
	}

	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}





}
