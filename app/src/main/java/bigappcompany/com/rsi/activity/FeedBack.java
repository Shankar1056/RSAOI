package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 03 Aug 2017 at 12:29 PM
 */

public class FeedBack extends BaseActivity implements View.OnClickListener{
	private EditText ed_feedback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		setContentView(R.layout.activity_feedback);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		setStatusBarColor();
		mapping();
		showFragment(-1);
	}
	
	private void mapping() {
		
		ed_feedback = (EditText)findViewById(R.id.ed_feedback);
		findViewById(R.id.sendfeedback).setOnClickListener(this);
	}
	
	private void setStatusBarColor() {
		Window window = this.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.setStatusBarColor(Color.parseColor("#00796A"));
		}
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
				startActivity(new Intent(FeedBack.this,MainActivity.class));
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.sendfeedback:
			if (ed_feedback.getText().toString().trim().equalsIgnoreCase(""))
			{
				Toast.makeText(this, "Enter your feedback", Toast.LENGTH_SHORT).show();
				return;
			}
			else {
				sendmail();
			}
			break;
		}
		
	}
	
	private void sendmail() {
			/*String[] TO = {""};
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:rsibangalore@yahoo.com"));
			
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for RSAOI Bangalore");
			emailIntent.putExtra(Intent.EXTRA_TEXT, ed_feedback.getText().toString());
			
			try {
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(FeedBack.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
			}
			finally {
				
				
			}*/
		
		
		Download_web web = new Download_web(FeedBack.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				closeDialog();
				try {
					if (new JSONObject(response).getString("status").equalsIgnoreCase("TRUE"))
					{
						Toast.makeText(FeedBack.this, ""+(new JSONObject(response).getString("message")), Toast
						    .LENGTH_SHORT)
						.show();
						
						finish();
					}
					//Log.e("",response);
				} catch (JSONException e) {
					Log.e("FEEDBACK", e.getMessage(), e);
				}
				
			}
		});
		web.setReqType(false);
		try {
			String cs_id = FeedBack.this.getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
			    (JsonParser.CS_ID, "");
			String cs_name = FeedBack.this.getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
			    (JsonParser.CS_NAME, "");
			String cs_lastname = FeedBack.this.getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
			    (JsonParser.CS_LASTNAME, "");
			String cs_contact = FeedBack.this.getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
			    (JsonParser.CS_CONTACT, "");
			String cs_email = FeedBack.this.getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
			    (JsonParser.CS_EMAIL, "");
			
			web.setData(
			    (new JSONObject()
				.put("apikey", ApiUrl.API_KEY)
				.put("name", cs_name+" "+cs_lastname)
				.put("email", cs_email)
				.put("contactnumber", cs_contact)
				.put("message", ed_feedback.getText().toString())
				.put("cs_id", cs_id)
			    ).toString());
			
			web.execute(ApiUrl.FEEDBACK);
			showDailog("Please wait we are booking for you...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
