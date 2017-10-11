package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.network.JsonParser;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Jun 2017 at 4:24 PM
 */

public class PayBillModel {
	
	private final String id, title, date, pdf_path;
	
	public PayBillModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.NEWS_ID);
		pdf_path = object.getString(JsonParser.LINK);
		title = object.getString(JsonParser.NAME);
		date = object.getString(JsonParser.DATE_PDF);
		
	}
	
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDate() {
		return date;
	}
	
	
	
	public String getPhoto() {
		return pdf_path;
	}
	

}
