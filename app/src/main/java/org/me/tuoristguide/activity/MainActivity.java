package org.me.tuoristguide.activity;


import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
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

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.me.tuoristguide.R;
import org.me.tuoristguide.fragment.ExploreFragment;
import org.me.tuoristguide.fragment.HomeFragment;
import org.me.tuoristguide.fragment.LocationsFragment;
import org.me.tuoristguide.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        Intent intent=getIntent();
        String str=(String)intent.getExtras().get("profile");
        System.out.println("str:" + str);
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header=navigationView.getHeaderView(0);
        textView1=(TextView)header.findViewById(R.id.username);
        textView2=(TextView)header.findViewById(R.id.email);
        imageView=(ImageView)header.findViewById(R.id.profile_image);

            try {
                profile=new JSONObject(str);
                name=profile.getString("name");
                System.out.println("name"+name);
                email=profile.getString("email");
                profile_pic_data=new JSONObject(profile.getString("picture"));
                profile_pic_url=new JSONObject(profile_pic_data.getString("data"));
//            picture=profile.getString("picture");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            textView1.setText(name);
            textView2.setText(email);
            try {
                Picasso.with(this).load(profile_pic_url.getString("url"))
                        .into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }



        //  imageView.setImageResource(picture);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
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
                        fragment = new HomeFragment();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();
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

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();






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
}






