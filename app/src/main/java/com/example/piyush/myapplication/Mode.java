package com.example.piyush.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Mode extends AppCompatActivity {

    String value,result,total;
    Button b1;
    Button b2;
    String homelat,homelong,strAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        b2 = (Button) findViewById(R.id.button2);
        value = getIntent().getStringExtra("transfer");
        homelat= getIntent().getStringExtra("homelat");
        homelong = getIntent().getStringExtra("homelong");
        strAddress = getIntent().getStringExtra("address");
      //  Toast.makeText(getApplicationContext(), strAddress, Toast.LENGTH_LONG).show();


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i3 = new Intent(Mode.this, Victim.class);
                i3.putExtra("transfer", value);
                i3.putExtra("homelat",homelat);
                i3.putExtra("homelong",homelong);
                startActivity(i3);

            }
        });

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://iirs.netne.net/name.php?username="+value);
            enableStrictMode();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");

           // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            JSONObject ja = new JSONObject(result);


        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
        }

        total = strAddress +  "Lat:" + homelat + "," + "Long:" + homelong;


        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(),
                PhoneStateListener.LISTEN_CALL_STATE);

    }

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    class TeleListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    // CALL_STATE_IDLE;
                    Toast.makeText(getApplicationContext(), "CALL_STATE_IDLE",
                            Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // CALL_STATE_OFFHOOK;
                    Toast.makeText(getApplicationContext(), "CALL_STATE_OFFHOOK",
                            Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // CALL_STATE_RINGING

                    Toast.makeText(getApplicationContext(), incomingNumber,
                            Toast.LENGTH_SHORT).show();
                    //     Toast.makeText(getApplicationContext(), "CALL_STATE_RINGING",
                    //          Toast.LENGTH_SHORT).show();
//
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(incomingNumber, null,total, null, null);
                        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }

    }
}
