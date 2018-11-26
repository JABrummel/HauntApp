package com.example.jessb.haunt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.ImageView;
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
    Intent lastActivity;
    String userType, startTime, endTime, startDate, endDate, categories, campus;
    Button marietta,kennesaw,listscreen,mapscreen;
    ImageView filter;
    private SensorManager sm;
    private float acelVal, lastAcel, movement; // CURRENT ACCELERATION VALUE AND GRAVITY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_events);
        mListView = findViewById(R.id.listView);
        marietta = (Button)findViewById(R.id.button_marietta);
        kennesaw = (Button)findViewById(R.id.button_kennesaw);
        listscreen = (Button)findViewById(R.id.button_listview);
        listscreen.setBackgroundColor(Color.LTGRAY);
        filter =  (ImageView) findViewById(R.id.button_filter);
        db = DatabaseHelper.getInstance(getApplicationContext());
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        FloatingActionButton logoutButton = findViewById(R.id.button_logout);
        lastActivity = getIntent();
         userType = lastActivity.getStringExtra("userType");
         userId = lastActivity.getIntExtra("userId", 0);
         if(lastActivity.hasExtra("filter")) {
             if(lastActivity.getBooleanExtra("filter", false)) {
                 startTime = lastActivity.getStringExtra("startTime");
                 endTime = lastActivity.getStringExtra("endTime");
                 startDate = lastActivity.getStringExtra("startDate");
                 endDate = lastActivity.getStringExtra("endDate");
                 categories = lastActivity.getStringExtra("categories");
                 campus = lastActivity.getStringExtra("campus");
                 filterEvents(startDate, endDate, startTime, endTime, categories, campus);
             }
         }
         else{
             populateEvents();
         }


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal=SensorManager.GRAVITY_EARTH;
        lastAcel = SensorManager.GRAVITY_EARTH;
        movement = 0.00f;

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Filter.class);
                i.putExtra("userType", userType);
                i.putExtra("userId", userId);
                startActivity(i);
            }
        });


        if(userType.equals("student")) {
            moreButton.hide();
            addButton.hide();
        }

        if(userType.equals("admin")){
            addButton.hide();
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ListedEvents.this, ClubList.class);
                    i.putExtra("userId", userId);
                    i.putExtra("userType", userType);
                    startActivity(i);
                }
            });
        }

        if(userType.equals("club")) {
            moreButton.hide();
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

    protected void filterEvents(String dateStart, String dateEnd, String startTime, String endTime, String categories, String campus ) {
        Cursor data = db.filterEvents(dateStart, dateEnd, startTime, endTime, categories, campus);
        setListView(data);
    }

    //Used to sort between campus events after buttonclick
    private void populateEvents(String campus) {
        Log.d("Databasehelper", "populateEvents: Displaying data in view");
        Cursor data = db.getEvents(campus);
        setListView(data);
    }

    private void populateEvents() {
        Log.d("Databasehelper", "populateEvents: Displaying data in view");
        Cursor data = db.getEvents();
        setListView(data);
//        ArrayList<String> eventData = new ArrayList<>();
//        Events tmp = new Events();
//        while(data.moveToNext()){
//            //get the value from column 1 (Event Name)
//            //then add to the array list
//            events.add(new Events(data.getString(1), data.getString(2),
//                    data.getString(3), data.getString(4),data.getString(5),
//                    data.getString(6), data.getString(7),data.getBlob(8), data.getInt(9)
//                    ));
//            eventData.add(data.getString(1));
////
//        }
//        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventData);
//        mListView.setAdapter(adapter);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String eventName = parent.getItemAtPosition(position).toString();
//                int index = position;
////
//                Log.i("eventview_looker", "1. eventName: " + eventName);
//                Cursor data = db.getEvent(eventName);
//                int eventId = -1;
//                while(data.moveToNext()){
//                    eventId = data.getInt(0);
//                }
//                if(eventId > -1){
//                    Log.i("eventview_looker", "2. Id is:" + eventId);
//                    Intent intent = new Intent(ListedEvents.this, EventView.class);
//
//                    mevent = (Events)events.get(index);
//                    Log.i("eventview_looker", "3. event being sent: " + mevent.getEventName());
//
//                    for(int i= 0; i< events.size(); i++) {
//                        Log.i("eventview_looker", events.get(i).getEventName());
//                    }
//                    intent.putExtra("event_object", mevent);
//                    intent.putExtra("userType", userType);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("eventId", index);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "No ID associated with name", Toast.LENGTH_SHORT);
//
//                }
//            }
//        });
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float xCor = sensorEvent.values[0];
            float yCor = sensorEvent.values[1];
            float zCor = sensorEvent.values[2];

            lastAcel = acelVal;
            acelVal = (float) Math.sqrt((double) (xCor*xCor + yCor*yCor + zCor*zCor));
            float change = acelVal - lastAcel;
            movement = movement * 0.9f + change;
            if ( movement > 12) {
                populateEvents();
                Toast toast = Toast.makeText(getApplicationContext(), "Filter has been cleared", Toast.LENGTH_SHORT);
                toast.show();

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    protected void setListView (Cursor data) {
        ArrayList<String> eventData = new ArrayList<>();
        Events tmp = new Events();
        while(data.moveToNext()){
            //get the value from column 1 (Event Name)
            //then add to the array list
            events.add(new Events(data.getString(1), data.getString(2),
                    data.getString(3), data.getString(4),data.getString(5),
                    data.getString(6), data.getString(7),data.getBlob(8), data.getInt(9)
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
                    Log.i("eventview_looker", "2. Id is:" + eventId);
                    Intent intent = new Intent(ListedEvents.this, EventView.class);

                    mevent = (Events)events.get(index);
                    Log.i("eventview_looker", "3. event being sent: " + mevent.getEventName());

                    for(int i= 0; i< events.size(); i++) {
                        Log.i("eventview_looker", events.get(i).getEventName());
                    }
                    intent.putExtra("event_object", mevent);
                    intent.putExtra("userType", userType);
                    intent.putExtra("userId", userId);
                    intent.putExtra("eventId", index);

                    if(lastActivity.hasExtra("filter")) {
                        intent.putExtra("campus", campus);
                        intent.putExtra("filter", true);
                        intent.putExtra("endTime", endTime);
                        intent.putExtra("startTime", startTime);
                        intent.putExtra("endDate", endDate);
                        intent.putExtra("startDate", startDate);

                    }
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

    protected void pickcampus(View v) {
        int id = v.getId();
        if(id == R.id.button_marietta) {
            populateEvents("marietta");
            marietta.setBackgroundColor(Color.LTGRAY);
            kennesaw.getBackground().setAlpha(0);
        }
        else {
            populateEvents("kennesaw");
            kennesaw.setBackgroundColor(Color.LTGRAY);
            marietta.getBackground().setAlpha(0);
        }

    }

    protected void pickview(View v) {}

    protected void goToFilter(View v) {
        Intent i = new Intent(this, Filter.class);
        i.putExtra("userType", userType);
        i.putExtra("userId", userId);
        startActivity(i);
    }


}
