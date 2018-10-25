package com.example.jessb.haunt;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

public class ClubList extends AppCompatActivity {
    ListView mListView;
    Button goBack, clubs, requests;
    DatabaseHelper db;
    ArrayList<Club> clubList;
    Club mclub = new Club();
    String userType;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
        mListView = findViewById(R.id.listview_club);
        goBack = findViewById(R.id.button_goBack);
        clubs = findViewById(R.id.button_clubs);
        requests = findViewById(R.id.button_requests);
         clubList = new ArrayList<Club>();
        Intent lastActivity = getIntent();
         userType = lastActivity.getStringExtra("userType");
         userId = lastActivity.getIntExtra("userId", 0);
        clubs.setBackgroundColor(Color.LTGRAY);
        requests.getBackground().setAlpha(0);
        db = DatabaseHelper.getInstance(getApplicationContext());


        populateClubs("true");

    }


    //Used to sort between campus events after buttonclick
    private void populateClubs(String approval) {
        Log.d("Databasehelper", "populateEvents: Displaying data in view");
        Cursor c = db.getClubs(approval);
         Club chosenClub = new Club();

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clubList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                Club chosenClub = new Club();

                    Intent intent = new Intent(ClubList.this, ClubView.class);

                    chosenClub = (Club)clubList.get(index);
                    //Log.i("eventview_looker", "3. event being sent: " + mevent.getEventName());


                    intent.putExtra("club_object", chosenClub);
                    intent.putExtra("userType", userType);
                    intent.putExtra("userId", userId);
                    intent.putExtra("eventId", index);
                    startActivity(intent);

            }
        });
    }


    protected void changeScreen(View v) {
        int id = v.getId();
        Toast.makeText(this, "clicked button", Toast.LENGTH_SHORT);
        switch (id) {
            case R.id.button_requests: {
                Toast.makeText(this, "asdfas", Toast.LENGTH_SHORT);
                Intent i = new Intent(this, RequestList.class);
                i.putExtra("userType", userType);
                i.putExtra("userId", userId);
                startActivity(i);
                break;
            }

            case R.id.button_goback: {
                Intent i = new Intent(this, ListedEvents.class);
                i.putExtra("userType", userType);
                i.putExtra("userId", userId);
                startActivity(i);
                break;
            }
            default:
                Toast.makeText(this, "what the fuck", Toast.LENGTH_SHORT);
        }
    }
}
