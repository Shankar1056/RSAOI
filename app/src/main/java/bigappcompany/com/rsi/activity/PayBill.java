package bigappcompany.com.rsi.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import bigappcompany.com.rsi.Fragment.MonthlyBillFragment;
import bigappcompany.com.rsi.Fragment.RechargeFragment;
import bigappcompany.com.rsi.R;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Jun 2017 at 3:56 PM
 */

public class PayBill extends AppCompatActivity {
	
	private static Toolbar toolbar;
	private static ViewPager viewPager;
	private static TabLayout tabLayout;
	
	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setContentView(R.layout.paybill);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		setupViewPager(viewPager);
		
		tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
		}
		
		//Implementing tab selected listener over tablayout
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
				switch (tab.getPosition()) {
					case 0:
						Log.e("TAG","Monthly Bill");
						break;
					case 1:
						Log.e("TAG","recharge");
						break;
					
				}
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}
	
	
	//Setting View Pager
	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(new MonthlyBillFragment("Monthly Bill"), "Monthly Bill");
		adapter.addFrag(new RechargeFragment("Recharge"), "Recharge");
		viewPager.setAdapter(adapter);
	}
	
	
	//View Pager fragments setting adapter class
	class ViewPagerAdapter extends FragmentPagerAdapter {
		private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
		private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist
		
		public ViewPagerAdapter(FragmentManager manager) {
			super(manager);
		}
		
		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}
		
		@Override
		public int getCount() {
			return mFragmentList.size();
		}
		
		
		//adding fragments and title method
		public void addFrag(Fragment fragment, String title) {
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				//onBackPressed();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
		
}
