package com.chungbuk.tasty;

import java.util.ArrayList;

import adapter.StoreInfoAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dto.StoreInfo;

public class FragmentKor extends ListFragment {
	
	ArrayList<StoreInfo> kor_list = new ArrayList<StoreInfo>();
	/**
	 * Default constructor.
	 */
	public FragmentKor() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_kor, container, false);
		
		for(int i=0; i<MainActivity.all_list.size(); i++)
		{
			if(MainActivity.all_list.get(i).get_type().equals("0"))
			{
				kor_list.add(MainActivity.all_list.get(i));
			}
		}
		
		ArrayAdapter<StoreInfo> adapter = new StoreInfoAdapter(getActivity(), R.layout.listview_store, kor_list);
		setListAdapter(adapter);
		
		return rootView;
	}
	
	@Override
	public void onListItemClick(ListView listview, View view, int position, long id) {
		super.onListItemClick(listview, view, position, id);
		
		StoreInfo item = (StoreInfo) getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity().getApplicationContext(), DetailPageActivity.class);
		intent.putExtra("ipx", item.get_ipx());
		intent.putExtra("what", 2);
		startActivity(intent);
	}
	
	
}
