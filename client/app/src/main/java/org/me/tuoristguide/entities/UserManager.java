package org.me.tuoristguide.entities;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.me.tuoristguide.database.DatabaseConnector;
import org.me.tuoristguide.model.User;
import org.me.tuoristguide.service.local.FacebookService;
import org.me.tuoristguide.service.remote.UserService;
import org.me.tuoristguide.ui.activity.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 4/14/16.
 */
public class UserManager {
    private static UserManager instance = null;

    private User currentUser = null;

    public static UserManager getInstance() {
        if (instance == null){
            System.out.println("times!");
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
        UserService userService=new UserService();
        userService.createNewUser(currentUser);

    }

    public void removeUser() {
        currentUser = null;
    }

    public void loadDBUser(Context context) {
        //Toast.makeText(context, "WELCOME!", Toast.LENGTH_LONG).show();
        // check if user has already logged in and read data from local database
       // if (FacebookService.getInstance().checkLoginStatus()) {
            //means the user is online
            // TODO load from databse
            //  DatabaseConnector databaseConnector = new DatabaseConnector(context, "User.db", null, 2);
            DatabaseConnector adb = new DatabaseConnector(context, "User.db", null, 2);
            List<Cursor> resultSet = adb.getData("select user_name, email, photo_url from User");
            if (resultSet.get(0).getString(0) != null) {
                currentUser = new User(resultSet.get(0).getString(1), resultSet.get(0).getString(0), resultSet.get(0).getString(2));

            }
        }
   // }

}
