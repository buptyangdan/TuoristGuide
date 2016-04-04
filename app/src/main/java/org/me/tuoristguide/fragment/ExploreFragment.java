package org.me.tuoristguide.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.tuoristguide.R;
import org.me.tuoristguide.activity.MainActivity;

public class ExploreFragment extends Fragment {

    private static View view;
    private static Double latitude, longitude;
    private static GoogleMap mMap;
    public ExploreFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_explore, container, false);
        // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = 37.422006;
        longitude = -122.084095;

        setUpMapIfNeeded(); // For setting up the MapFragment

        return view;
    }

    public static void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) MainActivity.fragmentManager
                    .findFragmentById(R.id.explore_map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();
        }
    }

    private static void setUpMap() {
        // For showing a move to my loction button
        //mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("Home Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude + 0.005, longitude + 0.002)).title("School").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("School Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude + 0.002, longitude - 0.010)).title("Safeway").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("Safeway Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude + 0.014, longitude - 0.023)).title("NASA").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("NASA Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude - 0.015, longitude + 0.026)).title("KFC").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("KFC Address"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), 12.0f));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (mMap != null)
            setUpMap();

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) MainActivity.fragmentManager
                    .findFragmentById(R.id.explore_map)).getMap(); // getMap is deprecated
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            MainActivity.fragmentManager.beginTransaction()
                    .remove(MainActivity.fragmentManager.findFragmentById(R.id.explore_map)).commit();
            mMap = null;
        }
    }
}
