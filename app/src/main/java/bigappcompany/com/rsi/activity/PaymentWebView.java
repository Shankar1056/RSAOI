package bigappcompany.com.rsi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import bigappcompany.com.rsi.R;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 20 Jun 2017 at 6:14 PM
 */

public class PaymentWebView extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_webview);
		
		try
		{
			Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
			TextView toolbattext = (TextView) findViewById(R.id.toolbartext);
			
				setSupportActionBar(toolbar);
				toolbar.setTitle("");
				
				toolbattext.setText("PAYMENT");
			
			WebView simpleWebView=(WebView) findViewById(R.id.simpleWebView);
			String url =getIntent().getStringExtra("url");
			simpleWebView.getSettings().setJavaScriptEnabled(true);
			simpleWebView.loadUrl(url);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				//onBackPressed();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
}
