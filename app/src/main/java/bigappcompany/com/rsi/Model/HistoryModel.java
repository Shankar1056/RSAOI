package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.network.JsonParser;


public class HistoryModel {
	private final String id, title, time, img,type,date;


	public HistoryModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.BK_ID);
		img = object.getString(JsonParser.FC_IMG);
		title = object.getString(JsonParser.FC_NAME);
		time = object.getString(JsonParser.FC_TIME);
		type = object.getString(JsonParser.FC_TYPE);
		date = object.getString(JsonParser.FC_DATE);


	}
	//For DBAccess
	public HistoryModel(String id, String musicUrl, String title, String description, String date,String type)
	{
		this.id=id;
		this.img =musicUrl;
		this.title=title;
		this.time =description;
		this.date=date;
		this.type=type;
	}



	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getTime() {
		return time;
	}

	public String getImg(){return  img;}

	public String getType(){return  type;}

	public String getDate(){return date;}


}
