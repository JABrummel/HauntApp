package com.example.jessb.haunt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Events;
import models.Location;
import sql.DatabaseHelper;

import static util.Constants.MAPVIEW_BUNDLE_KEY;

public class EventMapView extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private MapView mMapView;
    GoogleMap mMap;
    DatabaseHelper db;
    Button mariettaBtn, kennesawBtn;
    LatLng marietta = new LatLng(33.9376219, -84.52017189999998); //Atrium Building Coords
    LatLng kennesaw = new LatLng(34.0381707, -84.58174450000001); //Kennesaw Campus Green Coords
    float zoom = 16.0f; //Determines the zoom on the MapView. The value goes from 2 - 21 (2 being super zoomed out like God, where 21 is able to see the freckles on a redhead child)
    String userType;
    int userId;
    Intent lastActivity;
    Events mevent = new Events();
    ArrayList<Events> events = new ArrayList<Events>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        mMapView = findViewById(R.id.map_view);
        lastActivity = getIntent();
        db = DatabaseHelper.getInstance(getApplicationContext());
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        mariettaBtn = findViewById(R.id.marietta_btn);
        kennesawBtn = findViewById(R.id.kennesaw_btn);


        userType = lastActivity.getStringExtra("userType");
        userId = lastActivity.getIntExtra("userId", 0);

        initGoogleMap(savedInstanceState);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        if (userType.equals("student") || userType.equals("admin")) {
            addButton.hide();
        }

        if (userType.equals("club")) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEvent();
                }
            });
        }

        mariettaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(marietta, zoom);
                mMap.animateCamera(location);
            }
        });

        kennesawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(kennesaw, zoom);
                mMap.animateCamera(location);
            }
        });

    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // EventMapView requires that the Bundle you pass contain _ONLY_ EventMapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        populateEvents();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        addMarkersToMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marietta, zoom));
        mMap.setOnInfoWindowClickListener(this);

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    protected void logout() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Want to Logout?")
                .setMessage("Logging out will return you to the User Selection screen")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(EventMapView.this, UserType.class);
                        startActivity(i);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }

        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    protected void createEvent() {
        Intent newEvent = new Intent(this, CreateEvent.class);
        newEvent.putExtra("userType", "club");
        newEvent.putExtra("userId", userId);
        startActivity(newEvent);
    }

    private void populateEvents() {
        Log.d("DatabaseHelper", "populateevents: Display Data On Map");
        Cursor eventCursor = db.getEvents();
        Events tmp = new Events();
        if (eventCursor != null && eventCursor.getCount() > 0) {
            while (eventCursor.moveToNext()) {
                events.add(new Events(eventCursor.getString(1), eventCursor.getString(2),
                        eventCursor.getString(3), eventCursor.getString(4), eventCursor.getString(5),
                        eventCursor.getString(6), eventCursor.getString(7), eventCursor.getBlob(8), eventCursor.getInt(9),
                        eventCursor.getDouble(11), eventCursor.getDouble(12)
                ));
            }
        }
    }

    private void addMarkersToMap() {
        for (int i = 0; i < events.size(); i++) {
            LatLng EVENT = new LatLng(events.get(i).getLat(), events.get(i).getLong());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(EVENT)
                    .title(events.get(i).getEventName())
                    .snippet(events.get(i).getBio()));
        }

    }

    public void onInfoWindowClick(Marker marker) {

        String markerName = marker.getTitle();
        Cursor data = db.getEvent(markerName);
        int eventId = -1;
        while (data.moveToNext()) {
            eventId = data.getInt(0);
        }
        if (eventId > -1) {
            Intent intent = new Intent(EventMapView.this, EventView.class);

            intent.putExtra("event_object", events.get(eventId - 1));
            intent.putExtra("userType", userType);
            intent.putExtra("userId", userId);
            intent.putExtra("eventId", eventId -1);
            intent.putExtra("campus", events.get(eventId - 1).getCampus());
            intent.putExtra("endTime", events.get(eventId - 1).getEndTime());
            intent.putExtra("startTime", events.get(eventId - 1).getStartTime());
            intent.putExtra("endDate", events.get(eventId - 1).getDate());
            intent.putExtra("startDate", events.get(eventId - 1).getDate());

            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "No ID associated with name", Toast.LENGTH_SHORT);


        }
    }
}
