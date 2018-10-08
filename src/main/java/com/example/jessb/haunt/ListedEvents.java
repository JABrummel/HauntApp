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
import sql.DatabaseHelper;

public class ListedEvents extends AppCompatActivity {

    DatabaseHelper db;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_events);
        mListView = findViewById(R.id.listView);
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        Intent lastActivity = getIntent();
        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        String usertype = lastActivity.getStringExtra("user");


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
        }
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listed_events, eventData);
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
                    intent.putExtra("id", eventId);
                    intent.putExtra("eventName", eventName);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No ID associated with name", Toast.LENGTH_SHORT);

                }
            }
        });
    }
}
