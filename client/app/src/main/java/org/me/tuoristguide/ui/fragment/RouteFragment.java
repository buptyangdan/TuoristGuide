package org.me.tuoristguide.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.yelp.clientlib.entities.Business;

import org.me.tuoristguide.R;
import org.me.tuoristguide.service.local.YelpService;
import org.me.tuoristguide.service.task.NetworkAsyncTask;
import org.me.tuoristguide.ui.adapter.BusinessRouteAdapter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by zy on 4/30/16.
 */
public class RouteFragment extends RoboFragment implements OnMapReadyCallback {

    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT";
    private static final String CURRENT_LOCATION_ARG = "CURRENT_LOCATION";

    @InjectView(R.id.walking_directions_button)        private FloatingActionButton walkingDirectionsButton;
    @InjectView(R.id.driving_directions_button)        private FloatingActionButton drivingDirectionsButton;
    @InjectView(R.id.bike_directions_button)           private FloatingActionButton bikeDirectionsButton;
    @InjectView(R.id.travel_distance_container)        private CardView travelDistanceContainer;
    @InjectView(R.id.building_route_layout)            private LinearLayout buildingRouteLayout;
    @InjectView(R.id.travel_distance_text)             private TextView travelDistanceTextView;
    @InjectView(R.id.directions_menu)                  private FloatingActionMenu directionsMenu;
    @InjectView(R.id.route_container)                  private CardView routeContainer;
    @InjectView(R.id.route_list)                       private RecyclerView routeList;

    private GoogleMap googleMap;
    private Animation verticalShowAnimation;
    private Animation verticalHideAnimation;
    private BusinessRouteAdapter businessRouteAdapter;
    private RouteBuilderTask routeBuilderTask;
    private List<Business> businessList;
    private DirectionsRoute route;
    private GeoApiContext geoApiContext;
    private List<Marker> markers;


