package com.example.dimitarvashkov.grabble;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker marker;
    //private Set<String> markerIDs = new HashSet<>();
    private Circle circle;

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {

            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (!(isLocationEnabled(this))) {
            Toast.makeText(this, "PLEASE ENABLE LOCATION SERVICES", Toast.LENGTH_LONG).show();
        }


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

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        if (mLastLocation != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("Sup", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("Latitude", Double.doubleToLongBits(mLastLocation.getLatitude()));
            editor.putLong("Longitude", Double.doubleToLongBits(mLastLocation.getLongitude()));
            editor.commit();
        }

        super.onStop();

    }

    //TODO add onResume, onStop
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMinZoomPreference(18.0f);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences("Sup", 0);
        double latitude = Double.longBitsToDouble(sharedPreferences.getLong("Latitude", 0));
        double longitude = Double.longBitsToDouble(sharedPreferences.getLong("Longitude", 0));
        Location loc = new Location("");

        if (latitude == 0 && longitude == 0) {
            loc.setLatitude(55.02);
            loc.setLongitude(-3.96);
            onLocationChanged(loc);
        } else {
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            onLocationChanged(loc);
        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Log.d("No access", Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void startDemo() {
        if (isNetworkAvailable()) {
            try {
                retrieveFileFromUrl();
            } catch (Exception e) {
                Log.e("Exception caught", e.toString());
            }
        } else {
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

    //Retrieve KMl file depending on the day of play
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

    @Override
    public boolean onMarkerClick(final Marker marker) {

        //Calculate distance between user and marker
        float[] distance = new float[2];
        Location.distanceBetween(marker.getPosition().latitude, marker.getPosition().longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);

        if (distance[0] > circle.getRadius()) {
            //If user not in range, show marker information and (optional) vibrate
            marker.showInfoWindow();
            Toast.makeText(getBaseContext(), "Get closer!", Toast.LENGTH_SHORT).show();
            if (DataHolder.getInstance().getVibrate()) {
                Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
            }

        } else {
            //Add letter to DataHolder
            String markerLetter = (String) marker.getSnippet();
            DataHolder.getInstance().addLetter(markerLetter.toUpperCase());


            //I tried storing markerIDs but my parsing is using KMLLayer
            //String markerID = (String) marker.getId();
            //markerIDs.add(markerID);

            marker.remove();
            Toast.makeText(getBaseContext(), "Letter collected!", Toast.LENGTH_LONG).show();
        }


        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Make location updates, check for location services
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            Toast.makeText(this, "ENABLE LOCATION", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        //remove previous current location Marker
        if (marker != null) {
            marker.remove();
        }

        //Get location coordinates and place a marker
        double dLatitude = mLastLocation.getLatitude();
        double dLongitude = mLastLocation.getLongitude();
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                .title("My Location").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //marker.setVisible(false);
        //Marker visibility used for debugging purposes


        if (circle != null) {
            circle.remove();
        }

        //Add user range depending on powerUser status
        if (DataHolder.getInstance().getPowerUser()) {
            circle = mMap.addCircle(new CircleOptions()
                    .center(marker.getPosition())
                    .radius(15)
                    .strokeColor(Color.RED)
                    .fillColor(Color.TRANSPARENT));
        } else {
            circle = mMap.addCircle(new CircleOptions()
                    .center(marker.getPosition())
                    .radius(10)
                    .strokeColor(Color.RED)
                    .fillColor(Color.TRANSPARENT));
        }
        //Move camera to location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));

    }

    //Check for network
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * Download KML layer and apply to map
     */
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
                //Really easy to apply the markers
                //PROBLEM: Can't remove the markers using a KMLLayer
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


}
