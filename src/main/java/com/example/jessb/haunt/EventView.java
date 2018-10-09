package com.example.jessb.haunt;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import models.Events;
import sql.DatabaseHelper;

public class EventView extends AppCompatActivity implements Serializable {

    TextView eventName;
    TextView eventBio;
    TextView dateHolder;
    TextView location;
    TextView clubName;
    TextView time;
    ImageView clubPhoto;
    LinearLayout iconHolder;
    Button exitButton;
    Button deleteButton;
    Events mEvent;
    String userType;
    String clubNameText;
    DatabaseHelper db;
    int userId;
    int eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        db = DatabaseHelper.getInstance(getApplicationContext());
        Intent lastActivity = getIntent();
        userType = lastActivity.getStringExtra("userType");
        userId = lastActivity.getIntExtra("userId", 0);
        mEvent = (Events) lastActivity.getSerializableExtra("event_object");
        eventId = mEvent.getClubID();
        Cursor data = db.getClubName(eventId);
        if(data.moveToFirst()){
            int position = data.getColumnIndex("clubName");
            clubNameText = data.getString(position);
        }

        Log.i("eventview_looker", mEvent.getEventName());
        eventName = (TextView)findViewById(R.id.tv_eventName);
        eventBio = (TextView)findViewById(R.id.tv_bio);
        dateHolder = (TextView)findViewById(R.id.tv_date);
        time = (TextView)findViewById(R.id.tv_time);
        iconHolder = (LinearLayout) findViewById(R.id.icon_holder);
        clubPhoto = (ImageView)findViewById(R.id.iv_clubphoto);
        exitButton = (Button) findViewById(R.id.button_cancel);
        clubName = (TextView) findViewById(R.id.tv_clubname);
        location = (TextView) findViewById(R.id.tv_building);
        deleteButton = findViewById(R.id.button_delete);
        deleteButton.setVisibility(View.VISIBLE);
        clubName.setText("Hosted by: " + clubNameText);
        Log.i("eventview_looker", "" + mEvent.getClubID());
        if (userId != mEvent.getClubID())
        {
            deleteButton.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.VISIBLE);
        }

        eventName.setText(mEvent.getEventName());
        eventBio.setText("Event Description: " +mEvent.getBio());
        dateHolder.setText(mEvent.getDate());
        time.setText(mEvent.getStartTime() + " - " + mEvent.getEndTime());
        //clubName.setText("Hosted by: " + mEvent.getEventName());
        location.setText("Location: " +mEvent.getLocation());
        dateHolder.setText(mEvent.getDate());
        clubPhoto.setImageBitmap(convertToBitmap(mEvent.getPhoto()));
        Log.i("eventview_looker", "" + mEvent.getPhoto());
    }
    private Bitmap convertToBitmap(byte[] b){
        if (b != null)
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        else return null;
    }

    protected void goBack(View v) {
        Intent goBack = new Intent(this, ListedEvents.class);
        goBack.putExtra("userType", userType);
        goBack.putExtra("userId", userId);

        startActivity(goBack);
    }
    protected void deleteEvent(View v)
    {
        int clubid = mEvent.getClubID();
        Intent goBack = new Intent(this, ListedEvents.class);
        goBack.putExtra("userType", userType);
        goBack.putExtra("userId", userId);
        db.deleteEvent(clubid, mEvent.getEventName());

        startActivity(goBack);

    }
}
