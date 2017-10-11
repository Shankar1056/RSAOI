package bigappcompany.com.rsi.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.CustomPagerAdapter;
import bigappcompany.com.rsi.Adapter.DotsAdapter;
import bigappcompany.com.rsi.Adapter.PhotoAdapter;
import bigappcompany.com.rsi.Gesture.tooltip.Tooltip;
import bigappcompany.com.rsi.Model.ImageTypeModel;
import bigappcompany.com.rsi.Model.PhotoModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.BookingHistoryActivity;
import bigappcompany.com.rsi.activity.FeedBack;
import bigappcompany.com.rsi.activity.MainActivity;
import bigappcompany.com.rsi.activity.NotificationActivity;
import bigappcompany.com.rsi.activity.SplashActivity;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.ClsGeneral;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

import static android.content.Context.MODE_PRIVATE;


public class MainFragment extends Fragment implements OnTaskCompleted, View.OnClickListener {
	TextView maskTV;
	TextView ticket_Tv, feedback;
	RecyclerView PhotoRV;
	boolean isHidden = false;
	MainActivity activity;
	private int previousPage;
	private int previousState;
	private ImageView img_main, navIV;
	private boolean geasturesEnable;
	private ViewPager mViewPager;
	private CustomPagerAdapter mCustomPagerAdapter;
	private RecyclerView dots;
	private DotsAdapter dotsAdapter;
	private TextView cartcount;
	private Tooltip.ClosePolicy mClosePolicy = Tooltip.ClosePolicy.TOUCH_ANYWHERE_CONSUME;
	
