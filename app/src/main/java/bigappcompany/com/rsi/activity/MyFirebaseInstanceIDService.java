package bigappcompany.com.rsi.activity;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Dell on 11/18/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences sp;
    boolean isSeller;
    String token="";
    String ref_token="";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", "Refreshed token: " + refreshedToken);
        sp=getSharedPreferences("Seller",MODE_PRIVATE);
        isSeller=sp.getBoolean("isSeller",false);
        token=sp.getString("token","");
        // TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        if(!token.equals("")) {
            ref_token=refreshedToken;
            SharedPreferences shared=getSharedPreferences("Seller",MODE_PRIVATE);
            SharedPreferences.Editor edit=shared.edit();
            edit.putString("fcm_token",ref_token);
            edit.commit();

        }
    }



}
