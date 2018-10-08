package com.example.jessb.haunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import sql.DatabaseHelper;
import models.Club;
public class signup_club extends AppCompatActivity {
    Club club = new Club();
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
        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        Intent i = new Intent(this, signup_club2.class);
        EditText user = findViewById(R.id.et_username);
        EditText clubname = findViewById(R.id.et_clubname);
        EditText email = findViewById(R.id.et_email);
        EditText pw1 = findViewById(R.id.et_password);
        EditText pw2 = findViewById(R.id.et_passwordretype);
        EditText advisor = findViewById(R.id.et_faculty);


        String usernameText = user.getText().toString();
        club.setUsername(usernameText);
        String clubText = clubname.getText().toString();
        club.setClubName(clubText);
        String emailText = email.getText().toString();
        club.setClubEmail(emailText);
        String pw1Text = pw1.getText().toString();
        String pw2Text = pw2.getText().toString();
        club.setPassword(pw2Text);
        String advisorText = advisor.getText().toString();
        club.setFacultyEmail(advisorText);

        i.putExtra("username", usernameText);
        i.putExtra("club", clubText);
        i.putExtra("email", emailText);
        i.putExtra("pw1", pw1Text);
        i.putExtra("pw2", pw2Text);
        i.putExtra("advisor", advisorText);

        db.addClub(club);



        startActivity(i);
        this.overridePendingTransition(0,0);


    }
}
