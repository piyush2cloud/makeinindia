package com.example.piyush.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.util.Locale;


public class Registration extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    EditText et1, et2, et3, et4, et5, et6, et7, et8, et9, et10, et11, et12, et13,et14,et15,et16;
    Button b1;
    private DatePicker datePicker;
    private int day, month, year;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private double mlat;
    private double mlong;
    private MarkerOptions markerOption;
    private Marker currentMarker;
    Location mlastlocation;
    Bitmap bitmapImage;
    Button galeryBtn;
    String locationvalue;
    String proffesional,gender;
    ImageView imageView;
Spinner s1,s2;
    public static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        locationvalue = getIntent().getExtras().getString("geoaddress");

        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);
        et3 = (EditText) findViewById(R.id.emailid);
        et4 = (EditText) findViewById(R.id.phone);
        et5 = (EditText) findViewById(R.id.editgeoaddress);
        et7 = (EditText) findViewById(R.id.parentno);
        et8 = (EditText) findViewById(R.id.birthdate);
      //  et9 = (EditText) findViewById(R.id.profession);
        et10 = (EditText) findViewById(R.id.age);
        et11 = (EditText) findViewById(R.id.address);
        et12 = (EditText) findViewById(R.id.language);
        // et13 = (EditText) findViewById(R.id.gender);
        et14 = (EditText) findViewById(R.id.yourname);
        et15 = (EditText) findViewById(R.id.roomie1);
        et16 = (EditText) findViewById(R.id.roomie2);
        s1 = (Spinner) findViewById(R.id.proffesionspinner);
        s2 = (Spinner) findViewById(R.id.genderspinner);
        galeryBtn = (Button) findViewById(R.id.buttonGallery);
        imageView = (ImageView) findViewById(R.id.imgView);
        imageView.setVisibility(View.GONE);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item1 = parent.getItemAtPosition(position).toString();
                proffesional = item1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item2 = parent.getItemAtPosition(position).toString();
                gender = item2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        galeryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });



        buildGoogleApiClient();
        mGoogleApiClient.connect();


    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //   Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        et8.setText(new StringBuilder().append(year).append("/")
                .append(month).append("/").append(day));
    }

    private void setUpMapIfNeeded() {
        //  Toast.makeText(MapsActivity.this,"setupmapifneeded",Toast.LENGTH_LONG).show();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        markerOption = new MarkerOptions().position(new LatLng(mlat, mlong)).title("My Current Position");
        currentMarker = mMap.addMarker(markerOption);
        currentMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlat, mlong), 16));
    }

    protected synchronized void buildGoogleApiClient() {
        //  Toast.makeText(MapsActivity.this,"buildgoogleapi",Toast.LENGTH_LONG).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mlastlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mlastlocation != null) {

            mlat = mlastlocation.getLatitude();
            mlong = mlastlocation.getLongitude();
            et5.setText("Your Location is"+locationvalue);
          //  Toast.makeText(Registration.this, mlastlocation.toString(), Toast.LENGTH_LONG).show();
        }
        setUpMapIfNeeded();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void registerationPost(View view) {
        String username = et1.getText().toString();
        String password = et2.getText().toString();
        String emailid = et3.getText().toString();
        String phone = et4.getText().toString();
        String rlat = Double.toString(mlat);
        String rlong = Double.toString(mlong);
        String parentno = et7.getText().toString();
        String birthdate = et8.getText().toString();

       // String proffesional = et9.getText().toString();

        String age = et10.getText().toString();
        String address = et11.getText().toString();
        String language = et2.getText().toString();

      //  String gender = et13.getText().toString();

        String name = et14.getText().toString();
        String role = "Victim";
        String friend1 = et15.getText().toString();
        String friend2 = et16.getText().toString();

        new loginregister(this).execute(username, password, emailid, phone, rlat, rlong, parentno, birthdate, proffesional, age, address, language, gender,name,role,friend1,friend2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != intent) {


            Uri selectedImage = intent.getData();
            try {
                bitmapImage = decodeBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Show the Selected Image on ImageView
            imageView.setVisibility(View.VISIBLE);

            imageView.setImageBitmap(bitmapImage);


        }
    }
    public  Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

}
