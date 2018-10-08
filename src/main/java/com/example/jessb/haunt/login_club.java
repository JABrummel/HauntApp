package com.example.jessb.haunt;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import models.Club;

import sql.DatabaseHelper;

import static java.security.AccessController.getContext;

public class login_club extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_club);
        db = DatabaseHelper.getInstance(getApplicationContext());
        Cursor res = db.getAllClub();
        if (res.getCount() == 0){
            System.out.println("nothing found");
         } else {

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("Faculty Email: " + res.getString(1) + "\n");
                buffer.append("Club Name: " + res.getString(2) + "\n");
                buffer.append("Photo: " + res.getString(3) + "\n");
                buffer.append("UserName: " + res.getString(4) + "\n");
                buffer.append("Email: " + res.getString(5) + "\n");
                buffer.append("Password: " + res.getString(6) + "\n");
                buffer.append("Role: " + res.getString(7) + "\n");
            }
            System.out.println(buffer.toString());
        }

    }

    protected void login(View v) {
        EditText user = findViewById(R.id.et_username);
        EditText pw = findViewById(R.id.et_password);
        String username = user.getText().toString().trim();
        String password = pw.getText().toString().trim();
        if(db.checkClub(username, password)) {
            Intent i = new Intent(this, ListedEvents.class);
            i.putExtra("user", "club");
            startActivity(i);
        } else if (username.length() != 0){
            Toast.makeText(this, "Please enter an username", Toast.LENGTH_SHORT);
        } else if (password.length() != 0){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT);
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
    public boolean isUserFilled(EditText editText, TextInputLayout inputLayout, String message){
        String value = editText.getText().toString().trim();
        if(value.isEmpty())
        {
            inputLayout.setError(message);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

//    private void verifyLogin(){
//        if(!isUserFilled())
//    }
}
