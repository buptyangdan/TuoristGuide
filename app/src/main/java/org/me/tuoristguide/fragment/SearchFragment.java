package org.me.tuoristguide.fragment;


import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.me.tuoristguide.R;
import org.me.tuoristguide.activity.MainActivity;
import org.me.tuoristguide.model.Geo;
import org.me.tuoristguide.service.SearchService;

import java.io.IOException;
import java.util.List;



public class SearchFragment extends Fragment implements  View.OnClickListener {
    private static View view;
    private static Double latitude, longitude;
    private static GoogleMap mMap;
    SearchView searchview;
    public SearchFragment(){}
    Geo geo=new Geo();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            latitude=Double.parseDouble(bundle.getString(SearchService.Latitude));
            longitude=Double.parseDouble(bundle.getString(SearchService.Longtitude));
            if (bundle != null) {
                int resultCode = bundle.getInt(SearchService.RESULT);
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("@@@@@@@@---->"+latitude);
                    System.out.println("@@@@@@@@---->"+longitude);
                    geo.setGeoLat(latitude);
                    geo.setGeoLtn(longitude);
                    setUpMap(geo);
                    Toast.makeText(getContext(),
                            "successfully!",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "failed",
                            Toast.LENGTH_LONG).show();

                }
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(SearchService.NOTIFICATION));
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        if(view==null)
          view = (RelativeLayout) inflater.inflate(R.layout.fragment_search, container, false);
        setUpMapIfNeeded();
        searchview=(SearchView)view.findViewById(R.id.search_view);
        searchview.setOnSearchClickListener(this);
        return view;
    }



    public static void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) MainActivity.fragmentManager
                    .findFragmentById(R.id.search_map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap(null);
        }
    }

    private static void setUpMap(Geo geo) {
        // For showing a move to my loction button
        //mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        latitude = 37.422006;
        longitude = -122.084095;
        if(geo!=null){
            latitude=geo.getGeoLat();
            longitude=geo.getGeoLtn();
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("Home Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude + 0.005, longitude + 0.002)).title("School").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("School Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude + 0.002, longitude - 0.010)).title("Safeway").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("Safeway Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude + 0.014, longitude - 0.023)).title("NASA").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("NASA Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude - 0.015, longitude + 0.026)).title("KFC").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).snippet("KFC Address"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), 12.0f));
    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (mMap != null) {
//            MainActivity.fragmentManager.beginTransaction()
//                    .remove(MainActivity.fragmentManager.findFragmentById(R.id.search_map)).commit();
//            mMap = null;
//        }
//    }




    @Override
    public void onClick(View v) {

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() != 0) {
                    Intent intent=new Intent(getContext(), SearchService.class);
                    System.out.println("--->" + query);
                    // handle search here
                    intent.putExtra("queryString", query);
                    getActivity().startService(intent);
                    return true;
                }
                return false;
            }
        });
    }
}

