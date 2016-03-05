package com.example.piyush.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerDragListener,GoogleMap.OnMarkerClickListener
{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Location mlastlocation;
    EditText et1;
    EditText et2;
    private double mlat;
    private double mlong;
    private MarkerOptions markerOption;
    private Marker currentMarker;
    EditText et3;
    Button b1;
    EditText moffer,mdate,mshop,mtemp,mheight,mpeople;
    private Calendar cal;
    Button b2,mfood,mclothes,mmedicine,minjury;
    private int day,month,year;
    private DatePicker datePicker;
    String value,mcategory;
    private String[] states;

    String result;
    JSONObject ja;
    int jsonlength;

    String currenttemp,currenthumid,currentrain;


    Button b3,b4,record,play,stop;
    ImageView photoview;
    VideoView videoview;
    Circle circles;

    public static int RESULT_LOAD_IMAGE = 1;
    Bitmap bitmapImage;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    EditText radiustext,emergencyedit;
    Button radiusbutton;
    int floodradius = 100;

    Button service ;
    Button sosbutton ;





    protected CharSequence[] food = { "Daal", "Roti", "Sabji", "Water", "Fruits", "Milk" };
    protected ArrayList<CharSequence> selectfood = new ArrayList<CharSequence>();

    protected CharSequence[] clothes = { "Shirt", "Pant", "Sahwl", "Suit", "Saari", "Slippers" };
    protected ArrayList<CharSequence> selectclothe = new ArrayList<CharSequence>();

    protected CharSequence[] medicines = { "Head-Ache", "Stomach-Pain", "Breathing Problem", "Feeling Uneasy", "High Fever", "Cold & Cough","Others" };
    protected ArrayList<CharSequence> selectmedicine = new ArrayList<CharSequence>();

    protected CharSequence[] injury = { "Hand", "Leg", "Back", "Head", "Others", "Stomach"};
    protected ArrayList<CharSequence> selectinjury = new ArrayList<CharSequence>();




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        value = getIntent().getStringExtra("transfer");
        mlat = Double.parseDouble(getIntent().getStringExtra("homelat"));
        mlong = Double.parseDouble(getIntent().getStringExtra("homelong"));
        setUpMapIfNeeded();

        service = (Button) findViewById(R.id.servicebutton);
        sosbutton = (Button) findViewById(R.id.sosbutton);

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.setEnabled(false);
            }
        });

        sosbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sosbutton.setEnabled(false);
            }
        });



        //et1= (EditText) findViewById(R.id.editlat);
        //et2= (EditText) findViewById(R.id.editlong);
         //moffer=(EditText) findViewById(R.id.editoffer);
         //mshop = (EditText) findViewById(R.id.shopname);

       // b2    =(Button) findViewById(R.id.datebutton);
       // mdate =(EditText) findViewById(R.id.et4);
       /* cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);*/

       // mtemp = (EditText) findViewById(R.id.temperature);
       /* mshop.setText("0.24");
        findweather();*/


       // mheight = (EditText) findViewById(R.id.height);
        minjury = (Button)findViewById(R.id.injury);
        mpeople=(EditText) findViewById(R.id.people);
        mmedicine=(Button) findViewById(R.id.medicine);
        mclothes =(Button) findViewById(R.id.clothes);
        mfood = (Button) findViewById(R.id.food);

        b3 = (Button) findViewById(R.id.photobutton);
      //  b4 = (Button) findViewById(R.id.videobutton);
        record = (Button) findViewById(R.id.record1);
        play  = (Button) findViewById(R.id.record2);
        stop = (Button) findViewById(R.id.record3);
        photoview = (ImageView) findViewById(R.id.photoview);
        photoview.setVisibility(View.GONE);
    //    videoview = (VideoView) findViewById(R.id.runningview);

       // radiustext = (EditText) findViewById(R.id.radiusedit);
       // radiusbutton =(Button) findViewById(R.id.radiusbutton);
        emergencyedit = (EditText) findViewById(R.id.emergenymsg);

       /* radiusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                floodradius =  Integer.parseInt(radiustext.getText().toString());
                addthecircle(mlat, mlong, floodradius);
            }
        });*/


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


       // Toast.makeText(this,value,Toast.LENGTH_SHORT).show();


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

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
    }


    /*public void addthecircle(Double newlat,Double newlong,int newradius) {

        circles = mMap.addCircle(new CircleOptions()
                .center(new LatLng(newlat, newlong))
                .radius(newradius)
                .strokeColor(Color.RED)
                .fillColor(0x5500ff00));
    }*/



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

    public void onMarkerDrag(Marker marker)
    {
        // do nothing during drag
    }

    @Override
    public void onMarkerDragEnd(Marker marker)
    {
        mlat = marker.getPosition().latitude;
        mlong = marker.getPosition().longitude;

        et1.setText(Double.toString(mlat));
        et2.setText(Double.toString(mlong));
    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {

    }


    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Toast.makeText(this,marker.toString(),Toast.LENGTH_LONG).show();
        return true;
    }



    public void registerPost(View view)
    {
        //String offervalue = moffer.getText().toString();
        //String shopvalue =  mshop.getText().toString();
      //  String datevalue =  mdate.getText().toString();
        String peoplevalue = mpeople.getText().toString();
      //  String heightvalue = mheight.getText().toString();
        String foodvalue = mfood.getText().toString();
        String medicinevalue = mmedicine.getText().toString();
        String clothevalue = mclothes.getText().toString();
      //  String floodradiusvalue = radiustext.getText().toString();
        String injuryvalue = minjury.getText().toString();
        String victimmsg = emergencyedit.getText().toString();


        new register(this).execute(Double.toString(mlat + 0.0003), Double.toString(mlong + 0.0004), "11", "12", value, "2016-02-11"
                , peoplevalue, "100", "25", foodvalue, medicinevalue, clothevalue, "15", injuryvalue, victimmsg, "122", "0.24");
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

        builder.setTitle("Select Colours");

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

        builder.setTitle("Select Colours");

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

        builder.setTitle("Select Colours");

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

        builder.setTitle("Select Colours");

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

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


}