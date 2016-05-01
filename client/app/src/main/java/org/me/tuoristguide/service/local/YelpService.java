package org.me.tuoristguide.service.local;

import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.BoundingBoxOptions;

import org.me.tuoristguide.R;
import org.me.tuoristguide.entities.StoreManager;
import org.me.tuoristguide.ui.activity.DetailActivity;
import org.me.tuoristguide.ui.fragment.ExploreFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static org.me.tuoristguide.R.string.app_name;

/**
 * Created by zy on 4/14/16.
 */
public class YelpService {

    private static YelpService instance = null;
    private YelpServiceInterface controller = null;
    private static YelpAPI yelpAPI = null;
    private ArrayList<Business> businesses = null;


    public static YelpService getInstance() {
        if(instance == null) {
            instance = new YelpService();
            YelpAPIFactory apiFactory =
                    new YelpAPIFactory("NUBhGlHwegeyRXi5Ci9XLA",
                    "evZpCq1B7GcjEv-5WgFYH2iNBcM",
                    "Rl5mlNSOFd4sJ9G4KNVjDWa7wLmdyUVG",
                    "31cVuZroz3LPGrfRqy6IAfpn3n0");

            yelpAPI = apiFactory.createAPI();
        }
        return instance;
    }

    public void setController(YelpServiceInterface controller){
        if (instance != null)
            instance.controller = controller;
    }


    public ArrayList<Business> getBusinesses() {
        return this.businesses;
    }


    /***
     * This method is used to find all nearby recommendations
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public void yelpNearby(Double latitude, Double longitude) {
        // get neary businesses by Yelp API

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("limit", "20");

        final BoundingBoxOptions bounds = BoundingBoxOptions.builder()
                .swLatitude(latitude - 0.01)
                .swLongitude(longitude - 0.01)
                .neLatitude(latitude + 0.01)
                .neLongitude(longitude + 0.01).build();

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {

            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {

                // System.out.println("here is the response!!! " + response.body().businesses());
                businesses = response.body().businesses();
                if (controller != null)
                    controller.placeBusinessMarks(businesses);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("something wrong!!" + t);
            }

        };

        Call<SearchResponse>  call = yelpAPI.search(bounds, queryParams);
        call.enqueue(callback);
    }

    public void yelpNearby(Location location){
        if (location != null)
            yelpNearby(location.getLatitude(), location.getLongitude());
    }


    public interface YelpServiceInterface {
        void placeBusinessMarks(ArrayList<Business> businesses);
        void startDetailActivity();
    }

    public void yelpBussiness(String id){
        Callback<Business> callback = new Callback<Business>() {
            @Override
            public void onResponse(Response<Business> response, Retrofit retrofit) {
                Business business=response.body();
                StoreManager.getInstance().setCurrent_store(business);
                controller.startDetailActivity();
            }
            @Override
            public void onFailure(Throwable t) {

                System.out.println("something is wrong!");
            }
        };
        Call<Business> call = yelpAPI.getBusiness(id);
        call.enqueue(callback);
    }

}
