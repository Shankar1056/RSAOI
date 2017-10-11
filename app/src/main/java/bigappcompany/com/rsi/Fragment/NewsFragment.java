package bigappcompany.com.rsi.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.NewsPagerAdapter;
import bigappcompany.com.rsi.Adapter.PhAdapter;
import bigappcompany.com.rsi.Model.NewsModel;
import bigappcompany.com.rsi.Model.PhotosModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.expPager.ExpandingViewPagerTransformer;
import bigappcompany.com.rsi.network.PicassoTrustAll;


public class NewsFragment extends Fragment implements  View.OnClickListener {

	ViewPager pager_news;
	View rootView;
	private ArrayList<NewsModel> models;
	private int position;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_news, container, false);
		setupViewPager();
		NewsPagerAdapter adapter=new NewsPagerAdapter();
		adapter.setData(models,rootView.getContext(),this.getActivity());
		pager_news.setAdapter(adapter);
		return rootView;
	}

	private void setupViewPager() {
		pager_news=(ViewPager) rootView.findViewById(R.id.pager_news);
		//ViewGroup.LayoutParams layoutParams = pager_news.getLayoutParams();
		//layoutParams.width = ((Activity) pager_news.getContext()).getWindowManager().getDefaultDisplay().getWidth() / 7 * 6;
		//layoutParams.height = (int) ((layoutParams.width / 0.75));



		if (pager_news.getParent() instanceof ViewGroup) {
			ViewGroup viewParent = ((ViewGroup) pager_news.getParent());
			viewParent.setClipChildren(false);
			pager_news.setClipChildren(false);
		}
		pager_news.setOffscreenPageLimit(2);
		pager_news.setPageTransformer(true,new ExpandingViewPagerTransformer());

	}

	@Override
	public void onClick(View v) {

	}


	public static Fragment newInstance(int pos, ArrayList<NewsModel> models) {
		NewsFragment fragment=new NewsFragment();
		fragment.models=models;
		fragment.position=pos;
		return fragment;
	}
}


