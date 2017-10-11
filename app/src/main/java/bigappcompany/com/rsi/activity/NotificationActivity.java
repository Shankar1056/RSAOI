package bigappcompany.com.rsi.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Model.NotificationModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

@SuppressWarnings("ConstantConditions")
public class NotificationActivity extends BaseActivity {
	private static final String TAG = "NotificationActivity";
	private NotificationAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		
		mAdapter = new NotificationAdapter();
		RecyclerView notifRV = (RecyclerView) findViewById(R.id.rv_notification);
		notifRV.setAdapter(mAdapter);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//showDailog("Please wait Fetching information...");
		new Download_web(this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					JSONObject object = new JSONObject(response);
					JSONArray data = object.getJSONArray("data");
					
					for (int i = 0; i < data.length(); i++) {
						mAdapter.addItem(new NotificationModel(data.getJSONObject(i)));
					}
					
					closeDialog();
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}).execute(ApiUrl.NOTIFICATIONS);
		
		
		notificationread();
	}
	
	private void notificationread() {
		
		Download_web web = new Download_web(NotificationActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					closeDialog();
					if (new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equalsIgnoreCase("TRUE")) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		web.setReqType(false);
		try {
			String cs_id = NotificationActivity.this.getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE)
			    .getString
			    (JsonParser
				.CS_ID, "");
			web.setData(
			    (new JSONObject()
				.put("userId",cs_id)
				.put("apikey", ApiUrl.API_KEY)
			    ).toString());
			web.execute(ApiUrl.NOTIFICATIONREAD);
			showDailog("Please wait we are booking for you...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class ViewHolder extends RecyclerView.ViewHolder {
		ImageView imageIV;
		TextView titleTV, descTV, dateTV;
		
		public ViewHolder(View itemView) {
			super(itemView);
			
			imageIV = (ImageView) itemView.findViewById(R.id.iv_image);
			titleTV = (TextView) itemView.findViewById(R.id.tv_title);
			descTV = (TextView) itemView.findViewById(R.id.tv_desc);
			dateTV = (TextView) itemView.findViewById(R.id.tv_date);
		}
	}
	
	private class NotificationAdapter extends RecyclerView.Adapter<ViewHolder> {
		private final ArrayList<NotificationModel> mItemList = new ArrayList<>();
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = getLayoutInflater().inflate(R.layout.item_notification, parent, false);
			return new ViewHolder(itemView);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			NotificationModel item = mItemList.get(position);
			
			Picasso.with(NotificationActivity.this)
			    .load(item.getImageUrl())
			    .resizeDimen(R.dimen.notif_size, R.dimen.notif_size)
			    .centerCrop()
			    .into(holder.imageIV);
			
			holder.titleTV.setText(item.getTitle());
			holder.descTV.setText(item.getDesc());
			holder.dateTV.setText(item.getDate());
		}
		
		@Override
		public int getItemCount() {
			return mItemList.size();
		}
		
		void addItem(NotificationModel item) {
			mItemList.add(item);
			notifyItemInserted(mItemList.size() - 1);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}
