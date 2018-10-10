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
import java.util.ArrayList;

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
    ArrayList<Integer> categoryIds;
    int event_Id;
    int userId;
    int eventId;
    final int CAT_COMPETITION =1;
    final int CAT_FOOD =2;
    final int CAT_ATHLETICS=3;
    final int CAT_ACADEMICS=4;
    final int CAT_ENTERTAINMENT=5;
    final int CAT_LIVEPERFORMANCE=6;
    final int CAT_STEAM=7;
    final int CAT_LITART=8;
    final int CAT_GIVEAWAYS=9;
    final int CAT_MUSIC=10;
    final int CAT_BOARDGAMES=11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        db = DatabaseHelper.getInstance(getApplicationContext());
        Intent lastActivity = getIntent();
        userType = lastActivity.getStringExtra("userType");
        userId = lastActivity.getIntExtra("userId", 0);
        mEvent = (Events) lastActivity.getSerializableExtra("event_object");
        event_Id = lastActivity.getIntExtra("eventId", 0);
        Log.i("eventview_looker", "eventId" + event_Id);
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
        setEventCategories();
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

    protected void setEventCategories() {
        categoryIds = db.getEventCategories(eventId);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);

        for(int i=0; i<categoryIds.size(); i++) {

            switch(categoryIds.get(i)) {
                case CAT_COMPETITION : {
                    ImageView tv1 = new ImageView (this);
                    tv1.setImageResource(R.drawable.cat_trophy_checked);
                    tv1.setLayoutParams(lp);
                    iconHolder.addView(tv1);
                    break;
                }
                case CAT_FOOD : {
                    ImageView tv2 = new ImageView (this);
                    tv2.setImageResource(R.drawable.cat_food_checked);
                    tv2.setLayoutParams(lp);
                    iconHolder.addView(tv2);
                    break;
                }
                case CAT_ATHLETICS : {
                    ImageView tv3 = new ImageView (this);
                    tv3.setImageResource(R.drawable.cat_sports_checked);
                    tv3.setLayoutParams(lp);
                    iconHolder.addView(tv3);
                    break;
                }
                case CAT_ACADEMICS : {
                    ImageView tv4 = new ImageView (this);
                    tv4.setImageResource(R.drawable.cat_academics_checked);
                    tv4.setLayoutParams(lp);
                    iconHolder.addView(tv4);
                    break;
                }
                case CAT_ENTERTAINMENT : {
                    ImageView tv5 = new ImageView (this);
                    tv5.setImageResource(R.drawable.cat_entertainment_checked);
                    tv5.setLayoutParams(lp);
                    iconHolder.addView(tv5);
                    break;
                }
                case CAT_LIVEPERFORMANCE : {
                    ImageView tv6 = new ImageView (this);
                    tv6.setImageResource(R.drawable.cat_liveshow_checked);
                    tv6.setLayoutParams(lp);
                    iconHolder.addView(tv6);
                    break;
                }
                case CAT_STEAM : {
                    ImageView tv7 = new ImageView (this);
                    tv7.setImageResource(R.drawable.cat_stem_checked);
                    tv7.setLayoutParams(lp);
                    iconHolder.addView(tv7);
                    break;
                }
                case CAT_LITART : {
                    ImageView tv8 = new ImageView (this);
                    tv8.setImageResource(R.drawable.cat_art_checked);
                    tv8.setLayoutParams(lp);
                    iconHolder.addView(tv8);
                    break;
                }
                case CAT_GIVEAWAYS : {
                    ImageView tv9 = new ImageView (this);
                    tv9.setImageResource(R.drawable.cat_giveaway_checked);
                    tv9.setLayoutParams(lp);
                    iconHolder.addView(tv9);
                    break;
                }
                case CAT_MUSIC : {
                    ImageView tv10 = new ImageView (this);
                    tv10.setImageResource(R.drawable.cat_music_checked);
                    iconHolder.addView(tv10);
                    break;
                }
                case CAT_BOARDGAMES : {
                    ImageView tv11 = new ImageView (this);
                    tv11.setImageResource(R.drawable.cat_game_checked);
                    iconHolder.addView(tv11);
                    break;
                }
            }

        }
    }
}
