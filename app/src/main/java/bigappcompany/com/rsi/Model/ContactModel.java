package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.network.JsonParser;


public class ContactModel {
	private final String id, name, mobile;


	public ContactModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.CONTACT_PER_ID);
		name = object.getString(JsonParser.CONTACT_PER_NAME);
		mobile = object.getString(JsonParser.CONTACT_PER_MOB);

	}

	public ContactModel(String id, String title, String description)
	{
		this.id=id;
		this.name =title;
		this.mobile =description;

	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMobile() {
		return mobile;
	}

}
