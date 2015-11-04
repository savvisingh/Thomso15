package com.savvisingh.thomso15.activities;

import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.savvisingh.thomso15.R;
import com.savvisingh.thomso15.adapters.ContactsRecyclerViewAdapter;
import com.savvisingh.thomso15.adapters.EventsRecyclerViewAdapter;
import com.savvisingh.thomso15.utils.ContactClass;
import com.savvisingh.thomso15.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamContacts extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private ContactsRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;



    private String name = "";
    private String number = "";
    private String email = "";
    private String post = "";
    ListView lv;
    ArrayList<String> contacts_name;
    ArrayList<String> contacts_number;
    ArrayList<String> contacts_email;
    ArrayList<String> contacts_post;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(layoutManager);


        List<ContactClass> contactList = new ArrayList<ContactClass>();

        DatabaseHelper myDbHelper = new DatabaseHelper(TeamContacts.this);
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
        contacts_name = myDbHelper.getcontactsname();
        contacts_number = myDbHelper.getcontactsnumber();
        contacts_email = myDbHelper.getcontactsemail();
        contacts_post = myDbHelper.getcontactspost();

        int i = 0;
        while (i < contacts_name.size()) {
            ContactClass addContact = new ContactClass();
            addContact.setName(contacts_name.get(i));
            addContact.setNumber(contacts_number.get(i));
            addContact.setEmail(contacts_email.get(i));
            addContact.setPost(contacts_post.get(i));
            contactList.add(addContact);
            i++;
        }


        mAdapter = new ContactsRecyclerViewAdapter(contactList, this);
        recyclerView.setAdapter(mAdapter);

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

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
