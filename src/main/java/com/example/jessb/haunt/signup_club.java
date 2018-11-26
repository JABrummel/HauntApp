package com.example.jessb.haunt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
        boolean issue = false;
        String concern = "";
//        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        Intent i = new Intent(signup_club.this, signup_club2.class);
         user = findViewById(R.id.et_username);
         clubname = findViewById(R.id.et_clubname);
         email = findViewById(R.id.et_email);
         pw1 = findViewById(R.id.et_password);
         pw2 = findViewById(R.id.et_passwordretype);
         advisor = findViewById(R.id.et_faculty);


        String usernameText = user.getText().toString();
        if(usernameText.length()>0) {
            club.setUsername(usernameText);
        }
        else {
            issue = true;
            concern += "\n Please enter a username";
        }

        String clubText = clubname.getText().toString();
        if(clubText.length()>0) {
            club.setClubName(clubText);
        }else {
            issue = true;
            concern += "\n Please enter a club name";
        }


        String emailText = email.getText().toString();
        if(emailText.length() >0){
            club.setClubEmail(emailText);
        }else {
            issue = true;
            concern += "\n Please enter a email";
        }


        String pw1Text = pw1.getText().toString();
        String pw2Text = pw2.getText().toString();

        if((pw1Text.length() > 0 && pw2Text.length()>0) && (pw1Text.equals(pw1Text))) {
            club.setPassword(pw2Text);
        }else {
            issue = true;
            concern += "\n Please recheck the passwords";
        }


        String advisorText = advisor.getText().toString();
        if(advisorText.length()>0){
            club.setFacultyEmail(advisorText);
        }else {
            issue = true;
            concern += "\n Please enter an advisor";
        }


        i.putExtra("club_object", club);

        if(!issue) {
            startActivity(i);
        }
        else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Missing information")
                    .setMessage(concern)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        this.overridePendingTransition(0,0);


    }
}
