package com.zhuang.itoronto.activity;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zhuang.itoronto.R;
import com.zhuang.itoronto.model.*;
import com.zhuang.itoronto.utilities.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Location mCurrentLocation;
    String mLastUpdateTime;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private List<Marker> mMarkers = new ArrayList<Marker>();
    private static final String TAG = "MapsActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FATEST_INTERVAL = 1000 * 5;

    private ViewPager viewPager;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        // Create the LocationRequest object
        mLocationRequest = new LocationRequest().create()
                .setInterval(INTERVAL)
                .setFastestInterval(FATEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        List<View> viewList = new ArrayList<>();
        LayoutInflater lf = LayoutInflater.from(this);
        View view1 = lf.inflate(R.layout.layout_fragment_place, (ViewGroup) null);
        View view2 = lf.inflate(R.layout.layout_fragment_map, (ViewGroup)null);


        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);

        viewList.add(view1);
        viewList.add(view2);

        ViewPagerAdapter viewPager_adapter = new ViewPagerAdapter(viewList);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPager_adapter);
        /*XML_SampleParser parser = new XML_SampleParser();
        List<XML_Node> items = parser.parse(is);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        //Log.e(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if( mCurrentLocation == null ) {
                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
            else{
                handleNewLocation(mCurrentLocation);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : mMarkers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int padding = 0;
                //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
            //Log.e(TAG, "Firing onLocationChanged..............................................");
         mCurrentLocation = location;
         mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        //mMap.addMarker(options);
        mMarkers.add(mMap.addMarker(options));
        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 0;*/
        //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //new ReadHttpGet().execute("http://www.bikesharetoronto.com/stations/json");
        ReadHttpGet readHttpGet = new ReadHttpGet();
        readHttpGet.setTaskHandler(new ReadHttpGet.HttpTaskHandler(){
            public void taskSuccessful(String json) {
                LatLng latLng;
                MarkerOptions options;
                Double la,lo;
                BitmapDescriptor iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.bike);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("stationBeanList");
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        //鏂板缓涓�涓狫SON瀵硅薄锛岃瀵硅薄鏄煇涓暟缁勯噷鐨勫叾涓竴涓璞�
                        JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
                        la = jsonObject2.getDouble("latitude"); //鑾峰彇鏁版嵁
                        lo = jsonObject2.getDouble("longitude");
                        latLng = new LatLng(la, lo);

                        options = new MarkerOptions()
                                .icon(iconMarker)
                                .position(latLng);
                        //mMap.addMarker(options);
                        mMarkers.add(mMap.addMarker(options));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void taskFailed() {}
        });
        readHttpGet.execute("http://www.bikesharetoronto.com/stations/json");

        // Add a marker in City and move the camera
        LatLng Toronto = new LatLng(43.65, -79.38);
        mMap.addMarker(new MarkerOptions().position(Toronto).title("This is Toronto"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Toronto));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }


    }


}
