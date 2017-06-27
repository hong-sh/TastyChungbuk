package adapter;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chungbuk.tasty.FavoriteActivity;
import com.chungbuk.tasty.R;

import common.WebServiceUtil;
import dto.FavoriteInfo;

public class FavoriteAdapter extends ArrayAdapter<FavoriteInfo> {
	private Context context;
	private ArrayList<FavoriteInfo> items;
	private int user_idx;

	public FavoriteAdapter(Context context, int layoutViewResourceId,
			ArrayList<FavoriteInfo> items, int user_idx) {
		super(context, layoutViewResourceId, items);

		this.context = context;
		this.items = items;
		this.user_idx = user_idx;
	}

	FavoriteInfo store;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listview_favor, null);
		}

		store = items.get(position);

		((TextView) convertView.findViewById(R.id.textView_name)).setText(store
				.get_name());
		((TextView) convertView.findViewById(R.id.textView_address))
				.setText(store.get_address());
		((TextView) convertView.findViewById(R.id.textView_review_count))
				.setText(String.valueOf(store.get_review_count()));
		((RatingBar) convertView.findViewById(R.id.star_ratingbar))
				.setRating((float) ((float) store.get_rate() / 2.0));

		if (store.get_type().equals("0")) {
			((ImageView) convertView.findViewById(R.id.imageView_main))
					.setImageResource(R.drawable.listview_icon_kor);
			((View) convertView.findViewById(R.id.layout_right))
					.setBackgroundColor(Color.parseColor("#DC3E12"));
		} else if (store.get_type().equals("1")) {
			((ImageView) convertView.findViewById(R.id.imageView_main))
					.setImageResource(R.drawable.listview_icon_chinese);
			((View) convertView.findViewById(R.id.layout_right))
					.setBackgroundColor(Color.parseColor("#A47023"));
		}

		else if (store.get_type().equals("2")) {

			((ImageView) convertView.findViewById(R.id.imageView_main))
					.setImageResource(R.drawable.listview_icon_japan);
			((View) convertView.findViewById(R.id.layout_right))
					.setBackgroundColor(Color.parseColor("#2C4057"));
		}

		else if (store.get_type().equals("3")) {
			((ImageView) convertView.findViewById(R.id.imageView_main))
					.setImageResource(R.drawable.listview_icon_eng);
			((View) convertView.findViewById(R.id.layout_right))
					.setBackgroundColor(Color.parseColor("#F08719"));
		} else if (store.get_type().equals("4")) {
			((ImageView) convertView.findViewById(R.id.imageView_main))
					.setImageResource(R.drawable.listview_icon_etc);
			((View) convertView.findViewById(R.id.layout_right))
					.setBackgroundColor(Color.parseColor("#727272"));
		}

	
		return convertView;
	}

//	View.OnClickListener delete = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Log.d("MyTag", "v = " +v);
//			AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
//			alt_bld.setMessage("정말로 삭제하시겠습니까?").setPositiveButton("확인",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							new GetFavoriteTask().execute();
//						}
//					});
//			alt_bld.setNegativeButton("취소",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.cancel();
//						}
//					});
//			AlertDialog alert = alt_bld.create();
//			alert.setTitle("즐겨찾기 삭제");
//			// alert.setIcon(icon);
//			alert.show();
//
//		}
//	};
//
//	class GetFavoriteTask extends AsyncTask<String, String, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			return GetFavoriteSoap();
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//
//			if (result.equals("true")) {
//				for (int i = 0; i < items.size(); i++) {
//					if (items.get(i).get_ipx() == position) {
//						items.remove(i);
//						
//					}
//				}
//				
//				
//				items.remove(store);
//				notifyDataSetChanged();
//				notifyDataSetInvalidated();
//				// FavoriteAdapter.this.notifyDataSetChanged();
//
//			} else {
//				Toast.makeText(context, "즐겨찾기 삭제가 실패하였습니다.", Toast.LENGTH_SHORT)
//						.show();
//			}
//
//		}
//
//		private String GetFavoriteSoap() {
//			String result = "";
//			try {
//				SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
//						WebServiceUtil.tasty_set_favorites_method_name);
//
//				request.addProperty("store_idx", store.get_ipx());
//				request.addProperty("user_idx", 2);
//
//				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//						SoapEnvelope.VER11);
//				envelope.dotNet = true;
//				envelope.encodingStyle = SoapSerializationEnvelope.XSD;
//				envelope.setOutputSoapObject(request);
//
//				HttpTransportSE androidHttpTransport = new HttpTransportSE(
//						WebServiceUtil.URL);
//				androidHttpTransport.call(
//						WebServiceUtil.tasty_set_favorites_soap_action,
//						envelope);
//
//				result = envelope.getResponse().toString();
//				Log.d("MyTag", "result = " + result);
//
//			} catch (Exception e) {
//				result = "false";
//				Log.e("MyTag", "getInfoSoap Error : " + e.toString());
//			}
//
//			return result;
//		}
//
//	}

}