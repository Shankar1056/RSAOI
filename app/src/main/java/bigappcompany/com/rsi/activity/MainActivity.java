package bigappcompany.com.rsi.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import bigappcompany.com.rsi.Fragment.MainFragment;
import bigappcompany.com.rsi.R;


public class MainActivity extends BaseActivity {
	private static final String TAG_FRAGMENT = "fragment";
	private static final String TAG_BOTTOM = "bottom_fragment";
	public static boolean isAlive = false;
	public int catg_id = 0;
	ListView drawer_lv;
	CategoryAdapter adapter;
	int[] ids = new int[]{0, 1, 2, 3, 4};
	String[] values = new String[]{"HOME", "ABOUT", "CONTACT", "LOGOUT"};
	MainFragment fragment;
	LinearLayout MainLL;
	private boolean doubleBackToExitPressedOnce = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		//setStatusbarcolor for higher devices
		setStatusBarColor();
		isAlive = true;
		drawer_lv = (ListView) findViewById(R.id.left_drawer);
		adapter = new CategoryAdapter();
		adapter.setData(this, ids, values);
		drawer_lv.setAdapter(adapter);
		MainLL = (LinearLayout) findViewById(R.id.mainVRL);
		
		fragment = new MainFragment();
		getSupportFragmentManager().beginTransaction()
		    .replace(R.id.content_home, fragment, TAG_FRAGMENT)
		    .commit();
		fragment.setInstance(this);
		
		showFragment(0);
	}
	
	private void setStatusBarColor() {
		Window window = this.getWindow();
		
		// clear FLAG_TRANSLUCENT_STATUS flag:
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		
		// finally change the color
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.setStatusBarColor(Color.parseColor("#00796A"));
		}
	}
	
	@Override
	public void onBackPressed() {
		if (fragment.getStatus()) {
			fragment.hide();
			animateView(false);
		} else {
			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
			}
			
			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					doubleBackToExitPressedOnce = false;
				}
			}, 3000);
		}
	}
	
	public void animateView(boolean isHidden) {
		if (isHidden) {
			
			
			MainLL.animate().rotationY(-10f).translationX(getResources().getDisplayMetrics().widthPixels * 3 / 5).setDuration(500).start();
			
			
		} else {
			Log.e("animout", "inside");
			
			MainLL.animate().rotationY(0f).translationX(0).setDuration(500).start();
			fragment.setIsHidden(false);
		}
	}
	
	@Override
	public void onPause() {
		isAlive = false;
		fragment.stopTimer();
		//overridePendingTransition(R.anim.left_out,R.anim.activity_close_translate);
		super.onPause();
		overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
	}
	
	@Override
	public void onResume() {
		isAlive = true;
		fragment.startTimer();
		super.onResume();
	}
	
}
