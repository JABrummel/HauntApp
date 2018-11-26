package com.example.jessb.haunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import models.Club;
import sql.DatabaseHelper;

public class ClubView extends AppCompatActivity {
    TextView clubName, clubBio, faculty;
    Button approveButton, deleteButton;
    ImageView profilepic;
    Club mclub;
    DatabaseHelper db;
    String userType;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_view);
        clubName = findViewById(R.id.tv_clubname);
        clubBio = findViewById(R.id.tv_bio);
        faculty = findViewById(R.id.tv_faculty);
        approveButton = findViewById(R.id.button_approve);
        deleteButton = findViewById(R.id.button_delete);
        profilepic = findViewById(R.id.iv_profileimg);
        db = DatabaseHelper.getInstance(getApplicationContext());
        Intent i  = getIntent();
        userType = i.getStringExtra("userType");
        userId = i.getIntExtra("userId", 0);
        mclub = (Club)i.getSerializableExtra("club_object");

        clubName.setText("Club Name: "+mclub.getClubName());
        clubBio.setText( mclub.getBio());
        faculty.setText(mclub.getFacultyEmail());
        Log.i("clubview_looker", "" + mclub.getPhoto());
        profilepic.setImageBitmap(convertToBitmap(mclub.getPhoto()));

        if(mclub.getApproved().equals("true")) {
            approveButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClub();
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveClub();
            }
        });

    }

    private Bitmap convertToBitmap(byte[] b){
        if (b != null)
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        else return null;
    }

    protected void deleteClub() {
        AlertDialog.Builder builder;
        final Intent i = new Intent(this, ClubList.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Are You Sure?")
                .setMessage("Deleting a Club is permanent")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        String clubName = mclub.getClubName();
                        db.deleteClub(clubName);
                        startActivity(i);


                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    protected void approveClub() {
        AlertDialog.Builder builder;
        final Intent i = new Intent(this, RequestList.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Approve the Club")
                .setMessage("Approving a club allows the club to create events that students and other clubs can see.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        mclub.setApproved("true");
                        db.updateApproval(mclub);
                        startActivity(i);

                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        mclub.setApproved("false");
                        db.updateApproval(mclub);
                         startActivity(i);
            }
        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    protected void goBack(View v) {
        Intent go = new Intent(this, ClubList.class);
        go.putExtra("userType", userType);
        go.putExtra("userId", userId);
        startActivity(go);
    }

}
