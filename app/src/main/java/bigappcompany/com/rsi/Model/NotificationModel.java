package bigappcompany.com.rsi.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 19 May 2017 at 1:36 PM
 */

public class NotificationModel {
	private final String imageUrl;
	private final String title;
	private final String desc;
	private final String date;
	
	public NotificationModel(JSONObject item) throws JSONException {
		imageUrl = item.getString("image").isEmpty() ? null : item.getString("image");
		title = item.getString("sub");
		desc = item.getString("msg");
		date = item.getString("date");
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getDate() {
		return date;
	}
}
