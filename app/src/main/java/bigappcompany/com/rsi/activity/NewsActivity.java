package bigappcompany.com.rsi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.NewsPagerAdapter;
import bigappcompany.com.rsi.Model.Constants;
import bigappcompany.com.rsi.Model.NewsModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.expPager.ExpandingViewPagerTransformer;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class NewsActivity extends BaseActivity implements OnTaskCompleted{
    ViewPager pager_news;
    TabLayout tabs;
    private static final String TAG_BOTTOM = "bottom";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setupViewPager();

        new Download_web(this,this).execute(ApiUrl.BOTH);
        showDailog("Please wait...");


        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        tabs=(TabLayout)findViewById(R.id.tabs);
        setStatusBarColor();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(getApplicationContext(),tab.getText(),Toast.LENGTH_LONG).show();
                if(allModels!=null) {
                    if(tab.getPosition()<allModels.size())
                    setData(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        showFragment(1);
    }


    NewsPagerAdapter adapter,adapter1;
    void setData(int pos) {
        ArrayList<NewsModel> models=allModels.get(pos);
        if (pos == 0) {
            if(adapter==null)
            adapter = new NewsPagerAdapter();
            adapter.setData(models, this, this);
            pager_news.setAdapter(adapter);
        }
        else
        {
            if(adapter1==null)
            {
                adapter1=new NewsPagerAdapter();
            }
            adapter1.setData(models,this,this);
            pager_news.setAdapter(adapter1);
        }
    }
    private void setupViewPager() {
        pager_news=(ViewPager) findViewById(R.id.pager_news);
        ViewGroup.LayoutParams layoutParams = pager_news.getLayoutParams();
        layoutParams.width = ((Activity) pager_news.getContext()).getWindowManager().getDefaultDisplay().getWidth() / 7 * 6;
        //layoutParams.height = (int) ((layoutParams.width / 0.75));

        pager_news.setOffscreenPageLimit(2);

        if (pager_news.getParent() instanceof ViewGroup) {
            ViewGroup viewParent = ((ViewGroup) pager_news.getParent());
            viewParent.setClipChildren(false);
            pager_news.setClipChildren(false);
        }
        pager_news.setOffscreenPageLimit(2);
        pager_news.setPageTransformer(true,new ExpandingViewPagerTransformer());

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
    public void onPause()
    {
        //overridePendingTransition(R.anim.left_out,R.anim.activity_close_translate);
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    @SuppressWarnings("unchecked")
    public void startInfoActivity(View view, NewsModel model) {
        startActivity(new Intent(this,ScrollingActivity.class).putExtra("model",model));

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

    ArrayList<ArrayList<NewsModel>> allModels;
    @Override
    public void onTaskCompleted(String response) {
        closeDialog();
        allModels=new ArrayList<>();
        try
        {
            JSONObject object=new JSONObject(response);
            if(object.getBoolean(JsonParser.RESPONSE_STATUS))
            {
                JSONArray jsonArray=object.getJSONArray(JsonParser.DATA);
                ArrayList<NewsModel> models_news=new ArrayList<>();
                ArrayList<NewsModel> models_events=new ArrayList<>();

                for(int i=0;i<jsonArray.length();i++)
                {
                    if(jsonArray.getJSONObject(i).getString(JsonParser.NEWS_TYPE).equals(Constants.NEWS))
                        models_news.add(new NewsModel(jsonArray.getJSONObject(i)));
                    else if(jsonArray.getJSONObject(i).getString(JsonParser.NEWS_TYPE).equals(Constants.EVENT))
                        models_events.add(new NewsModel(jsonArray.getJSONObject(i)));
                }
                allModels.add(models_news);
                allModels.add(models_events);
            }
            setData(0);
        }
        catch (Exception e)
        {

        }
    }
}
