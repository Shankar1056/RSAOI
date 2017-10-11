package bigappcompany.com.rsi.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by embed on 13/5/15.
 */
public class Utility
{
    /**
     * 3embed
     * method to get the device id
     * @param context
     * @return
     */

   
    public static boolean isNetworkAvailable(Context context)
    {

        ConnectivityManager connectivity  = null;
        boolean isNetworkAvail = false;

        try
        {
            connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity != null)
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();

                if (info != null)
                {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {

                            return true;
                        }
                    }
                }
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(connectivity !=null)
            {
                connectivity = null;
            }
        }
        return false;
    }



    private static final int HTTP_TIMEOUT = 60 * 1000; // milliseconds

    private static HttpClient mHttpClient;


    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;

    }


    public static String executeHttpPost(String url,
                                         ArrayList<NameValuePair> postParameters) throws Exception {
        final String basicAuth = "Basic " + Base64.encodeToString(new ApiUrl().getUnp().getBytes(), Base64.NO_WRAP);
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
           // HttpPost request = new HttpPost(url);

            HttpPost request = new HttpPost();
            request.setHeader("Authorization",basicAuth);
            
            request.setURI(new URI(url));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();

            return sb.toString();
          //  return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();

            return sb.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   

    public static boolean isValidEmail1(CharSequence target) {
       return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static int getpayableamount(String receivedamount) {
        int amount,totalamount = 0;
        try
        {
            amount = Integer.parseInt(receivedamount);
            totalamount = amount+((amount*1)/100);
            
        }
        catch (NumberFormatException e)
        {
            
        }
        catch (Exception e)
        {
            
        }
        return totalamount;
    }
    
}
