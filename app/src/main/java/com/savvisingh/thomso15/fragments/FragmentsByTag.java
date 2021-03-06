package com.savvisingh.thomso15.fragments;

import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.adapters.EventsRecyclerViewAdapter;
import com.savvisingh.thomso15.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Savvi Singh on 10/7/2015.
 */
public class FragmentsByTag extends Fragment {

    private LinearLayoutManager layoutManager;
    private EventsRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;

    private String tag;

    private Context context;

    ArrayList<String> eventname;

    public static FragmentsByTag newInstance(String tag) {
        FragmentsByTag fragment = new FragmentsByTag();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        fragment.setArguments(args);
        return fragment;
    }

    DatabaseHelper myDbHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_day, container, false);

        context = getActivity();
        tag = getArguments().getString("tag");
        Log.d("Tag", tag);

        List<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
        myDbHelper = new DatabaseHelper(getActivity()
                .getBaseContext());
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

        eventname = myDbHelper.getEventName(tag);



        if(eventname.size()>0){
            for (int i = 0; i < eventname.size(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("eventname", eventname.get(i));
                hm.put("eventtime", setTime(eventname.get(i)));
                hm.put("eventvenue",myDbHelper.getVenueDisplay(eventname.get(i)));
                hm.put("eventtag",myDbHelper.getEventTag((eventname.get(i))));
                hm.put("eventdate",myDbHelper.getEventDate((eventname.get(i))));
                eventList.add(hm);
            }
        }


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EventsRecyclerViewAdapter(eventList,"Tag", getActivity());
        recyclerView.setAdapter(mAdapter);


        myDbHelper.close();

        return view;
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


}
