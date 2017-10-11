package bigappcompany.com.rsi.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.PaymentWebView;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;
import bigappcompany.com.rsi.network.Utility;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 21 Jun 2017 at 11:36 AM
 */

public class RechargeFragment extends Fragment implements View.OnClickListener{
	private String title;
	private EditText input_name,input_phone,input_email,input_amount,input_desc,input_member,input_bill;
	
	public RechargeFragment() {
	}
	
	@SuppressLint("ValidFragment")
	public RechargeFragment(String title) {
		this.title = title;//Setting tab title
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.recharge_fragment,container,false);
		
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		input_name = (EditText)view.findViewById(R.id.input_name);
		input_phone = (EditText)view.findViewById(R.id.input_phone);
		input_email = (EditText)view.findViewById(R.id.input_email);
		input_amount = (EditText)view.findViewById(R.id.input_amount);
		input_desc = (EditText)view.findViewById(R.id.input_desc);
		input_member = (EditText)view.findViewById(R.id.input_member);
		input_bill = (EditText)view.findViewById(R.id.input_bill);
		
		view.findViewById(R.id.paynow).setOnClickListener(this);
		
		
		getdetails();
	}
	
	private void getdetails() {
		
		Download_web web=new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					closeDialog();
					if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
					{
						JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
						for (int i=0;i<jsonArray.length();i++)
						{
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							input_name.setText(jsonObject.optString("customers_first_name")
							    +" "+jsonObject.optString("customers_last_name"));
							input_phone.setText(jsonObject.optString("customers_mobile"));
							input_email.setText(jsonObject.optString("customers_email"));
							input_member.setText(jsonObject.optString("mcode"));
							input_desc.setText(jsonObject.optString("member_description"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		web.setReqType(true);
		try {
			String cs_id=getActivity().getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString
			    (JsonParser
				.CS_ID,"");
			
			web.execute(ApiUrl.GETUSERDETAILS+cs_id);
			showDailog("Please wait we are booking for you...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.paynow:
				if (input_name.getText().toString().trim().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(), "Enter name", Toast.LENGTH_SHORT).show();
					input_name.setText("");
					return;
				}
				if (input_phone.getText().toString().trim().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(), "Enter phone number ", Toast.LENGTH_SHORT).show();
					input_phone.setText("");
					return;
				}
				if (input_phone.getText().toString().length()<=9)
				{
					Toast.makeText(getActivity(), "Enter 10 digit phone number", Toast.LENGTH_SHORT)
					    .show();
					return;
				}
				if (input_email.getText().toString().trim().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(), "Enter Email id", Toast.LENGTH_SHORT)
					    .show();
					input_email.setText("");
					return;
				}
				if (!isValidEmail1(input_email.getText().toString()))
				{
					Toast.makeText(getActivity(), "Enter valid Email id", Toast.LENGTH_SHORT)
					    .show();
					return;
				}
				if (input_amount.getText().toString().trim().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(), "Enter amount", Toast.LENGTH_SHORT)
					    .show();
					input_amount.setText("");
					return;
				}
				try {
					if (Double.parseDouble(input_amount.getText().toString()) < 1) {
						Toast.makeText(getActivity(), "Amount should be more than 1", Toast
						    .LENGTH_SHORT)
						    .show();
						return;
					}
				}
				catch (NumberFormatException e)
				{
					Toast.makeText(getActivity(), "Amount should be more than 1", Toast.LENGTH_SHORT)
				    .show();
					return;
					
				}
				catch (Exception e)
				{
					Toast.makeText(getActivity(), "Amount should be more than 1", Toast.LENGTH_SHORT)
					    .show();
					return;
				}
				
				if (input_member.getText().toString().trim().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(), "Enter Member id", Toast.LENGTH_SHORT).show();
					input_member.setText("");
					return;
				}
				/*if (input_bill.getText().toString().trim().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(), "Enter bill number", Toast.LENGTH_SHORT).show();
					input_bill.setText("");
					return;
				}*/
				if (input_desc.getText().toString().trim().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(), "Enter Description", Toast.LENGTH_SHORT)
					    .show();
					input_desc.setText("");
					return;
				}
				else {
					
					String cs_id=getActivity().getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString
					    (JsonParser
						.CS_ID,"");
					DoRecharge doRecharge = new DoRecharge();
					doRecharge.execute(ApiUrl.RECHARGE,input_name.getText().toString(),input_phone
					    .getText().toString(),input_member.getText().toString(), input_amount.getText
						().toString(), cs_id,ApiUrl
					    .API_KEY,input_email.getText().toString(),input_desc.getText().toString());
				
				//doRecgarge();
				}
				break;
		}
		
	}
	
	/*private void doRecgarge() {
		
		Download_web web=new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					closeDialog();
					if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
					{
						JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
						for (int i=0;i<jsonArray.length();i++)
						{
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							input_name.setText(jsonObject.optString("customers_first_name")
							    +" "+jsonObject.optString("customers_last_name"));
							input_phone.setText(jsonObject.optString("customers_mobile"));
							input_email.setText(jsonObject.optString("customers_email"));
							input_member.setText(jsonObject.optString("mcode"));
							input_desc.setText(jsonObject.optString("member_description"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		web.setReqType(false);
		try {
			String cs_id=getActivity().getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE).getString
			    (JsonParser
				.CS_ID,"");
			
			
			web.setData((new JSONObject()
			    .put("firsr_name",input_name.getText().toString())
			    .put("mobile",input_phone.getText().toString())
			    .put("member",input_member.getText().toString())
			    .put("amount",input_amount.getText().toString())
			    .put("email",input_email.getText().toString())
			    .put("description",input_desc.getText().toString())
			    .put("id_customer",cs_id)
			    .put("api_key", ApiUrl.API_KEY)).toString());
			
			
			
			web.execute(ApiUrl.RECHARGE);
			showDailog("Please wait we are booking for you...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
	
	private class DoRecharge extends AsyncTask<String, Void, String> {
		String result;
		final ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDailog("storing, please waid");
			
		}
		
		
		@Override
		protected String doInBackground(String... params) {
			
			nameValuePairs.add(new BasicNameValuePair("firsr_name", params[1]));
			nameValuePairs.add(new BasicNameValuePair("mobile", params[2]));
			nameValuePairs.add(new BasicNameValuePair("member", params[3]));
			nameValuePairs.add(new BasicNameValuePair("amount", params[4]));
			nameValuePairs.add(new BasicNameValuePair("id_customer", params[5]));
			nameValuePairs.add(new BasicNameValuePair("api_key", params[6]));
			nameValuePairs.add(new BasicNameValuePair("email", params[7]));
			//nameValuePairs.add(new BasicNameValuePair("bill_number", params[8]));
			nameValuePairs.add(new BasicNameValuePair("description", params[8]));
			
			try {
				result = Utility.executeHttpPost(params[0], nameValuePairs);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}
		
		
		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			closeDialog();
			
			try
			{
				String transaction_id = null,amount=null;
				JSONObject jsonObject = new JSONObject(s);
				if (jsonObject.has("status"))
				{
				if (jsonObject.getString("status").equalsIgnoreCase("TRUE"))
				{
					JSONObject jsonObject1 = jsonObject.getJSONObject("data");
					if (jsonObject1.has("transaction_id")) {
						 transaction_id = jsonObject1.getString("transaction_id");
					}
					if (jsonObject1.has("amount")) {
						 amount = jsonObject1.getString("amount");
					}
					
					gotopaymentpage(transaction_id,amount);
				}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	
	private void gotopaymentpage(String transaction_id, String amount) {
		String cus_id = getActivity().getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
		    (JsonParser.CS_ID, "");
		Intent intent = new Intent(getActivity(), PaymentWebView.class);
				/*intent.putExtra("cus_id", getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getString
				    (JsonParser.CS_ID, ""));*/
		intent.putExtra("url",ApiUrl.RECHARGE_WEBVIEW+cus_id+"/"+transaction_id+"/"+amount);
		startActivity(intent);
		
	}
	
	ProgressDialog dialog;
		
		public void showDailog(String msg) {
			dialog = new ProgressDialog(getActivity());
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage(msg);
			dialog.show();
		}
		
		public void closeDialog() {
			if (dialog != null && dialog.isShowing())
				dialog.cancel();
		}
		
		public boolean isValidEmail1(CharSequence target) {
			return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
