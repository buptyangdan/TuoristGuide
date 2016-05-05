package org.me.tuoristguide.service.local;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import org.me.tuoristguide.model.Geo;

import java.io.IOException;
import java.util.List;

/**
 * Created by caoyi on 16/4/7.
 */
public class SearchService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public int result=Activity.RESULT_CANCELED;
    public static final String RESULT="result";
    public static final String GEO="geo";
    public static final String Latitude="latitude";
    public static final String Longtitude="longtitude";
    public static final String NOTIFICATION="org.me.tuoristguide.service";
    public SearchService() {
        super("SearchService");
    }

    @Override
  protected void onHandleIntent(Intent intent){
        Geocoder geocoder = new Geocoder(getBaseContext());
        Geo geo=new Geo();
        System.out.println("here is the intent service!");
      try {
          List<Address> addressList = geocoder.getFromLocationName(intent.getStringExtra("queryString"), 1);
          geo.setGeoLat(addressList.get(0).getLatitude());
          geo.setGeoLtn(addressList.get(0).getLongitude());
          System.out.println(geo.getGeoLat());
          System.out.println(geo.getGeoLtn());

      } catch (IOException e) {
          e.printStackTrace();
      }
        if(geo!=null){
            result= Activity.RESULT_OK;

        }
        publishResults(geo,result);

  }
    private void publishResults(Geo geo, int result) {
        Intent intent = new Intent(NOTIFICATION);
        System.out.println("ss"+geo.getGeoLat());
        System.out.println("ss"+geo.getGeoLtn());
        intent.putExtra(Latitude,  geo.getGeoLat()+"");
        intent.putExtra(Longtitude,geo.getGeoLtn()+"");
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }


}
