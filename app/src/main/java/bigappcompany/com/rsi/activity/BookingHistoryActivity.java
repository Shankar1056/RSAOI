package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.BookingHistoryAdapter;
import bigappcompany.com.rsi.Fragment.ICFragment;
import bigappcompany.com.rsi.Model.HistoryModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class BookingHistoryActivity extends BaseActivity implements BookingHistoryAdapter.OnFacility, ICFragment.onClick {

    RecyclerView rv_history;
    private String TAG_BOTTOM="bottom";
    private Fragment frg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Bookings");
        rv_history=(RecyclerView)findViewById(R.id.rv_history);
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
                    {
                        JSONArray array=new JSONObject(response).getJSONArray(JsonParser.DATA);
                        ArrayList<HistoryModel> models=new ArrayList<>();
                        for(int i=0;i<array.length();i++)
                        {
                            models.add(new HistoryModel(array.getJSONObject(i)));
                        }
                        rv_history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_history.setAdapter(new BookingHistoryAdapter(models,BookingHistoryActivity.this));
                        if(models.size()<1)
                        {
                            showFrg("Oops! there are no bookings. Please utilize facilites to make bookings");
                        }
                    }
                    else
                    {
                        showFrg("Oops! there are no bookings. Please utilize facilites to make bookings");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        String url=ApiUrl.BK_HIST+"/"+getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString(JsonParser.CS_ID,"");

        web.execute(url);
        showDailog("Please wait loading booking history...");
        showFragment(-1);
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


    @Override
    public void onClicked(HistoryModel model) {
        startActivity(new Intent(this,BookingHDActivity.class).putExtra(JsonParser.NEWS_ID,model.getId()));
    }
    public void showFrg(String msg)
    {
        if(frg==null) {
            frg = new ICFragment();
            ((ICFragment)frg).setMessage(msg);
            ((ICFragment)frg).setListener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.container_err, frg).commit();



        }
    }
    public void closeFrg()
    {
        if(frg!=null)
        {
            getSupportFragmentManager().beginTransaction().remove(frg).commit();
        }
    }


    @Override
    public void onClicked() {

    }

    @Override
    public void updateUI(String msg) {
        ((ICFragment)frg).setMessage(msg);
    }
}
