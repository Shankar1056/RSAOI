package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;

import bigappcompany.com.rsi.R;



/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 04 Oct 2017 at 4:19 PM
 */

public class LoginTest extends AppCompatActivity {
	
	public static final int RC_SIGN_IN = 1;
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
	private static final String TAG = "PhoneAuthActivity";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		
			
		
		
		/*PhoneAuthProvider.getInstance().verifyPhoneNumber(
		    "8002877277",        // Phone number to verify
		    60,                 // Timeout duration
		    TimeUnit.SECONDS,   // Unit of timeout
		    this,               // Activity (for callback binding)
		    mCallbacks);*/
		
		
		AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
		    new OnSuccessListener() {
			   
			    @Override
			    public void onSuccess(Object o) {
				    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
				    if (u != null) {
					    // Either a user was already logged in or token exchange succeeded
					    Log.d("MyApp", "Digits id preserved:" + u.getUid());
					    Log.d("MyApp", "Digits phone number preserved: " + u.getPhoneNumber());
				    } else {
					    // No tokens were found to exchange and no Firebase user logged in.
					    startActivityForResult(
					        AuthUI.getInstance()
						    .createSignInIntentBuilder()
						    .setProviders(Arrays.asList(
						        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
						
						    ))
						    .build(),
					        RC_SIGN_IN);
				      }
			    }
		    }).addOnFailureListener(this,
		    new OnFailureListener() {
			    @Override
			    public void onFailure(@NonNull Exception e) {
				    e.printStackTrace();
				    // Error migrating Digits token
			    }
		    });
		
		
		mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
				PhoneAuthCredential ph = phoneAuthCredential;
				Log.e("phoneAuthCredential",""+phoneAuthCredential);
			}
			
			@Override
			public void onVerificationFailed(FirebaseException e) {
				e.printStackTrace();
			}
		};
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);
			// Successfully signed in
			if (resultCode == ResultCodes.OK) {
				//startActivity(new Intent(PhoneNumberAuthentication.this,MainActivity.class));
				//finish();
				return;
			} else {
				// Sign in failed
				if (response == null) {
					// User pressed back button
					Log.e("Login", "Login canceled by User");
					return;
				}
				if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
					Log.e("Login", "No Internet Connection");
					return;
				}
				if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
					Log.e("Login", "Unknown Error");
					return;
				}
			}
			Log.e("Login", "Unknown sign in response");
		}
	}
	
}
