package org.me.tuoristguide.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.me.tuoristguide.R;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by zy on 4/30/16.
 */
public class RouteFragment extends RoboFragment implements OnMapReadyCallback {

    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT";

    @InjectView(R.id.recalculating_route_progress_bar) private ProgressBar recalculatingRouteProgressBar;
    @InjectView(R.id.walking_directions_button)        private FloatingActionButton walkingDirectionsButton;
    @InjectView(R.id.transit_directions_button)        private FloatingActionButton transitDirectionsButton;
    @InjectView(R.id.driving_directions_button)        private FloatingActionButton drivingDirectionsButton;
    @InjectView(R.id.travel_distance_container)        private CardView travelDistanceContainer;
    @InjectView(R.id.bike_directions_button)           private FloatingActionButton bikeDirectionsButton;
    @InjectView(R.id.building_route_layout)            private LinearLayout buildingRouteLayout;
    @InjectView(R.id.travel_distance_text)             private TextView travelDistanceTextView;
    @InjectView(R.id.directions_menu)                  private FloatingActionMenu directionsMenu;
    @InjectView(R.id.route_container)                  private CardView routeContainer;
    @InjectView(R.id.route_list)                       private RecyclerView routeList;

    private GoogleMap googleMap;
    private Animation verticalShowAnimation;
    private Animation verticalHideAnimation;


    public static RouteFragment newInstance() {
        RouteFragment fragment = new RouteFragment();
        return fragment;
    }

    public RouteFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.map_container, fragment, MAP_FRAGMENT_TAG)
                    .commit();
        }

        verticalShowAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.vertical_show);
        verticalHideAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.vertical_hide);

        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                toggleRouteContainer();
            }
        });
    }

    private TravelMode getCurrentTravelMode() {
        String travelModeStr = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString(getString(R.string.travel_mode_key), TravelMode.DRIVING.name());
        return TravelMode.valueOf(travelModeStr);
    }

    private void saveCurrentTravelMode(TravelMode selectedTravelMode) {
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .edit()
                .putString(getString(R.string.travel_mode_key), selectedTravelMode.name())
                .commit();
    }

    private void toggleRouteContainer() {
        if (routeContainer.getVisibility() == View.VISIBLE) {
            routeContainer.startAnimation(verticalHideAnimation);
            routeContainer.setVisibility(View.GONE);
            googleMap.setPadding(0, 0, 0, 0);
        } else {
            routeContainer.startAnimation(verticalShowAnimation);
            routeContainer.setVisibility(View.VISIBLE);
            googleMap.setPadding(0, 0, 0, routeContainer.getHeight());
        }
    }
}
