package bigappcompany.com.rsi.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import bigappcompany.com.rsi.Adapter.PhAdapter;
import bigappcompany.com.rsi.Model.PhotosModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.PicassoTrustAll;


public class PhotosFragment extends Fragment implements  View.OnClickListener {
	PhotosModel model;
	int position;
	ImageView img;
	RecyclerView recyclerView;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
		img=(ImageView)rootView.findViewById(R.id.img_photo);
		recyclerView=(RecyclerView)rootView.findViewById(R.id.photos_RV);

		GridLayoutManager manager=new GridLayoutManager(img.getContext(),3);
		recyclerView.setLayoutManager(manager);

		recyclerView.setAdapter(new PhAdapter(model.getImages()));
		ViewTreeObserver vto = img.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				int finalHeight, finalWidth;

				img.getViewTreeObserver().removeOnPreDrawListener(this);

				finalHeight = img.getMeasuredHeight();
				finalWidth = img.getMeasuredWidth();


				Log.e("height,width",finalHeight+","+finalWidth);
				//Picasso.with(img.getContext()).load(model.getPhoto()).resize(Math.max(finalHeight,finalWidth),Math.max(finalHeight,finalWidth)).centerCrop().into(img);
				PicassoTrustAll.getInstance(img.getContext()).load(model.getPhoto()).placeholder(R.drawable.placeholder).resize(finalWidth,finalHeight).centerCrop().into(img);
				return true;
			}
		});
		return rootView;
	}



	@Override
	public void onClick(View v) {

	}


	public static Fragment newInstance(int pos, PhotosModel model) {
		PhotosFragment fragment=new PhotosFragment();
		fragment.model=model;
		fragment.position=pos;
		return fragment;
	}
}


