package com.example.jessb.haunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import models.Events;

public class EventView extends AppCompatActivity {

    TextView eventName;
    TextView eventBio;
    TextView dateHolder;
    TextView location;
    TextView clubName;
    TextView time;
    ImageView clubPhoto;
    LinearLayout iconHolder;
    Button exitButton;
    Events mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        mEvent = (Events) getIntent().getSerializableExtra("event_object");
        eventName = (TextView)findViewById(R.id.tv_eventName);
        eventBio = (TextView)findViewById(R.id.tv_clubname);
        dateHolder = (TextView)findViewById(R.id.tv_date);
        time = (TextView)findViewById(R.id.tv_time);
        iconHolder = (LinearLayout) findViewById(R.id.icon_holder);
        clubPhoto = (ImageView)findViewById(R.id.iv_clubphoto);
        exitButton = (Button) findViewById(R.id.button_cancel);
        clubName = (TextView) findViewById(R.id.tv_clubname);
        location = (TextView) findViewById(R.id.tv_building);


        eventName.setText(mEvent.getEventName());
        eventBio.setText(mEvent.getBio());
        dateHolder.setText(mEvent.getDate());
        time.setText(mEvent.getStartTime() + " - " + mEvent.getEndTime());
        clubName.setText("Hosted by: " + mEvent.getEventName());
        location.setText(mEvent.getLocation());
        dateHolder.setText(mEvent.getDate());
        //clubPhoto.setImageURI(mEvent.getPhoto());



    }

    protected void goBack(View v) {
        Intent goBack = new Intent(this, ListedEvents.class);
        startActivity(goBack);
    }
}
