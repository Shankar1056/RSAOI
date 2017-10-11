package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.FacilityMainAdapter;
import bigappcompany.com.rsi.Model.Constants;
import bigappcompany.com.rsi.Model.PhotoModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class FacilitiesActivity extends BaseActivity implements FacilityMainAdapter.OnFacility {
    private static final String TAG_BOTTOM = "bottom_fragment";
    RecyclerView rv_fac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showFragment(2);
        rv_fac=(RecyclerView) findViewById(R.id.rv_fac);
        GridLayoutManager manager=new GridLayoutManager(this,2);
        rv_fac.setLayoutManager(manager);
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
                        JSONArray array=new JSONObject(response).getJSONArray(JsonParser.DATA);
                        ArrayList<PhotoModel> models=new ArrayList<>();
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject object=array.getJSONObject(i);
                            PhotoModel model=new PhotoModel(
                                    object.getString(JsonParser.NEWS_ID),
                                    object.getString(JsonParser.IMAGE),
                                    object.getString(JsonParser.NAME),
                                    object.getString(JsonParser.DESCRIPTION),
                                    "");
                            models.add(model);
                        }
                        rv_fac.setAdapter(new FacilityMainAdapter(models,FacilitiesActivity.this));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        web.execute(ApiUrl.facilities);
        showDailog("Please wait...");


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
                //onBackPressed();
                startActivity(new Intent(FacilitiesActivity.this,MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onClicked(PhotoModel model) {
        startActivity(new Intent(this,FacilitiesSubList.class).putExtra(Constants.TITILE,model.getTitle()).putExtra(JsonParser.NEWS_ID,model.getId()));

    }

}
