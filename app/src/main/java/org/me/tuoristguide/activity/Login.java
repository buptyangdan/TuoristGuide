package org.me.tuoristguide.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;
import org.me.tuoristguide.R;

import java.util.Arrays;
import java.util.List;


public class Login extends Activity {

    private SharedPreferences mPrefs;
    public static LoginButton btn_login;
    CallbackManager callbackManager;
    GraphRequest graphRequest;
    String jsonresult;
    Intent intent;
    public LoginManager instance;
    public AccessTokenTracker mAccessTokenTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //if last time still facebook on then first log out

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        btn_login=(LoginButton)findViewById(R.id.btn_login);
        instance=LoginManager.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instance==null){
                    login();
                }


            }
        });

    }

    /***
     * This method is used to log in fb
     */
    protected  void login(){

        List<String> permissionNeeds = Arrays.asList("email","user_photos","public_profile","user_about_me","user_birthday", "user_friends", "user_photos");
        btn_login.setReadPermissions(permissionNeeds);
        btn_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
               View view=(View)findViewById(R.id.login);
                view.setVisibility(View.INVISIBLE);
               graphRequest= GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                    Log.d("ERROR!","cuole");
                                    System.out.println("ERROR");
                                } else {
                                    System.out.println("Success");

                                          jsonresult = String.valueOf(json);
                                          System.out.println("JSON Result" + jsonresult);
                                          Log.d("SUCCESS!", jsonresult);
                                    intent = new Intent(Login.this,MainActivity.class);
                                    intent.putExtra("profile",jsonresult);
                                    startActivity(intent);
                                }
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday, picture");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();


            }

            @Override
            public void onCancel()
            {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance=LoginManager.getInstance();
        if(instance!=null){
            instance.logInWithReadPermissions(Login.this,Arrays.asList("public_profile"));
            View view=(View)findViewById(R.id.login);
            view.setVisibility(View.INVISIBLE);
            graphRequest= GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject json, GraphResponse response) {
                            if (response.getError() != null) {
                                // handle error
                                Log.d("ERROR!", "cuole");
                                System.out.println("ERROR");
                            } else {
                                System.out.println("Success");

                                jsonresult = String.valueOf(json);
                                System.out.println("JSON Result" + jsonresult);
                                Log.d("SUCCESS!", jsonresult);
                                intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("profile", jsonresult);
                                startActivity(intent);
                            }
                        }

                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email, gender, birthday, picture");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();


        }
    }
}
