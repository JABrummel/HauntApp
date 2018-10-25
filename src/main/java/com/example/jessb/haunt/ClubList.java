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
        Cursor data = db.getClubs(approval);
        ArrayList<String> clubData = new ArrayList<>();
        Log.i("clubview_looker", "entering populateClubs");


        while(data.moveToNext()){
            //get the value from column 1 (Event Name)
            //then add to the array list
            clubList.add(new Club(data.getString(1), data.getString(2),
                    data.getBlob(3),  data.getString(4), data.getString(5),data.getString(6),
                    data.getString(7),  data.getString(8), data.getString(9)
            ));
            Log.i("clubview_looker", "club col1 Id is:" + data.getString(1));
            Log.i("clubview_looker", "club col2 Id is:" + data.getString(2));
            Log.i("clubview_looker", "club col0 Id is:" + data.getString(0));
//            Log.i("clubview_looker", "club col3 Id is:" + data.getString(3));

            clubData.add(data.getString(2));
//
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clubData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clubName = parent.getItemAtPosition(position).toString();
                int index = position;
                Log.i("clubview_looker", "index " + index);
//
                Log.i("clubview_looker", "1. eventName: " + clubName);
                Cursor data = db.getClub(clubName);
//                int clubId = -1;
//                while(data.moveToNext()){
//                    clubId = data.getInt(1);
//                }
//                if(clubId > -1){

                Intent intent = new Intent(ClubList.this, ClubView.class);

                Club club = (Club)clubList.get(index);
                Log.i("clubview_looker", "3. event being sent: " + club.getClubName());

                for(int i= 0; i< clubList.size(); i++) {
                    Log.i("clubview_looker", clubList.get(i).getClubName());
                }
                intent.putExtra("club_object", club);
                intent.putExtra("userType", userType);
                intent.putExtra("userId", userId);
                intent.putExtra("eventId", index);
                startActivity(intent);


            }

        });
//        Log.d("Databasehelper", "populateEvents: Displaying data in view");
//        Cursor c = db.getClubs(approval);
//         Club chosenClub = new Club();
//
//        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clubList);
//        mListView.setAdapter(adapter);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int index = position;
//                Club chosenClub = new Club();
//
//                    Intent intent = new Intent(ClubList.this, ClubView.class);
//
//                    chosenClub = (Club)clubList.get(index);
//                    //Log.i("eventview_looker", "3. event being sent: " + mevent.getEventName());
//
//
//                    intent.putExtra("club_object", chosenClub);
//                    intent.putExtra("userType", userType);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("eventId", index);
//                    startActivity(intent);
//
//            }
//        });


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
