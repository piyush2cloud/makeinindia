package com.example.piyush.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;


public class DealLookup extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Boolean k;
        k= haveNetworkConnection();

        if(k==false)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    DealLookup.this).create();
            alertDialog.setTitle("deal LookUp Welcomes You");
            alertDialog.setMessage("Please Connect to Internet to use this exciting App");
            alertDialog.setIcon(R.drawable.abc_textfield_default_mtrl_alpha);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    Toast.makeText(getApplicationContext(), "Thank You. Visit us again", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });



            // Showing Alert Message
            alertDialog.show();
        }
        else
        {
            Intent i = new Intent(DealLookup.this,home.class);
            startActivity(i);
        }
    }

    private boolean haveNetworkConnection()
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo)
        {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }

        return haveConnectedWifi || haveConnectedMobile;
    }



}
