package com.example.jessb.haunt;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
    Events mevent = new Events();
    ArrayList<Events> events = new ArrayList<Events>();
    private ListView mListView;
    int userId;
    String userType;
    private boolean mLocationPermissionGranted = false; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_events);
        mListView = findViewById(R.id.listView);
        db = DatabaseHelper.getInstance(getApplicationContext());
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        FloatingActionButton mapView = findViewById(R.id.map_btn);
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
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapView();
            }
        });

        if(userType.equals("student") || userType.equals("admin")) {

            addButton.hide();
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

    public void openMapView() {
        Intent intent = new Intent(this, EventMapView.class);
        intent.putExtra("userType", "club");
        intent.putExtra("userId", userId);
        startActivity(intent);
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
