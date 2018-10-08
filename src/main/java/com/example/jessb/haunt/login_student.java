package com.example.jessb.haunt;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class login_student extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);
    }

    protected void goBack(View v) {
        Intent i = new Intent(this, UserType.class);
        startActivity(i);
    }

    protected void login(View v) {
        EditText user = findViewById(R.id.et_username);
        EditText pw = findViewById(R.id.et_password);
        String username = user.getText().toString();
        String password = pw.getText().toString();
        if(username.equals("1") && password.equals("1")) {
            Intent i = new Intent(this, ListedEvents.class);
            i.putExtra("user", "student");
            startActivity(i);
        }
    }
}
