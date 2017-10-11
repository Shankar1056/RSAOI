package bigappcompany.com.rsi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import bigappcompany.com.rsi.Model.NewsModel;
import bigappcompany.com.rsi.Model.PhotoModel;
import bigappcompany.com.rsi.R;

public class ScrollingActivity extends BaseActivity{
    private static final String TAG_BOTTOM = "bottom_fragment";
    NestedScrollView mScrollView;
    CardView card_content;
    private static final String EXTRA_TRAVEL = "EXTRA_TRAVEL";
    NewsModel model;
    ImageView rp_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scrolling);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setStatusBarColor();

        mScrollView=(NestedScrollView) findViewById(R.id.scroll);
        card_content=(CardView) findViewById(R.id.card_content);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());
        rp_back=(ImageView) findViewById(R.id.back);
        rp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        try
        {
            model = (NewsModel) getIntent().getSerializableExtra("model");
            ((TextView)findViewById(R.id.title_tv)).setText(model.getTitle());
            ((TextView)findViewById(R.id.des_tv)).setText(model.getDescription());
            ((TextView)findViewById(R.id.date_tv)).setText(model.getDate());
            final ImageView img=(ImageView)findViewById(R.id.img_photo);
            ViewTreeObserver vto = img.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    Picasso.with(img.getContext()).load(model.getPhoto()).resize(img.getMeasuredWidth(),img.getMeasuredHeight()).centerCrop().into(img);
                    return true;
                }
            });

        }
        catch (Exception e)
        {

        }

        showFragment(-1);

    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }




    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

            private int mImageViewHeight;
        float scale=1;

            public ScrollPositionObserver() {
                mImageViewHeight = getResources().getDisplayMetrics().widthPixels;
            }

            @Override
            public void onScrollChanged() {
                int scrollY = Math.min(Math.max(mScrollView.getScrollY(), 0), mImageViewHeight);

                // changing position of ImageView
                //card_content.setTranslationY(scrollY / 2);
                Log.e("scroll",mScrollView.getScrollY()+" ");
                if(mScrollView.getScrollY()<=100)
                {

                        mScrollView.setScaleX(1.0f+((float)mScrollView.getScrollY()/1000));
                        mScrollView.setScaleY(1.0f+((float)mScrollView.getScrollY()/1000));


                }
                else if(mScrollView.getScrollY()>100)
                {
                    mScrollView.setScaleX(1.1f);
                    mScrollView.setScaleY(1.1f);
                }
                //card_content.setScaleX();

            }
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

    public static Intent newInstance(Context context, PhotoModel model) {
        Intent intent = new Intent(context, ScrollingActivity.class);

        intent.putExtra(EXTRA_TRAVEL,  model.toString());
        return intent;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
