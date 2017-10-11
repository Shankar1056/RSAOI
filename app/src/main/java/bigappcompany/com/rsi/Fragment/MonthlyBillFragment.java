package bigappcompany.com.rsi.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.PayBillAdapter;
import bigappcompany.com.rsi.Model.PayBillListModel;
import bigappcompany.com.rsi.Model.PayBillModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.PaymentPage;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 21 Jun 2017 at 11:30 AM
 */

public class MonthlyBillFragment extends Fragment {
	private String title;
	private RecyclerView rv_pdf;
	private PayBillModel model;
	TextView nodatafound;
	
	public MonthlyBillFragment() {
	}
	
	
	@SuppressLint("ValidFragment")
	public MonthlyBillFragment(String title) {
		this.title = title;
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.monthlybill_fragment,container,false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		rv_pdf = (RecyclerView) view.findViewById(R.id.rv_paybill);
		nodatafound = (TextView)view.findViewById(R.id.nodatafound);
		Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					closeDialog();
					if (new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
						ArrayList<PayBillListModel> models = new ArrayList<>();
						for (int i = 0; i < new JSONObject(response).getJSONArray(JsonParser.DATA).length(); i++) {
							models.add(new PayBillListModel(new JSONObject(response).getJSONArray
							    (JsonParser.DATA).getJSONObject(i)));
						}
						rv_pdf.setLayoutManager(new LinearLayoutManager(getActivity()));
						rv_pdf.setAdapter(new PayBillAdapter(models, MonthlyBillFragment.this));
					}
					else if (new JSONObject(response).getString(JsonParser.RESPONSE_STATUS)
					    .equalsIgnoreCase("FALSE"))
					{
						nodatafound.setVisibility(View.VISIBLE);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		/*web.execute(ApiUrl.PAY_BILL+"/"+getActivity().getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE)
		    .getString
		    (JsonParser.CS_ID,""));*/
		web.execute(ApiUrl.PAY_BILL+"/5236");
		//web.execute(ApiUrl.PAY_BILL+"/3766");
		showDailog("Please Wait...");
	}
	ProgressDialog dialog;
	public void showDailog(String msg)
	{
		dialog=new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(msg);
		dialog.show();
	}
	public void closeDialog()
	{
		if(dialog!=null && dialog.isShowing())
			dialog.cancel();
	}
	
	
	
	public void onClicked(PayBillListModel model, int position) {
		Intent intent = new Intent(getActivity(),PaymentPage.class);
		intent.putExtra("bill_id",model.getBill_id());
		intent.putExtra("month",model.getBill_year_month());
		startActivity(intent);
	}
	
}
