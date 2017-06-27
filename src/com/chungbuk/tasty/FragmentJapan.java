package com.chungbuk.tasty;

import java.util.ArrayList;
import java.util.List;

import dto.StoreInfo;
import adapter.StoreInfoAdapter;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentJapan extends ListFragment {
	
	ArrayList<StoreInfo> japan_list = new ArrayList<StoreInfo>();
	/**
	 * Default constructor.
	 */
	public FragmentJapan() {
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_japan, container, false);
		
		for(int i=0; i<MainActivity.all_list.size(); i++)
		{
			if(MainActivity.all_list.get(i).get_type().equals("2"))
			{
				japan_list.add(MainActivity.all_list.get(i));
			}
		}
		
		ArrayAdapter<StoreInfo> adapter = new StoreInfoAdapter(getActivity(), R.layout.listview_store, japan_list);
		setListAdapter(adapter);
		
		
		return rootView;
	}
	
	@Override
	public void onListItemClick(ListView listview, View view, int position, long id) {
		super.onListItemClick(listview, view, position, id);
		
		StoreInfo item = (StoreInfo) getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity().getApplicationContext(), DetailPageActivity.class);
		intent.putExtra("ipx", item.get_ipx());
		intent.putExtra("what", 4);
		startActivity(intent);
	}
	
	
	
}
