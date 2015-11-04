package com.savvisingh.thomso15.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.adapters.CustomListAdapterDialog;
import com.savvisingh.thomso15.map.CampusMap;
import com.savvisingh.thomso15.utils.DatabaseHelper;
import com.savvisingh.thomso15.utils.GPSTracker;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Savvi Singh on 10/7/2015.
 */
public class EventDescriptionActivity extends AppCompatActivity {

    private DatabaseHelper myDbHelper;

    private TextView eventVenue, eventTime, eventDate, eventDescription;

    private Bundle b;

    private ArrayList<String> contacts_name, contacts_number;

    private String day, type;

    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_description_card);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b = getIntent().getExtras();
        day = b.getString("day");
        type = b.getString("type");

        myDbHelper = new DatabaseHelper(this);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(b.getString("event"));

        eventVenue = (TextView) findViewById(R.id.event_venue);
        eventDate = (TextView) findViewById(R.id.event_date);
        eventTime = (TextView) findViewById(R.id.event_time);
        eventDescription = (TextView) findViewById(R.id.event_description);



            eventDate.setText(day);
            switch (day.length()){
                case 1:
                    break;
                case 4:
                    eventDate.setTextSize(35);
                    break;
                case 5:
                    eventDate.setTextSize(30);
                    break;
                case 7:
                    eventDate.setTextSize(25);
                    break;
            }


        eventTime.setText(setTime(b.getString("event")));
        eventVenue.setText(myDbHelper.getVenueDisplay(b.getString("event")));
        eventDescription.setText(myDbHelper.getEventDescription(b.getString("event")));

        final PointF coord = myDbHelper.searchPlaceForLatLong(myDbHelper
                .getVenueMap(b.getString("event")));
        eventVenue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("venue1 : ", b.getString("event"));

                getPathFromPresentLocation(coord.x, coord.y);
            }
        });


        contacts_name = myDbHelper.getEventContactsName(b.getString("event"));
        contacts_number = myDbHelper.getEventContactsNumber(b.getString("event"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contacts_name.size() > 0) {
                    final Dialog dialog = new Dialog(EventDescriptionActivity.this);

                    dialog.setTitle("Contact");
                    View customView = getLayoutInflater().inflate(R.layout.custom_list_dialog, null);

                    ListView lv = (ListView) customView.findViewById(R.id.custom_list);

                    // Change MyActivity.this and myListOfItems to your own values
                    CustomListAdapterDialog clad = new CustomListAdapterDialog(EventDescriptionActivity.this, contacts_name, contacts_number);

                    lv.setAdapter(clad);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + contacts_number.get(arg2)));
                            startActivity(callIntent);
                        }
                    });

                    dialog.setContentView(customView);

                    dialog.show();
                }else {

                    Snackbar.make(view, "Sorry no contacts available", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

       myDbHelper.close();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }




    private void getPathFromPresentLocation(double destLat, double destLong) {
        // TODO Auto-generated method stub
        // create class object
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            onlineMap(latitude, longitude, destLat, destLong);

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private void onlineMap(double startLat, double startLong, double destLat,
                           double destLong) {
        // TODO Auto-generated method stub
        String uri = "http://maps.google.com/maps?saddr=" + startLat + ","
                + startLong + "&daddr=" + destLat + "," + destLong;
        Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(uri));
        intent1.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }

    private void showZoomedMap(String place) {

        PointF coord = myDbHelper.searchPlaceForCoordinates(place);
        Bundle mapParams = new Bundle();
        mapParams.putInt("mode", 1); // mode = 0 for normal and mode = 1 for
        // zoomed
        mapParams.putFloat("X", (float) coord.x);
        mapParams.putFloat("Y", (float) coord.y);
        Log.i("coord : ", coord.x + " : " + coord.y);

        Intent i = new Intent(this, CampusMap.class);
        i.putExtras(mapParams);
        startActivity(i);
        // myDbHelper.close();
    }

    public String setTime(String event) {

        int start = myDbHelper.getStartTime(event);
        int end = myDbHelper.getEndTime(event);

        String startX;
        String endX;

        if(start % 100 == 0){
            startX = start/100 +":" + start%100 + "0";
        }else{
            startX = start/100 +":" + start%100;
        }
        if(end % 100 == 0){
            endX = end/100 +":" + end%100 + "0";
        }else{
            endX = end/100 + ":" + end%100;

        }
        return startX + " - " + endX;
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
