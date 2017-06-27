package com.chungbuk.tasty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import dto.UserInfo;

public class DialogRate extends DialogFragment {

	/**
	 * Create a new instance of RequestDialog, providing "_address" as an
	 * argument.
	 */
	private View rootView;
	private Button button_save;
	private RatingBar rating_bar;
	private RatingBar setRatingBar;
	private UserInfo user = new UserInfo(getActivity());
	private static int store_rate;
	private RateListener mListener;

	public static DialogRate newInstance() {
		DialogRate rate_dialog = new DialogRate();
		return rate_dialog;
	}

	public interface RateListener {
		public void onCallBackActivity();
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// container activity 가 interface 를 구현하였는지 확인한다.
		// 그렇지 않다면 exception 을 발생시킨다.
		try {
			mListener = (RateListener) activity;
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
		rootView = inflater.inflate(R.layout.dialog_rate, container, false);

		init();

		return rootView;
	}
	View.OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.button_rating:
				double temp = (((RatingBar) rootView.findViewById(R.id.setRatingBar)).getRating()) * (2.0);
				
				store_rate = (int)temp;
				
				dismiss();
				break;

			}
		}
	};

	@Override
	public void onDismiss(android.content.DialogInterface dialog) {
		mListener.onCallBackActivity();
	};

	private void init() {
		button_save = ((Button) rootView.findViewById(R.id.button_rating));
		rating_bar = ((RatingBar) rootView.findViewById(R.id.setRatingBar));
		
		button_save.setOnClickListener(buttonClickListener);


	}

	public static int get_rate() {
		return store_rate;
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
