package org.me.tuoristguide.ui.fragment;

import android.content.Intent;
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
import com.yelp.clientlib.entities.Business;

import org.me.tuoristguide.R;
import org.me.tuoristguide.service.local.YelpService;
import org.me.tuoristguide.ui.activity.DetailActivity;
import org.me.tuoristguide.ui.activity.MainActivity;

import java.util.ArrayList;


public class ExploreFragment extends Fragment implements YelpService.YelpServiceInterface {

    private static View view;
    private static Double latitude, longitude;
    private static GoogleMap mMap;


    public ExploreFragment() {
        // set self as controller in YelpService
        YelpService.getInstance().setController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        view = (RelativeLayout) inflater.inflate(R.layout.fragment_explore, container, false);

        // TODO change to getting real
        latitude = 37.422006;
        longitude = -122.084095;
        setUpMapIfNeeded();
        YelpService.getInstance().yelpNearby(latitude, longitude);

        return view;
    }

    public static void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) MainActivity.fragmentManager
                    .findFragmentById(R.id.explore_map)).getMap();
            // Check if we were successful in obtaining the map.

        }
    }

    // interface method from YelpService interface
    public void setUpMap(ArrayList<Business> businesses) {
        // For showing a move to my loction button
        //mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        //set up the map according to the result
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).snippet("Home Address"));
        for (Business b : businesses) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(b.location().coordinate().latitude(), b.location().coordinate().longitude())).title(b.name())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet(b.name()));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), 14.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
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


    public void swithToDetailView(String store_name, String store_id) {
        Intent startdetail = new Intent(getActivity(), DetailActivity.class);
        startdetail.putExtra("store_name", store_name);
        startdetail.putExtra("store_id", store_id);
        startActivity(startdetail);
    }

}
