package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.network.JsonParser;


public class SlotModel {
	private final String id, from_time, to_time;
	//For DBACCess
	private String local_path;

	public SlotModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.SLOT_ID);
		from_time = object.getString(JsonParser.SL_FROM)+" "+object.getString(JsonParser.SL_FROM_TYPE);
		to_time = object.getString(JsonParser.SL_TO)+" "+object.getString(JsonParser.SL_TO_TYPE);

	}
	//For DBAccess
	public SlotModel(String id,  String title, String description)
	{
		this.id=id;
		this.from_time =title;
		this.to_time =description;

	}

	//For DBACCESS
	public void setLocal_path(String local_path)
	{
		this.local_path=local_path;
	}

	public String getId() {
		return id;
	}

	public String getFrom_time() {
		return from_time;
	}

	public String getTo_time() {
		return to_time;
	}


	//DBACCESS
	public String getLocal_path() {
		if(local_path==null)
		{return "";}
		else
		return local_path;
	}
}
