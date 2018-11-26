package com.example.jessb.haunt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import models.Events;
import sql.DatabaseHelper;

public class CreateEvent extends AppCompatActivity  {

    TextView datePicked; //shows the date of the event
    TextView startTime; //shows the start time of the event
    TextView endTime; //shows the end time of the event
    EditText eventName;
    EditText eventBio;
    EditText roomNumber;
    Boolean selectedImage = false;
    Button uploadButton;
    ImageView profImage;
    private Bitmap bp;
    private byte[] photo;
    private static final int PICK_IMAGE = 100;
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
    ArrayList<String> categories = new ArrayList<String>(); //all of the subcategories chose
    String mainCategory; //chosen main category
    String categoryString;
    int myYear;
    int myMonth;
    int myDay;
    int startHour;
    int endHour;
    int startMin;
    int endMin;
    String building, startTimeValue, endTimeValue, date;
    RadioGroup selectedCampus; //Kennesaw vs Marietta
    RadioGroup mainCategories; //The 5 main categories
    DatePickerDialog.OnDateSetListener dateSetListener; //listener for sellecting the date
    TimePickerDialog.OnTimeSetListener startTimeSetListener; //listener for selecting start time
    TimePickerDialog.OnTimeSetListener endTimeSetListener; //listener for selecting end time
    List<String> mariettaLocations = new ArrayList(Arrays.asList("J-Building", "Academic Building", "Joe Mack Wilson Student Center",
     "Gym", "Recreation and Wellness Center", "Lawrence V Johnson Library", "Norton Hall", "Howell Hall",
    "Stingers", "Q Building", "Architecture Building", "Design Building", "Mathematics Building"));
    List <String>kennesawLocations = new ArrayList(Arrays.asList("The Commons", "Sturgis Library", "Kennesaw Hall", "Siegal Student Recreation",
    "Convocation Center", "Bailey Performance Center", "Arboretum"));
    Spinner locationSpinner; //Drop down for buildings
    Events newEvent = new Events();
    int userId;
    String campus;

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
        eventName = (EditText) findViewById(R.id.tv_eventName);
        eventBio = (EditText) findViewById(R.id.tv_eventBio);
        roomNumber = (EditText) findViewById(R.id.et_roomnumber);
        uploadButton = (Button)findViewById(R.id.upload_img);
        profImage=(ImageView)findViewById(R.id.event_image);

        Intent lastActivity = getIntent();
        userId = lastActivity.getIntExtra("userId", 0);

        //setup for marietta location dropdown
        final ArrayAdapter<String> mariettaAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mariettaLocations);
                mariettaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //setup for the kennesaw location dropdown
                final ArrayAdapter<String> kennesawAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kennesawLocations);
                kennesawAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //The following functions are for the dropdown
        locationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                building = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + building, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
         //Radiogroup for the two campus locations
        selectedCampus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_marietta) {
                    locationSpinner.setAdapter(mariettaAdapter);
                    campus = "marietta";
                }
                else {
                   locationSpinner.setAdapter(kennesawAdapter);
                   campus="kennesaw";
                }
            }
        });

        //Radiogroup for the main categories
        mainCategories.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rb_competition:
                        mainCategory = CAT_COMPETITION;
                        break;
                    case R.id.rb_food:
                        mainCategory = CAT_FOOD;
                        break;
                    case R.id.rb_athletics:
                        mainCategory = CAT_ATHLETICS;
                        break;
                    case R.id.rb_academics:
                        mainCategory = CAT_ACADEMICS;
                        break;
                    case R.id.rb_entertainment:
                        mainCategory = CAT_ENTERTAINMENT;
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
                String currentTime;
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




                startTimeValue = "" + hour + min;
                newEvent.setStartTime(startTimeValue);
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
                TimePickerDialog endDialog = new TimePickerDialog(CreateEvent.this,
                        endTimeSetListener, startHour, startMin,true);
                endDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                endDialog.show();
            }
        });

        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


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

                endTimeValue = ""+hour +min;
                newEvent.setEndTime(endTimeValue);
                endTimeValue = hour +":"+min;
                endTime.setText(endTimeValue);

            }
        };

        //creates the calendar dialog
        datePicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                 myYear = calendar.get(Calendar.YEAR);
                 myMonth = calendar.get(Calendar.MONTH);
                 myDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpDialog = new DatePickerDialog(CreateEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, myYear, myMonth, myDay);
                dpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpDialog.show();
            }

        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month+=1;
                String m = "" + month;
                if(month<10) {
                    m = "0"+month;
                }
                String day = "" + dayOfMonth;
                if(dayOfMonth<10) {
                    day = "0"+dayOfMonth;
                }

                datePicked.setText(m + "/" + day + "/" + year);
                date = (""+year+m+day);
                newEvent.setDate(date);
            }
        };


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = true;
                selectImage();
            }
        });
    }

    public void selectImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 2:
                if(resultCode == RESULT_OK) {
                    Uri chosenImage = data.getData();
                    if (chosenImage != null) {
                        bp = decodeUri(chosenImage, 200);
                        profImage.setImageBitmap(bp);
                    }
                }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE){
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true){
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private byte[] profileImage(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    private void getPhotoValue(){
        photo = profileImage(bp);
    }

    //cancels the create event
    protected void cancel(View v) {
        Intent i = new Intent(this, ListedEvents.class);
        i.putExtra("userType", "club");
        i.putExtra("userId", userId);
        startActivity(i);
    }

    protected void save(View v){
        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        String eventNameValue = eventName.getText().toString();
        String eventBioValue = eventBio.getText().toString();
        String roomNumberValue = roomNumber.getText().toString();
        String location = roomNumberValue + " " + building;

        newEvent.setEventName(eventNameValue);
        newEvent.setBio(eventBioValue);
        newEvent.setLocation(location);
        newEvent.setCampus(campus);
        newEvent.setClubID(userId);

        categories.add(0, mainCategory);

        for(int i =0; i< categories.size(); i++) {
            categoryString += categories.get(i);
        }

        newEvent.setCategories(categoryString);
        if(eventNameValue.length()<0 || eventBioValue.length()<0 || campus.length()<0 ||
                eventName.length()<0 || categoryString.length()<0 || date.length()<0 || startTimeValue.length() <0
                || endTimeValue.length()<0 || selectedImage == false) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Missing information")
                    .setMessage("Please make sure to select all information")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }else {
            getPhotoValue();
            newEvent.setPhoto(photo);
            db.addEvents(newEvent);
            Intent i = new Intent(this, ListedEvents.class);
            i.putExtra("userType", "club");
            i.putExtra("userId", userId);

        startActivity(i);}
    }





    //monitors the checkbox clicks
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
            }


    }

}
