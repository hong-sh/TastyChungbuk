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

public class BallonDialog extends DialogFragment {

	/**
	 * Create a new instance of RequestDialog, providing "_address" as an
	 * argument.
	 */
	private View rootView;
	
	
	public static BallonDialog newInstance() {
		BallonDialog ballon_dialog = new BallonDialog();
		return ballon_dialog;
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
		rootView = inflater.inflate(R.layout.dialog_filter, container, false);
		init();
		return rootView;
	}
	
	private void init()
	{
		
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
