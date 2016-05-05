package org.me.tuoristguide.service.remote;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.me.tuoristguide.model.User;
import org.me.tuoristguide.util.NetworkConnector;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    public void createNewUser(final User user) {
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
                params.put("Mode","user_info");
                params.put("user_name", user.name);
                params.put("email", user.email);
                params.put("photo_url",user.picture_url);
                return params;
            };
        };
       NetworkConnector.getInstance().mQueue.add(mReq);
    }

}
