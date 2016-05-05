package org.me.tuoristguide.ui.fragment;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fortysevendeg.android.wunderground.api.service.WundergroundApiProvider;
import com.fortysevendeg.android.wunderground.api.service.request.Feature;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.fortysevendeg.android.wunderground.api.service.request.Query;
import com.fortysevendeg.android.wunderground.api.service.response.WundergroundResponse;
import com.squareup.picasso.Picasso;

import org.me.tuoristguide.R;

import it.restrung.rest.cache.RequestCache;
import it.restrung.rest.client.ContextAwareAPIDelegate;


public class LocationsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Location mLastLocation;
    private boolean mRequestingLocationUpdates = false;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String TAG = LocationsFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1000;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 1; // 1 meters

    private TextView lblLocation;

    View view;

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (container == null) {
            return null;
        }
        view = (LinearLayout) inflater.inflate(R.layout.fragment_locations, container, false);
        lblLocation = (TextView) view.findViewById(R.id.lblLocation);
        //1. check the availability of the google service
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed");
    }

    /**
     * This method is used to verify the google play services on the device
     *
     * @return
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Toast the recoverable error
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), REQUEST_CODE).show();
            }
            return false;
        }
        return true;
    }

    /**
     * This method is used to build the google api client
     * since maybe multiple api client, then we should synchronize this method
     */
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /***
     * This method is used to display the location
     * 1. check location provider
     * 2. check the permission of api
     * 3. get last known location
     */
    private void displayLocation() {
        String provider;
        //1. check the location provider : is the google api client
        //2.check the permission of api
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //3. get lastKnown location
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            Log.i(TAG, latitude + "," + longitude);
            lblLocation.setText(latitude + ", " + longitude);
            weather(latitude,longitude);
        } else {

            lblLocation
                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }

    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        // Displaying the new location on UI
        displayLocation();
    }


    private void weather(Double latitude, Double longtitude) {

        WundergroundApiProvider.getClient().query(new ContextAwareAPIDelegate<WundergroundResponse>(getContext(), WundergroundResponse.class, RequestCache.LoadPolicy.NEVER) {

            public void onResults(WundergroundResponse wundergroundResponse) {
                TextView location= (TextView) view.findViewById(R.id.location_view);
                TextView condition=(TextView)view.findViewById(R.id.condition_view);
                TextView temperature=(TextView)view.findViewById(R.id.temperature__view);
                ImageView image=(ImageView)view.findViewById(R.id.imageView);
                TextView time=(TextView)view.findViewById(R.id.time);
                location.setText(wundergroundResponse.getCurrentObservation().getDisplayLocation().getCity());
                condition.setText(wundergroundResponse.getCurrentObservation().getWeather());
                temperature.setText(wundergroundResponse.getCurrentObservation().getTemperatureString());
                time.setText(wundergroundResponse.getCurrentObservation().getLocalTimeRfc822());
                Picasso
                        .with(getContext())
                        .load(wundergroundResponse.getCurrentObservation().getIconUrl())
                        .into(image);
              //  Toast.makeText(getActivity(), wundergroundResponse.getCurrentObservation().getTemperatureString(), Toast.LENGTH_LONG).show();
            }

            public void onError(Throwable e) {
                Toast.makeText(getContext(), "fail", Toast.LENGTH_LONG).show();
            }
        }, "ce486683c442171b", Query.latLng(latitude, longtitude), Feature.conditions, Feature.astronomy);
    }
}
