package com.chungbuk.tasty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

public class DialogFilter extends DialogFragment {

	/**
	 * Create a new instance of RequestDialog, providing "_address" as an
	 * argument.
	 */
	private View rootView;
	private ToggleButton button_kor, button_chinese, button_eng, button_etc,
			button_japan;
	private static String[] filter_flag = new String[] { "0", "1", "2", "3", "4" };
	FilterListener mListener;
	
	public static DialogFilter newInstance() {
		DialogFilter filter_dialog = new DialogFilter();
		return filter_dialog;
	}
	
	public interface FilterListener
	{
		public void onCallBackActivity();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

        // container activity 가 interface 를 구현하였는지 확인한다.
        //  그렇지 않다면 exception 을 발생시킨다.
        try {
        	mListener = (FilterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnHeadlineSelectedListener");
        }
	};
	
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
		rootView = inflater.inflate(R.layout.dialog_filter, container, false);
		init();
		setuptoggle();
		return rootView;
	}
	
	public static String[] get_flag()
	{
		return filter_flag;
	}
	
	@Override
	public void onDismiss(android.content.DialogInterface dialog) {
		mListener.onCallBackActivity();
	};

	private void init() {
		button_kor = ((ToggleButton) rootView
				.findViewById(R.id.toggleButton_kor));
		button_kor.setOnClickListener(buttonClickListener);
		button_chinese = ((ToggleButton) rootView
				.findViewById(R.id.toggleButton_chinese));
		button_chinese.setOnClickListener(buttonClickListener);
		button_japan = ((ToggleButton) rootView
				.findViewById(R.id.toggleButton_japan));
		button_japan.setOnClickListener(buttonClickListener);
		button_eng = ((ToggleButton) rootView
				.findViewById(R.id.toggleButton_eng));
		button_eng.setOnClickListener(buttonClickListener);
		button_etc = ((ToggleButton) rootView
				.findViewById(R.id.toggleButton_etc));
		button_etc.setOnClickListener(buttonClickListener);
		((Button)rootView.findViewById(R.id.button_close)).setOnClickListener(buttonClickListener);
	}
	
	private void setuptoggle()
	{
		if(filter_flag[0].equals("0"))
			button_kor.setChecked(true);
		else
			button_kor.setChecked(false);
		
		if(filter_flag[1].equals("1"))
			button_chinese.setChecked(true);
		else
			button_chinese.setChecked(false);
		
		if(filter_flag[2].equals("2"))
			button_japan.setChecked(true);
		else
			button_japan.setChecked(false);
		
		if(filter_flag[3].equals("3"))
			button_eng.setChecked(true);
		else
			button_eng.setChecked(false);
		
		if(filter_flag[4].equals("4"))
			button_etc.setChecked(true);
		else
			button_etc.setChecked(false);
		
	}

	View.OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.toggleButton_kor:
				if (button_kor.isChecked())
					filter_flag[0] = "0";
				else
					filter_flag[0] = "-1";

				break;
			case R.id.toggleButton_chinese:
				if (button_chinese.isChecked()) 
					filter_flag[1] = "1";
				else
					filter_flag[1] ="-1";
				break;
			case R.id.toggleButton_japan:
				if(button_japan.isChecked())
					filter_flag[2] ="2";
				else
					filter_flag[2] ="-1";
				break;
			case R.id.toggleButton_eng:
				if(button_eng.isChecked())
					filter_flag[3] ="3";
				else
					filter_flag[3] ="-1";
				break;
			case R.id.toggleButton_etc:
				if(button_etc.isChecked())
					filter_flag[4] ="4";
				else
					filter_flag[4] ="-1";
				break;
			case R.id.button_close:
				dismiss();
				break;
				
			}
		}
	};

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
