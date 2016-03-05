package com.example.piyush.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class travellerclass extends AsyncTask<String,Void,String>
{

private Context context;


//flag 0 means get and 1 means post.(By default it is get.)
public travellerclass(Context context)
{
this.context = context;
}

protected void onPreExecute(){

}
    @Override
    protected String doInBackground(String... arg0)
    {
            try{

                String username = (String)arg0[0];
                String password = (String)arg0[1];
                String offers   =  (String)arg0[2];
                Log.d("1",offers);

                String link="http://iirs.netne.net/vendor.php";
                String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("offers", "UTF-8") + "=" + URLEncoder.encode(offers, "UTF-8");


                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            }
            catch(Exception e)
            {
                return new String("Exception: " + e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(String result)
        {

        }

}
