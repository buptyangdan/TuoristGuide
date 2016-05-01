package org.me.tuoristguide.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.me.tuoristguide.R;
import org.me.tuoristguide.ui.fragment.RouteFragment;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by zy on 4/17/16.
 */

@ContentView(R.layout.activity_route)
public class RouteActivity extends RoboActionBarActivity {

    @InjectView(R.id.toolbar) private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, RouteFragment.newInstance())
                    .commit();
        }
    }
}
