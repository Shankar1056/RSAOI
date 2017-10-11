package bigappcompany.com.rsi.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class Download_web extends AsyncTask<String, String, String>
    {
        private final Context context;
        private String response="";
        private OnTaskCompleted listener;
        private boolean isGet=true;

        public Download_web(Context context,OnTaskCompleted listener)
        {
            this.context=context;
            this.listener=listener;
        }
        public void setReqType(boolean isGet)
        {
            this.isGet=isGet;
        }
        public void setData(String data)
        {
            this.data=data;
        }
        private String data;
        @Override
        protected String doInBackground(String... params)
        {

            for(String url:params)
            {
                if(isGet)
                {
                    response=doGet(url);
                }
                else
                {
                    response=doPost(url);
                }
            }

            return response;
        }
        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result)
        {

            if(!result.equals(""))
            {
                listener.onTaskCompleted(result);
            }
            else
            {

                Toast.makeText(context,  "something wrong", Toast.LENGTH_LONG).show();
            }

        }

        private String doGet(String url)
        {
            try
            {

                HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
                c.setRequestProperty("Authorization", "Basic " +
                        Base64.encodeToString(new ApiUrl().getUnp().getBytes(), Base64.DEFAULT));
                c.setUseCaches(false);
                c.connect();
                BufferedReader buf=new BufferedReader(new InputStreamReader(c.getInputStream()));
                String sr="";
                while((sr=buf.readLine())!=null)
                {
                    response+=sr;
                }
                Log.e("json", response);

            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.e("Exception",e.getMessage());
                response="";
                return response;
            }
            return response;
        }private String doPost(String url)
        {
            try
            {

                HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
                c.setRequestProperty("Authorization", "Basic " +
                        Base64.encodeToString(new ApiUrl().getUnp().getBytes(), Base64.DEFAULT));
                c.setUseCaches(false);
                c.setDoOutput(true);
                c.connect();

                OutputStreamWriter Out_wr=new OutputStreamWriter(c.getOutputStream());
                Log.e("sending_data",data);
                Out_wr.write(data.toString());
                Out_wr.flush();
                BufferedReader buf=new BufferedReader(new InputStreamReader(c.getInputStream()));
                String sr="";
                while((sr=buf.readLine())!=null)
                {
                    response+=sr;
                }
                Log.e("json", response);
                c.disconnect();

            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.e("Exception",e.getMessage());
                response="";
                return response;
            }
            return response;
        }



    }