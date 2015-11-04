package com.savvisingh.thomso15.adapters;

/**
 * Created by akshay on 8/26/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.activities.EventDescriptionActivity;
import com.savvisingh.thomso15.utils.DatabaseHelper;
import com.savvisingh.thomso15.utils.StartService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Gowtham Chandrasekar on 31-07-2015.
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.MyViewHolder> {


    private DatabaseHelper myDbHelper;
    private Context context;
    private String type;
    private List<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();


    public EventsRecyclerViewAdapter(List<HashMap<String, String>> evlist, String type, Context context) {
        this.eventList = evlist;
        this.context = context;
        this.type = type;

        // TODO Auto-generated constructor stub
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclingTest", "onCreateViewHolder method is called");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.events_list_card_view, parent, false);

        myDbHelper = new DatabaseHelper(context);
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

        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("RecyclingTest", "onBindViewHolder method is called");

        HashMap<String, String> hm = new HashMap<String, String>();
        hm=eventList.get(position);

        holder.eventname.setText(hm.get("eventname"));
        holder.eventtime.setText(hm.get("eventtime"));
        holder.eventvenue.setText(hm.get("eventvenue"));
        if(type.equals("Day")) {
            holder.eventtag.setText(hm.get("eventtag"));
        }
        else{
            holder.eventtag.setText(hm.get("eventdate")+" Oct");
        }


        if (myDbHelper.isFavourite(hm.get("eventname"))) {

           holder.bookmark.setChecked(true);
        } else {

           holder.bookmark.setChecked(false);
        }

    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView eventname,eventtag,eventtime,eventvenue;

        private CheckBox bookmark;

        public MyViewHolder(View itemView) {
            super(itemView);
            //implementing onClickListener
            itemView.setOnClickListener(this);


            eventname = (TextView) itemView.findViewById(R.id.event_name);
            eventtime = (TextView) itemView.findViewById(R.id.event_time);
            eventvenue = (TextView) itemView.findViewById(R.id.event_venue);
            eventtag = (TextView) itemView.findViewById(R.id.event_tag);
            bookmark = (CheckBox) itemView.findViewById(R.id.checkbox);


            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!bookmark.isChecked()) {
                        myDbHelper.unmarkAsFavourite(eventList.get(getAdapterPosition()).get("eventname"));

                    } else {
                        myDbHelper.markAsFavourite(eventList.get(getAdapterPosition()).get("eventname"));

                        Intent  service_intent=new Intent(context,StartService.class);
                        context.startService(service_intent);
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {
            //Every time you click on the row toast is displayed


            int pos = getAdapterPosition();


                    Intent intent = new Intent(context, EventDescriptionActivity.class);
                    Bundle data = new Bundle();
                    data.putString("event", eventList.get(pos).get("eventname"));
                    data.putString("day", eventList.get(pos).get("eventdate"));
                    data.putString("type",type);
                    intent.putExtras(data);
                    context.startActivity(intent);





        }
    }
}