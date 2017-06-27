package com.chungbuk.tasty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import common.ActivityManager;

public class GuideActivity extends ActionBarActivity {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	PagerAdapter mPagerAdapter;

	private ActivityManager act = ActivityManager.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// getActionBar().hide();
		setContentView(R.layout.activity_guide);

		act.addlist(this);
		setupActionBar();
		init();
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

		((Button) findViewById(R.id.button_ok))
				.setOnClickListener(buttonClickListener);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);

	}

	View.OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.button_ok) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				act.removeAct(GuideActivity.this);
				startActivity(intent);
			}

		}
	};

	private class PagerAdapter extends FragmentStatePagerAdapter {

		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 해당하는 page의 Fragment를 생성합니다.

			return GuidePageFragment.create(position);
		}

		@Override
		public int getCount() {
			return 5; // 총 5개의 page를 보여줍니다.
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			act.removeAct(GuideActivity.this);
			startActivity(intent);
			break;
		}
		return false;
	}

}
