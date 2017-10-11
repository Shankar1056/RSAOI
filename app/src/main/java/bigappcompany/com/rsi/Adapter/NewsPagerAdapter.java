package bigappcompany.com.rsi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;

import bigappcompany.com.rsi.Model.NewsModel;
import bigappcompany.com.rsi.activity.NewsActivity;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.RippleView;


/**
 * Created by Admin on 27-07-2016.
 */
public class NewsPagerAdapter extends PagerAdapter {

	Context mContext;
	Activity activity;
	LayoutInflater mLayoutInflater;
	RippleView rp_item;
	ArrayList<NewsModel> models;
	Timer timer;
	int page = 1;
	private boolean isDynamic = false;
	private ViewPager viewPager;

	@Override
	public int getCount() {
		return models.size();
	}

	public void setData(ArrayList<NewsModel> res, Context context, Activity activity) {
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
		final View itemView = mLayoutInflater.inflate(R.layout.news_item_vp, container, false);

		final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_news);
		((TextView)itemView.findViewById(R.id.title_news)).setText(models.get(position).getTitle());
		((TextView)itemView.findViewById(R.id.des_news)).setText(models.get(position).getDescription());
		((TextView)itemView.findViewById(R.id.dt_news)).setText(models.get(position).getDate());
		((TextView)itemView.findViewById(R.id.title_news)).setText(models.get(position).getTitle());

		rp_item=(RippleView)itemView.findViewById(R.id.rp_news_item);
		rp_item.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
			@Override
			public void onComplete(RippleView rippleView) {
				((NewsActivity)activity).startInfoActivity(imageView,models.get(position));
			}
		});
		try {

				Log.e("inside","this");
				ViewTreeObserver vto = imageView.getViewTreeObserver();
				vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
				{
					 @Override
					 public boolean onPreDraw() {
						 Log.e("width,height",imageView.getMeasuredHeight()+" "+imageView.getMeasuredWidth());
						 Picasso.with(activity).load(models.get(position).getPhoto()).placeholder(R.drawable.placeholder).resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight()).centerCrop().into(imageView);
						 return true;
					 }
				});

		} catch (Exception e) {
			Log.e("image Exception", e.getMessage()+" ");
		}


		container.addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((CardView) object);
	}

}
