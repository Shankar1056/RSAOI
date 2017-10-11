package bigappcompany.com.rsi.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.FacilitiesActivity;
import bigappcompany.com.rsi.activity.MainActivity;
import bigappcompany.com.rsi.activity.NewsActivity;
import bigappcompany.com.rsi.activity.NewsLetter;
import bigappcompany.com.rsi.activity.PayBill;


public class BottomFragment extends Fragment implements View.OnClickListener {
	int position=-1;
	ImageView img_home,img_news,img_fac,img_newsletter,img_paybill;
	TextView home_tv,news_tv,fac_tv,newsletter_tv,paybill_tv;
	LinearLayout rv_home,rv_news,rv_fac,rv_newsletter,rv_paybill;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.bottombar, container, false);
		img_home=(ImageView) rootView.findViewById(R.id.img_home);
		img_news=(ImageView) rootView.findViewById(R.id.img_news);
		img_fac=(ImageView) rootView.findViewById(R.id.img_fac);
		img_newsletter=(ImageView) rootView.findViewById(R.id.img_newsletter);
		img_paybill=(ImageView) rootView.findViewById(R.id.img_paybill);
		home_tv=(TextView)rootView.findViewById(R.id.home_tv);
		news_tv=(TextView)rootView.findViewById(R.id.news_tv);
		fac_tv=(TextView)rootView.findViewById(R.id.fac_tv);
		newsletter_tv=(TextView)rootView.findViewById(R.id.newsletter_tv);
		paybill_tv=(TextView)rootView.findViewById(R.id.paybill_tv);
		rv_home=(LinearLayout) rootView.findViewById(R.id.rv_home);
		rv_news=(LinearLayout) rootView.findViewById(R.id.rv_news);
		rv_fac=(LinearLayout) rootView.findViewById(R.id.rv_fac);
		rv_newsletter=(LinearLayout) rootView.findViewById(R.id.rv_newsletter);
		rv_paybill=(LinearLayout) rootView.findViewById(R.id.rv_paybill);
		
		
		rv_home.setOnClickListener(this);
		rv_fac.setOnClickListener(this);
		rv_news.setOnClickListener(this);
		rv_newsletter.setOnClickListener(this);
		rv_paybill.setOnClickListener(this);

		return rootView;
	}

	public void setReference(int position)
	{
		this.position=position;

	}

	private void setChecked() {
		if(position==0)
		{
			Log.e("class","MainActivity");
			//img_home.setColorFilter(Color.parseColor("#009788"));
			img_home.setImageResource(R.drawable.home);
			home_tv.setTextColor(Color.parseColor("#009788"));
		}else if(position==1)
		{
			Log.e("class","News");
			//img_news.setColorFilter(Color.parseColor("#009788"));
			img_news.setImageResource(R.drawable.newevents);
			news_tv.setTextColor(Color.parseColor("#009788"));
		}else if(position==2)
		{
			//img_fac.setColorFilter(Color.parseColor("#009788"));
			img_fac.setImageResource(R.drawable.fecilities);
			fac_tv.setTextColor(Color.parseColor("#009788"));
			Log.e("class","MainActivity");
		}else if(position==3)
		{
			//img_fac.setColorFilter(Color.parseColor("#009788"));
			img_newsletter.setImageResource(R.drawable.fecilities);
			newsletter_tv.setTextColor(Color.parseColor("#009788"));
			Log.e("class","MainActivity");
		}else if(position==4)
		{
			//img_fac.setColorFilter(Color.parseColor("#009788"));
			img_paybill.setImageResource(R.drawable.fecilities);
			paybill_tv.setTextColor(Color.parseColor("#009788"));
			Log.e("class","MainActivity");
		}
		
	}

	@Override
	public void onStart() {
		setChecked();
		super.onStart();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.rv_home:
				if(position!=0) {
					startActivity(new Intent(getContext(), MainActivity.class));
					getActivity().finish();
				}
				break;
			case R.id.rv_news:
				if(position!=1) {
					startActivity(new Intent(getContext(), NewsActivity.class));
					//getActivity().finish();
				}
				break;
			case R.id.rv_fac:
				if(position!=2)
				{
					startActivity(new Intent(getContext(), FacilitiesActivity.class));
				}
				break;
			case R.id.rv_newsletter:
				if(position!=3)
				{
					startActivity(new Intent(getContext(), NewsLetter.class));
				}
				break;
			case R.id.rv_paybill:
				if(position!=4)
				{
					startActivity(new Intent(getContext(), PayBill.class));
				}
				break;
		}
	}
}


