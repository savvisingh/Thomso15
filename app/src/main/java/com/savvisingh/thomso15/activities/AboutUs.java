package com.savvisingh.thomso15.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.savvisingh.thomso15.R;

public class AboutUs extends AppCompatActivity implements View.OnClickListener{


    private ImageView githubSavvi, facebookSavvi, behaceRuchil, facebookRuchil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        githubSavvi = (ImageView) findViewById(R.id.github_savvi);
        facebookSavvi = (ImageView) findViewById(R.id.facebook_savvi);
        behaceRuchil = (ImageView) findViewById(R.id.behance_ruchil);
        facebookRuchil = (ImageView) findViewById(R.id.facebook_ruchil);

        githubSavvi.setOnClickListener(this);
        facebookRuchil.setOnClickListener(this);
        facebookSavvi.setOnClickListener(this);
        behaceRuchil.setOnClickListener(this);


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
    public void onClick(View view) {

        int id = view.getId();

        String uri = null;

        switch (id){
            case R.id.github_savvi:

                uri = "https://github.com/savvisingh";
                break;
            case R.id.facebook_savvi:
                uri = "https://www.facebook.com/savvi.singh";
                break;
            case R.id.facebook_ruchil:

                uri = "https://www.facebook.com/r9kiitr";
                break;

            case R.id.behance_ruchil:
                uri = "https://www.behance.net/r9k";
                break;
        }

        Intent playIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(uri));
        startActivity(playIntent);

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
