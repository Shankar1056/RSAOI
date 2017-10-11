package bigappcompany.com.rsi.activity;


import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.circularreveal.CircularAnimationUtils;
import bigappcompany.com.rsi.network.ApiUrl;
import bigappcompany.com.rsi.network.ClsGeneral;
import bigappcompany.com.rsi.network.Download_web;
import bigappcompany.com.rsi.network.JsonParser;
import bigappcompany.com.rsi.network.OnTaskCompleted;


public class SplashActivity extends AppCompatActivity {
	
	String[] permissions = new String[]{android.Manifest.permission.RECEIVE_SMS};
	FrameLayout rootLayout;
	ImageView imageView;
	private boolean canGo = false;
	private SharedPreferences sp;
	private int REQ_SMS = 200;
	
	public static final int RC_SIGN_IN = 1;
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
	private static final String TAG = "AplashActivity";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		imageView = (ImageView) findViewById(R.id.image_main);
		rootLayout = (FrameLayout) findViewById(R.id.rootLayout);
		sp = getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE);
		
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				checksession();
				
			}
		}, 3000);
		
		
		//changing

		/*authConfigBuilder = new AuthConfig.Builder()
				.withAuthCallBack(new AuthCallback() {
					@Override
					public void success(DigitsSession session, String phoneNumber) {

						login(phoneNumber,"1");
					}

					@Override
					public void failure(DigitsException error) {
						Toast.makeText(getApplicationContext(),"Missed",Toast.LENGTH_SHORT).show();
					}
				})
				.withPhoneNumber("+91");
		if(Digits.getActiveSession()!=null)
		login(Digits.getActiveSession().getPhoneNumber(),"0");
		else
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if(Digits.getActiveSession()==null) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
					{
						int res = checkCallingOrSelfPermission(permissions[0]);

						if (res == PackageManager.PERMISSION_GRANTED) {
								Digits.authenticate(authConfigBuilder.build());
						} else
						{
							requestPermissions(permissions, REQ_SMS);
						}
					}
					else
						Digits.authenticate(authConfigBuilder.build());
				}
				else if (!getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false)) {
					
					startActivity(new Intent(SplashActivity.this, Contact.class));
					finish();
				} else {
					Intent i = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(i);
					finish();
				}


			}

		}, 3000);

*/
	}
	
	private void checksession() {
		AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
		    new OnSuccessListener() {
			    
			    @Override
			    public void onSuccess(Object o) {
				    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
				    if (u != null) {
					    // Either a user was already logged in or token exchange succeeded
					    Log.d("MyApp", "Digits id preserved:" + u.getUid());
					    Log.d("MyApp", "Digits phone number preserved: " + u.getPhoneNumber());
					  //  login(u.getPhoneNumber().toString(), "0");
					    if (!getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false)) {
						
						    startActivity(new Intent(SplashActivity.this, Contact.class));
						    finish();
					    }
					    else {
						    startActivity(new Intent(SplashActivity.this, MainActivity.class));
						    finish();
					    }
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
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);
			// Successfully signed in
			if (resultCode == ResultCodes.OK) {
				login(response.getPhoneNumber(), "1");
//				return;
			} else {
				// Sign in failed
				if (response == null) {
					// User pressed back button
					Log.e("Login", "Login canceled by User");
//					return;
				}
				if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
					Log.e("Login", "No Internet Connection");
//					return;
				}
				if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
					Log.e("Login", "Unknown Error");
//					return;
				}
			}
			Log.e("Login", "Unknown sign in response");
			
					}
	}
	
	private void login(String phone, final String update_session_token) {
		Download_web web = new Download_web(SplashActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					
					if (new JSONObject(response).getString(JsonParser.RESPONSE_STATUS).equals("TRUE")) {
						getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE).edit().putBoolean(JsonParser.GO, true).commit();
						
						//Toast.makeText(getApplicationContext(),"Calling home",Toast.LENGTH_SHORT).show();
						getSharedPreferences(JsonParser.APPNAME, MODE_PRIVATE)
						    .edit()
						    .putString(JsonParser.CS_ID, new JSONObject(response)
							.getJSONArray(JsonParser.DATA)
							.getJSONObject(0)
							.getString("customers_id"))
						    .putString(JsonParser.CS_NAME, new JSONObject(response)
							.getJSONArray(JsonParser.DATA)
							.getJSONObject(0)
							.getString("customers_first_name"))
						    .putString(JsonParser.CS_LASTNAME, new JSONObject(response)
							.getJSONArray(JsonParser.DATA)
							.getJSONObject(0)
							.getString("customers_last_name"))
						    .putString(JsonParser.CS_EMAIL, new JSONObject(response)
							.getJSONArray(JsonParser.DATA)
							.getJSONObject(0)
							.getString("customers_email"))
						    .putString(JsonParser.CS_CONTACT, new JSONObject(response)
							.getJSONArray(JsonParser.DATA)
							.getJSONObject(0)
							.getString("customers_mobile"))
						    .putString(JsonParser.SESSIONTOKEN, new JSONObject(response)
							.getString("token"))
						    .commit();
						if (update_session_token.equalsIgnoreCase("1"))
						
						{
							ClsGeneral.setPreferences(SplashActivity.this, ClsGeneral
							    .UPDATESESSIONTOKEN, new JSONObject(response)
							    .getString("token"));
						}
						
						PackageInfo pInfo = null;
						try {
							pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
							int version = pInfo.versionCode;
							if (Integer.parseInt(new JSONObject(response).getString
							    ("version_number")) < version) {
								startActivity(new Intent(SplashActivity.this, MainActivity.class));
								finish();
							} else {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
								alertDialogBuilder.setMessage("Please Update app to continue");
								alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										finish();
									}
								});
								alertDialogBuilder.setPositiveButton("YES",
								    new DialogInterface.OnClickListener() {
									    @Override
									    public void onClick(DialogInterface arg0, int arg1) {
										    Uri uri = Uri.parse("market://details?id=" + SplashActivity.this.getPackageName());
										    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
										    // To count with Play market backstack, After pressing back button,
										    // to taken back to our application, we need to add following flags to intent.
										    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
											Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
											Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
										    try {
											    startActivity(goToMarket);
										    } catch (ActivityNotFoundException e) {
											    startActivity(new Intent(Intent.ACTION_VIEW,
												Uri.parse("http://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName())));
										    }
									    }
								    });
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
							}
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
						}
						
						
					} else {
						startActivity(new Intent(SplashActivity.this, Contact.class).putExtra(JsonParser.INVALID, true));
						
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		web.setReqType(false);
		try {
			/*try {
				String a = FirebaseInstanceId.getInstance().getToken();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}*/
			web.setData((new JSONObject().put("api_key", ApiUrl.API_KEY).put("mobile", phone).put
			    ("push_token", FirebaseInstanceId.getInstance().getToken()).put("update_session_token", update_session_token))
			    .toString());
			web.execute(ApiUrl.VERIFY);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void gotoHome(View view) {
		if (canGo) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	
	private void circularRevealActivity() {
		
		int cx = rootLayout.getWidth();
		int cy = rootLayout.getHeight();
		
		float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight()) + 500;
		
		// create the animator for this view (the start radius is zero)
		
		Animator circularReveal = null;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			
			circularReveal = CircularAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
			
		} else {
			
			circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
		}
		
		circularReveal.setDuration(1000);
		
		rootLayout.setVisibility(View.VISIBLE);
		circularReveal.start();
		circularReveal.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				imageView.setVisibility(View.VISIBLE);
				AnimationSet animationSet = new AnimationSet(true);
				animationSet.setDuration(800);
				animationSet.setFillAfter(true);
				Animation animation1 = new AlphaAnimation(0.0f, 1.0f);
				animation1.setFillAfter(true);
				animation1.setDuration(300);
				
				
				Animation animation2 = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f);
				animation2.setFillAfter(true);
				animation2.setInterpolator(new AccelerateDecelerateInterpolator());
				animation2.setDuration(600);
				
				animationSet.addAnimation(animation1);
				animationSet.addAnimation(animation2);
				
				imageView.setAnimation(animationSet);
				imageView.startAnimation(animationSet);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				
			}
		});
	}
	
	@Override
	public void onStart() {
		rootLayout.setVisibility(View.INVISIBLE);
		
		
		Picasso.with(this).load(R.drawable.splash).into(imageView);
		
		
		ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					circularRevealActivity();
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
						rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					} else {
						rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
					
					canGo = true;
				}
			});
		}
		
		
		super.onStart();
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			
		} else {
			Toast.makeText(this, "Permission not granted,Auto detect OTP will not work", Toast.LENGTH_LONG).show();
		}
		/*if (Digits.getActiveSession()==null) {
			Digits.authenticate(authConfigBuilder.build());
		}*/
	}
	
	
}
