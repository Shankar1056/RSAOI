package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.network.JsonParser;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 20 Jun 2017 at 1:15 PM
 */

public class PayBillListModel {
	
	
	private String id, title, date, pdf_path;
	
	public String getBill_year_month() {
		return bill_year_month;
	}
	
	public String getMembership_id() {
		return membership_id;
	}
	
	public String getBill_id() {
		return bill_id;
	}
	
	public String getPayment_status() {
		return payment_status;
	}
	
	private final String bill_year_month, membership_id, bill_id, payment_status;
	
	public PayBillListModel(JSONObject object) throws JSONException {
		bill_year_month = object.getString(JsonParser.BILLYEARMONTH);
		membership_id = object.getString(JsonParser.MEMBERSHIPID);
		bill_id = object.getString(JsonParser.BILLID);
		payment_status = object.getString(JsonParser.PAYMENTSTATUS);
		
	}
}
