package org.me.tuoristguide.entities;

import com.yelp.clientlib.entities.Business;

import org.json.JSONObject;
import org.me.tuoristguide.model.Store;
import org.me.tuoristguide.model.User;

/**
 * Created by caoyi on 16/4/23.
 */
public class StoreManager {
    private static StoreManager instance;
    private Store current_store=null;
    public static StoreManager getInstance() {
        if (instance == null){
            instance = new StoreManager();
        }
        return instance;
    }

    public Store getCurrent_store(){
        return current_store;
    }

    public void setCurrent_store(Business business){
        current_store=new Store(business);
    }

    public void removeUser(){current_store=null;}

    public void loadDBStore(){
        // TODO load from databse
    }

}
