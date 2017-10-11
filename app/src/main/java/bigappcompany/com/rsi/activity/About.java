package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class About extends BaseActivity {
    private static final String TAG_BOTTOM = "bottom_fragment";
    ImageView img_main,img;
    TextView txt_org,txt_est;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarColor();
        init();
        showFragment(-1);
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
                    {
                        JSONObject jsonObject=new JSONObject(response).getJSONObject(JsonParser.DATA);
                        //txt_about.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.ABOUT));
                        String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";

                        String data = (jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.ABOUT)).replace("\n","<br>");

                        WebView webView = (WebView) findViewById(R.id.wv_about);
                        webView.loadData(String.format(text, data), "text/html", "utf-8");
                        
                        txt_est.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.DATE));
                        txt_org.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.ORG_NAME));
                        Picasso.with(getApplicationContext()).load(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.COV_IMG)).resize(img_main.getMeasuredWidth(),img_main.getMeasuredHeight()).into(img_main);
                        Picasso.with(getApplicationContext()).load(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.IMG)).resize(img.getMeasuredWidth(),img.getMeasuredHeight()).into(img);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        web.execute(ApiUrl.ABOUT_US);
        showDailog("Please wait...");
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
    void init()
    {
        txt_est=(TextView)findViewById(R.id.tv_est);
        txt_org=(TextView)findViewById(R.id.tv_org);
        img_main=(ImageView)findViewById(R.id.img_main);
        img=(ImageView)findViewById(R.id.img);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                startActivity(new Intent(About.this,MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
