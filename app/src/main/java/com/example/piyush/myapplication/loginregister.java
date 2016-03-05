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

public class loginregister extends AsyncTask<String,Void,String>
{
    private Context context;
    ProgressDialog pd;


    loginregister(Context c)
    {
         this.context=c;
         pd = new ProgressDialog(context);
    }

    protected void onPreExecute()
    {
       pd.setMessage("Registering Your Account!");
        pd.show();
    }

    @Override
    protected String doInBackground(String... arg0) {

            try
            {
                String username  = (String) arg0[0];
                String password  = (String) arg0[1];
                String emailid   = (String) arg0[2];
                String phone     = (String) arg0[3];
                String rlat     =   (String) arg0[4];
                String rlong     =  (String) arg0[5];
                String parentno     =  (String) arg0[6];
                String birthdate     =  (String) arg0[7];
                String proffesional     =  (String) arg0[8];
                String age     =  (String) arg0[9];
                String address     =  (String) arg0[10];
                String language     =  (String) arg0[11];
                String gender     =  (String) arg0[12];

                String name = (String) arg0[13];
                String role = (String) arg0[14];
                String friend1 = (String) arg0[15];
                String friend2 = (String) arg0[16];



                String link = "http://iirs.netne.net/register.php";

                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("emailid", "UTF-8") + "=" + URLEncoder.encode(emailid, "UTF-8");
                data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

                data += "&" + URLEncoder.encode("rlat", "UTF-8") + "=" + URLEncoder.encode(rlat, "UTF-8");

                data += "&" + URLEncoder.encode("rlong", "UTF-8") + "=" + URLEncoder.encode(rlong, "UTF-8");
                data += "&" + URLEncoder.encode("parentno", "UTF-8") + "=" + URLEncoder.encode(parentno, "UTF-8");
                data += "&" + URLEncoder.encode("birthdate", "UTF-8") + "=" + URLEncoder.encode(birthdate, "UTF-8");
                data += "&" + URLEncoder.encode("proffesional", "UTF-8") + "=" + URLEncoder.encode(proffesional, "UTF-8");
                data += "&" + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8");
                data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");
                data += "&" + URLEncoder.encode("language", "UTF-8") + "=" + URLEncoder.encode(language, "UTF-8");
                data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8");
                data += "&" + URLEncoder.encode("friend1", "UTF-8") + "=" + URLEncoder.encode(friend1, "UTF-8");
                data += "&" + URLEncoder.encode("friend2", "UTF-8") + "=" + URLEncoder.encode(friend2, "UTF-8");



                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }


    @Override
       protected void onPostExecute(String result)
       {


         if(result.equals("inserted"))
        {
            Toast.makeText(context, "Registration Successful", Toast.LENGTH_LONG).show();
            pd.dismiss();
            Intent i3 = new Intent(context,MainActivity.class);
            context.startActivity(i3);
        }
        else
        {
            Toast.makeText(context, "Login already exists", Toast.LENGTH_LONG).show();
            pd.dismiss();

        }

    }
}