package bigappcompany.com.rsi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.ContactsAdapter;
import bigappcompany.com.rsi.Model.ContactModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class Contact extends BaseActivity implements ContactsAdapter.onContact {
    private static final String TAG_BOTTOM = "bottom_fragment";
    RecyclerView contacts_rv;
    Button bt_ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
        setStatusBarColor();

        if (getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false)&&!getIntent().getBooleanExtra(JsonParser.INVALID,false)) {
            showFragment(-1);
        }
        else
        {
            open("You are not allowed to use RSI. To activate your number please contact us");
            findViewById(R.id.bottombar).setVisibility(View.GONE);
            bt_ch=(Button)findViewById(R.id.bt_ch);
            bt_ch.setVisibility(View.VISIBLE);
            bt_ch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Contact.this,SplashActivity.class));
                    finish();
                }
            });
        }
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
                    {
                        JSONObject jsonObject=new JSONObject(response).getJSONObject(JsonParser.DATA);
                        txt_est.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.DATE));
                        txt_org.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.ORG_NAME));
                        txt_address.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.ADDRESS));
                        txt_email.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.EMAIL));
                        txt_mobile.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.MOBILE));
                        txt_city.setText(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.CITY)+" - "+jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.PIN_CODE));
                        Picasso.with(getApplicationContext()).load(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.COV_IMG)).resize(img_main.getMeasuredWidth(),img_main.getMeasuredHeight()).into(img_main);
                        Picasso.with(getApplicationContext()).load(jsonObject.getJSONArray(JsonParser.CONTACT_INFO).getJSONObject(0).getString(JsonParser.IMG)).resize(img.getMeasuredWidth(),img.getMeasuredHeight()).into(img);

                        JSONArray contacts=jsonObject.getJSONArray(JsonParser.CONTACT_PER);
                        ArrayList<ContactModel> models=new ArrayList<>();
                        for(int i=0;i<contacts.length();i++)
                        {
                            models.add(new ContactModel(contacts.getJSONObject(i)));
                        }
                        contacts_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        contacts_rv.setAdapter(new ContactsAdapter(models,Contact.this));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        web.execute(ApiUrl.CONTACT_US);
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    ImageView img_main,img;
    TextView txt_org,txt_est,txt_address,txt_city,txt_email,txt_mobile;
    void init()
    {

        txt_est=(TextView)findViewById(R.id.tv_est);
        txt_org=(TextView)findViewById(R.id.tv_org);
        txt_address=(TextView)findViewById(R.id.tv_address);
        txt_city=(TextView)findViewById(R.id.tv_city);
        txt_email=(TextView)findViewById(R.id.tv_email);
        txt_mobile=(TextView)findViewById(R.id.tv_mobile);
        img_main=(ImageView)findViewById(R.id.img_main);
        img=(ImageView)findViewById(R.id.img);
        contacts_rv=(RecyclerView)findViewById(R.id.rv_contacts);

    }

    @Override
    public void onClicked(ContactModel model) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+(model.getMobile())));
        startActivity(intent);

    }

    public void open(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
