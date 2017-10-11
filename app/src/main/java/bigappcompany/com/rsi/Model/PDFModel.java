package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.network.JsonParser;


public class PDFModel {
	private final String id, title, date, pdf_path;
	//For DBACCess
	private String local_path;

	public PDFModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.NEWS_ID);
		pdf_path = object.getString(JsonParser.LINK);
		title = object.getString(JsonParser.NAME);
		date = object.getString(JsonParser.DATE_PDF);

	}

	public PDFModel(String id, String musicUrl, String title, String description, String date)
	{
		this.id=id;
		this.pdf_path =musicUrl;
		this.title=title;
		this.date =description;

	}

	//For DBACCESS
	public void setLocal_path(String local_path)
	{
		this.local_path=local_path;
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

	//DBACCESS
	public String getLocal_path() {
		if(local_path==null)
		{return "";}
		else
		return local_path;
	}
}
