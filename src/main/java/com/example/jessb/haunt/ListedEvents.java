package com.example.jessb.haunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import models.Club;
import models.Events;
import sql.DatabaseHelper;

public class ListedEvents extends AppCompatActivity implements Serializable {

    DatabaseHelper db;
    Events mevent = new Events();
    ArrayList<Events> events = new ArrayList<Events>();
    private ListView mListView;
    int userId;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_events);
        Toast.makeText(getApplicationContext(), "ONCREATE CALLED", Toast.LENGTH_SHORT).show();
        mListView = findViewById(R.id.listView);
        db = DatabaseHelper.getInstance(getApplicationContext());
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        Intent lastActivity = getIntent();
         userType = lastActivity.getStringExtra("userType");
         userId = lastActivity.getIntExtra("userId", 0);
        populateEvents();

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logout();
            }
        });

        if(userType.equals("student")) {

            addButton.hide();
            moreButton.hide();
        }

        if(userType.equals("club")) {
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
        newEvent.putExtra("userType", "club");
        newEvent.putExtra("userId", userId);
        startActivity(newEvent);
    }
    private void populateEvents() {
        Log.d("Databasehelper", "populateEvents: Displaying data in view");
        Cursor data = db.getEvents();
        ArrayList<String> eventData = new ArrayList<>();
        Events tmp = new Events();
        while(data.moveToNext()){
            //get the value from column 1 (Event Name)
            //then add to the array list
            events.add(new Events(data.getString(1), data.getString(2),
                    data.getString(3), data.getString(4),data.getString(5),
                    data.getString(6), data.getBlob(7), data.getInt(8)
                    ));
            eventData.add(data.getString(1));
//
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = parent.getItemAtPosition(position).toString();
                int index = position;
//
                Log.i("eventview_looker", "1. eventName: " + eventName);
                Cursor data = db.getEvent(eventName);
                int eventId = -1;
                while(data.moveToNext()){
                    eventId = data.getInt(0);
                }
                if(eventId > -1){
//                    Log.d("Listed Events", "ID is : " + eventId);
                    Log.i("eventview_looker", "2. Id is:" + eventId);
                    Intent intent = new Intent(ListedEvents.this, EventView.class);
//                    intent.putExtra("id", eventId);
//                    intent.putExtra("eventName", eventName);
                    mevent = (Events)events.get(index);
                    Log.i("eventview_looker", "3. event being sent: " + mevent.getEventName());

                    for(int i= 0; i< events.size(); i++) {
                        Log.i("eventview_looker", events.get(i).getEventName());
                    }
                    intent.putExtra("event_object", mevent);
                    intent.putExtra("userType", userType);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No ID associated with name", Toast.LENGTH_SHORT);

                }
            }
        });
    }

    protected void logout() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Want to Logout?")
                .setMessage("Logging out will return you to the usertype screen")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ListedEvents.this, UserType.class);
                        startActivity(i);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }

        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
