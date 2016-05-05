package org.me.tuoristguide.service.remote;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.me.tuoristguide.model.Comment;
import org.me.tuoristguide.util.NetworkConnector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyi on 16/4/24.
 * This class is used to get info from server
 */
public class CommentService {
    private static  CommentService instance;
    private CommentServiceInterface controller;
    public static CommentService getInstance(){
        if (instance == null)
            instance=new CommentService();
        return instance;
    }
    public void setController(CommentServiceInterface controller){
        this.controller=controller;
    }

    public void getByStore(String store_id) {
        String uri = "http://54.172.42.185:8080/org.me.backend/user_info?Mode=byStore&store_id=" + store_id;
        final JSONObject[] result = {new JSONObject()};
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(controller!=null){
                            controller.getJsonResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        NetworkConnector.getInstance().mQueue.add(getRequest);
    }


    public void getByUser(String user_email){
        String uri = "http://54.172.42.185:8080/org.me.backend/user_info?Mode=byUser&email="+user_email;
        final JSONObject[] result = {new JSONObject()};
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, uri, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(controller!=null){
                            controller.getJsonResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        NetworkConnector.getInstance().mQueue.add(getRequest);
    }


    public interface CommentServiceInterface{
          void getJsonResponse(JSONObject jsonObject);
    }
}



