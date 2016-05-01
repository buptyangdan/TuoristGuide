package org.me.tuoristguide.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.me.tuoristguide.R;
import org.me.tuoristguide.service.local.LocationService;
import org.me.tuoristguide.ui.fragment.RouteFragment;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by zy on 4/17/16.
 */

@ContentView(R.layout.activity_route)
public class RouteActivity extends RoboActionBarActivity {

    private static final String CURRENT_LOCATION_ARG = "CURRENT_LOCATION";


    public static Intent getIntent(Context context, Location currentLocation) {
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(CURRENT_LOCATION_ARG, currentLocation);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            Location currentLocation = getIntent().getExtras().getParcelable(CURRENT_LOCATION_ARG);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, RouteFragment.newInstance(currentLocation))
                    .commit();
        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
