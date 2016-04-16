package org.me.tuoristguide.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zy on 4/14/16.
 */
public class NetworkConnector {

    private static NetworkConnector instance = null;
    private RequestQueue mQueue;
    private Context context;

    private static String host_url = "http://ec2-54-84-135-175.compute-1.amazonaws.com:3000/api/v0";

    protected NetworkConnector() {
        // Exists only to defeat instantiation.
    }

    public static NetworkConnector getInstance() {
        if(instance == null) {
            instance = new NetworkConnector();
        }
        return instance;
    }


    public void setmQueue(RequestQueue q){
        // thread pool
        mQueue = q;
    }

    public void test() {
        String url = "http://ec2-54-84-135-175.compute-1.amazonaws.com:3000/api/v0/promos/editor";
        JsonObjectRequest jsonRequest =
                new JsonObjectRequest(Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject result) {
                        System.out.println(result);

                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mQueue.add(jsonRequest);
    }

    public void getJsonData(String endpoint, final OnJSONResponseListener listener) {

        String url = host_url + endpoint;
        mQueue.add(new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                }));

    }

    public interface OnJSONResponseListener {
        void onResponse(JSONObject result);
    }
}

