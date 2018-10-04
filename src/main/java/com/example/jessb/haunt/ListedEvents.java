package com.example.jessb.haunt;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListedEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_events);
        FloatingActionButton moreButton = findViewById(R.id.button_more);
        FloatingActionButton addButton = findViewById(R.id.button_add);
        Intent lastActivity = getIntent();
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
}
