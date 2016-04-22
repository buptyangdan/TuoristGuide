package org.me.tuoristguide.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zy on 4/14/16.
 */
public class User {

    public String id;
    public String name;
    public String email;
    public String picture_url;

    public User(JSONObject json){

        try {
            id = json.getString("id");
            name = json.getString("name");
            email = json.getString("email");
            picture_url = json.getJSONObject("picture").getJSONObject("data").getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
