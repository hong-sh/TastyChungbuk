package adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chungbuk.tasty.R;

import dto.StoreInfo;

public class StoreInfoAdapter extends ArrayAdapter<StoreInfo>{
	private Context context;
	private ArrayList<StoreInfo> items;

	public StoreInfoAdapter(Context context, int layoutViewResourceId, ArrayList<StoreInfo> items) {
		super(context, layoutViewResourceId, items);
		
		this.context = context;
		this.items = items;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listview_store, null);
		}
		
		StoreInfo store = items.get(position);
		
		((TextView)convertView.findViewById(R.id.textView_name)).setText(store.get_name());
		((TextView)convertView.findViewById(R.id.textView_address)).setText(store.get_address());
		((TextView)convertView.findViewById(R.id.textView_review_count)).setText(String.valueOf(store.get_review_count()));
		((TextView)convertView.findViewById(R.id.textView_distance)).setText(store.get_distance());
		((RatingBar)convertView.findViewById(R.id.star_ratingbar)).setRating((float)((float)store.get_rate()/2.0));
		
		if(store.get_type().equals("0"))
		{
			((ImageView)convertView.findViewById(R.id.imageView_main)).setImageResource(R.drawable.listview_icon_kor);
			((View)convertView.findViewById(R.id.layout_right)).setBackgroundColor(Color.parseColor("#DC3E12"));
		}
		else if(store.get_type().equals("1"))
		{
			((ImageView)convertView.findViewById(R.id.imageView_main)).setImageResource(R.drawable.listview_icon_chinese);
			((View)convertView.findViewById(R.id.layout_right)).setBackgroundColor(Color.parseColor("#A47023"));
		}
			
		else if(store.get_type().equals("2"))
		{
			
			((ImageView)convertView.findViewById(R.id.imageView_main)).setImageResource(R.drawable.listview_icon_japan);
			((View)convertView.findViewById(R.id.layout_right)).setBackgroundColor(Color.parseColor("#2C4057"));
		}
		
		else if(store.get_type().equals("3"))
		{
			((ImageView)convertView.findViewById(R.id.imageView_main)).setImageResource(R.drawable.listview_icon_eng);
			((View)convertView.findViewById(R.id.layout_right)).setBackgroundColor(Color.parseColor("#F08719"));
		}
		else if(store.get_type().equals("4"))
		{
			((ImageView)convertView.findViewById(R.id.imageView_main)).setImageResource(R.drawable.listview_icon_etc);
			((View)convertView.findViewById(R.id.layout_right)).setBackgroundColor(Color.parseColor("#727272"));
		}
		
		
		
		return convertView;
	}

	
}