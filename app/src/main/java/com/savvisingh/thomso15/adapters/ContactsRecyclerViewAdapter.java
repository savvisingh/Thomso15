package com.savvisingh.thomso15.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.activities.EventDescriptionActivity;
import com.savvisingh.thomso15.utils.ContactClass;
import com.savvisingh.thomso15.utils.DatabaseHelper;
import com.savvisingh.thomso15.utils.StartService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Savvi Singh on 10/7/2015.
 */
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.MyViewHolder> {


    private DatabaseHelper myDbHelper;
    private Context context;
    private String type;
    private List<ContactClass> eventList = new ArrayList<ContactClass>();


    public ContactsRecyclerViewAdapter(List<ContactClass> evlist, Context context) {
        this.eventList = evlist;
        this.context = context;


        // TODO Auto-generated constructor stub
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclingTest", "onCreateViewHolder method is called");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_contact_recycler_view, parent, false);


        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("RecyclingTest", "onBindViewHolder method is called");

        ContactClass contact = eventList.get(position);
        holder.contactnumber.setText(contact.getNumber());
        holder.contactname.setText(contact.getName());
        holder.contactpost.setText(contact.getPost());


    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView contactname, contactpost, contactnumber;
         private ImageView contactlink;

        private CheckBox bookmark;

        public MyViewHolder(View itemView) {
            super(itemView);
            //implementing onClickListener
            itemView.setOnClickListener(this);


            contactname = (TextView) itemView.findViewById(R.id.contact_name);
            contactpost = (TextView) itemView.findViewById(R.id.contact_post);
            contactnumber = (TextView) itemView.findViewById(R.id.contact_number);
            contactlink = (ImageView) itemView.findViewById(R.id.contact_link);


            contactlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent fbIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(eventList.get(getAdapterPosition()).getEmail()));
                    context.startActivity(fbIntent);
                }
            });

        }

        @Override
        public void onClick(View view) {
            //Every time you click on the row toast is displayed

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + eventList.get(getAdapterPosition()).getNumber() ));
            context.startActivity(callIntent);

        }
    }
}