	OnTaskCompleted listener = new OnTaskCompleted() {
		@Override
		public void onTaskCompleted(String response) {
			try {
				
				
					String status = new JSONObject(response).optString("status");
					if (status.equalsIgnoreCase
					    ("SESSION_EXPIRED")) {
						//Digits.logout();
						startActivity(new Intent(getActivity(), SplashActivity.class));
						getActivity().finish();
						return;
					}
				
				
				
				if (MainActivity.isAlive)
					((MainActivity) getActivity()).closeDialog();
				new Download_web(getContext(), MainFragment.this).execute(ApiUrl.IMG_TYPES);
				((MainActivity) getActivity()).showDailog("Please wait...");
				
				ArrayList<PhotoModel> models = new ArrayList<>();
				JSONArray jsonArray = new JSONObject(response).getJSONArray(JsonParser.DATA);
				for (int i = 0; i < jsonArray.length(); i++) {
					models.add(new PhotoModel(jsonArray.getJSONObject(i)));
				}
				mCustomPagerAdapter = new CustomPagerAdapter();
				mCustomPagerAdapter.setData(models, getContext(), getActivity());
				mCustomPagerAdapter.isDynamic(true);
				mCustomPagerAdapter.setVP(mViewPager);
				mViewPager.setAdapter(mCustomPagerAdapter);
				geasturesEnable = true;
				if (models.size() > 1) {
					//Dots
					
					dots.setHasFixedSize(true);
					dots.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
					dotsAdapter = new DotsAdapter(models.size());
					dots.setAdapter(dotsAdapter);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	};
	
	@Override
	public void onTaskCompleted(String response) {
		if (MainActivity.isAlive)
			((MainActivity) getActivity()).closeDialog();
		ArrayList<ImageTypeModel> models = new ArrayList<>();
		try {
			JSONObject object = new JSONObject(response);
			if (object.getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
				JSONArray array = object.getJSONArray(JsonParser.DATA);
				for (int i = 0; i < array.length(); i++) {
					models.add(new ImageTypeModel(array.getJSONObject(i)));
					
				}
				PhotoRV.setAdapter(new PhotoAdapter(models));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.content_main, container, false);
		
		
		dots = (RecyclerView) rootView.findViewById(R.id.dots);
		ticket_Tv = (TextView) rootView.findViewById(R.id.ticketTV);
		feedback = (TextView) rootView.findViewById(R.id.feedback);
		feedback.setOnClickListener(this);
		ticket_Tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getContext(), BookingHistoryActivity.class));
			}
		});
		
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager_zoom);
		
		navIV = (ImageView) rootView.findViewById(R.id.nav_IV);
		navIV.setOnClickListener(this);
		
		// Notification icon listener
		rootView.findViewById(R.id.iv_notification).setOnClickListener(this);
		
		maskTV = (TextView) rootView.findViewById(R.id.maskTV);
		maskTV.setOnClickListener(this);
		
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				//Log.e("onPageScrool","position "+position+" , positionoffset "+positionOffset+" , positionoffsetPixels"+positionOffsetPixels);
			}
			
			@Override
			public void onPageSelected(int position) {
				Log.e("onPageSelected", "position " + position);
				if (previousPage != position) {
					//changed
					
					dotsAdapter.setSelected(position);
					dotsAdapter.notifyDataSetChanged();
				}
				previousPage = position;
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				Log.e("onScrollStateChanged", "state " + state);
				
				previousState = state;
				
			}
		});
		
		img_main = (ImageView) rootView.findViewById(R.id.image_zoom);
		cartcount = (TextView) rootView.findViewById(R.id.cartcount);
		cartcount.setOnClickListener(this);
		
		PhotoRV = (RecyclerView) rootView.findViewById(R.id.photos_RV);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		PhotoRV.setLayoutManager(layoutManager);
		
		
		//Picasso.with(albumArtIV.getContext()).load(R.drawable.nav_banner).fit().centerCrop();
		try {
			String sessiontoken = ClsGeneral.getPreferences(getActivity(),ClsGeneral.UPDATESESSIONTOKEN);
			new Download_web(getContext(), listener).execute(ApiUrl.SLIDERS + sessiontoken);
			((MainActivity) getActivity()).showDailog("Please wait...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		Tooltip.make(
		    getContext(),
		    new Tooltip.Builder()
			.anchor(feedback, Tooltip.Gravity.BOTTOM)
			.closePolicy(mClosePolicy, 40000)
			.text("Feedback")
			.withArrow(true)
			.withOverlay(false)
			.maxWidth((int) (metrics.widthPixels / 2.5))
			//.withCallback(getActivity().getApplicationContext())
			.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
			.build()
		).show();
		
		
		getnewnotification();
		
		return rootView;
	}
	
	private void getnewnotification() {
		Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					closeDialog();
					if (new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equalsIgnoreCase("TRUE")) {
						String count = new JSONObject(response).getJSONArray("data").getJSONObject(0).getString
						    ("count");
						if (Integer.parseInt(count) > 0) {
							cartcount.setVisibility(View.VISIBLE);
							RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.rotatenotification);
							cartcount.setAnimation(rotate);
							cartcount.setText(count);
							MediaPlayer mPlayer = MediaPlayer.create(getActivity(), R.raw.notification);
							mPlayer.start();
						} else {
							cartcount.setVisibility(View.GONE);
							cartcount.setText("0");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		web.setReqType(true);
		try {
			String cs_id = getActivity().getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
			    (JsonParser
				.CS_ID, "");
			web.execute(ApiUrl.GETNOTIFICATIONCOUNT + "" + ApiUrl.API_KEY + "/" + cs_id);
			showDailog("Please wait we are booking for you...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nav_IV:
				isHidden = !isHidden;
				if (isHidden) {
					activity.animateView(isHidden);
					maskTV.setVisibility(View.VISIBLE);
				} else {
					maskTV.setVisibility(View.GONE);
				}
				break;
			case R.id.maskTV:
				isHidden = !isHidden;
				maskTV.setVisibility(View.GONE);
				activity.animateView(isHidden);
				break;
			
			case R.id.iv_notification:
				startActivity(new Intent(getContext(), NotificationActivity.class));
				cartcount.setText("0");
				cartcount.setVisibility(View.GONE);
				break;
			case R.id.cartcount:
				startActivity(new Intent(getContext(), NotificationActivity.class));
				cartcount.setText("0");
				cartcount.setVisibility(View.GONE);
				break;
			case R.id.feedback:
				activity.startActivity(new Intent(activity, FeedBack.class));
				break;
		}
	}
	
	public boolean getStatus() {
		return isHidden;
	}
	
	public void setIsHidden(boolean status) {
		isHidden = status;
	}
	
	public void hide() {
		maskTV.setVisibility(View.GONE);
	}
	
	public void setInstance(MainActivity mainActivity) {
		activity = mainActivity;
	}
	
	public void stopTimer() {
		if (mCustomPagerAdapter != null) {
			mCustomPagerAdapter.stopTimer();
		}
	}
	
	public void startTimer() {
		if (mCustomPagerAdapter != null)
			mCustomPagerAdapter.pageSwitcher(3);
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	ProgressDialog dialog;
	
	public void showDailog(String msg) {
		dialog = new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(msg);
		dialog.show();
	}
	
	public void closeDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.cancel();
	}
	
	public boolean isValidEmail1(CharSequence target) {
		return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
}


