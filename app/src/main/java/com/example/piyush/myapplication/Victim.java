package com.example.piyush.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class Victim extends AppCompatActivity implements GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    String homelat, homelong,uservalue;
    private GoogleMap mMap;
    MarkerOptions markerOption;
    Marker currentMarker;
    Double currentlat, currentlong;
    Spinner operations;
    Button b1, b2;
    EditText e1;
    private static final LatLng hospital1 = new LatLng(19.2434934, 72.9163528);
    private static final LatLng hospital2 = new LatLng(19.26313232, 72.8232323);
    private static final LatLng hospital3 = new LatLng(19.283981821, 72.976000);

    private static final LatLng police1 = new LatLng(19.2334934, 72.913333);
    private static final LatLng police2 = new LatLng(19.2324444, 72.8232323);
    private static final LatLng police3 = new LatLng(19.19834231, 72.816000);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victim);
        homelat = getIntent().getStringExtra("homelat");
        homelong = getIntent().getStringExtra("homelong");
        uservalue = getIntent().getStringExtra("transfer");

        currentlat = Double.parseDouble(homelat);
        currentlong = Double.parseDouble(homelong);

        b1 = (Button) findViewById(R.id.contact);
        b2 = (Button) findViewById(R.id.act);

        operations = (Spinner) findViewById(R.id.spinneroperation);
        e1 = (EditText) findViewById(R.id.messagerescue);
        setUpMapIfNeeded();

        operations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);

                String operationselected =  parent.getItemAtPosition(position).toString();

                if(operationselected.equals("Rescue-Team")){


                }

                if(operationselected.equals("Near Police-Stations")){

                    Marker policee1 = mMap.addMarker(new MarkerOptions()
                            .position(police1)
                            .title("Police")
                            .snippet("Speciality: Common")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.police)));

                    LatLng origin = new LatLng(currentlat, currentlong);
                    LatLng dest = new LatLng(police1.latitude, police1.longitude);
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);

                    Marker policee2 = mMap.addMarker(new MarkerOptions()
                            .position(police2)
                            .title("Police")
                            .snippet("Speciality: Common")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.police)));

                    LatLng origin2 = new LatLng(currentlat, currentlong);
                    LatLng dest2 = new LatLng(police2.latitude, police2.longitude);
                    String url2 = getDirectionsUrl(origin2, dest2);
                    DownloadTask downloadTask2 = new DownloadTask();
                    downloadTask2.execute(url2);



                    Marker policee3 = mMap.addMarker(new MarkerOptions()
                            .position(police3)
                            .title("Police")
                            .snippet("Speciality: Common")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.police)));

                    LatLng origin3 = new LatLng(currentlat, currentlong);
                    LatLng dest3 = new LatLng(police3.latitude, police3.longitude);
                    String url3 = getDirectionsUrl(origin3, dest3);
                    DownloadTask downloadTask3 = new DownloadTask();
                    downloadTask3.execute(url3);
                }

                if(operationselected.equals("Near Hospitals")){


                    Marker hospitall1 = mMap.addMarker(new MarkerOptions()
                            .position(hospital1)
                            .title("Hospital")
                            .snippet("Speciality: Common")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));

                    LatLng origin = new LatLng(currentlat, currentlong);
                    LatLng dest = new LatLng(hospital1.latitude, hospital1.longitude);
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);

                    Marker hospitall2 = mMap.addMarker(new MarkerOptions()
                            .position(hospital2)
                            .title("Hospital")
                            .snippet("Speciality: Common")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));

                    LatLng origin2 = new LatLng(currentlat, currentlong);
                    LatLng dest2 = new LatLng(hospital2.latitude, hospital2.longitude);
                    String url2 = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask2 = new DownloadTask();
                    downloadTask2.execute(url2);

                    Marker hospitall3 = mMap.addMarker(new MarkerOptions()
                            .position(hospital3)
                            .title("Hospital")
                            .snippet("Speciality: Common")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Victim.this, MapsActivity.class);
                i1.putExtra("homelat", homelat);
                i1.putExtra("homelong", homelong);
                i1.putExtra("transfer", uservalue);
                startActivity(i1);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(Victim.this, Normalmode.class);
                i1.putExtra("homelat", homelat);
                i1.putExtra("homelong", homelong);
                i1.putExtra("transfer", uservalue);
                startActivity(i1);
            }
        });


    }

    private void setUpMapIfNeeded() {
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


    private void setUpMap() {

        markerOption = new MarkerOptions().position(new LatLng(currentlat, currentlong)).title("My Current Position").draggable(true);
        currentMarker = mMap.addMarker(markerOption);
        currentMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentlat, currentlong), 12));
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
            mMap.addPolyline(lineOptions);
        }
    }
}
