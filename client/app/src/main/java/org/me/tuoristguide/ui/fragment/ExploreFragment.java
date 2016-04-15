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
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.BoundingBoxOptions;

import org.me.tuoristguide.R;
import org.me.tuoristguide.ui.activity.DetailActivity;
import org.me.tuoristguide.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class ExploreFragment extends Fragment {

    private static View view;
    private static Double latitude, longitude;
    private static GoogleMap mMap;

    Call<SearchResponse> call;


    public ExploreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

           view = (RelativeLayout) inflater.inflate(R.layout.fragment_explore, container, false);

        latitude = 37.422006;
        longitude = -122.084095;
        setUpMapIfNeeded();
        yelpNearby(latitude, longitude);
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

    private static void setUpMap(ArrayList<Business> businesses) {
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

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        if (mMap != null)
//            setUpMap();
//
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((MapFragment) MainActivity.fragmentManager
//                    .findFragmentById(R.id.explore_map)).getMap(); // getMap is deprecated
//            // Check if we were successful in obtaining the map.
//            if (mMap != null)
//                setUpMap();
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            MainActivity.fragmentManager.beginTransaction()
                    .remove(MainActivity.fragmentManager.findFragmentById(R.id.explore_map)).commit();
            mMap = null;
        }
        call.cancel();
    }

    /***
     * This method is used to find all nearby recommendations
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public void yelpNearby(Double latitude, Double longitude) {
        YelpAPIFactory apiFactory = new YelpAPIFactory("NUBhGlHwegeyRXi5Ci9XLA", "evZpCq1B7GcjEv-5WgFYH2iNBcM",
                "Rl5mlNSOFd4sJ9G4KNVjDWa7wLmdyUVG", "31cVuZroz3LPGrfRqy6IAfpn3n0");
        YelpAPI yelpAPI = apiFactory.createAPI();
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("limit", "20");
        BoundingBoxOptions bouds = BoundingBoxOptions.builder()
                .swLatitude(latitude - 0.01)
                .swLongitude(longitude - 0.01)
                .neLatitude(latitude + 0.01)
                .neLongitude(longitude + 0.01).build();



        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {

                System.out.println("here is the response!!! " + response.body().businesses());
                ArrayList<Business> businesses=response.body().businesses();
                if (mMap != null)
                    setUpMap(businesses);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("something wrong!!");
                System.out.println(t);
            }
        };
        call = yelpAPI.search(bouds, queryParams);
        call.enqueue(callback);

    }


    public void swithToDetailView(String store_name, String store_id) {
        Intent startdetail = new Intent(getActivity(), DetailActivity.class);
        startdetail.putExtra("store_name", store_name);
        startdetail.putExtra("store_id", store_id);
        startActivity(startdetail);
    }

}
