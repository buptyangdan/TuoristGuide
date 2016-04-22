package org.me.tuoristguide;

import com.android.volley.toolbox.Volley;
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
        FacebookSdk.sdkInitialize(getApplicationContext());

        // init UserManager
        UserManager.getInstance().loadDBUser();

    }

}
