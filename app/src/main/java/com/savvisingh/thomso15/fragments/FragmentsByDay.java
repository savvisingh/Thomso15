package com.savvisingh.thomso15.fragments;

import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * Created by Savvi Singh on 10/6/2015.
 */
public class FragmentsByDay extends Fragment {

    private LinearLayoutManager layoutManager;
    private EventsRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;

    private int day, eventDate;

    private Context context;

    ArrayList<String> eventname;

    public static FragmentsByDay newInstance(int day) {
        FragmentsByDay fragment = new FragmentsByDay();
        Bundle args = new Bundle();
        args.putInt("day", day);
        fragment.setArguments(args);
        return fragment;
    }

    private DatabaseHelper myDbHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day, container, false);

        context = getActivity();
        day = getArguments().getInt("day");

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

        eventname = myDbHelper.getEventNamex(day);

        switch(day){
            case 0:
                eventDate = 8;
                break;
            case 1:
                eventDate = 9;
                break;
            case 2:
                eventDate = 10;
                break;
            case 3:
                eventDate = 11;
                break;

        }
        for (int i = 0; i < eventname.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();

            hm.put("eventname", eventname.get(i));

            hm.put("eventtime", setTime(eventname.get(i)));
            hm.put("eventvenue",myDbHelper.getVenueDisplay(eventname.get(i)));
            hm.put("eventtag",myDbHelper.getEventTag((eventname.get(i))));
            hm.put("eventdate",String.valueOf(eventDate));


            eventList.add(hm);
        }


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EventsRecyclerViewAdapter(eventList,"Day", getActivity());
        recyclerView.setAdapter(mAdapter);

        myDbHelper.close();


        return view;
    }

    public String setTime(String event) {

        int start = myDbHelper.getStartTime(event);
        int end = myDbHelper.getEndTime(event);

        String startX;
        String endX;

//        if (start < 1200) {
//            if (start % 100 == 0) {
//                startX = start / 100 + ":" + "00";
//            } else {
//                startX = start / 100 + ":" + start % 100 + "";
//            }
//        } else if (start >= 1200 && start < 1300) {
//            if (start % 100 == 0) {
//                startX = start / 100 + ":" + "00";
//            } else {
//                startX = start / 100 + ":" + start % 100 + "";
//            }
//        } else {
//            if (start % 100 == 0) {
//                startX = (start / 100) - 12 + ":" + "00";
//            } else {
//                startX = (start / 100) - 12 + ":" + start % 100 + "";
//            }
//        }
//
//        if (end < 1300) {
//            if (end % 100 == 0) {
//                endX = end / 100 + ":" + "00";
//            } else {
//                endX = end / 100 + ":" + end % 100 + "";
//            }
//            if (end > 1200 && end < 1300) {
//                if (end % 100 == 0) {
//                    endX = end / 100 + ":" + "00";
//                } else {
//                    endX = start / 100 + ":" + end % 100 + "";
//                }
//            }
//        } else {
//            if (end % 100 == 0) {
//                endX = (end / 100) - 12 + ":" + "00";
//            } else {
//                endX = (end / 100) - 12 + ":" + end % 100 + "";
//            }
//        }

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
