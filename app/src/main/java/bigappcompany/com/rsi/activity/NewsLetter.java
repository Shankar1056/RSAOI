package bigappcompany.com.rsi.activity;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.rsi.Adapter.PDFAdapter;
import bigappcompany.com.rsi.DB.SongsDBHandler;
import bigappcompany.com.rsi.Model.PDFModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.DownloadTask;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class NewsLetter extends BaseActivity implements PDFAdapter.OnFacility {
	private static final int RC_PERM_WRITE = 158;
	RecyclerView rv_pdf;
	PDFModel model;
	String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_letter);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		rv_pdf = (RecyclerView) findViewById(R.id.rv_pdf);
		Download_web web = new Download_web(NewsLetter.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					closeDialog();
					if (new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
						ArrayList<PDFModel> models = new ArrayList<>();
						for (int i = 0; i < new JSONObject(response).getJSONArray(JsonParser.DATA).length(); i++) {
							models.add(new PDFModel(new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(i)));
						}
						rv_pdf.setLayoutManager(new LinearLayoutManager(NewsLetter.this));
						rv_pdf.setAdapter(new PDFAdapter(models, NewsLetter.this));
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		web.execute(ApiUrl.PDF);
		showDailog("Please Wait...");
	}

	@Override
	public void onClicked(PDFModel model) {
		
		this.model = model;
		downloadCurrentMusic(model);
	}
	
	private void downloadCurrentMusic(PDFModel model) {
		SongsDBHandler db = new SongsDBHandler(NewsLetter.this);
		//if (db.getRing(model.getId()) == null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				int res = this.checkCallingOrSelfPermission(permissions[0]);
				
				if (res == PackageManager.PERMISSION_GRANTED) {
					download();
				} else {
					
					requestPermissions(permissions, RC_PERM_WRITE);
					
				}
			} else {
				download();
			}
		//} else {
		//	Toast.makeText(NewsLetter.this, "already downloaded to " + db.getRing(model.getId()), Toast
		//    .LENGTH_LONG).show();
		//}
	}
	
	void download() {
		DownloadTask task = new DownloadTask(NewsLetter.this);
		task.setModel(model);
		task.execute(model.getPhoto());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			download();
		} else {
			Toast.makeText(NewsLetter.this, "Permission not granted", Toast.LENGTH_LONG).show();
		}
	}
}
