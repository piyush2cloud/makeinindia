package com.example.piyush.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class register extends AsyncTask<String,Void,String>
{

private Context context;


//flag 0 means get and 1 means post.(By default it is get.)
public register(Context context)
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
                String shopname =  (String)arg0[3];
                String userentered =  (String)arg0[4];
                String dateentered =  (String)arg0[5];

                String peopleval = (String)arg0[6];
                String heightval = (String)arg0[7];
                String tempval =   (String)arg0[8];
                String foodval = (String)arg0[9];
                String medicineval= (String)arg0[10];
                String clotheval= (String)arg0[11];
                String floodradius = (String)arg0[12];

                String injury = (String)arg0[13];
                String msg = (String)arg0[14];
                String humidity = (String)arg0[15];
                String rainfall = (String)arg0[16];



                String link="http://iirs.netne.net/vendor.php";

                String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("offers", "UTF-8") + "=" + URLEncoder.encode(offers, "UTF-8");
                data += "&" + URLEncoder.encode("shop", "UTF-8") + "=" + URLEncoder.encode(shopname, "UTF-8");
                data += "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(userentered, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(dateentered, "UTF-8");
                data += "&" + URLEncoder.encode("people", "UTF-8") + "=" + URLEncoder.encode(peopleval, "UTF-8");
                data += "&" + URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(heightval, "UTF-8");
                data += "&" + URLEncoder.encode("temp", "UTF-8") + "=" + URLEncoder.encode(tempval, "UTF-8");
                data += "&" + URLEncoder.encode("food", "UTF-8") + "=" + URLEncoder.encode(foodval, "UTF-8");
                data += "&" + URLEncoder.encode("medicine", "UTF-8") + "=" + URLEncoder.encode(medicineval, "UTF-8");
                data += "&" + URLEncoder.encode("clothes", "UTF-8") + "=" + URLEncoder.encode(clotheval, "UTF-8");
                data += "&" + URLEncoder.encode("floodradius", "UTF-8") + "=" + URLEncoder.encode(floodradius, "UTF-8");
                data += "&" + URLEncoder.encode("injury", "UTF-8") + "=" + URLEncoder.encode(injury, "UTF-8");
                data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(msg, "UTF-8");
                data += "&" + URLEncoder.encode("humidity", "UTF-8") + "=" + URLEncoder.encode(humidity, "UTF-8");
                data += "&" + URLEncoder.encode("rainfall", "UTF-8") + "=" + URLEncoder.encode(rainfall, "UTF-8");


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
            Toast.makeText(context,"Disaster Has been Successfully Submitted",Toast.LENGTH_SHORT).show();
            Intent i4 = new Intent(context,home.class);
            context.startActivity(i4);
        }

}
