package com.example.jessb.haunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
    String userType, startTime, endTime, startDate, endDate, categoryList, campus;
    ImageView clubPhoto;
    LinearLayout iconHolder;
    Button exitButton;
    Button deleteButton;
    Events mEvent;
    String clubNameText;
    DatabaseHelper db;
    int event_Id;
    int userId;
    int eventId;
    String mainCategory;
    String categories;
    final String CAT_COMPETITION ="a";
    final String CAT_FOOD ="b";
    final String CAT_ATHLETICS="c";
    final String CAT_ACADEMICS="d";
    final String CAT_ENTERTAINMENT="e";
    final String CAT_LIVEPERFORMANCE="f";
    final String CAT_STEAM="g";
    final String CAT_LITART="h";
    final String CAT_GIVEAWAYS="i";
    final String CAT_MUSIC="j";
    final String CAT_BOARDGAMES="k";
    Intent lastActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        db = DatabaseHelper.getInstance(getApplicationContext());
        lastActivity = getIntent();
        userType = lastActivity.getStringExtra("userType");
        userId = lastActivity.getIntExtra("userId", 0);
        mEvent = (Events) lastActivity.getSerializableExtra("event_object");
        event_Id = lastActivity.getIntExtra("eventId", 0);
        Log.i("eventview_looker", "eventId" + event_Id);
        eventId = mEvent.getClubID();

        if(lastActivity.hasExtra("filter")) {
            startTime = lastActivity.getStringExtra("startTime");
            endTime = lastActivity.getStringExtra("endTime");
            startDate = lastActivity.getStringExtra("startDate");
            endDate = lastActivity.getStringExtra("endDate");
            categoryList = lastActivity.getStringExtra("categories");
            campus = lastActivity.getStringExtra("campus");

        }
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
        String startTime = mEvent.getStartTime().substring(0,2) + ":" + mEvent.getStartTime().substring(2);
        String endTime = mEvent.getEndTime().substring(0,2) + ":" + mEvent.getEndTime().substring(2);
        time.setText(startTime + " - " + endTime);
        location.setText("Location: " +mEvent.getLocation());
        String eventDate = mEvent.getDate();
        String formattedDate = eventDate.substring(4,6) + "/"+ eventDate.substring(6) +"/" + eventDate.substring(0,4);
        dateHolder.setText(formattedDate);
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
        goBack.putExtra("campus", campus);
        goBack.putExtra("filter", true);
        goBack.putExtra("endTime", endTime);
        goBack.putExtra("startTime", startTime);
        goBack.putExtra("endDate", endDate);
        goBack.putExtra("startDate", startDate);

        startActivity(goBack);
    }
    protected void deleteEvent(View v)
    {

        final Intent goBack = new Intent(this, ListedEvents.class);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Are You Sure?")
                .setMessage("You cannot retrieve the event after deleting.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        goBack.putExtra("userType", userType);
                        goBack.putExtra("userId", userId);

                        int clubid = mEvent.getClubID();
                        db.deleteEvent(clubid, mEvent.getEventName());
                        startActivity(goBack);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();




    }

    protected void setEventCategories() {
        categories = db.getCategories(mEvent.getEventName(), mEvent.getClubID());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);

        for(int i=0; i<categories.length(); i++) {

            switch(categories.substring(i, i+1)) {
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
