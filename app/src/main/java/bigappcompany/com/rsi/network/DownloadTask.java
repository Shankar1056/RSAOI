package bigappcompany.com.rsi.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import bigappcompany.com.rsi.DB.SongsDBHandler;
import bigappcompany.com.rsi.Model.PDFModel;
import bigappcompany.com.rsi.R;


public class DownloadTask extends AsyncTask<String, String, String> {
	String local_path = "";
	PDFModel model;
	private ProgressDialog pDialog;
	private Context context;
	
	public DownloadTask(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... f_url) {
		int count;
		
		try {
			URL url = new URL(f_url[0]);
			URLConnection conection = url.openConnection();
			conection.connect();
			
			File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			/*try {
				if (dir.mkdir()) {
					System.out.println("Directory created");
				} else {
					System.out.println("Directory is not created");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			
			// this will be useful so that you can show a tipical 0-100%
			// progress bar
			int lenghtOfFile = conection.getContentLength();
			
			// download the file
			InputStream input = new BufferedInputStream(url.openStream(),
			    8192);
			
			local_path = dir.getPath() + "/" + model.getTitle() + "_" + System.currentTimeMillis() + ".pdf";
			// Output stream
			OutputStream output = new FileOutputStream(local_path);
			
			byte data[] = new byte[1024];
			
			long total = 0;
			
			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				// After this onProgressUpdate will be called
				//publishProgress("" + (int) ((total * 100) / lenghtOfFile));
				
				//pDialog.setProgress((int) ((total * 100) / lenghtOfFile));
				
				// writing data to file
				output.write(data, 0, count);
			}
			
			// flushing output
			output.flush();
			
			// closing streams
			output.close();
			input.close();
			return "success";
			
		} catch (Exception e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(context, R.style.MyTheme);
		pDialog.setMessage("Downloading PDF...");
		//pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		pDialog.show();
	}
	
	@Override
	protected void onPostExecute(String file_url) {
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}
		if (file_url != null) {
			if (local_path != null && !local_path.equals("")) {
				try {
					//store data locally
					if (model != null) {
						model.setLocal_path(local_path);
						new SongsDBHandler(context).addRing(model.getId(), model.getPhoto(), model.getLocal_path());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(context, "internet connection lost", Toast.LENGTH_SHORT).show();
	}
	
	
	public void setModel(PDFModel model) {
		this.model = model;
	}
}