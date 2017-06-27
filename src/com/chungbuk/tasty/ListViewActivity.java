package com.chungbuk.tasty;

import java.util.List;
import java.util.Locale;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import common.ActivityManager;

public class ListViewActivity extends ActionBarActivity implements
		ActionBar.TabListener{

	/**
	 * ? The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private int what;
	private SearchView mSearchView;
	private ActivityManager act = ActivityManager.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Intent intent = getIntent();
		what = intent.getIntExtra("what", 1);
		act.addlist(this);
		setupActionBar();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));

		}

		actionBar.setSelectedNavigationItem(what - 1);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);

		return true;

	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		if (item.getItemId() == R.id.action_map) {
			Intent intent = new Intent(ListViewActivity.this,
					MainActivity.class);
			startActivity(intent);

		}

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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			Bundle arguments = new Bundle();
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new FragmentAll();
				fragment.setArguments(arguments);
				break;
			case 1:
				fragment = new FragmentKor();
				fragment.setArguments(arguments);
				break;
			case 2:
				fragment = new FragmentChinese();
				fragment.setArguments(arguments);
				break;
			case 3:
				fragment = new FragmentJapan();
				fragment.setArguments(arguments);
				break;
			case 4:
				fragment = new FragmentEng();
				fragment.setArguments(arguments);
				break;
			case 5:
				fragment = new FragmentEtc();
				fragment.setArguments(arguments);
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "    전  체    ".toUpperCase(l);
			case 1:
				return "    한  식    ".toUpperCase(l);
			case 2:
				return "    중  식    ".toUpperCase(l);
			case 3:
				return "    일  식    ".toUpperCase(l);
			case 4:
				return "    양  식    ".toUpperCase(l);
			case 5:
				return "    기  타    ".toUpperCase(l);
			}
			return null;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			act.removeAct(this);
			startActivity(intent);
			break;
		}
		return false;
	}



}
