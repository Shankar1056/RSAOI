package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import bigappcompany.com.rsi.network.JsonParser;


/**
 * Created by sam on 5/2/17.
 */

public class ImageObj implements Serializable{
	private final String id,sl_image;
	//For DBACCess
	private String local_path;

	public ImageObj(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.IMG_ID);
		sl_image = object.getString(JsonParser.IMG_LINK);


	}
	//For DBAccess
	public ImageObj(String id, String musicUrl, String title, String description, String date)
	{
		this.id=id;
		this.sl_image=musicUrl;


	}

	//For DBACCESS
	public void setLocal_path(String local_path)
	{
		this.local_path=local_path;
	}

	public String getId() {
		return id;
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
}
