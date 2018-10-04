package com.example.jessb.haunt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class CreateEvent extends AppCompatActivity implements OnItemSelectedListener {

    TextView datePicked; //shows the date of the event
    TextView startTime; //shows the start time of the event
    TextView endTime; //shows the end time of the event
    int year;
    int month;
    int day;
    int startHour;
    int endHour;
    int startMin;
    int endMin;
    String startTimeVal;
    String endTimeVal;
    RadioGroup selectedCampus; //Kennesaw vs Marietta
    RadioGroup mainCategories; //The 5 main categories
    String mainCategory; //chosen main category
    Vector<String> subCategories = new Vector<String>(); //all of the subcategories chose
    DatePickerDialog.OnDateSetListener dateSetListener; //listener for sellecting the date
    TimePickerDialog.OnTimeSetListener startTimeSetListener; //listener for selecting start time
    TimePickerDialog.OnTimeSetListener endTimeSetListener; //listener for selecting end time
    List<String> mariettaLocations = new ArrayList(Arrays.asList("J-Building", "Academic Building", "Joe Mack Wilson Student Center",
     "Gym", "Recreation and Wellness Center", "Lawrence V Johnson Library", "Norton Hall", "Howell Hall",
    "Stingers", "Q Building", "Architecture Building", "Design Building", "Mathematics Building"));
    List <String>kennesawLocations = new ArrayList(Arrays.asList("The Commons", "Sturgis Library", "Kennesaw Hall", "Siegal Student Recreation",
    "Convocation Center", "Bailey Performance Center", "Arboretum"));
    Spinner locationSpinner; //Drop down for buildings
    String building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        datePicked = (TextView)findViewById(R.id.tv_date);
        startTime = (TextView)findViewById(R.id.tv_startTime);
        endTime = (TextView)findViewById(R.id.tv_endTime);
        selectedCampus= (RadioGroup)findViewById(R.id.rg_campus);
        mainCategories=(RadioGroup)findViewById(R.id.rg_categories);
        locationSpinner=(Spinner)findViewById(R.id.spinner_locations);


        //setup for marietta location dropdown
        final ArrayAdapter<String> mariettaAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mariettaLocations);
                mariettaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //setup for the kennesaw location dropdown
                final ArrayAdapter<String> kennesawAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kennesawLocations);
                kennesawAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

         //Radiogroup for the two campus locations
        selectedCampus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_marietta) {
                    locationSpinner.setAdapter(mariettaAdapter);
                }
                else {
                   locationSpinner.setAdapter(kennesawAdapter);
                }
            }
        });

        //Radiogroup for the main categories
        mainCategories.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rb_competition:
                        mainCategory = "competition";
                        break;
                    case R.id.rb_food:
                        mainCategory = "food";
                        break;
                    case R.id.rb_athletics:
                        mainCategory = "athletics";
                        break;
                    case R.id.rb_academics:
                        mainCategory = "academics";
                        break;
                    case R.id.rb_entertainment:
                        mainCategory = "entertainment";
                        break;
                }
            }
        });

        //Creates the calendar dialog
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();
                startHour = time.get(Calendar.HOUR_OF_DAY);
                startMin = time.get(Calendar.MINUTE);
                TimePickerDialog startDialog = new TimePickerDialog(CreateEvent.this,
                        startTimeSetListener, startHour, startMin,true);
                startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDialog.show();
            }
        });

        startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(minute > 10)
                    startTime.setText(hourOfDay + ": " + minute);
                else
                    startTime.setText(hourOfDay + ": 0" + minute);
            }
        };

        //creates the end time dialog
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();
                endHour = time.get(Calendar.HOUR_OF_DAY);
                endMin = time.get(Calendar.MINUTE);
                TimePickerDialog endDialog = new TimePickerDialog(CreateEvent.this,
                        endTimeSetListener, startHour, startMin,true);
                endDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                endDialog.show();
            }
        });

        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(minute > 10)
                endTime.setText(hourOfDay + ": " + minute);
                else
                    endTime.setText(hourOfDay + ": 0" + minute);

            }
        };

        //creates the calendar dialog
        datePicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                 year = calendar.get(Calendar.YEAR);
                 month = calendar.get(Calendar.MONTH);
                 day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(CreateEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, year, month, day);
                dpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpDialog.show();
            }

        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePicked.setText(month+1 + "/" + dayOfMonth + "/" + year);
            }
        };



    }

    //cancels the create event
    protected void cancel(View v) {
        Intent i = new Intent(this, ListedEvents.class);
        i.putExtra("user", "club");
        startActivity(i);
    }

    //The following functions are for the dropdown
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String building = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + building, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    //monitors the checkbox clicks
    protected void checkBoxClick(View view) {
        int checkboxId = view.getId();
        CheckBox cb = (CheckBox) view;

        if(cb.isChecked())
        switch(checkboxId) {
            case R.id.cb_liveperformance:
                subCategories.add("Live Performance");
                break;
            case R.id.cb_steam:
                subCategories.add("STEAM");
                break;
            case R.id.cb_art:
                subCategories.add("Literature And Arts");
                break;
            case R.id.cb_giveaways:
                subCategories.add("Giveaways");
                break;
            case R.id.cb_music:
                subCategories.add("Music");
                break;
            case R.id.cb_boardgames:
                subCategories.add("Boardgames");
                break;
        }

        else
            switch(checkboxId) {
                case R.id.cb_liveperformance:
                    subCategories.remove("Live Performance");
                    break;
                case R.id.cb_steam:
                    subCategories.remove("STEAM");
                    break;
                case R.id.cb_art:
                    subCategories.remove("Literature And Arts");
                    break;
                case R.id.cb_giveaways:
                    subCategories.remove("Giveaways");
                    break;
                case R.id.cb_music:
                    subCategories.remove("Music");
                    break;
                case R.id.cb_boardgames:
                    subCategories.remove("Boardgames");
                    break;
            }


    }

}
