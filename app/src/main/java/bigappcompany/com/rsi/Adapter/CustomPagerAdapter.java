package bigappcompany.com.rsi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import bigappcompany.com.rsi.Model.PhotoModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.PicassoTrustAll;

public class CustomPagerAdapter extends PagerAdapter {

	Context mContext;
	Activity activity;
	LayoutInflater mLayoutInflater;
	ArrayList<PhotoModel> models;
	Timer timer;
	int page = 1;
	private boolean isDynamic = false;
	private ViewPager viewPager;

	@Override
	public int getCount() {
		return models.size();
	}

	public void setData(ArrayList<PhotoModel> res, Context context, Activity activity) {
		this.models = res;
		this.mContext = context;
		this.activity = activity;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void isDynamic(boolean isAnimatable) {
		this.isDynamic = isAnimatable;
	}

	public void setVP(ViewPager viewPager) {
		this.viewPager = viewPager;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

		final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager);
		try {
			if(!isDynamic)
			{
					Picasso.with(activity).load(models.get(position).getPhoto()).resize(imageView.getResources().getDisplayMetrics().widthPixels,imageView.getResources().getDisplayMetrics().heightPixels).centerCrop().into(imageView);
			}
			else {
				ViewTreeObserver vto = imageView.getViewTreeObserver();
				vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
											 @Override
											 public boolean onPreDraw() {
												 try {
													 PicassoTrustAll.getInstance(activity).load(models.get(position).getPhoto()).placeholder(R.drawable.placeholder).resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight()).centerCrop().into(imageView);
												 }
												 catch (Exception e)
												 {
													 e.printStackTrace();
												 }


												 return true;
											 }
										 });

			}
		} catch (Exception e) {
			Log.e("image Exception", e.getMessage());
		}

		container.addView(itemView);
		if (isDynamic) {
			if (timer == null) {
				pageSwitcher(3);
			}
		}

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((LinearLayout) object);
	}

	public void pageSwitcher(int seconds) {
		timer = new Timer(); // At this line a new Thread will be created
		timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
		// in
		// milliseconds
	}

	public void stopTimer()
	{
		if(timer!=null)
			timer.cancel();
	}

	// this is an inner class...
	class RemindTask extends TimerTask {

		@Override
		public void run() {

			// As the TimerTask run on a seprate thread from UI thread we have
			// to call runOnUiThread to do work on UI thread.
			activity.runOnUiThread(new Runnable() {
				public void run() {
					viewPager.setCurrentItem(++page % models.size());
				}
			});

		}
	}

}
