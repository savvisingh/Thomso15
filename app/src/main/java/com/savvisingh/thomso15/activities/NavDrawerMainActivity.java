package com.savvisingh.thomso15.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.fragments.EventsByDayFragmentPager;
import com.savvisingh.thomso15.fragments.EventsByTagFragmentPager;
import com.savvisingh.thomso15.fragments.MyFavouriteEvents;
import com.savvisingh.thomso15.fragments.OnGoingEvents;
import com.savvisingh.thomso15.map.CampusMap;
import com.savvisingh.thomso15.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class NavDrawerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;



    private DatabaseHelper myDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        myDbHelper = new DatabaseHelper(getBaseContext());
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        addHomeFragment();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ComponentName componentName = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(componentName) );



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        switch (id){
            case R.id.contact:
                Intent intent = new Intent(this, TeamContacts.class);
                startActivity(intent);
                break;
            case R.id.about_us:
                Intent intent2 = new Intent(this, AboutUs.class);
                startActivity(intent2);
                break;
            case R.id.map:
                Bundle mapParams = new Bundle();
                mapParams.putInt("mode", 0); // mode = 0 for normal and mode = 1 for
                // zoomed
                Intent i = new Intent(this,CampusMap.class);
                i.putExtras(mapParams);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        switch (id){
            case R.id.day0:
                updateFragment(2);
                break;
            case R.id.day1:
                updateFragment(3);
                break;
            case R.id.day2:
                updateFragment(4);
                break;
            case R.id.day3:
                updateFragment(5);
                break;
            case R.id.events_by_tag:
                updateFragment(6);
                break;
            case R.id.ongoing_events:
                updateFragment(1);
                break;
            case R.id.my_favourite:
                updateFragment(0);
                break;
            case R.id.map_iitr:
                Bundle mapParams = new Bundle();
                mapParams.putInt("mode", 0); // mode = 0 for normal and mode = 1 for
                // zoomed
                Intent i = new Intent(this,CampusMap.class);
                i.putExtras(mapParams);
                startActivity(i);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void addHomeFragment() {

        // initialize the HomeFragment
       updateFragment(2);
    }


    private void updateFragment(int pos){
        Fragment fragment = null;

        switch (pos){
            case 0:

                ArrayList<String> eventname = myDbHelper.getFavouritesName();
                Log.i("event.size()", String.valueOf(eventname.size()));
                if (eventname.size() == 0) {
                    Toast.makeText(this, "You have no favourite events yet, first mark the events",
                            Toast.LENGTH_SHORT).show();

                    return;
                } else {

                    fragment = new MyFavouriteEvents();
                }

                break;
            case 1:

                Calendar c = Calendar.getInstance();
                int date =c.get(Calendar.DAY_OF_MONTH);
                Log.i("date", String.valueOf(date));
               int day ;
                switch (date) {
                    case 9:
                        day = 1;
                        break;
                    case 10:
                        day = 2;
                        break;
                    case 11:
                        day = 3;
                        break;
                    default:
                        day = 0;
                        break;
                }

                ArrayList<String> onGoingEventNames = myDbHelper.getOnGoingEventNames(day);
                if(onGoingEventNames.size() > 0){
                    fragment = new OnGoingEvents();
                }else {
                    Toast.makeText(this, "No Ongoing Events",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                break;
            case 2:
                fragment = EventsByDayFragmentPager.newInstance(0);
                break;
            case 3:
                fragment = EventsByDayFragmentPager.newInstance(1);
                break;
            case 4:
                fragment = EventsByDayFragmentPager.newInstance(2);
                break;
            case 5:
                fragment = EventsByDayFragmentPager.newInstance(3);
                break;
            case 6:
                fragment = new EventsByTagFragmentPager();
                break;
            case 7:
                break;
        }


        fragmentManager = getSupportFragmentManager();

        // Creating a fragment transaction
        ft = fragmentManager.beginTransaction();

        // Adding a fragment to the fragment transaction
        ft.replace(R.id.frameLayout, fragment);
        // Committing the transaction
        ft.commit();

    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
