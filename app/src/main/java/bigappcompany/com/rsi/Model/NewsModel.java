package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import bigappcompany.com.rsi.network.JsonParser;


public class NewsModel implements Serializable{
	private final String id, title, description,sl_image,date;
	//For DBACCess
	private String local_path;

	public NewsModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.NEWS_ID);
		sl_image = object.getString(JsonParser.NEWS_IMAGE);
		title = object.getString(JsonParser.NEWS_TITLE);
		description = object.getString(JsonParser.NEWS_DESCRIPTION);
		date=object.getString(JsonParser.NEWS_DATE);
	}
	//For DBAccess
	public NewsModel(String id, String musicUrl, String title, String description, String date)
	{
		this.id=id;
		this.sl_image=musicUrl;
		this.title=title;
		this.description=description;
		this.date=date;

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

	public String getDescription() {
		return description;
	}



	public String getPhoto() {
		return sl_image;
	}

	//DBACCESS
	public String getLocal_path() {
		if(local_path==null)
		{return "";}
		else
		return local_path;
	}

	public String getDate() {
		return date;
	}
}
