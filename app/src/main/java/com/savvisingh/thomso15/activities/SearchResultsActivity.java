package com.savvisingh.thomso15.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.adapters.EventsRecyclerViewAdapter;
import com.savvisingh.thomso15.utils.DatabaseHelper;
import com.savvisingh.thomso15.utils.MySuggestionProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {


    //  DatabaseHelper myDbHelper;


    ArrayList<String> eventname;

    List<HashMap<String, String>> eventList;

    DatabaseHelper myDbHelper;

    private LinearLayoutManager layoutManager;
    private EventsRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(layoutManager);

        eventList = new ArrayList<HashMap<String, String>>();
        eventname = new ArrayList<String>();

        mAdapter = new EventsRecyclerViewAdapter(eventList,"Tag", this);
        recyclerView.setAdapter(mAdapter);
        myDbHelper = new DatabaseHelper(getApplicationContext());
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


        handleIntent(getIntent());
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

//        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
//        if (appData != null) {
//            String hello = appData.getString("hello");
//
//            showResults(hello);
//        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            showResults(query);
        }
    }

    private void showResults(String query) {
        // Query your data set and show results
        // ...
        Log.d("Query", query);

        getSupportActionBar().setTitle(query);
        eventname = myDbHelper.getSearchEventsName(query+"*");


        for (int i = 0; i < eventname.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();

            hm.put("eventname", eventname.get(i));
            hm.put("eventtime", setTime(eventname.get(i)));
            hm.put("eventvenue",myDbHelper.getVenueDisplay(eventname.get(i)));
            hm.put("eventtag",myDbHelper.getEventTag((eventname.get(i))));
            hm.put("eventdate",myDbHelper.getEventDate((eventname.get(i))));

            eventList.add(hm);
        }


        mAdapter.notifyDataSetChanged();


        myDbHelper.close();


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
