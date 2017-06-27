package com.chungbuk.tasty;

import adapter.StoreInfoAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dto.StoreInfo;

public class FragmentAll extends ListFragment{
	
	//ArrayList<StoreInfo> list = new ArrayList<StoreInfo>();
	
	
	/**
	 * Default constructor.
	 */
	public FragmentAll() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_all, container, false);
		
		ArrayAdapter<StoreInfo> adapter = new StoreInfoAdapter(getActivity(), R.layout.listview_store, MainActivity.all_list);
		setListAdapter(adapter);
		
		return rootView;
	}
	
	@Override
	public void onListItemClick(ListView listview, View view, int position, long id) {
		super.onListItemClick(listview, view, position, id);
		
		StoreInfo item = (StoreInfo) getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity().getApplicationContext(), DetailPageActivity.class);
		intent.putExtra("ipx", item.get_ipx());
		intent.putExtra("what", 1);
		startActivity(intent);
		
	}

	
	
	
	
}
