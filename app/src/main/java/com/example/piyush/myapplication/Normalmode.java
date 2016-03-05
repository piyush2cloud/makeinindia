package com.example.piyush.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Normalmode extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    String result;
    JSONObject ja;
    int jsonlength;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Location mlastlocation;
    private MarkerOptions markerOption;
    private Marker currentMarker;
    Circle circles;

    EditText et1, et2, et3, et4, et5, et6, et7, et8, et9;
    Double mlat;
    Double mlong;
    ImageView photoview;
    Spinner condition,nopeople;
    String homelat,homelong,uservalue;

    Button mfood,mclothes,mmedicine,minjury,record,play,stop,photobutton,msubmit;

    public static int RESULT_LOAD_IMAGE = 1;
    Bitmap bitmapImage;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    Button radiusbutton;
    EditText editheight,editradius;


    protected CharSequence[] food = { "Daal", "Roti", "Sabji", "Water", "Fruits", "Milk" };
    protected ArrayList<CharSequence> selectfood = new ArrayList<CharSequence>();

    protected CharSequence[] clothes = { "Shirt", "Pant", "Sahwl", "Suit", "Saari", "Slippers" };
    protected ArrayList<CharSequence> selectclothe = new ArrayList<CharSequence>();

    protected CharSequence[] medicines = { "Head-Ache", "Stomach-Pain", "Breathing Problem", "Feeling Uneasy", "High Fever", "Cold & Cough","Others" };
    protected ArrayList<CharSequence> selectmedicine = new ArrayList<CharSequence>();

    protected CharSequence[] injury = { "Hand", "Leg", "Back", "Head", "Others", "Stomach"};
    protected ArrayList<CharSequence> selectinjury = new ArrayList<CharSequence>();

    String rusername,rfood,rmedicine,rclothes,rinjury,rradius,rpeople,rcondition,rtemp,rhumid,rdesc,rspeed,rrain;

    int floodradius;

    private ArrayList<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normalmode);



        nameValuePairs = new ArrayList<NameValuePair>();

        radiusbutton = (Button) findViewById(R.id.radiusbutton);
        editheight = (EditText) findViewById(R.id.height);
        editradius = (EditText) findViewById(R.id.radiusedit);

        et1 = (EditText) findViewById(R.id.temperature);
        et2 = (EditText) findViewById(R.id.Humidity);
        et3 = (EditText) findViewById(R.id.rain);
        et4 = (EditText) findViewById(R.id.description);
        et5 = (EditText) findViewById(R.id.sunrise);
        et6 = (EditText) findViewById(R.id.pressure);
        et7 = (EditText) findViewById(R.id.windspeed);
        et8 = (EditText) findViewById(R.id.latitude);
        et9 = (EditText) findViewById(R.id.longitude);
        msubmit = (Button) findViewById(R.id.submitrescue);

        editradius.setText("100");

        floodradius = Integer.parseInt(editradius.getText().toString());

        radiusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floodradius = Integer.parseInt(editradius.getText().toString());
                addthecircle(mlat, mlong, floodradius);
            }
        });

        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onsubmitrescue();
            }
        });
        et3.setText("0.24");


        homelat = getIntent().getStringExtra("homelat");
        homelong = getIntent().getStringExtra("homelong");
        uservalue = getIntent().getStringExtra("transfer");
        rusername = uservalue;

        condition = (Spinner) findViewById(R.id.spinnercondition);
        nopeople = (Spinner) findViewById(R.id.spinnerpeople);

        condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                String item2 = parent.getItemAtPosition(position).toString();
                rcondition = item2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);

            }
        });

        nopeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                String item2 = parent.getItemAtPosition(position).toString();
                rpeople = item2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
            }
        });

        mfood = (Button) findViewById(R.id.foodbutton);
        mclothes = (Button) findViewById(R.id.clothesbutton);
        mmedicine = (Button) findViewById(R.id.medicinebutton);
        minjury = (Button) findViewById(R.id.injurybutton);

        photoview = (ImageView) findViewById(R.id.photoview);

        photoview.setVisibility(View.GONE);

        photobutton = (Button) findViewById(R.id.photobutton);

        photobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        mfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectfoodDialog();
            }
        });

        mclothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectClothesDialog();
            }
        });

        mmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectmedicinesDialog();
            }
        });


        minjury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectinjuryDialog();
            }
        });

        record = (Button) findViewById(R.id.record1);
        play  = (Button) findViewById(R.id.record2);
        stop = (Button) findViewById(R.id.record3);

        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        record.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder  = null;

                stop.setEnabled(false);
                play.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();

            }
        });


        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    public void addthecircle(Double newlat,Double newlong,int newradius) {

        circles = mMap.addCircle(new CircleOptions()
                .center(new LatLng(newlat, newlong))
                .radius(newradius)
                .strokeColor(Color.BLUE)
                .fillColor(0x5500ff00));
    }




    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    protected synchronized void buildGoogleApiClient() {
        //  Toast.makeText(MapsActivity.this,"buildgoogleapi",Toast.LENGTH_LONG).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void setUpMapIfNeeded()
    {
        //  Toast.makeText(MapsActivity.this,"setupmapifneeded",Toast.LENGTH_LONG).show();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMarkerDragListener(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap()
    {

        markerOption = new MarkerOptions().position(new LatLng(mlat,mlong)).title("My Current Position").draggable(true);
        currentMarker = mMap.addMarker(markerOption);
        currentMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlat, mlong), 17));
    }

    @Override
    public void onConnected(Bundle bundle) {

        mlastlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mlastlocation != null) {
            mlat = mlastlocation.getLatitude();
            mlong = mlastlocation.getLongitude();

            et8.setText(Double.toString(mlat));
            et9.setText(Double.toString(mlong));

            setUpMapIfNeeded();
            findweather();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void findweather() {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.openweathermap.org/data/2.5/weather?lat=" + mlat + "&lon=" + mlong + "&appid=289ae02237d978d414b1fa53ae903911");
            enableStrictMode();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");

            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            JSONObject ja = new JSONObject(result);

            // Toast.makeText(getApplicationContext(), ja.toString(), Toast.LENGTH_LONG).show();

            JSONObject main = ja.getJSONObject("main");

            String temperature = main.getString("temp");
            et1.setText(temperature);
            rtemp =temperature;

            String humid = main.getString("humidity");
            et2.setText(humid);
            rhumid = humid;

            String press = main.getString("pressure");
            et6.setText(press);
            //  Toast.makeText(this, humid, Toast.LENGTH_LONG).show();

            JSONObject syst = ja.getJSONObject("sys");
            String sunrise = syst.getString("sunrise");
            //  Toast.makeText(this,"sunrise"+ sunrise, Toast.LENGTH_LONG).show();
            et5.setText(sunrise);

            String ok = ja.getString("name");
          //  Toast.makeText(this, ok, Toast.LENGTH_LONG).show();

            JSONObject wind = ja.getJSONObject("wind");
            String speed = wind.getString("speed");
            et7.setText(speed);
            rspeed = speed;


            JSONArray weathers = ja.optJSONArray("weather");

            JSONObject weatherdescr = weathers.getJSONObject(0);
            String descr = weatherdescr.optString("description").toString();
            et4.setText(descr);
            rdesc = descr;

            String icon = weatherdescr.getString("icon");
            //  Toast.makeText(this,"icon"+ icon, Toast.LENGTH_LONG).show();

            JSONObject rainfalls = ja.getJSONObject("rain");
            String rain = rainfalls.getString("3h");
            et3.setText(rain);
            rrain =rain;

            //  Toast.makeText(this, rainfalls.toString(), Toast.LENGTH_LONG).show();
            //  Toast.makeText(this, rain, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
        }
    }


    protected void showSelectfoodDialog() {

        boolean[] checkedColours = new boolean[food.length];
        int count = food.length;
        for(int i = 0; i < count; i++)
            checkedColours[i] = selectfood.contains(food[i]);

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)

                    selectfood.add(food[which]);

                else

                    selectfood.remove(food[which]);

                onChangeselectedfood();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Food");

        builder.setMultiChoiceItems(food, checkedColours, coloursDialogListener);

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    protected void onChangeselectedfood() {

        StringBuilder stringBuilder = new StringBuilder();

        for(CharSequence colour : selectfood)

            stringBuilder.append(colour + ",");

        mfood.setText(stringBuilder.toString());

    }

    protected void showSelectClothesDialog() {

        boolean[] checkedColours = new boolean[clothes.length];
        int count = clothes.length;
        for(int i = 0; i < count; i++)
            checkedColours[i] = selectclothe.contains(clothes[i]);

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)

                    selectclothe.add(clothes[which]);

                else

                    selectclothe.remove(clothes[which]);

                onChangeSelectedClothes();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Clothes");

        builder.setMultiChoiceItems(clothes, checkedColours, coloursDialogListener);

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    protected void onChangeSelectedClothes() {

        StringBuilder stringBuilder = new StringBuilder();

        for(CharSequence colour : selectclothe)

            stringBuilder.append(colour + ",");

        mclothes.setText(stringBuilder.toString());

    }


    protected void showSelectmedicinesDialog() {

        boolean[] checkedColours = new boolean[medicines.length];
        int count = medicines.length;
        for(int i = 0; i < count; i++)
            checkedColours[i] = selectmedicine.contains(medicines[i]);

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)

                    selectmedicine.add(medicines[which]);

                else

                    selectmedicine.remove(medicines[which]);

                onChangeSelectedmedicine();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Medicine");

        builder.setMultiChoiceItems(medicines, checkedColours, coloursDialogListener);

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    protected void onChangeSelectedmedicine() {

        StringBuilder stringBuilder = new StringBuilder();

        for(CharSequence colour : selectmedicine)

            stringBuilder.append(colour + ",");

        mmedicine.setText(stringBuilder.toString());

    }

    protected void showSelectinjuryDialog() {

        boolean[] checkedColours = new boolean[injury.length];
        int count = injury.length;
        for(int i = 0; i < count; i++)
            checkedColours[i] = selectinjury.contains(injury[i]);

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)

                    selectinjury.add(injury[which]);

                else

                    selectinjury.remove(injury[which]);

                onChangeSelectedColours();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Injury Occured");

        builder.setMultiChoiceItems(injury, checkedColours, coloursDialogListener);

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    protected void onChangeSelectedColours() {

        StringBuilder stringBuilder = new StringBuilder();

        for(CharSequence colour : selectinjury)

            stringBuilder.append(colour + ",");

        minjury.setText(stringBuilder.toString());

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
            photoview.setVisibility(View.VISIBLE);
            photoview.setImageBitmap(bitmapImage);


        }
    }

    public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
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

    public void onsubmitrescue() {

        rusername = uservalue;
        rfood = mfood.getText().toString();
        rmedicine = mmedicine.getText().toString();
        rclothes = mclothes.getText().toString();
        rinjury = minjury.getText().toString();
        rradius = Integer.toString(floodradius);


        nameValuePairs.add(new BasicNameValuePair("username", rusername));
        nameValuePairs.add(new BasicNameValuePair("rfood", rfood));
        nameValuePairs.add(new BasicNameValuePair("rmedicine", rmedicine));
        nameValuePairs.add(new BasicNameValuePair("rclothes", rclothes));
        nameValuePairs.add(new BasicNameValuePair("rinjury", rinjury));
        nameValuePairs.add(new BasicNameValuePair("rradius", rradius));
        nameValuePairs.add(new BasicNameValuePair("rpeople", rpeople));
        nameValuePairs.add(new BasicNameValuePair("rcondition", rcondition));
        nameValuePairs.add(new BasicNameValuePair("rtemp", rtemp));
        nameValuePairs.add(new BasicNameValuePair("rhumid", rhumid));
        nameValuePairs.add(new BasicNameValuePair("rdesc", rdesc));
        nameValuePairs.add(new BasicNameValuePair("rspeed", rspeed));
        nameValuePairs.add(new BasicNameValuePair("rlat", Double.toString(mlat)));
        nameValuePairs.add(new BasicNameValuePair("rlong", Double.toString(mlong)));
        nameValuePairs.add(new BasicNameValuePair("rheight", editheight.getText().toString()));



        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://iirs.netne.net/rescue.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            enableStrictMode();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");

            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            Toast.makeText(this,"Your Information has Been Succesfully sent",Toast.LENGTH_SHORT).show();
            Intent i1 =  new Intent(Normalmode.this,home.class);
            startActivity(i1);


        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }


}






