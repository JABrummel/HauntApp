package com.example.jessb.haunt;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
        if(username.equals("student1") && password.equals("1")) {
            Intent i = new Intent(this, ListedEvents.class);
            i.putExtra("userType", "student");
            startActivity(i);
        }
        else checkEntries();
    }
    protected void checkEntries() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Invalid Entry")
                .setMessage("Username or Password is incorrect")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
