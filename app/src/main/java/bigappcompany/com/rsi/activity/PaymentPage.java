package bigappcompany.com.rsi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;
import bigappcompany.com.rsi.network.Utility;


/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Jun 2017 at 5:19 PM
 */

public class PaymentPage extends AppCompatActivity {
	private LinearLayout opening_balance_layout, cover_charges_layout, enhanced_subscription_layout,
	    excess_paid_amount_layout, others_layout, bank_layout, misk_debits_layout;
	
	private TextView opening_balance_amount, cover_charges_amount, enhanced_subscription_amount,
	    excess_paid_amount, others_amount, bank_amount, misk_debits_amount, totalpayable, totalcredit,
	    totaldebit, totalpayableamount;
	private String TAG = "PaymentPAge.this";
	private String bill_id, amount;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymentpage);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		TextView toolbattext = (TextView) findViewById(R.id.toolbartext);
		try
		{
			setSupportActionBar(toolbar);
			toolbar.setTitle("");
			
			toolbattext.setText(getIntent().getStringExtra("month"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		domapping();
		
		
	}
	
	private void setdata(JSONObject jsonObject) {
		
		try {
			bill_id = jsonObject.getString("xml_files_id");
			
			if (jsonObject.getString("opening_balance") != null) {
				opening_balance_amount.setText(jsonObject.getString("opening_balance"));
			}
			if (jsonObject.getString("cover_charges") != null) {
				cover_charges_amount.setText(jsonObject.getString("cover_charges"));
			}
			if (jsonObject.getString("enhanced_subscription") != null) {
				enhanced_subscription_amount.setText(jsonObject.getString("enhanced_subscription"));
			}
			if (!(jsonObject.getString("excess_paid_amount") == null)) {
				excess_paid_amount.setText(jsonObject.getString("excess_paid_amount"));
			}
			if (jsonObject.getString("others") != null) {
				others_amount.setText(jsonObject.getString("others"));
			}
			if (jsonObject.getString("bank") != null) {
				bank_amount.setText(jsonObject.getString("bank"));
			}
			if (jsonObject.getString("misk_debits") != null) {
				misk_debits_amount.setText(jsonObject.getString("misk_debits"));
			}
			if (jsonObject.getString("amount_to_be_received") != null) {
				totalpayable.setText(jsonObject.getString("amount_to_be_received"));
			}
			if (jsonObject.getString("total_credit") != null) {
				totalcredit.setText(jsonObject.getString("total_credit"));
			}
			if (jsonObject.getString("total_debit") != null) {
				totaldebit.setText(jsonObject.getString("total_debit"));
			}
			try {
				if (Integer.parseInt(jsonObject.getString("amount_to_be_received")) > 0) {
					String receivedamount = jsonObject.getString("amount_to_be_received");
					amount = ""+ Utility.getpayableamount(receivedamount);
				} else {
					totalpayableamount.setVisibility(View.GONE);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				totalpayableamount.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
				totalpayableamount.setVisibility(View.GONE);
			}
			
			
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	
	private void domapping() {
		
		final String cus_id = getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
		    (JsonParser.CS_ID, "");
		opening_balance_layout = (LinearLayout) findViewById(R.id.opening_balance_layout);
		cover_charges_layout = (LinearLayout) findViewById(R.id.cover_charges_layout);
		enhanced_subscription_layout = (LinearLayout) findViewById(R.id.enhanced_subscription_layout);
		excess_paid_amount_layout = (LinearLayout) findViewById(R.id.excess_paid_amount_layout);
		others_layout = (LinearLayout) findViewById(R.id.others_layout);
		bank_layout = (LinearLayout) findViewById(R.id.bank_layout);
		misk_debits_layout = (LinearLayout) findViewById(R.id.misk_debits_layout);
		
		
		opening_balance_amount = (TextView) findViewById(R.id.opening_balance_amount);
		cover_charges_amount = (TextView) findViewById(R.id.cover_charges_amount);
		enhanced_subscription_amount = (TextView) findViewById(R.id.enhanced_subscription_amount);
		excess_paid_amount = (TextView) findViewById(R.id.excess_paid_amount);
		others_amount = (TextView) findViewById(R.id.others_amount);
		bank_amount = (TextView) findViewById(R.id.bank_amount);
		misk_debits_amount = (TextView) findViewById(R.id.misk_debits_amount);
		totalpayable = (TextView) findViewById(R.id.totalpayable);
		totalcredit = (TextView) findViewById(R.id.totalcredit);
		totaldebit = (TextView) findViewById(R.id.totaldebit);
		totalpayableamount = (TextView) findViewById(R.id.totalpayableamount);
		totalpayableamount.setText(getResources().getString(R.string.pay_caps)+"  :  "+amount);
		
		totalpayableamount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(PaymentPage.this, PaymentWebView.class);
				/*intent.putExtra("cus_id", getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
				    (JsonParser.CS_ID, ""));*/
				intent.putExtra("url",ApiUrl.PAYMENT_WEBVIEW+cus_id+"/"+bill_id+"/"+amount);
				startActivity(intent);
				
				
				
				
			}
		});
		
		callapi();
	}
	
	private void callapi() {
		
		Download_web web = new Download_web(PaymentPage.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					closeDialog();
					if (new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
						setdata(new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(0));
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		String userid = getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
		    (JsonParser.CS_ID, "");
		web.execute(ApiUrl.PAY_DESCRIPTION + "/" + userid + "/" + getIntent().getStringExtra("bill_id"));
		//web.execute(ApiUrl.PAY_DESCRIPTION+"/3766/8377");
		showDailog("Please Wait...");
	}
	
	ProgressDialog dialog;
	
	public void showDailog(String msg) {
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(msg);
		dialog.show();
	}
	
	public void closeDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.cancel();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				//onBackPressed();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
	
}
