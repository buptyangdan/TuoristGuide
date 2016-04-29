package org.me.tuoristguide.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;

import org.me.tuoristguide.R;
import org.me.tuoristguide.entities.StoreManager;
import org.me.tuoristguide.service.local.LocationService;
import org.me.tuoristguide.service.local.YelpService;
import org.me.tuoristguide.ui.activity.DetailActivity;
import org.me.tuoristguide.ui.adapter.StoresAdapter;

import java.util.ArrayList;


public class ExploreFragment extends Fragment implements YelpService.YelpServiceInterface {

    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT";
    private static final String TAG = "ExploreFragment";
    private FloatingActionButton locationButton;
    private ImageButton plan_route;
    private ViewPager viewPager;
    private LocationService locationService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set self as controller in YelpService
        YelpService.getInstance().setController(this);
        setRetainInstance(true);
        locationService = new LocationService(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
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
                locationService.showCurrentLocationInMap();
                YelpService.getInstance().yelpNearby(locationService.getCurrentLocation());
            }
        });

//        //detail Button
//        plan_route=(ImageButton)getActivity().findViewById(R.id.route_plan);
//        plan_route.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // get the click store and redirect to detail Activity
//
//            }
//        });

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
}
