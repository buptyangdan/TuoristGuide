package org.me.tuoristguide.service.local;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.tuoristguide.R;
import org.me.tuoristguide.model.Comment;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class provides Google Map and Location function supports
 *
 * Created by zy on 4/14/16.
 */
public class LocationService implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    // google map services
    private Location currentLocation;
    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private ArrayList<Marker> markers;
    private ViewPager viewPager;
    private static LocationService instance = null;

    static public LocationService getInstance(Context context){
        if(instance!=null){
            return instance;
        }else{
            instance=new LocationService(context);
            return  instance;
        }
    }

    private LocationService(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        markers = new ArrayList<>();
    }

    public Location getCurrentLocation() {
        currentLocation=getLastLocation();
        return currentLocation;
    }

    public void setViewPager(ViewPager v) {
        viewPager = v;
    }

    public Location getLastLocation() {
        Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (lastLocation != null) {
            LatLng latLng = new LatLng( lastLocation.getLatitude(), lastLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 0f));
        }
        currentLocation = lastLocation;
        return currentLocation;
    }

    public void showCurrentLocationInMap() {
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            float zoom = googleMap.getCameraPosition().zoom;

            if (zoom < 14f) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            }
            else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location)));
        }
    }

    public void showCurrentLocationInMap(Double latitude, Double longtitude, String type) {
            LatLng latLng = new LatLng(latitude,longtitude );
            float zoom = googleMap.getCameraPosition().zoom;

            if (zoom < 12.5f)
            {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f));
            }
            else
            {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            if(type.equals("search")){
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Searched Location")
                        .icon(BitmapDescriptorFactory.defaultMarker()));


            }else {

                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Current Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location)));
            }
    }

    public void requestLocationUpdates() {
        //onPreLocationUpdate();
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1_000)        // 10 seconds
                .setFastestInterval(1_000); // 1 second
        LocationServices.FusedLocationApi
                .requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private Location getMapCenterLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(googleMap.getCameraPosition().target.latitude);
        location.setLongitude(googleMap.getCameraPosition().target.longitude);
        return location;
    }

    private int getRadiusFromMapProjection() {
        LatLng point1 = googleMap.getProjection().getVisibleRegion().nearLeft;
        LatLng point2 = googleMap.getProjection().getVisibleRegion().nearRight;
        float[] results = new float[1];
        Location.distanceBetween(point1.latitude,
                point1.longitude, point2.latitude, point2.longitude, results);
        return Math.round(results[0] / 2f);
    }

    private void onPreLocationUpdate() {
        currentLocation = null;
        googleMap.setPadding(0, 0, 0, 0);
    }

    public void stopLocationUpdates() {
        if (googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        googleApiClient.disconnect();
    }

    public void clearMarker(){
        googleMap.clear();
        markers.clear();
        //showCurrentLocationInMap();
    }

    public void addMarker(MarkerOptions markerOptions){
        Marker marker = googleMap.addMarker(markerOptions);
        markers.add(marker);
    }

    public void showMarkerAtPosition(int position){
        Marker marker = markers.get(position);
        if (marker != null) {
            marker.showInfoWindow();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15f));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = markers.indexOf(marker);
                viewPager.setCurrentItem(position, true);
                return false;
            }
        });
    }
}
