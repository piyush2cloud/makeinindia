package com.example.piyush.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.PolylineOptions;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class traveller extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener {
    private GoogleMap mMapp;
    MarkerOptions markerOptions;
    Marker currentMarkers;
    private double mlat;
    private double mlong;
    private Location mlastlocationn;
    private GoogleApiClient mGoogleApiClient;
    EditText et11;
    Button b1;
    private Double mlatt, mlongg;
    private JSONArray ja;
    private String result;
    private ArrayList<NameValuePair> nameValuePairs;
    private ArrayList<NameValuePair> countValuePairs;
    private String id, name;
    private Double mlattt, mlonggg;
    private int jsonlength, count = 0;
    Circle circles;
    Circle circles2;

    String georadius = "0";
    String oferid;
    ArrayList<LatLng> markerPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(traveller.this,"oncreate",Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller);
        et11 = (EditText) findViewById(R.id.editText1);
        b1 = (Button) findViewById(R.id.button1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circles.remove();
                georadius = et11.getText().toString();
                insert();
            }
        });

        nameValuePairs = new ArrayList<NameValuePair>();
        countValuePairs = new ArrayList<NameValuePair>();
        markerPoints = new ArrayList<LatLng>();


        buildGoogleApiClient();
        mGoogleApiClient.connect();


    }


    protected synchronized void buildGoogleApiClient() {
        //      Toast.makeText(traveller.this,"buildgoogleapi",Toast.LENGTH_LONG).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //   Toast.makeText(traveller.this,"onconnected",Toast.LENGTH_LONG).show();
        mlastlocationn = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mlastlocationn != null) {
            mlatt = mlastlocationn.getLatitude();
            mlongg = mlastlocationn.getLongitude();

            id = Double.toString(mlatt);
            name = Double.toString(mlongg);
            Toast.makeText(traveller.this, mlastlocationn.toString(), Toast.LENGTH_LONG).show();
        }

        setUpMapIfNeeded();
        insert();
    }


    private void setUpMapIfNeeded() {
        //Toast.makeText(traveller.this, "setupmapifneeded", Toast.LENGTH_LONG).show();

        if (mMapp == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMapp = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2))
                    .getMap();
            mMapp.setOnMarkerClickListener(this);
            mMapp.setOnMarkerDragListener(this);
            // Check if we were successful in obtaining the map.
            if (mMapp != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        //   Toast.makeText(traveller.this, "setupmap", Toast.LENGTH_LONG).show();

        markerOptions = new MarkerOptions().position(new LatLng(mlatt, mlongg)).title("MY CURRENT POSITION").draggable(true);
        currentMarkers = mMapp.addMarker(markerOptions);
        mMapp.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlatt, mlongg), 13));
    }

    public void addthecircle() {

        circles = mMapp.addCircle(new CircleOptions()
                .center(new LatLng(mlatt, mlongg))
                .radius(Integer.parseInt(georadius))
                .strokeColor(Color.GREEN)
                .fillColor(0x5500ff00));
    }

    public void addthecircleloop(Double newlat, Double newlong, int newradius) {

        circles2 = mMapp.addCircle(new CircleOptions()
                .center(new LatLng(newlat, newlong))
                .radius(newradius)
                .strokeColor(Color.RED)
                .fillColor(0x5500ff00));
    }


    public void insert() {
        addthecircle();

        Toast.makeText(getApplicationContext(), "For accuracy you can drag and drop the marker", Toast.LENGTH_LONG).show();
        nameValuePairs.clear();
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("dist", georadius));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://iirs.netne.net/traveller.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            enableStrictMode();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");

            //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            ja = new JSONArray(result);
            jsonlength = ja.length();
            //  Toast.makeText(getApplicationContext(),Integer.toString(jsonlength), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
        }

        int flag = 0;
        try {
            if (jsonlength == 1 && ja.getJSONObject(0).getString("distance").equals("xyz")) {
                flag = 1;
                mMapp.clear();
                markerOptions = new MarkerOptions().position(new LatLng(mlatt, mlongg)).title("MY CURRENT POSITION").draggable(true);
                currentMarkers = mMapp.addMarker(markerOptions);
                Toast.makeText(getApplicationContext(), "No Disaster", Toast.LENGTH_SHORT).show();
                addthecircle();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (flag == 0) {
            Toast.makeText(getApplicationContext(), "Buzzes For You....", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; (i < jsonlength) && flag == 0; i++) {
            try {
                //Toast.makeText(getApplicationContext(),"forloop", Toast.LENGTH_LONG).show();
                mlattt = Double.parseDouble(ja.getJSONObject(i).getString("latitude")); //write name of column latutude;
                mlonggg = Double.parseDouble(ja.getJSONObject(i).getString("longitude")); //write name of column longitude;
                String currentoffers = ja.getJSONObject(i).getString("offers");
                String distance = ja.getJSONObject(i).getString("distance");
                String shopname = ja.getJSONObject(i).getString("shop");
                String dates = ja.getJSONObject(i).getString("dates");
                String userentered = ja.getJSONObject(i).getString("user");
                String offerid = ja.getJSONObject(i).getString("offerid");
                String countoffer = ja.getJSONObject(i).getString("count");

                String temperature = ja.getJSONObject(i).getString("temperature");
                String height = ja.getJSONObject(i).getString("height");
                String people = ja.getJSONObject(i).getString("people");
                String medicine = ja.getJSONObject(i).getString("medicine");
                String food = ja.getJSONObject(i).getString("food");
                String clothes = ja.getJSONObject(i).getString("clothes");


                LatLng origin = new LatLng(mlatt, mlongg);
                LatLng dest = new LatLng(mlattt, mlonggg);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);


                markerOptions = new MarkerOptions().
                        position(new LatLng(mlattt, mlonggg)).
                        title(offerid).
                        snippet("Amount of Rainfall ->" + shopname + "\n" +
                                "Relative-Humidity ->" + currentoffers + "\n" +
                                "Temperature ->" + temperature + "\n" +
                                "Height-From-Ground ->" + height + "\n" +
                                "People-Nearby ->" + people + "\n" +
                                "Medicine Required->" + medicine + "\n" +
                                "Food Required->" + food + "\n" +
                                "Clothes Required->" + clothes + "\n" +
                                "Disaster Code ->" + offerid + "\n" +
                                "Date ->" + dates + "\n" +
                                "No. of People visited ->" + countoffer + "\n").
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                mMapp.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        TextView info = new TextView(traveller.this);
                        info.setTextColor(Color.DKGRAY);
                        info.setText(marker.getSnippet().toString());

                        return info;
                    }
                });

                mMapp.addMarker(markerOptions);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonlength = 0;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        oferid = marker.getTitle();
        //  Toast.makeText(traveller.this,oferid,Toast.LENGTH_LONG).show();
        countinsert();

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

        mlatt = marker.getPosition().latitude;
        mlongg = marker.getPosition().longitude;
        Toast.makeText(traveller.this, mlatt.toString() + mlongg.toString(), Toast.LENGTH_LONG).show();


        id = Double.toString(mlatt);
        name = Double.toString(mlongg);
        circles.remove();
        insert();
    }

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void countinsert() {
        // Toast.makeText(traveller.this,"count insert",Toast.LENGTH_LONG).show();
        countValuePairs.clear();


        countValuePairs.add(new BasicNameValuePair("offersid", oferid));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://iirs.netne.net/count.php");
            httppost.setEntity(new UrlEncodedFormEntity(countValuePairs));
            enableStrictMode();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //   result = EntityUtils.toString(entity, "utf-8");
            //Toast.makeText(traveller.this,result,Toast.LENGTH_LONG).show();
            // ja = new JSONArray(result);
            //  jsonlength = ja.length();
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMapp.addPolyline(lineOptions);
        }
    }


}

