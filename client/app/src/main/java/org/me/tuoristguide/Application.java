package org.me.tuoristguide;

import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.util.NetworkConnector;

/**
 * Created by zy on 4/16/16.
 */
public class Application extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // create Network thread pool here
        NetworkConnector.getInstance().setmQueue(Volley.newRequestQueue(this));

        // init Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                if (AccessToken.getCurrentAccessToken() == null) {
                    System.out.println("not logged in yet");
                } else {
                    System.out.println("Logged in");
                    // init UserManager
                    UserManager.getInstance().loadDBUser(getApplicationContext());

                }
            }
        });



    }

}
