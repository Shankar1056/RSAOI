package bigappcompany.com.rsi.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bigappcompany.com.rsi.HorizontalCalendar.HorizontalCalendar;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;


public class BookingHDActivity extends BaseActivity {

    HorizontalCalendar horizontalCalendar;
    private String TAG_BOTTOM="BOTTOM";
    String id="";
    TextView tv_name,tv_time,tv_date,tv_month,tv_year;
    private Button bt_terms,bt_pricing;
    String terms="";
    String[] pricing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_hs);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showFragment(-1);
        initialize();
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
                    {
                        JSONObject obj=new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(0);
                        Date date=new SimpleDateFormat("MMM dd yyyy").parse(obj.getString(JsonParser.FC_DATE));

                        tv_name.setText(obj.getString(JsonParser.FC_NAME));
                        tv_time.setText(obj.getJSONArray(JsonParser.FC_TIME).getJSONObject(0).getString(JsonParser.SL_FROM)+" "+obj.getJSONArray(JsonParser.FC_TIME).getJSONObject(0).getString(JsonParser.SL_FROM_TYPE));
                        tv_date.setText((new SimpleDateFormat("dd").format(date)));
                        tv_month.setText((new SimpleDateFormat("MMM").format(date)));
                        tv_year.setText((new SimpleDateFormat("yyyy").format(date)));
                        status=obj.getString("booking_status_id");
                        price=obj.getString("price");
                        terms=obj.getString("terms");
                        setStatus(status);

                        JSONArray charts=obj.getJSONArray(JsonParser.PRICE_CHARTS);
                        pricing=new String[charts.length()];
                        for(int i=0;i<charts.length();i++)
                        {
                            pricing[i]=charts.getJSONObject(i).getString(JsonParser.PC_NAME)+" : "+charts.getJSONObject(i).getString(JsonParser.PC_DESC);
                        }

                    }
                }
                catch (ParseException pe)
                {
                    pe.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                bt_terms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        open(terms);
                    }
                });
                bt_pricing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pricing!=null)
                        open(pricing);
                    }
                });
            }
        });
        id=getIntent().getStringExtra(JsonParser.NEWS_ID);
        web.execute(ApiUrl.BK_DETAILS+"/"+getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString(JsonParser.CS_ID,"")+"/"+id);
        showDailog("Please wiat, Loading booking detials");
    }
    String status="",price="";
    private void setStatus(String booking_status_id) {

        switch (booking_status_id)
        {
            case "1":
                ((ImageView)findViewById(R.id.img_aw)).setImageResource(R.drawable.round_rank_white);
                ((ImageView)findViewById(R.id.img_st_aw)).setImageResource(R.drawable.done);
                ((TextView)findViewById(R.id.tv_aw_c)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                ((TextView)findViewById(R.id.tv_aw_c)).setText("");
                ((TextView)findViewById(R.id.txt_aw)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                bt_cancel.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BookingHDActivity.this);
                        alertDialogBuilder.setMessage("Are you sure, you want to Cancel booking?");
                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialogBuilder.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Download_web web=new Download_web(BookingHDActivity.this, new OnTaskCompleted() {
                                            @Override
                                            public void onTaskCompleted(String response) {
                                                try {
                                                    closeDialog();
                                                    if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
                                                        Toast.makeText(getApplicationContext(), "Cancelled successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(BookingHDActivity.this,MainActivity.class));
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Unable to cancel", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        web.execute(ApiUrl.BK_CANCEL+"/"+getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString(JsonParser.CS_ID,"")+"/"+id);
                                        showDailog("Please wait, Trying to cancel your request...");
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                });
                break;
            case "2":
                ((ImageView)findViewById(R.id.img_dec)).setImageResource(R.drawable.round_rank_white);
                ((ImageView)findViewById(R.id.img_st_dec)).setImageResource(R.drawable.done);
                ((TextView)findViewById(R.id.tv_dec_C)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                ((TextView)findViewById(R.id.tv_dec_C)).setText("");
                ((TextView)findViewById(R.id.txt_dc)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                break;
            case "3":
                ((ImageView)findViewById(R.id.img_app)).setImageResource(R.drawable.round_rank_white);
                ((ImageView)findViewById(R.id.img_st_ap)).setImageResource(R.drawable.done);
                ((TextView)findViewById(R.id.tv_app_c)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                ((TextView)findViewById(R.id.tv_app_c)).setText("");
                ((TextView)findViewById(R.id.txt_ap)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                bt_pay.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
                if(price!=null)
                bt_pay.setText("Pay "+price);
                bt_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(BookingHDActivity.this,PaymentActivity.class);
                        i.putExtra("c_id",getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString(JsonParser.CS_ID,""));
                        i.putExtra("id",id);
                        i.putExtra("amount",price);
                        startActivity(i);
                        finish();
                    }
                });
                break;
            case "4":
                ((ImageView)findViewById(R.id.img_dec)).setImageResource(R.drawable.round_rank_white);
                ((ImageView)findViewById(R.id.img_st_dec)).setImageResource(R.drawable.done);
                ((TextView)findViewById(R.id.tv_dec_C)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                ((TextView)findViewById(R.id.tv_dec_C)).setText("");
                ((TextView)findViewById(R.id.txt_dc)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                break;
            case "7":
                ((ImageView)findViewById(R.id.img_app)).setImageResource(R.drawable.round_rank_white);
                ((ImageView)findViewById(R.id.img_st_ap)).setImageResource(R.drawable.done);
                ((TextView)findViewById(R.id.tv_app_c)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                ((TextView)findViewById(R.id.tv_app_c)).setText("");
                ((TextView)findViewById(R.id.txt_ap)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                if(price!=null)
                    bt_pay.setText("PAID ONLINE");
                break;
            case "8":
                ((ImageView)findViewById(R.id.img_app)).setImageResource(R.drawable.round_rank_white);
                ((ImageView)findViewById(R.id.img_st_ap)).setImageResource(R.drawable.done);
                ((TextView)findViewById(R.id.tv_app_c)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                ((TextView)findViewById(R.id.tv_app_c)).setText("");
                ((TextView)findViewById(R.id.txt_ap)).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                if(price!=null)
                    bt_pay.setText("PAID CASH");
                break;
        }

    }

    private void initialize() {
        tv_date=(TextView)findViewById(R.id.txt_date);
        tv_month=(TextView)findViewById(R.id.txt_month);
        tv_name=(TextView)findViewById(R.id.txt_name);
        tv_time=(TextView)findViewById(R.id.txt_time);
        tv_year=(TextView)findViewById(R.id.txt_year);
        bt_pay=(Button)findViewById(R.id.bt_pay);
        bt_cancel=(Button)findViewById(R.id.bt_cancel);
        bt_terms=(Button)findViewById(R.id.bt_terms);
        bt_pricing=(Button)findViewById(R.id.bt_pricing);

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

    Button bt_cancel,bt_pay;

    public void open(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("CLOSE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void open(String[] msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setItems(msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setPositiveButton("CLOSE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }






}
