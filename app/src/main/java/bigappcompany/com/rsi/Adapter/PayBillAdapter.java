package bigappcompany.com.rsi.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bigappcompany.com.rsi.Fragment.MonthlyBillFragment;
import bigappcompany.com.rsi.Model.PayBillListModel;
import bigappcompany.com.rsi.Model.PayBillModel;
import bigappcompany.com.rsi.R;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Jun 2017 at 4:45 PM
 */

public class PayBillAdapter extends RecyclerView.Adapter<PayBillAdapter.ViewHolder> {
	private ArrayList<PayBillListModel> models;
	MonthlyBillFragment pillBill;
	boolean type=false;
	
	public void setType(boolean b) {
		this.type=b;
	}
	
	public interface OnFacility{
		void onClicked(PayBillModel model);
	}
	
	public PayBillAdapter(ArrayList<PayBillListModel> models, MonthlyBillFragment payBill) {
		this.models = models;
		this.pillBill=payBill;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paybill, parent,
		    false);
		
		return new ViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		final PayBillListModel model = this.models.get(position);
		holder.membership_id.setText("Membership - "+model.getMembership_id());
		holder.bill_year_month.setText(model.getBill_year_month());
		holder.payment_status.setText(model.getPayment_status());
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pillBill.onClicked(model,position);
			}
		});
	}
	
	@Override
	public int getItemCount() {
		return models.size();
	}
	
	
	
	class ViewHolder extends RecyclerView.ViewHolder {
		TextView bill_year_month,membership_id,payment_status;
		
		
		
		ViewHolder(View itemView) {
			super(itemView);
			
			
			bill_year_month = (TextView) itemView.findViewById(R.id.bill_year_month);
			membership_id = (TextView) itemView.findViewById(R.id.membership_id);
			payment_status = (TextView) itemView.findViewById(R.id.payment_status);
			
			
			
		}
		
		
		
		
	}
	
}

