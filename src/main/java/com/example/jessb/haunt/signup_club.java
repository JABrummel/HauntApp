package com.example.jessb.haunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import sql.DatabaseHelper;
import models.Club;
public class signup_club extends AppCompatActivity {
    Club club = new Club();
    EditText user, clubname, email, pw1, pw2, advisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_club);
    }

    protected void goBack(View v) {
        Intent i = new Intent(this, login_club.class);
        startActivity(i);
    }

    protected void nextScreen(View v) {
//        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        Intent i = new Intent(signup_club.this, signup_club2.class);
         user = findViewById(R.id.et_username);
         clubname = findViewById(R.id.et_clubname);
         email = findViewById(R.id.et_email);
         pw1 = findViewById(R.id.et_password);
         pw2 = findViewById(R.id.et_passwordretype);
         advisor = findViewById(R.id.et_faculty);


        String usernameText = user.getText().toString();
        club.setUsername(usernameText);
        Log.i("clubview_looker", "club: " + usernameText);

        String clubText = clubname.getText().toString();
        club.setClubName(clubText);
        Log.i("clubview_looker", "club: " + clubText);

        String emailText = email.getText().toString();
        club.setClubEmail(emailText);
        Log.i("clubview_looker", "club: " + emailText);

        String pw1Text = pw1.getText().toString();
        String pw2Text = pw2.getText().toString();

        club.setPassword(pw2Text);
        String advisorText = advisor.getText().toString();
        club.setFacultyEmail(advisorText);

//        i.putExtra("username", usernameText);
//        i.putExtra("club", clubText);
//        i.putExtra("email", emailText);
//        i.putExtra("pw1", pw1Text);
//        i.putExtra("pw2", pw2Text);
//        i.putExtra("advisor", advisorText);

        i.putExtra("club_object", club);

        startActivity(i);
        this.overridePendingTransition(0,0);


    }
}
