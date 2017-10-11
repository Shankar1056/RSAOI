package bigappcompany.com.rsi.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;

public class PaymentActivity extends BaseActivity {
    String c_id,b_id,amount;
    WebView wv_pay;
    private ProgressDialog progressBar;
    private String TAG="WEBVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        c_id=getIntent().getStringExtra("c_id");
        b_id=getIntent().getStringExtra("id");
        amount=getIntent().getStringExtra("amount");
        wv_pay=(WebView)findViewById(R.id.wv_pay);

        WebSettings settings = wv_pay.getSettings();
        settings.setJavaScriptEnabled(true);
        wv_pay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        progressBar = ProgressDialog.show(PaymentActivity.this,"Connecting to server", "Please wait...");

        wv_pay.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(PaymentActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);
                alertDialogBuilder.setMessage(description);
                alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        wv_pay.loadUrl(ApiUrl.PAYMENT+"/"+c_id+"/"+b_id+"/"+amount);
    }
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to cancel?");
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
