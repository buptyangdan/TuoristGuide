package org.me.tuoristguide.service.local;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;
import org.me.tuoristguide.entities.UserManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zy on 4/21/16.
 */
public class FacebookService {


    private static CallbackManager callbackManager = null;

    private static OnFacebookLoggedIn registered_activity = null;

    private static FacebookService instance = null;

    static public FacebookService getInstance(OnFacebookLoggedIn activity){
        if (instance == null){
            instance = new FacebookService(activity);
        }
        registered_activity = activity;
        return instance;
    }

    // Constructor, to create private instances;
    private FacebookService(OnFacebookLoggedIn activity){
        callbackManager = CallbackManager.Factory.create();
        registered_activity = activity;
    }

    public boolean checkLoginStatus() {
        // check if user has already login
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void setupFBLoginButton(LoginButton button){

        List<String> permissionNeeds =
                Arrays.asList("email","user_photos","public_profile","user_about_me",
                        "user_birthday", "user_friends", "user_photos");

        button.setReadPermissions(permissionNeeds);
        button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult)
            {
                GraphRequest graphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    // TODO handle error
                                } else {
                                    // set up user information locally
                                    UserManager.getInstance().setupUser(json);
                                    if (registered_activity != null)
                                        registered_activity.onFacebookLoggedIn();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday, picture");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel(){
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public interface OnFacebookLoggedIn{
        void onFacebookLoggedIn();
    }

}
