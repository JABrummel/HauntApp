package com.example.jessb.haunt;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import models.Club;
import models.Events;
import sql.DatabaseHelper;

public class ListedEvents extends AppCompatActivity {

    DatabaseHelper db;
    Events mevent = new Events();
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_events);
        mListView = findViewById(R.id.listView);
        db = DatabaseHelper.getInstance(getApplicationContext());
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        Intent lastActivity = getIntent();
        String usertype = lastActivity.getStringExtra("user");
        populateEvents();

        if(usertype.equals("student")) {

            addButton.hide();
            moreButton.hide();
        }

        if(usertype.equals("club")) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEvent();
                }
            });

        }


    }

    protected void createEvent(){
        Intent newEvent = new Intent(this, CreateEvent.class);
        startActivity(newEvent);
    }
    private void populateEvents() {
        Log.d("Databasehelper", "populateEvents: Displaying data in view");
        Cursor data = db.getEvents();
        ArrayList<String> eventData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from column 1 (Event Name)
            //then add to the array list
            eventData.add(data.getString(1));
            mevent.setEventName(data.getString(1));
            mevent.setLocation(data.getString(2));
            mevent.setStartTime(data.getString(3));
            mevent.setEndTime(data.getString(4));
            mevent.setDate(data.getString(5));
            mevent.setBio(data.getString(6));
            //mevent.setPhoto(data.getString(7));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = parent.getItemAtPosition(position).toString();
                Log.d("Listed Events", "onItemClick: You clicked on " + eventName);

                Cursor data = db.getEvent(eventName);
                int eventId = -1;
                while(data.moveToNext()){
                    eventId = data.getInt(0);
                }
                if(eventId > -1){
                    Log.d("Listed Events", "ID is : " + eventId);
                    Intent intent = new Intent(ListedEvents.this, EventView.class);
//                    intent.putExtra("id", eventId);
//                    intent.putExtra("eventName", eventName);
                    mevent.setEventID(eventId);
                    intent.putExtra("event_object", mevent);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No ID associated with name", Toast.LENGTH_SHORT);

                }
            }
        });
    }
}
