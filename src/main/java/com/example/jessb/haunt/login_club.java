package com.example.jessb.haunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import models.Club;

import sql.DatabaseHelper;

public class login_club extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_club);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.checkUser("jbrummel");
        db.checkClub("jvice");


    }

    protected void login(View v) {
        EditText user = findViewById(R.id.et_username);
        EditText pw = findViewById(R.id.et_password);
        String username = user.getText().toString();
        String password = pw.getText().toString();
        if(username.equals("1") && password.equals("1")) {
            Intent i = new Intent(this, ListedEvents.class);
            i.putExtra("user", "club");
            startActivity(i);
        }
    }

    protected void goBack(View v) {
        Intent i = new Intent(this, UserType.class);
        startActivity(i);
 }
    protected void createAccount(View v) {
        Intent i = new Intent(this, signup_club.class);
        startActivity(i);
    }
}
