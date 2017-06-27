package com.chungbuk.tasty;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageFragment extends Fragment {

	private int mPageNumber;
	static ViewGroup rootView = null;

	public static PageFragment create(int pageNumber) {
		PageFragment fragment = new PageFragment();
		Bundle args = new Bundle();
		args.putInt("page", pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt("page");
	}

	public static String[] getText() {
		String[] Text = new String[2];
		Text[0] = ((EditText) rootView.findViewById(R.id.editText_EmailAddress))
				.getText().toString();
		Text[1] = ((EditText) rootView.findViewById(R.id.editText_Password))
				.getText().toString();

		return Text;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_page,
				container, false);
		Log.d("MyTag", "mPageNumber = " + mPageNumber);
		switch (mPageNumber) {
		case 0:
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setVisibility(View.VISIBLE);
			((View) rootView.findViewById(R.id.layout_login))
					.setVisibility(View.GONE);
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page0);
			SplashActivity.page_flag = false;
			break;
		case 1:
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setVisibility(View.VISIBLE);
			((View) rootView.findViewById(R.id.layout_login))
					.setVisibility(View.GONE);
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page1);
			SplashActivity.page_flag = false;
			break;
		case 2:
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setVisibility(View.VISIBLE);
			((View) rootView.findViewById(R.id.layout_login))
					.setVisibility(View.GONE);
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page2);
			SplashActivity.page_flag = false;
			break;
		case 3:
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setVisibility(View.VISIBLE);
			((View) rootView.findViewById(R.id.layout_login))
					.setVisibility(View.GONE);
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page3);
			SplashActivity.page_flag = false;
			break;
		case 4:
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setVisibility(View.VISIBLE);
			((View) rootView.findViewById(R.id.layout_login))
					.setVisibility(View.GONE);
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page4);
			SplashActivity.page_flag = false;
			break;
		case 5:
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setVisibility(View.GONE);
			((View) rootView.findViewById(R.id.layout_login))
					.setVisibility(View.VISIBLE);

			final AlphaAnimation layout_animation = new AlphaAnimation(
					(float) 0.0, (float) 1.0);
			((ImageView) rootView.findViewById(R.id.imageView))
					.startAnimation(layout_animation);
			((LinearLayout) rootView.findViewById(R.id.layout_hidden))
					.setVisibility(View.VISIBLE);
			layout_animation.setDuration(2000);
			layout_animation.setFillAfter(true);
			
			((LinearLayout) rootView.findViewById(R.id.layout_hidden))
					.startAnimation(layout_animation);
			
			SplashActivity.page_flag = true;
			break;

		}

		return rootView;
	}
}
