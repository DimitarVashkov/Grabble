package com.example.dimitarvashkov.grabble;
import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener  {

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker marker;
    private GridView gridView;
    private ArrayAdapter<String> storage;

    //TODO change letter collection to Strings
    private ArrayList<String> bucket = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        startDemo();

        Button combinerButton = (Button) findViewById(R.id.combiner);
        combinerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MapsActivity.this, LetterCombinerActivity.class);
                startActivity(i);
            }
        });
        Button menuButton = (Button) findViewById(R.id.menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MapsActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

    }



    //TODO add onResume, onStop
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMinZoomPreference(21.0f);
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
        mMap.setMyLocationEnabled(true);}
        else {
            Log.d("Fuck", Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    public void startDemo() {
        try {
            retrieveFileFromUrl();
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }


    private void retrieveFileFromUrl() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                new DownloadKmlFile(getString(R.string.kml_sunday)).execute();
                break;

            case Calendar.MONDAY:
                new DownloadKmlFile(getString(R.string.kml_monday)).execute();
                break;

            case Calendar.TUESDAY:
                new DownloadKmlFile(getString(R.string.kml_tuesday)).execute();
                break;

            case Calendar.WEDNESDAY:
                new DownloadKmlFile(getString(R.string.kml_wednesday)).execute();
                break;

            case Calendar.THURSDAY:
                new DownloadKmlFile(getString(R.string.kml_thursday)).execute();
                break;

            case Calendar.FRIDAY:
                new DownloadKmlFile(getString(R.string.kml_friday)).execute();
                break;

            case Calendar.SATURDAY:
                new DownloadKmlFile(getString(R.string.kml_saturday)).execute();
                break;
        }

    }


    private class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {
        private final String mUrl;

        public DownloadKmlFile(String url) {
            mUrl = url;
        }

        protected byte[] doInBackground(String... params) {
            try {
                InputStream is = new URL(mUrl).openStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(byte[] byteArr) {
            try {
                KmlLayer kmlLayer = new KmlLayer(mMap, new ByteArrayInputStream(byteArr),
                        getApplicationContext());
                kmlLayer.addLayerToMap();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO store letters efficiently, fix buttons
    @Override
    public boolean onMarkerClick(final Marker marker) {
        //marker.showInfoWindow();
        //Letter is contained in the Snippet attribute
        String markerLetter = (String) marker.getSnippet();
        //Letter letter = Letter.createLetter(markerLetter);
        //bucket.add(markerLetter);
        DataHolder.getInstance().addLetter(markerLetter.toUpperCase());
        //TODO Collect marker IDS

        marker.remove();

        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this,"onConnected", Toast.LENGTH_SHORT).show();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //mLocationRequest.setSmallestDisplacement(0.1F);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            Log.d("FUCK", android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //TODO fix markers
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        //remove previous current location Marker
        if (marker != null){
            marker.remove();
        }

        double dLatitude = mLastLocation.getLatitude();
        double dLongitude = mLastLocation.getLongitude();
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                .title("My Location").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));

    }


}
