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

public class GuidePageFragment extends Fragment {

	private int mPageNumber;
	private Handler handler;
	static ViewGroup rootView = null;
	public static GuidePageFragment create(int pageNumber) {
		GuidePageFragment fragment = new GuidePageFragment();
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
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_page, container, false);
		
		switch (mPageNumber) {
		case 0:
			
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page0);
			break;
		case 1:
			
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page1);
			break;
		case 2:
			
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page2);
			break;
		case 3:
			
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page3);
			break;
		case 4:
			
			((ImageView) rootView.findViewById(R.id.imageView_page))
					.setImageResource(R.drawable.page4);
			break;
		

		}

		return rootView;
	}
}
