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

import dto.ReviewInfo;
import dto.StoreInfo;

public class ReviewInfoAdapter extends ArrayAdapter<ReviewInfo>{
	private Context context;
	private ArrayList<ReviewInfo> items;

	public ReviewInfoAdapter(Context context, int layoutViewResourceId, ArrayList<ReviewInfo> items) {
		super(context, layoutViewResourceId, items);
		
		this.context = context;
		this.items = items;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listview_review, null);
		}
		
		ReviewInfo review = items.get(position);
		if (review.get_user_sex().equals("남")) {

			((ImageView)convertView.findViewById(R.id.imageView_main))
					.setImageResource(R.drawable.icon_man);

		} else {
			((ImageView) convertView.findViewById(R.id.imageView_main))
					.setImageResource(R.drawable.icon_woman);

		}

		((TextView) convertView.findViewById(R.id.textView_name)).setText(review.get_user_name());
		((TextView) convertView.findViewById(R.id.textView_gender)).setText(review.get_user_sex());
		((TextView) convertView.findViewById(R.id.textView_age)).setText(review.get_user_age());
		((RatingBar) convertView.findViewById(R.id.ratingBar)).setRating(review.get_rate());
		((TextView) convertView.findViewById(R.id.textView_opinion)).setText(review.get_comment());
		((TextView) convertView.findViewById(R.id.textView_date)).setText(review.get_mod_date());
		
		
		return convertView;
	}

	
}