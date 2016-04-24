package org.me.tuoristguide.entities;

import org.json.JSONObject;
import org.me.tuoristguide.model.User;
import org.me.tuoristguide.service.local.FacebookService;
import org.me.tuoristguide.service.remote.UserService;

import java.util.ArrayList;

/**
 * Created by zy on 4/14/16.
 */
public class UserManager {
    private static UserManager instance = null;

    private User currentUser = null;

    public static UserManager getInstance() {
        if (instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setupUser(JSONObject json){
        currentUser = new User(json);
        //save the User into server side
        System.out.println("currentUser!");
        System.out.println(currentUser);
        UserService userService=new UserService();
        userService.createNewUser(currentUser);

    }



    public void removeUser() {
        currentUser = null;
    }

    public void loadDBUser() {
        // check if user has already logged in and read data from local database
        if (FacebookService.getInstance(null).checkLoginStatus()){
            // TODO load from databse
        }
    }

}