    public static RouteFragment newInstance(Location currentLocation) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_LOCATION_ARG, currentLocation);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        geoApiContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_key));
        markers = new ArrayList<>();
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

        directionsMenu.setIconAnimated(false);
        walkingDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionsMenu.close(true);
                if (routeBuilderTask != null && !routeBuilderTask.isActive())
                    recalculateRoute(TravelMode.WALKING);
            }
        });
        drivingDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionsMenu.close(true);
                if (routeBuilderTask != null && !routeBuilderTask.isActive()) recalculateRoute(TravelMode.DRIVING);
            }
        });
        bikeDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionsMenu.close(true);
                if (routeBuilderTask != null && !routeBuilderTask.isActive()) recalculateRoute(TravelMode.BICYCLING);
            }
        });


        routeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        routeList.setItemAnimator(null);
        businessRouteAdapter = new BusinessRouteAdapter(getCurrentLocationArg());
        businessRouteAdapter.setOnDeleteItemListener(new BusinessRouteAdapter.OnDeleteItemListener() {
            @Override
            public void onDelete(Business business, int position) {
                onDeleteBusinessInRoute(position);
            }

            @Override
            public void showMarkerAtPosition(String markerName) {
                showMarker(markerName);
            }
        });
        routeList.setAdapter(businessRouteAdapter);

        verticalShowAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.vertical_show);
        verticalHideAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.vertical_hide);

        fragment.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //shouldRestoreRoute = false;
        if (savedInstanceState == null) {
            businessList = YelpService.getInstance().getRecommendedBusiness();
            businessRouteAdapter.set(businessList);
            routeBuilderTask = new RouteBuilderTask()
                    .setGeoAPI(geoApiContext)
                    .setBusinesses(businessList)
                    .origin(getCurrentLocationArg())
                    .travelMode(getCurrentTravelMode());
            routeBuilderTask.request();
        } else {
            /*
            if (routeBuilderTask != null && routeBuilderTask.isActive()) onPreExecuteRouteBuilderTask();
            if (recalculateRouteTask != null && recalculateRouteTask.isActive()) onPreExcecuteRecalculateRouteTask();
            else shouldRestoreRoute = true;
            restorePois();
            */
        }

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
        showCurrentLocationInMap();
    }

    /**
     * @param position
     */
    private void onDeleteBusinessInRoute(int position) {
        int poiPosition = position-1;
        businessList.remove(poiPosition);
        recalculateRoute(getCurrentTravelMode());
    }

    private void recalculateRoute(TravelMode travelMode) {
        saveCurrentTravelMode(travelMode);
        routeBuilderTask = new RouteBuilderTask()
                .setGeoAPI(geoApiContext)
                .setBusinesses(businessList)
                .origin(getCurrentLocationArg())
                .travelMode(getCurrentTravelMode());
        routeBuilderTask.request();
    }

    private Location getCurrentLocationArg() {
        return (Location) getArguments().getParcelable(CURRENT_LOCATION_ARG);
    }

    private void showCurrentLocationInMap() {
        Location currentLocation = getCurrentLocationArg();
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.current_location))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_location)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
    }

    /**
     * @return
     */
    private TravelMode getCurrentTravelMode() {
        String travelModeStr = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString(getString(R.string.travel_mode_key), TravelMode.DRIVING.name());
        return TravelMode.valueOf(travelModeStr);
    }

    /**
     * @param selectedTravelMode
     */
    private void saveCurrentTravelMode(TravelMode selectedTravelMode) {
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .edit()
                .putString(getString(R.string.travel_mode_key), selectedTravelMode.name())
                .commit();
    }

    /**
     *
     */
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

    private void addBusinessMarker(Business business) {
        if (business != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(business.location().coordinate().latitude(), business.location().coordinate().longitude()))
                    .title(business.name()));
            markers.add(marker);
        }
    }

    public void showMarker(String name){
        for (int i = 0; i< businessList.size(); i++){
            Business b = businessList.get(i);
            if (b.name().toString().equals(name)) {
                Marker marker = markers.get(i);
                if (marker != null) {
                    marker.showInfoWindow();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15f));
                }
            }
        }
    }

    /**
     * @param route
     */
    private void onAcquireRoute(DirectionsRoute route) {
        if (businessList != null && businessList.size() > 0){
            businessRouteAdapter.set(businessList);
            businessRouteAdapter.notifyDataSetChanged();
            markers.clear();
            for (Business b: businessList) addBusinessMarker(b);
        }
        this.route = route;
        if (route != null) {
            double totalDistance = 0f;
            for (DirectionsLeg leg : route.legs)
                totalDistance += leg.distance.inMeters;
            travelDistanceTextView.setText(getString(R.string.travel_distance, totalDistance / 1_000f));
            travelDistanceContainer.setVisibility(View.VISIBLE);
            List<com.google.maps.model.LatLng> polyline = route.overviewPolyline.decodePath();
            PolylineOptions pathOptions = new PolylineOptions().color(getResources().getColor(R.color.accent));
            for (com.google.maps.model.LatLng point : polyline)
                pathOptions.add(new LatLng(point.lat, point.lng));
            googleMap.addPolyline(pathOptions);
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.unavailable_route, Toast.LENGTH_SHORT).show();
        }
    }

    private void onCompleteRouteBuilderTask() {
        travelDistanceContainer.setVisibility(View.VISIBLE);
        buildingRouteLayout.setVisibility(View.GONE);
        routeList.setVisibility(View.VISIBLE);
    }


    private void onPreExecuteBuildRouteTask() {
        travelDistanceContainer.setVisibility(View.GONE);
        buildingRouteLayout.setVisibility(View.VISIBLE);
        if (googleMap != null) {
            for (Business b: businessList){
                addBusinessMarker(b);
            }
            googleMap.clear();
            showCurrentLocationInMap();
        }
    }


    //  --------------------------------- async tasks ---------------------------------------------

    private class RouteBuilderTask extends NetworkAsyncTask<DirectionsRoute> {

        private GeoApiContext geoApiContext;
        private Location origin;
        private TravelMode travelMode;
        private List<Business> businesses;

        private RouteBuilderTask() {
            super(getActivity().getApplicationContext());
        }

        private RouteBuilderTask setGeoAPI(GeoApiContext context) {
            geoApiContext = context;
            return this;
        }

        private RouteBuilderTask origin(Location origin) {
            this.origin = origin;
            return this;
        }

        private RouteBuilderTask travelMode(TravelMode travelMode) {
            this.travelMode = travelMode;
            return this;
        }

        private RouteBuilderTask setBusinesses(List<Business> businesses) {
            this.businesses = businesses;
            return this;
        }

        @Override
        protected void onPreExecute() throws Exception {
            super.onPreExecute();
            onPreExecuteBuildRouteTask();
        }

        @Override
        public DirectionsRoute call() throws Exception {
            String[] waypoints = new String[this.businesses.size() - 1];

            for (int i = 0; i < waypoints.length; i++)
                waypoints[i] = toGoogleMapsServicesLatlng(this.businesses.get(i).location());

            DirectionsRoute[] routes = DirectionsApi.newRequest(geoApiContext)
                    .origin(toGoogleMapsServicesLatLng(origin))
                    .destination(toGoogleMapsServicesLatlng(businesses.get(businesses.size() - 1).location()))
                    .mode(travelMode)
                    .waypoints(waypoints)
                    .await();
            return routes == null || routes.length == 0 ? null : routes[0];
        }

        @Override
        protected void onSuccess(DirectionsRoute route) throws Exception{
            super.onSuccess(route);
            onAcquireRoute(route);
        }

        @Override
        protected void onFinally() throws RuntimeException {
            super.onFinally();
            onCompleteRouteBuilderTask();
        }

        private String toGoogleMapsServicesLatLng(Location location) {
            return String.valueOf(location.getLatitude()) + ',' + location.getLongitude();
        }

        private String toGoogleMapsServicesLatlng(com.yelp.clientlib.entities.Location location) {
            return String.valueOf(location.coordinate().latitude()) + ',' +
                    location.coordinate().longitude();
        }


    }
}
