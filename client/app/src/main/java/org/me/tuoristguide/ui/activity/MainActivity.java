package org.me.tuoristguide.ui.activity;


import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.me.tuoristguide.R;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.service.local.FacebookService;
import org.me.tuoristguide.ui.fragment.ExploreFragment;
import org.me.tuoristguide.ui.fragment.LocationsFragment;
import org.me.tuoristguide.ui.fragment.SearchFragment;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 1;

    //Defining Variables
    public Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    String name;
    String email;
    String picture;
    TextView textView1;
    TextView textView2;
    ImageView imageView;
    JSONObject profile,profile_pic_data,profile_pic_url;
    public static FragmentManager fragmentManager;

    @Override
    protected void onResume() {
        super.onResume();

        if (UserManager.getInstance().getCurrentUser() != null){
            // TODO load user data from local database
            System.out.println("login");
        } else {
            // TODO Setup header with default data
            System.out.println("not login");
            UserManager.getInstance().removeUser();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        Intent intent=getIntent();
        //String str=(String)intent.getExtras().get("profile");
        //System.out.println("str:" + str);
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header=navigationView.getHeaderView(0);
        textView1=(TextView)header.findViewById(R.id.username);
        textView2=(TextView)header.findViewById(R.id.email);
        imageView=(ImageView)header.findViewById(R.id.profile_image);


        //  imageView.setImageResource(picture);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener());

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer){

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes as
                        // we dont want anything to happen so we leave this blank
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open as we
                        // dont want anything to happen so we leave this blank
                        super.onDrawerOpened(drawerView);
                    }
                };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


        checkAndRequestLocationPermission();

    }

    private void checkAndRequestLocationPermission(){

        // check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private class OnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener
    {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            //Checking if the item is in checked state or not, if not make it in checked state
            if(menuItem.isChecked()) menuItem.setChecked(false);
            else menuItem.setChecked(true);

            //Closing drawer on item click
            drawerLayout.closeDrawers();
            android.support.v4.app.Fragment fragment = null;
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()){
                //Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.profile:
                    Intent startLogin = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(startLogin);
                    /*
                    fragment = new ProfileFragment();
                    fragmentTransaction.replace(R.id.frame,fragment);
                    fragmentTransaction.commit();
                    */
                    return true;
                // For rest of the options we just show a toast on click
                case R.id.explore:
                    fragment = new ExploreFragment();
                    fragmentTransaction.replace(R.id.frame,fragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.locations:
                    fragment = new LocationsFragment();
                    fragmentTransaction.replace(R.id.frame,fragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.search:
                    fragment = new SearchFragment();
                    fragmentTransaction.replace(R.id.frame,fragment);
                    fragmentTransaction.commit();
                    return true;

                case R.id.detail:

                    Intent startdetail = new Intent(MainActivity.this, DetailActivity.class);

                    startdetail.putExtra("store_name","CMU Silicon Valley");
                    startActivity(startdetail);

                    return true;

                default:
                    Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                    return true;
            }
        }
    }
}






