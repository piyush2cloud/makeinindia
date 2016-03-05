package com.example.piyush.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText usernameField,passwordField;
    ProgressDialog progressDialog;
    String latvalue;
    String longvalue,address;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText) findViewById(R.id.edittext1);
        passwordField = (EditText) findViewById(R.id.editText2);

        latvalue = getIntent().getStringExtra("homelat");
        longvalue = getIntent().getStringExtra("homelong");
        address = getIntent().getStringExtra("totaladdress");


    }


    public void loginPost(View view)
    {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String homelat  = latvalue;
        String homelong = longvalue;
        new SigninActivity(this,progressDialog).execute(username, password,homelat,homelong,address);
    }

}