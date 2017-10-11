package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;

public class LoginActivity extends BaseActivity {

    private SharedPreferences sp;
    EditText et_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bigappcompany.com.rsi.R.layout.activity_login);
        et_phone=(EditText)findViewById(R.id.et_mobile);
//        et_phone.setText(Digits.getActiveSession().getPhoneNumber());
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setStatusBarColor();

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((RippleView)findViewById(R.id.rp_login)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                Download_web web=new Download_web(LoginActivity.this, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String response) {
                        try {
                            closeDialog();
                            getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).edit().putBoolean(JsonParser.GO, true).commit();
                            if(new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE"))
                            {
                                //Toast.makeText(getApplicationContext(),"Calling home",Toast.LENGTH_SHORT).show();
                                getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE)
                                        .edit()
                                        .putString(JsonParser.CS_ID,new JSONObject(response)
                                                        .getJSONArray(JsonParser.DATA)
                                                        .getJSONObject(0)
                                                        .getString("customers_id"))
                                    .putString(JsonParser.SESSIONTOKEN,new JSONObject(response)
                                        .getString("token"))
                                        .commit();
                                
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }
                            else
                            {
//                                startActivity(new Intent(LoginActivity.this, ContactsActivity.class));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                web.setReqType(false);
                try {
                    web.setData((new JSONObject().put("api_key",ApiUrl.API_KEY).put("mobile",et_phone.getText().toString()).put("push_token",FirebaseInstanceId.getInstance().getToken())
                    .put("update_session_token","1")).toString());
                    web.execute(ApiUrl.VERIFY);
                    showDailog("Please wait...");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });




        sp=getSharedPreferences(JsonParser.APPNAME,MODE_PRIVATE);
        

    }
    private void setStatusBarColor() {
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#009788"));
        }
    }
    @Override
    public void onPause()
    {

        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

}
