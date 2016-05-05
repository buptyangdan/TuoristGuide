package org.me.tuoristguide.model;

import com.yelp.clientlib.entities.Business;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zy on 4/14/16.
 */
public class Store {

    public String store_id;
    public String store_name;
    public String store_pic;
    public Double store_rating;
    public Store(Business business){
            store_id=business.id();
            store_name=business.name();
            store_pic=business.imageUrl();
            store_rating=business.rating();
    }





}
