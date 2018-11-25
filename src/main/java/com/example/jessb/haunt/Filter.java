package com.example.jessb.haunt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Filter extends AppCompatActivity {

    String userType, endDateVal, startDateVal, startTimeVal, endTimeVal, categoriesVal, campus;
    int startHour, startMin, endHour, endMin, userId;
    Button applyFilter, cancel;
    TextView endDate, startDate, startTime, endTime;
    DatePickerDialog.OnDateSetListener startDateListener, endDateListener; //listener for sellecting the date
    TimePickerDialog.OnTimeSetListener startTimeSetListener; //listener for selecting start time
    TimePickerDialog.OnTimeSetListener endTimeSetListener;
    RadioGroup selectedCampus;
    ArrayList<String> categories;
    final String CAT_COMPETITION ="a";
    final String CAT_FOOD ="b";
    final String CAT_ATHLETICS="c";
    final String CAT_ACADEMICS="d";
    final String CAT_ENTERTAINMENT="e";
    final String CAT_LIVEPERFORMANCE="f";
    final String CAT_STEAM="g";
    final String CAT_LITART="h";
    final String CAT_GIVEAWAYS="i";
    final String CAT_MUSIC="j";
    final String CAT_BOARDGAMES="k";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Intent lastIntent = getIntent();
        userType = lastIntent.getStringExtra("userType");
        userId = lastIntent.getIntExtra("userId", 0);
        endDate = findViewById(R.id.tv_endDate);
        startDate = findViewById(R.id.tv_startDate);
        startTime = findViewById(R.id.tv_startTime);
        endTime = findViewById(R.id.tv_endTime);
        applyFilter = findViewById(R.id.button_applyFilter);
        cancel = findViewById(R.id.button_cancelFilter);
        selectedCampus = findViewById(R.id.rgf_campus);
        categories = new ArrayList<>();


        //creates the calendar dialog
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int myYear = calendar.get(Calendar.YEAR);
                int myMonth = calendar.get(Calendar.MONTH);
                int myDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(Filter.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateListener, myYear, myMonth, myDay);
                dpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpDialog.show();
            }

        });
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                month+=1;
                String m = "" + month;
                if(month<10) {
                    m = "0"+month;
                }
                String day = "" + dayOfMonth;
                if(dayOfMonth<10) {
                    day = "0"+dayOfMonth;
                }

                startDate.setText(m + "/" + day + "/" + year);
                startDateVal = (""+year+m+day);

            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int myYear = calendar.get(Calendar.YEAR);
                int myMonth = calendar.get(Calendar.MONTH);
                int myDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(Filter.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                       endDateListener, myYear, myMonth, myDay);
                dpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpDialog.show();
            }

        });
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                month+=1;
                String m = "" + month;
                if(month<10) {
                    m = "0"+month;
                }
                String day = "" + dayOfMonth;
                if(dayOfMonth<10) {
                    day = "0"+dayOfMonth;
                }

                endDate.setText(m + "/" + day + "/" + year);
                endDateVal = (""+year+m+day);

            }
        };

        selectedCampus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbf_marietta) {
                    campus = "marietta";
                }
                else if(checkedId == R.id.rbf_kennesaw){
                    campus="kennesaw";
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();
                startHour = time.get(Calendar.HOUR_OF_DAY);
                startMin = time.get(Calendar.MINUTE);
                TimePickerDialog startDialog = new TimePickerDialog(Filter.this,
                        startTimeSetListener, startHour, startMin,true);
                startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDialog.show();

            }
        });

        startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String currentTime;
                String startTimeValue;
                String min;
                String hour;
                if (minute < 10) {
                    min = "0"+minute;
                }
                else min = "" +minute;

                if(hourOfDay<10) {
                    hour = "0"+hourOfDay;
                }
                else hour = ""+hourOfDay;




                startTimeVal = "" + hour + min;
                startTimeValue = "" + hour + ":" + min;
                startTime.setText(startTimeValue);

            }
        };

        //creates the end time dialog
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();
                endHour = time.get(Calendar.HOUR_OF_DAY);
                endMin = time.get(Calendar.MINUTE);
                TimePickerDialog endDialog = new TimePickerDialog(Filter.this,
                        endTimeSetListener, startHour, startMin,true);
                endDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                endDialog.show();
            }
        });

        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String endTimeValue;
                String min;
                String hour;
                if (minute < 10) {
                    min = "0"+minute;
                }
                else min = "" +minute;

                if(hourOfDay<10) {
                    hour = "0"+hourOfDay;
                }
                else hour = ""+hourOfDay;

                endTimeVal = ""+hour +min;
                endTimeValue = hour +":"+min;
                endTime.setText(endTimeValue);

            }
        };
    }

    protected void checkBoxClick(View view) {
        int checkboxId = view.getId();
        CheckBox cb = (CheckBox) view;

        if(cb.isChecked())
            switch(checkboxId) {
                case R.id.cb_liveperformance:
                    categories.add(CAT_LIVEPERFORMANCE);
                    break;
                case R.id.cb_steam:
                    categories.add(CAT_STEAM);
                    break;
                case R.id.cb_art:
                    categories.add(CAT_LITART);
                    break;
                case R.id.cb_giveaways:
                    categories.add(CAT_GIVEAWAYS);
                    break;
                case R.id.cb_music:
                    categories.add(CAT_MUSIC);
                    break;
                case R.id.cb_boardgames:
                    categories.add(CAT_BOARDGAMES);
                    break;
                case R.id.cbf_academics:
                    categories.add(CAT_ACADEMICS);
                    break;
                case R.id.cbf_athletics:
                    categories.add(CAT_ATHLETICS);
                    break;
                case R.id.cbf_food:
                    categories.add(CAT_FOOD);
                    break;
                case R.id.cbf_competition:
                    categories.add(CAT_COMPETITION);
                    break;
                case R.id.cbf_entertainment:
                    categories.add(CAT_ENTERTAINMENT);
                    break;

            }

        else
            switch(checkboxId) {
                case R.id.cb_liveperformance:
                    categories.remove(CAT_LIVEPERFORMANCE);
                    break;
                case R.id.cb_steam:
                    categories.remove(CAT_STEAM);
                    break;
                case R.id.cb_art:
                    categories.remove(CAT_LITART);
                    break;
                case R.id.cb_giveaways:
                    categories.remove(CAT_GIVEAWAYS);
                    break;
                case R.id.cb_music:
                    categories.remove(CAT_MUSIC);
                    break;
                case R.id.cb_boardgames:
                    categories.remove(CAT_BOARDGAMES);
                    break;
                case R.id.cbf_academics:
                    categories.remove(CAT_ACADEMICS);
                    break;
                case R.id.cbf_athletics:
                    categories.remove(CAT_ATHLETICS);
                    break;
                case R.id.cbf_food:
                    categories.remove(CAT_FOOD);
                    break;
                case R.id.cbf_competition:
                    categories.remove(CAT_COMPETITION);
                    break;
                case R.id.cbf_entertainment:
                    categories.remove(CAT_ENTERTAINMENT);
                    break;
            }


    }
    protected void buttonClick(View v) {
        int id = v.getId();
        String concern = "";
        categoriesVal = "";
        boolean issue = false;
        Intent i = new Intent(this, ListedEvents.class);
        i.putExtra("userType", userType);
        i.putExtra("userId", userId);
        switch(id){
            case R.id.button_cancelFilter : {
                startActivity(i);

            }
            case R.id.button_applyFilter: {
                //handling the categories entries
                if(categories.size() == 0) {
                    issue = true;
                    concern += "\n Please choose categories.";

                } else{
                    for(int x = 0; x<categories.size(); x++) {
                        categoriesVal += categories.get(x);
                    }
                    i.putExtra("categories" , categoriesVal);

                }

                //handling the date entries
                if(endDateVal == null || startDateVal == null) {
                    issue = true;
                    concern += "\n Please enter a start and end date.";
                }
                else {
                    i.putExtra("endDate", endDateVal);
                    i.putExtra("startDate", startDateVal);
                }

                //handling the time entries
                if(endTimeVal == null || startTimeVal == null) {
                    issue = true;
                    concern += "\n Please enter a start and end time.";
                }
                else {
                    i.putExtra("endTime", endTimeVal);
                    i.putExtra("startTime", startTimeVal);
                }

                if(issue) {
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
                else {
                    i.putExtra("campus", campus);
                    i.putExtra("filter", true);
                    startActivity(i);
                }

            }
        }
    }
}
