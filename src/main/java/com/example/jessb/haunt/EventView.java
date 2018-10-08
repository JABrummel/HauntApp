package com.example.jessb.haunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import models.Events;

public class EventView extends AppCompatActivity {

    Events mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

    }
}
