package com.chungbuk.tasty;

import java.util.ArrayList;
import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

public class zListView_tempActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	private View searchView;
	private View loadingStatusView;
	private ListView searchListView;

	private ArrayList<String> searchList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zactivity_listview_temp);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		searchView = findViewById(R.id.search_view_layout);
		loadingStatusView = findViewById(R.id.search_loading_status_layout);
		searchListView = (ListView) findViewById(R.id.search_list_view);
		searchListView.setOnItemClickListener(searchListOnItemClickListener);
		searchList = new ArrayList<String>();

		// Show the Up button in the action bar.
		setupActionBar();
		setupViewPager();
		searchLayoutVisibility(true);

	}

	/**
	 * Set up the
	 * {@link android.support.v7.app.ActionBarActivity.getSupportActionBar}.
	 */
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(android.R.color.transparent);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

	private void setupViewPager() {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		final ActionBar actionBar = getSupportActionBar();

		// actionBar.setStackedBackgroundDrawable(new
		// ColorDrawable(getResources().getColor(R.color.viewpagergray)));

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getSupportActionBar().setSelectedNavigationItem(
								position);
					}
				});
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < 6; i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			getSupportActionBar().addTab(
					getSupportActionBar().newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this)
							);
			
		}
	}

	/**
	 * Alternates on either showing the loading progress layout or the main view
	 * layout
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void searchLayoutVisibility(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mViewPager.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewPager.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			searchView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							searchView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
			searchView.setVisibility(show ? View.GONE : View.VISIBLE);
		}

//		getSupportActionBar().setNavigationMode(
//				show ? ActionBar.NAVIGATION_MODE_TABS
//						: ActionBar.NAVIGATION_MODE_STANDARD);
	}

	// @Override
	// protected void onNewIntent(Intent intent) {
	// super.onNewIntent(intent);
	// setIntent(intent);
	// handleIntent(intent);
	// }

	// private void handleIntent(Intent intent) {
	// if (intent != null) {
	// if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
	// doSearch(intent.getStringExtra(SearchManager.QUERY));
	// } else if (intent.getAction().equals(Intent.ACTION_VIEW)) {
	// getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
	// }
	// }
	// }

	// private void doSearch(String query) {
	// Bundle data = new Bundle();
	// data.putString("query", query);
	// getSupportLoaderManager().restartLoader(0, data, this);
	// }
	//
	// private void getPlace(String query) {
	// Bundle data = new Bundle();
	// data.putString("query", query);
	// getSupportLoaderManager().restartLoader(1, data, this);
	// }
	//
	// @Override
	// public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
	// // Log.d("TestActivity", "onCreateLoader");
	// CursorLoader cLoader = null;
	// // searchList.clear();
	// // showProgress(true);
	// // if (arg0 == 0)
	// // cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI,
	// null, null, new String[] { query.getString("query") }, null);
	// // else if (arg0 == 1)
	// // cLoader = new CursorLoader(getBaseContext(),
	// PlaceProvider.DETAILS_URI, null, null, new String[] {
	// query.getString("query") }, null);
	// return cLoader;
	// }
	//
	// @Override
	// public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
	// showLocations(c);
	// }

	/**
	 * Shows and drop pins to the searched places.
	 */
	private void showLocations(Cursor c) {
		// List<SearchPlace> searchList = new ArrayList<SearchPlace>();
		// while (c.moveToNext()) {
		// // searchList.add(c.getString(0));
		//
		// searchList.add(new SearchPlace(c.getString(0), c.getString(0),
		// Double.valueOf(c.getString(1)), Double.valueOf(c.getString(2))));
		// }
		//
		// showProgress(false);
		// // searchListView.setAdapter(new ArrayAdapter<SearchPlace>(this,
		// R.layout.listview_search_layout, searchList));
		//
		// ArrayAdapter<SearchPlace> adapter = new SearchListAdapter(this,
		// R.layout.listview_search_layout, searchList);
		// searchListView.setAdapter(adapter);

	}

	// @Override
	// public void onLoaderReset(Loader<Cursor> arg0) {
	// }

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
////
////		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////		final MenuItem searchMenuItem = menu
////		final SearchView searchView = (SearchView) MenuItemCompat
////				.getActionView(searchMenuItem);
////		searchView.setSearchableInfo(searchManager
////				.getSearchableInfo(getComponentName()));
////		searchView.setIconifiedByDefault(true);
////		searchView.setIconified(true);
////
////		searchView.setOnSearchClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View v) {
////				searchLayoutVisibility(false);
////			}
////		});
//
//		searchView.setOnCloseListener(new OnCloseListener() {
//
//			@Override
//			public boolean onClose() {
//				searchLayoutVisibility(true);
//				searchView.onActionViewCollapsed();
//				return true;
//			}
//		});

		return super.onCreateOptionsMenu(menu);
	}

	protected void hideSoftKeyboard(View view) {
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	AdapterView.OnItemClickListener searchListOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			// SearchPlace searchPlace = (SearchPlace)
			// searchListView.getAdapter().getItem(position);
			// Intent intent = new Intent();
			// intent.putExtra("latitude", searchPlace.getLatitude());
			// intent.putExtra("longitude", searchPlace.getLongitude());
			// setResult(RESULT_OK, intent);
			// finish();
		}

	};

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
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Bundle arguments = new Bundle();
			Fragment fragment = null;
			switch (position) {
			case 0:
				// arguments.putString("passenger_userid",
				// getIntent().getStringExtra("passenger_userid"));
				fragment = new FragmentChinese();
				fragment.setArguments(arguments);
				break;
			case 1:
				// arguments.putDouble("latitude",
				// getIntent().getDoubleExtra("latitude", 0.0));
				// arguments.putDouble("longitude",
				// getIntent().getDoubleExtra("longitude", 0.0));
				fragment = new FragmentKor();
				fragment.setArguments(arguments);
				break;
			case 2:
				// fragment = new FourSquareFragment();
				break;

			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 12;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.kor_fragment).toUpperCase(l);
			case 1:
				return getString(R.string.chinese_fragment).toUpperCase(l);
			case 2:
				return getString(R.string.japan_fragment).toUpperCase(l);
			case 3:
				return getString(R.string.eng_fragment).toUpperCase(l);
			case 4:
				return getString(R.string.etc_fragment).toUpperCase(l);
			case 5:
				return "전체".toUpperCase(l);
			case 6:
				return getString(R.string.kor_fragment).toUpperCase(l);
			case 7:
				return getString(R.string.chinese_fragment).toUpperCase(l);
			case 8:
				return getString(R.string.japan_fragment).toUpperCase(l);
			case 9:
				return getString(R.string.eng_fragment).toUpperCase(l);
			case 10:
				return getString(R.string.etc_fragment).toUpperCase(l);
			case 11:
				return "전체".toUpperCase(l);
			}
			return null;
		}
	}

}
