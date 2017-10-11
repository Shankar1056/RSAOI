package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.FacilityAdapter;
import bigappcompany.com.rsi.Model.Constants;
import bigappcompany.com.rsi.Model.PhotoModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class FacilitiesSubList extends BaseActivity implements FacilityAdapter.OnFacility {
    RecyclerView recyclerView;
    private String TAG_BOTTOM="Bottom";
String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_sub_list);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        id=getIntent().getStringExtra(JsonParser.NEWS_ID);

        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.TITILE));

        recyclerView=(RecyclerView)findViewById(R.id.list_recycle);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
                                    object.getString(JsonParser.ISALLOWED));
                            models.add(model);
                        }
                        setValues(models);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        web.execute(ApiUrl.FACILITY_LIST+"/"+id);
        showDailog("Please wait");


        showFragment(-1);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    void setValues(ArrayList<PhotoModel> models)
    {
        recyclerView.setAdapter(new FacilityAdapter(models,this));
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
    void book(PhotoModel model)
    {
        if(model.getDate().equals("1"))
        startActivity(new Intent(FacilitiesSubList.this, BookingActivity.class).putExtra(JsonParser.NEWS_ID,model.getId()));
        else
        {
            Toast.makeText(this,"Booking Not allowed, Please contact admin for more details",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClicked(PhotoModel model) {
        book(model);
    }


}
