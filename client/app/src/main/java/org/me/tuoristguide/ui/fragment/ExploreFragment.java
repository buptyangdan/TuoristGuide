package org.me.tuoristguide.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;

import org.me.tuoristguide.R;
import org.me.tuoristguide.service.local.LocationService;
import org.me.tuoristguide.service.local.YelpService;

import java.util.ArrayList;



public class ExploreFragment extends Fragment implements YelpService.YelpServiceInterface,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT";
    private static final String TAG = "ExploreFragment";

    private FloatingActionButton locationButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set self as controller in YelpService
        YelpService.getInstance().setController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set google map fragment
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment)
                fragmentManager.findFragmentById(R.id.explore_map);

        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.explore_map, fragment, MAP_FRAGMENT_TAG)
                    .commit();
        }

        // floating location button
        locationButton = (FloatingActionButton) getActivity().findViewById(R.id.my_location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationService.getInstance().requestLocationUpdates(ExploreFragment.this);
                LocationService.getInstance().showAndMoveCurrentLocationInMap();
                YelpService.getInstance().yelpNearby(LocationService.getInstance().getCurrentLocation());
            }
        });

        // set up google api client
        LocationService.getInstance().setGoogleApiClient(
                new GoogleApiClient.Builder(getActivity())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build());

        fragment.getMapAsync(this);

    }


    // interface method from YelpService interface
    public void placeBusinessMarks(ArrayList<Business> businesses) {
        for (Business b : businesses) {
            Coordinate coord = b.location().coordinate();
            LocationService.getInstance().addMarker(
                    new MarkerOptions()
                            .position(new LatLng(coord.latitude(), coord.longitude()))
                            .title(b.name())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                            .snippet(b.name())
            );
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationService.getInstance().stopLocationUpdates(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Location services disconnected. Please reconnect.");
    }

    @Override
    public void onLocationChanged(Location location) {
        // inform LocationService the change
        LocationService.getInstance().locationChanged(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // set google map in the LocationService instance
        LocationService.getInstance().setUpGoogleMap(googleMap);
        LocationService.getInstance().showAndMoveCurrentLocationInMap();
    }
}
