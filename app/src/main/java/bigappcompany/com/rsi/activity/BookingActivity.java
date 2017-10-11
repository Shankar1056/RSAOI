package bigappcompany.com.rsi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bigappcompany.com.rsi.Adapter.PCAdapter;
import bigappcompany.com.rsi.Adapter.SlotsAdapter;
import bigappcompany.com.rsi.HorizontalCalendar.HorizontalCalendar;
import bigappcompany.com.rsi.HorizontalCalendar.HorizontalCalendarListener;
import bigappcompany.com.rsi.Model.SlotModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;


public class BookingActivity extends BaseActivity implements SlotsAdapter.OnFacility {

    HorizontalCalendar horizontalCalendar;
    RecyclerView rv_slots,rv_pc;
    private String id;
    Button bt_select;
    TextView tv_terms,tv_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bt_select=(Button)findViewById(R.id.bt_select);
        /** end after 1 month from now */
        final Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 3);
        tv_read=(TextView)findViewById(R.id.txt_read);
        tv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(terms);
            }
        });

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,1);
        //startDate.add(Calendar.MONTH, -1);
        id=getIntent().getStringExtra(JsonParser.NEWS_ID);

        tv_terms=(TextView)findViewById(R.id.txt_terms);

        rv_slots=(RecyclerView)findViewById(R.id.recycle_slots);
        rv_pc=(RecyclerView)findViewById(R.id.recycle_pc);

        GridLayoutManager manager=new GridLayoutManager(this,3);
        LinearLayoutManager lmngr=new LinearLayoutManager(this);
        rv_pc.setLayoutManager(lmngr);
        rv_slots.setLayoutManager(manager);
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
                        JSONArray array=new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(0).getJSONArray(JsonParser.TIMESLOTS);
                        ArrayList<SlotModel> models=new ArrayList<>();
                        for(int i=0;i<array.length();i++)
                        {

                            SlotModel model=new SlotModel(array.getJSONObject(i));
                            models.add(model);
                        }
                        rv_slots.setAdapter(new SlotsAdapter(models,BookingActivity.this));
                        terms=new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(0).getString("terms");
                        tv_terms.setText(terms);
                        JSONArray arr_pc=new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(0).getJSONArray(JsonParser.PRICE_CHARTS);
                        ArrayList<SlotModel> models_pc=new ArrayList<>();
                        for(int i=0;i<arr_pc.length();i++)
                        {

                            SlotModel model=new SlotModel(arr_pc.getJSONObject(i).getString(JsonParser.PC_ID),
                                    arr_pc.getJSONObject(i).getString(JsonParser.PC_NAME),
                                    arr_pc.getJSONObject(i).getString(JsonParser.PC_DESC));
                            models_pc.add(model);
                        }
                        PCAdapter adapter=new PCAdapter(models_pc,null);
                        adapter.setType(true);
                        rv_pc.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        web.execute(ApiUrl.FACILITY_DESC+"/"+id);
        showDailog("Please wait Fetching information...");


        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayFormat("EEE")
                .dayNumberFormat("dd")
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.parseColor("#fec107"))
                .build();
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        dateSel=format.format(startDate.getTime());
        //horizontalCalendar.setCalendarSpeed();



        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                horizontalCalendar.getmCalendarAdapter().refresh();
                SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(BookingActivity.this, format.format(date) + " is selected!", Toast.LENGTH_SHORT).show();
                dateSel=format.format(date);
            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
            }
        });

        ((Button)findViewById(R.id.bt_select)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t_id.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please choose slot",Toast.LENGTH_LONG).show();
                }
                else if(dateSel.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please choose date",Toast.LENGTH_LONG).show();
                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BookingActivity.this);
                    alertDialogBuilder.setMessage("Are you sure, you want to book?");
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
                                    Download_web web=new Download_web(BookingActivity.this, new OnTaskCompleted() {
                                        @Override
                                        public void onTaskCompleted(String response) {
                                            try {
                                                closeDialog();
                                                if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
                                                {
                                                    //TODO show dialog booking successful
                                                    Toast.makeText(getApplicationContext(),"Booking successful",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(BookingActivity.this,MainActivity.class));
                                                    finish();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    web.setReqType(false);
                                    try {
                                        String cs_id=getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString(JsonParser.CS_ID,"");
                                        web.setData(
                                                (new JSONObject()
                                                        .put("api_key",ApiUrl.API_KEY)
                                                        .put("customer_id",cs_id)
                                                        .put("facility_id",id)
                                                        .put("timeslot_id",t_id)
                                                        .put("requested_date",dateSel)
                                                ).toString());
                                        web.execute(ApiUrl.BOOK);
                                        showDailog("Please wait we are booking for you...");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            }
        });

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

    String terms="";
    @Override
    public void onClicked(SlotModel model) {
        bt_select.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        t_id=model.getId();
    }
    String dateSel="",t_id="";



}
