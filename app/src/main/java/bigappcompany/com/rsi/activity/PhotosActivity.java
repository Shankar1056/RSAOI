package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Fragment.PhotosFragment;
import bigappcompany.com.rsi.Model.PhotosModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class PhotosActivity extends AppCompatActivity {
    ViewPager pager;
    ArrayList<PhotosModel> models;
    String currentId="";
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarColor();
        ID=getIntent().getStringExtra("id");
        new Download_web(this,listener).execute(ApiUrl.IMAGES+"/"+ID);
        pager= (ViewPager) findViewById(R.id.pager_photos);
        models=new ArrayList<>();
    }
    int pos=-1;
    OnTaskCompleted listener=new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            try
            {
                JSONObject object=new JSONObject(response);
                if(object.getBoolean(JsonParser.RESPONSE_STATUS))
                {
                    currentId=object.getString(JsonParser.TYPE_ID_CUR);
                    JSONArray array=object.getJSONArray(JsonParser.DATA);
                    for(int i=0;i<array.length();i++)
                    {
                        models.add(new PhotosModel(array.getJSONObject(i).getJSONObject(JsonParser.DETAILS)));
                        if(pos==-1)
                        pos=models.get(i).getId().equals(ID)?i:-1;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            if(pos>-1)
            {
                pager.setCurrentItem(pos);
            }
        }
    };
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
                return PhotosFragment.newInstance(pos,models.get(pos));
        }

        @Override
        public int getCount() {
            return models.size();
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
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
