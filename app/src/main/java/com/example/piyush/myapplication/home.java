package com.example.piyush.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class home extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Button b1;
    Button b2;
    Button b3,b4;
    Location mlastlocation;
    private GoogleApiClient mGoogleApiClient;
    public double mlat;
    public double mlong;
    Geocoder geocoder;
    String strAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.loginvictim);
        b4 = (Button) findViewById(R.id.emergency);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(home.this, Registration.class);
                i1.putExtra("geoaddress", strAddress);
                startActivity(i1);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i3 = new Intent(home.this, MainActivity.class);
                i3.putExtra("homelat",Double.toString(mlat));
                i3.putExtra("homelong",Double.toString(mlong));
                i3.putExtra("totaladdress",strAddress);

                startActivity(i3);
            }
        });

       /* b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(home.this, traveller.class);
                startActivity(i2);
            }
        });*/

        geocoder = new Geocoder(this, Locale.getDefault());

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+919634911939", null,strAddress, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(MapsActivity.this,"buildgoogleapi",Toast.LENGTH_LONG).show();
      mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mlastlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mlat = mlastlocation.getLatitude();
        mlong = mlastlocation.getLongitude();


        if (mlastlocation != null) {
            if (geocoder.isPresent()) {
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(mlat, mlong, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = list.get(0);

                StringBuffer str = new StringBuffer();
                str.append("Name: " + address.getLocality() + "\n");
                str.append("Location:" + address.getFeatureName() + "\n");
                str.append("Country:" + address.getCountryName() + "\n");
                str.append("Country Code:" + address.getCountryCode() + "\n");

                strAddress = str.toString();
                Toast.makeText(getApplicationContext(),"Flood Rescue App", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
