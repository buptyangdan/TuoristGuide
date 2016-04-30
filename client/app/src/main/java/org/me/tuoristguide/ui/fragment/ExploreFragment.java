package org.me.tuoristguide.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;

import org.me.tuoristguide.R;
import org.me.tuoristguide.model.Geo;
import org.me.tuoristguide.service.local.LocationService;
import org.me.tuoristguide.service.local.SearchService;
import org.me.tuoristguide.service.local.YelpService;
import org.me.tuoristguide.ui.activity.DetailActivity;
import org.me.tuoristguide.ui.adapter.StoresAdapter;

import java.util.ArrayList;


public class ExploreFragment extends Fragment implements YelpService.YelpServiceInterface,View.OnClickListener {

    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT";
    private static final String TAG = "ExploreFragment";
    private FloatingActionButton locationButton;
    private ViewPager viewPager;
    private LocationService locationService;
    private static Double latitude, longitude;
    private static GoogleMap mMap;
    SearchView searchview;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //YelpService.getInstance().setController(this);
        setRetainInstance(true);
        locationService = new LocationService(getActivity());
    }
    @Override
    public void onResume() {
        super.onResume();
        // set self as controller in YelpService
        YelpService.getInstance().setController(this);
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

        view=(RelativeLayout)inflater.inflate(R.layout.fragment_explore, container, false);
        searchview=(SearchView)view.findViewById(R.id.search_view);
        searchview.setOnSearchClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // google map fragment
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
                locationService.requestLocationUpdates();
                Location currentlocation=locationService.getCurrentLocation();
                if(currentlocation!=null)
                {
                    locationService.showCurrentLocationInMap(currentlocation.getLatitude(),currentlocation.getLongitude(),"current_location");
                    YelpService.getInstance().yelpNearby(currentlocation);
                }

            }
        });



        // pager
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        locationService.setViewPager(viewPager);
        viewPager.setClipToPadding(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                locationService.showMarkerAtPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        fragment.getMapAsync(locationService);
    }


    // interface method from YelpService interface
    public void placeBusinessMarks(ArrayList<Business> businesses) {
        for (Business b : businesses) {
            Coordinate coord = b.location().coordinate();
            //locationService.googleMap.setOnMarkerClickListener(this);
            locationService.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(coord.latitude(), coord.longitude()))
                            .title(b.name())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                            .snippet(b.id())
            );
        }
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new StoresAdapter(getChildFragmentManager(), businesses));
    }

    @Override
    public void startDetailActivity() {
            Intent intent=new Intent(getContext(),DetailActivity.class);
            getContext().startActivity(intent);

    }

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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            latitude=Double.parseDouble(bundle.getString(SearchService.Latitude));
            longitude=Double.parseDouble(bundle.getString(SearchService.Longtitude));
            if (bundle != null) {
                int resultCode = bundle.getInt(SearchService.RESULT);
                if (resultCode == Activity.RESULT_OK) {
                    locationService.showCurrentLocationInMap(latitude,longitude,"search");
                    YelpService.getInstance().yelpNearby(latitude,longitude);
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
}
