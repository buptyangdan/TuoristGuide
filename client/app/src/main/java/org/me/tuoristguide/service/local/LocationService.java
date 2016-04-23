package org.me.tuoristguide.service.local;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.tuoristguide.R;

/**
 * Created by zy on 4/14/16.
 */
public class LocationService {

    // singleton
    private static LocationService instance = null;

    public static LocationService getInstance() {
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }


    // google map services
    private Location currentLocation;
    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;
    private LocationManager locationManager;

    public void setGoogleApiClient(GoogleApiClient client) {
        googleApiClient = client;
        googleApiClient.connect();
    }

    public void setUpGoogleMap(GoogleMap gmap) {
        googleMap = gmap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Location getLastLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {
            LatLng latLng = new LatLng( lastLocation.getLatitude(), lastLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 0f));
        }
        currentLocation = lastLocation;
        return currentLocation;
    }


    public void showCurrentLocationInMap() {
        if (currentLocation != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location)));
        }
    }

    public void requestLocationUpdates(LocationListener listener) {
        onPreLocationUpdate();
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1_000)        // 10 seconds
                .setFastestInterval(1_000); // 1 second
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, listener);
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
        Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results);
        return Math.round(results[0] / 2f);
    }

    private void onPreLocationUpdate() {
        currentLocation = null;
        googleMap.setPadding(0, 0, 0, 0);
    }

    public void stopLocationUpdates(LocationListener listener) {
        if (googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, listener);
        googleApiClient.disconnect();
    }

    public void locationChanged(Location location) {

        Log.d("LocationService", "Location Changed..........");
        currentLocation = location;
        googleMap.clear();
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        float zoom = googleMap.getCameraPosition().zoom;
        if (zoom < 12.5f) googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f));
        else googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        showCurrentLocationInMap();
    }

    public void addMarker(MarkerOptions marker){
        googleMap.addMarker(marker );
    }
}
