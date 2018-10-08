package com.example.jessb.haunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import sql.DatabaseHelper;

public class UserType extends AppCompatActivity {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        db = DatabaseHelper.getInstance(getApplicationContext());

    }

    protected void changeScreen(View view) {

        Button button = (Button)view;
        int buttonId = button.getId();
        Intent intent;

        switch (buttonId) {
            case R.id.button_admin: {
                intent = new Intent(this, login_admin.class);
                startActivity(intent);
                break;
            }
            case R.id.button_club: {
                intent = new Intent(this, login_club.class);
                startActivity(intent);
                break;
            }
            case R.id.button_student: {
                intent = new Intent(this, login_student.class);
                startActivity(intent);
                break;
            }

        }
    }

}
