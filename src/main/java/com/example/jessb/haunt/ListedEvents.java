package com.example.jessb.haunt;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.Serializable;
import java.util.ArrayList;

import models.Events;
import sql.DatabaseHelper;

import static util.Constants.ERROR_DIALOG_REQUEST;
import static util.Constants.PERMISSION_REQUEST_ENABLE_GPS;
import static util.Constants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;

public class ListedEvents extends AppCompatActivity implements Serializable {

    DatabaseHelper db;
    Boolean filterOn = false;
    Events mevent = new Events();
    Intent lastActivity;
    ArrayList<Events> events = new ArrayList<Events>();
    private ListView mListView;
    int userId;
    String userType, startTime, endTime, startDate, endDate, categories, campus;
    Button marietta,kennesaw,listscreen,mapscreen;
    ImageView filter;
    private SensorManager sm;
    private float acelVal, lastAcel, movement; // CURRENT ACCELERATION VALUE AND GRAVITY
    private boolean mLocationPermissionGranted = false; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_events);
        mListView = findViewById(R.id.listView);
        marietta = (Button)findViewById(R.id.button_marietta);
        kennesaw = (Button)findViewById(R.id.button_kennesaw);
        listscreen = (Button)findViewById(R.id.button_listview);
        mapscreen = findViewById(R.id.button_map);
        listscreen.setBackgroundColor(Color.LTGRAY);
        filter= (ImageView)findViewById(R.id.button_filter);
        db = DatabaseHelper.getInstance(getApplicationContext());
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        FloatingActionButton logoutButton = findViewById(R.id.button_logout);
        lastActivity = getIntent();
        userType = lastActivity.getStringExtra("userType");
        userId = lastActivity.getIntExtra("userId", 0);
        if(lastActivity.hasExtra("filter")) {
            if(lastActivity.getBooleanExtra("filter", false) == true) {
                filterOn = true;
                startTime = lastActivity.getStringExtra("startTime");
                endTime = lastActivity.getStringExtra("endTime");
                startDate = lastActivity.getStringExtra("startDate");
                endDate = lastActivity.getStringExtra("endDate");
                categories = lastActivity.getStringExtra("categories");
                campus = lastActivity.getStringExtra("campus");
                filterEvents(startDate, endDate, startTime, endTime, categories, campus);
            }
            else populateEvents();
        }
        else{
            populateEvents();
        }


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mapscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapView();
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

        if(userType.equals("student") ) {
            moreButton.hide();
            addButton.hide();
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



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices())
        {
            if(!mLocationPermissionGranted){
                getLocationPermission();
            }
        }
    }

    protected void filterEvents(String dateStart, String dateEnd, String startTime, String endTime, String categories, String campus ) {
        Cursor data = db.filterEvents(dateStart, dateEnd, startTime, endTime, categories, campus);
        setListView(data);
    }

    public void openMapView() {
        Intent intent = new Intent(this, EventMapView.class);
        intent.putExtra("userType", userType);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    protected void createEvent(){
        Intent newEvent = new Intent(this, CreateEvent.class);
        newEvent.putExtra("userType", userType);
        newEvent.putExtra("userId", userId);
        startActivity(newEvent);
    }
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
//                    data.getString(6), data.getString(7),
//                    data.getBlob(8), data.getInt(9)
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
                    data.getString(6), data.getString(7),data.getBlob(8),
                    data.getInt(9), data.getDouble(10), data.getDouble(11)
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

                    if(filterOn) {

                            intent.putExtra("campus", campus);
                            intent.putExtra("filter", true);
                            intent.putExtra("endTime", endTime);
                            intent.putExtra("startTime", startTime);
                            intent.putExtra("endDate", endDate);

                            intent.putExtra("startDate", startDate);
                            intent.putExtra("categories", categories);

                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No ID associated with name", Toast.LENGTH_SHORT);

                }
            }
        });
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

    /*Google Maps Permissions Stuff
    * We need to ask the user for permissions to use GPS/Location because this is a democracy*/
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSION_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean checkMapServices(){
        if(isServicesOk()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*Request Location Permission*/
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(available == ConnectionResult.SUCCESS)
        {
            Log.d("Service Permission", "Google play service is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d("Service Permssion", "An error occurred but it can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch(requestCode){
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is canceled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Service Permission", "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSION_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                }
                else{
                    getLocationPermission();
                }
            }
        }
    }
}
