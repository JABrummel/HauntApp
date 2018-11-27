package com.example.jessb.haunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
                //buffer.append("Faculty Email: " + res.getString(1) + "\n");
                buffer.append("Club Name: " + res.getString(2) + "\n");
               // buffer.append("Photo: " + res.getString(3) + "\n");
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
        String clubApproved = "";
        if(db.checkClub(username, password)) {
            Cursor userCursor = db.getClub(username);
            int clubId = -1;

            while(userCursor.moveToNext()) {

                clubId = userCursor.getInt(0);
                clubApproved = userCursor.getString(9);
            }
           if(clubId > -1) {
                if (clubApproved.equals("true")) {
                    Intent i = new Intent(this, ListedEvents.class);
                    i.putExtra("userType", "club");
                    i.putExtra("userId", clubId);
                    startActivity(i);
                }
                else if(clubApproved.equals("false")) {
                    pending();
                }
                else {
                    notApproved();
                }
           }
        } else checkEntries();
    }
    protected void createAccount(View v) {
        Intent i = new Intent(this, signup_club.class);
        startActivity(i);
    }


    protected void goBack(View v) {
        Intent i = new Intent(this, UserType.class);
        startActivity(i);
 }


    protected void pending() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Club is not approved yet")
                .setMessage("Your club is currently waiting admin approval. Thank you for your patience.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    protected void notApproved() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Club has been denied")
                .setMessage("Your request for a club account has been denied by admin.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }
    protected void checkEntries() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Invalid Entry")
                .setMessage("Username or Password is incorrect, or you club has been deleted by admin.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

//    private void verifyLogin(){
//        if(!isUserFilled())
//    }
}
