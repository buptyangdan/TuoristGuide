package org.me.tuoristguide.service.remote;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yelp.clientlib.entities.Business;

import org.me.tuoristguide.entities.StoreManager;
import org.me.tuoristguide.model.Comment;
import org.me.tuoristguide.model.Store;
import org.me.tuoristguide.service.local.YelpService;
import org.me.tuoristguide.util.NetworkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyi on 16/4/24.
 * This class is used to post store_info and users_stores info to the server side
 */
public class StoreService{


     private static StoreService instance;

     public static StoreService getInstance(){
         instance=new StoreService();
         return  instance;
     }

      public void CreateStore(final Store store){
          String uri="http://54.172.42.185:8080/org.me.backend/user_info";
          StringRequest mReq = new StringRequest(Request.Method.POST, uri,new Response.Listener<String>() {
              public void onResponse(String string) {
                  System.out.println(string);
              }
          }, new Response.ErrorListener() {
              public void onErrorResponse(VolleyError error) {
              }
          }
          ) {
              protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                  Map<String, String> params = new HashMap<String, String>();
                  params.put("Mode","stores_info");
                  params.put("pic_url", store.store_pic);
                  params.put("store_id", store.store_id);
                  params.put("store_name",store.store_name);
                  return params;
              };
          };
          NetworkConnector.getInstance().mQueue.add(mReq);
      }

      public void CreateUserStore(final Comment comment){
          String uri="http://54.172.42.185:8080/org.me.backend/user_info";
          StringRequest mReq = new StringRequest(Request.Method.POST, uri,new Response.Listener<String>() {
              public void onResponse(String string) {
                  System.out.println(string);
              }
          }, new Response.ErrorListener() {
              public void onErrorResponse(VolleyError error) {
              }
          }
          ) {
              protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                  Map<String, String> params = new HashMap<String, String>();
                  params.put("Mode","users_stores");
                  params.put("email",comment.getEmail());
                  params.put("store_id",comment.getStore_id());
                  params.put("comment_text",comment.getComment_text());
                  params.put("created_time",comment.getCreated_time());
                  return params;
              };
          };
          NetworkConnector.getInstance().mQueue.add(mReq);
      }


//    @Override
//    public void placeBusinessMarks(ArrayList<Business> businesses) {
//            YelpService.getInstance().setController(StoreService.getInstance());
//            Business business=businesses.get(0);
//
//            System.out.println("bad place");
//            System.out.println(business.id());
//            StoreManager.getInstance().setCurrent_store(business);
//    }
}
