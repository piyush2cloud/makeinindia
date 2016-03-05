package com.example.piyush.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SigninActivity  extends AsyncTask<String,Void,String>
{
    private Context context;
    private ProgressDialog progressDialog;
    private String checking;
    String homelat;
    String homelong,address;

    SigninActivity(Context c,ProgressDialog progressDialog)
    {
        this.progressDialog = progressDialog;
        context = c;
        this.progressDialog = new ProgressDialog(context);
    }


    protected void onPreExecute()
    {
        progressDialog.setMessage("Logging In");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0)
    {

        try
        {

            String username = (String)arg0[0];
            String password = (String)arg0[1];
            homelat = (String)arg0[2];
            homelong = (String)arg0[3];
            checking = username;
            address = (String)arg0[4];

            String link="http://iirs.netne.net/login.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
        if(result.equals("1"))
        {

            //Toast.makeText(context,"Login-succesful", Toast.LENGTH_LONG).show();
            //Toast.makeText(context,checking, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            Intent i2= new Intent(context,Mode.class);
            i2.putExtra ( "transfer", checking);
            i2.putExtra ( "homelat", homelat);
            i2.putExtra ( "homelong",homelong);
            i2.putExtra("address",address);

            context.startActivity(i2);


        }
        else
        {
            Intent i2= new Intent(context,home.class);
            Toast.makeText(context,"Login-Failed", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            context.startActivity(i2);

        }
    }
}



