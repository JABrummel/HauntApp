package sql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import models.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = DatabaseHelper.class.getName();
    //DB version
    private static final int DATABASE_VERSION = 11;

    private static DatabaseHelper sInstance;
    private final Context ctx;

    //DB Name
    private static final String DATABASE_NAME = "Haunt.db";

    //Table Names
    private static final String TABLE_USER = "User";
    private static final String TABLE_CLUB = "Club";
    private static final String TABLE_EVENTS = "Events";
    private static final String TABLE_CATEGORY = "Category";
    private static final String TABLE_EVENTCATEGORIES = "Event_Categories";
    private static final String TABLE_LOCATION = "Location";
    private static final String TABLE_ACCOUNT_APPROVAL = "Account_Approval";

    //region Column Names

    /*USER TABLE*/
    //CLUB SHARES ALL THESE COLUMNS
    private static final String COLUMN_USER_ID = "ID";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";


    /*CLUB TABLE*/
    private static final String COLUMN_CLUBID = "clubID"; //ACCOUNT APPROVAL SHARES THIS
    private static final String COLUMN_CLUBNAME = "clubName";
    private static final String COLUMN_CLUBEMAIL = "clubEmail";
    private static final String COLUMN_FACULTYEMAIL = "facultyEmail";
    private static final String COLUMN_PHOTO = "photo";
    private static final String COLUMN_APPROVED = "approved";
    private static final String COLUMN_BIO = "bio";
    //USER COLUMNS

    /*EVENTS TABLE*/
    private static final String COLUMN_EVENTID = "eventID"; //EVENT CATEGORY SHARES THIS
    private static final String COLUMN_EVENTNAME = "eventName";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_STARTTIME = "startTime";
    private static final String COLUMN_ENDTIME = "endTime";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_CAMPUS = "campus";
    //PHOTO COLUMN
    //CLUB ID
    private static final String COLUMN_ROOMNUMBER = "roomNumber";

    /*EVENTCATEGORIES TABLE*/
    //EventID
    private static final String COLUMN_CATEGORYNAME = "categoryName";
    //CATEGORY ID

    /*CATEGORY*/
    private static final String COLUMN_CATEGORYID = "categoryID"; //CATEGORY SHARES THIS
    //IMAGE COLUMN

    /*ACCOUNTAPPROVAL TABLE*/

    //CLUB ID COLUMN
    private static final String COLUMN_ISAPPROVED = "isApproved";
    private static final String COLUMN_REASON = "reason";

    /*LOCATION TABLE*/
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_NAME = "name";


    //endregion

    //region CREATE TABLES
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY ," + COLUMN_USERNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_ROLE + " TEXT" + ")";

    private String CREATE_CLUB_TABLE = "CREATE TABLE " + TABLE_CLUB + "("
            + COLUMN_CLUBID + " INTEGER PRIMARY KEY ," + COLUMN_FACULTYEMAIL + " TEXT,"
            + COLUMN_CLUBNAME + " TEXT," + COLUMN_PHOTO + " BLOB," + COLUMN_USERNAME + " TEXT,"
            + COLUMN_CLUBEMAIL + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_ROLE + " TEXT,"
            + COLUMN_BIO + " TEXT," + COLUMN_APPROVED + " TEXT" + ")";

    private String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
            + COLUMN_EVENTID + " INTEGER PRIMARY KEY ," + COLUMN_EVENTNAME + " TEXT,"
            + COLUMN_LOCATION + " TEXT," + COLUMN_CAMPUS + " TEXT," + COLUMN_STARTTIME + " TEXT," + COLUMN_ENDTIME + " TEXT," + COLUMN_DATE + " INTEGER,"
            + COLUMN_BIO + " TEXT,"  + COLUMN_PHOTO + " BLOB," + COLUMN_CLUBID + " INTEGER," + COLUMN_CATEGORIES + " TEXT" + ")";

    private String CREATE_EVENT_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_EVENTCATEGORIES + "("
            + COLUMN_EVENTID + " INTEGER," + COLUMN_CATEGORYNAME + " TEXT," + COLUMN_CATEGORYID + " INTEGER" + ")";

    private String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORYID + " INTEGER PRIMARY KEY ," + COLUMN_PHOTO + " BLOB" + ")";

    private String CREATE_ACCOUNT_APPROVAL_TABLE = "CREATE TABLE " + TABLE_ACCOUNT_APPROVAL + "("
            + COLUMN_CLUBID +  " INTEGER PRIMARY KEY ," + COLUMN_ISAPPROVED + " INTEGER,"
            + COLUMN_REASON + " TEXT" + ")";

    private String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
            + COLUMN_ADDRESS + " TEXT," + COLUMN_NAME + " TEXT," + COLUMN_CAMPUS + " TEXT" + ")";
    //endregion

    public static synchronized DatabaseHelper getInstance(Context context){
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        //Default user values
        db.execSQL("insert into User (username, email, password, role) values ('jvice1', 'jvice1@kennesaw.edu', 'test', 'admin')");
        db.execSQL("insert into User (username, email, password, role) values ('jbrummel', 'jbrummel@kennesaw.edu', 'test', 'admin')");
        db.execSQL("insert into User (username, email, password, role) values ('ebell', 'ebell@kennesaw.edu', 'test', 'admin')");

        db.execSQL(CREATE_CLUB_TABLE);
        try {
            db.execSQL("insert into Club (facultyEmail, clubName, photo, username, clubEmail, password, role) values ('jvice@kennesaw.edu', 'Club Penguin', " +
                    "null, 'jvice', 'cp@kennesaw.edu', 'test', 'club')");
            db.execSQL("insert into Club (facultyEmail, clubName, photo, username, clubEmail, password, role) values ('frodgers@kennesaw.edu', 'Chess Club', " +
                    "null, 'frodgers', 'chessclub@kennesaw.edu', 'chess', 'club')");
            db.execSQL("insert into Club (facultyEmail, clubName, photo, username, clubEmail, password, role) values ('george@kennesaw.edu', 'Chess Club', " +
                    "null, 'george', 'chessclub@kennesaw.edu', 'chess', 'club')");
        } catch (SQLException e){
            System.out.print(e);
        }
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_EVENT_CATEGORIES_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);

        db.execSQL("insert into Category(categoryID, photo) values ('1', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('2', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('3', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('4', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('5', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('6', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('7', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('8', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('9', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('10', null)");
        db.execSQL("insert into Category(categoryID, photo) values ('11', null)");

        db.execSQL(CREATE_ACCOUNT_APPROVAL_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLUB);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTCATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_APPROVAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);

        onCreate(db);
    }

    //region Add Data to Tables Methods
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_ROLE, user.getRole());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void addClub(Club club){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLUBNAME, club.getClubName());
        values.put(COLUMN_PHOTO, club.getPhoto());
        values.put(COLUMN_USERNAME, club.getUsername());
        values.put(COLUMN_CLUBEMAIL, club.getClubEmail());
        values.put(COLUMN_FACULTYEMAIL, club.getFacultyEmail());
        values.put(COLUMN_PASSWORD, club.getPassword());
        values.put(COLUMN_ROLE, club.getRole());
        values.put(COLUMN_BIO, club.getBio());
        values.put(COLUMN_APPROVED, club.getApproved());

        db.insert(TABLE_CLUB, null, values);
        db.close();
    }

    public long addEvents(Events events){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENTNAME, events.getEventName());
        values.put(COLUMN_LOCATION, events.getLocation());
        values.put(COLUMN_CAMPUS, events.getCampus());
        values.put(COLUMN_STARTTIME, events.getStartTime());
        values.put(COLUMN_ENDTIME, events.getEndTime());
        values.put(COLUMN_DATE, events.getDate());
        values.put(COLUMN_BIO, events.getBio());
        values.put(COLUMN_PHOTO, events.getPhoto());
        values.put(COLUMN_CLUBID, events.getClubID());
        values.put(COLUMN_CATEGORIES, events.getCategories());

        Log.d("DatabaseHelper", "Adding " + events + " to " + TABLE_EVENTS);

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return result;
    }



    public void addLocation(Location location){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS, location.getAddress());
        values.put(COLUMN_NAME, location.getName());
        values.put(COLUMN_CAMPUS, location.getCampus());

        db.insert(TABLE_LOCATION, null, values);
        db.close();
    }

    public void addAccountApprovals(AccountApprovals approval){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLUBID, approval.getClubID());
        values.put(COLUMN_ISAPPROVED, approval.isApproved());
        values.put(COLUMN_REASON, approval.getReason());

        db.insert(TABLE_ACCOUNT_APPROVAL, null, values);
        db.close();
    }


    public Cursor getEvents(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENTS ;
        Cursor events = db.rawQuery(query, null);
        return events;
    }

    public Cursor getEvents(String c){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENTS
                + " WHERE " + COLUMN_CAMPUS + " = '" + c + "'";
        Cursor events = db.rawQuery(query, null);
        return events;
    }

    public Cursor filterEvents(String startTime, String endTime, String date, String location, String categories, String campus) {
        SQLiteDatabase db = this.getReadableDatabase();
        int startHour;
        String startMin;
        String endMin;
        String endHour;
        if(startTime!=null) {
            startHour = Integer.parseInt(startTime.substring(0,2));

        }
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE ";
        return null;
    }

    public String getCategories(String eventN, int clubid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORIES + " FROM " + TABLE_EVENTS +
                " WHERE " + COLUMN_CLUBID + " = '" + clubid + "' AND " + COLUMN_EVENTNAME + " = '" + eventN + "'";
        Cursor c = db.rawQuery(query, null);
        String categories = "";
        if(c.moveToFirst()) {
            do {
                categories = c.getString(c.getColumnIndex(COLUMN_CATEGORIES));
            } while (c.moveToNext());
        }

        return categories;
    }


    public Cursor getClubs(String approval) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CLUB
                + " WHERE " + COLUMN_APPROVED + " = '" + approval + "'";
        Cursor clubs = db.rawQuery(query, null);
        return clubs;
    }

    public List<Events> getAllEvents() {
        List<Events> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Events event = new Events();
                event.setClubID(c.getInt((c.getColumnIndex(COLUMN_CLUBID))));
                event.setEventName(c.getString((c.getColumnIndex(COLUMN_EVENTNAME))));
                event.setEventID(c.getInt((c.getColumnIndex(COLUMN_EVENTID))));
                event.setLocation(c.getString((c.getColumnIndex(COLUMN_LOCATION))));
                event.setStartTime(c.getString((c.getColumnIndex(COLUMN_STARTTIME))));
                event.setEndTime(c.getString((c.getColumnIndex(COLUMN_ENDTIME))));
                event.setDate(c.getString((c.getColumnIndex(COLUMN_DATE))));
                event.setBio(c.getString((c.getColumnIndex(COLUMN_BIO))));
                //event.setPhoto(c.getBlob((c.getColumnIndex(COLUMN_PHOTO)))); //Gotta figure out how pictures are saved properly into DB more
                event.setRoomNumber(c.getInt((c.getColumnIndex(COLUMN_ROOMNUMBER))));

                events.add(event);
            } while (c.moveToNext());
        }
        return events;
    }

    public Cursor getEvent(String name) {
        String query = "SELECT " + COLUMN_EVENTID + " FROM " + TABLE_EVENTS +
                " WHERE " + COLUMN_EVENTNAME + " = '" + name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getEvent(String name, String location, String startTime, String endTime, String date, String bio){
        String query = "SELECT " + COLUMN_EVENTID + " FROM " + TABLE_EVENTS +
                " WHERE " + COLUMN_EVENTNAME + " = '" + name + "' AND " + COLUMN_LOCATION  + " = '" + location + "' AND "
                + COLUMN_STARTTIME + " = '" + startTime + "' AND " + COLUMN_ENDTIME + " = '" + endTime + "' AND " + COLUMN_DATE +
                " = '" + date + "' AND " + COLUMN_BIO + " = '" + bio + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getClubName(int id){
        String query = "SELECT " + COLUMN_CLUBNAME + " FROM " + TABLE_CLUB +
                " WHERE " + COLUMN_CLUBID + " = '" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    //region Update/Delete Methods
    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_ROLE, user.getRole());

        return db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[] {String.valueOf(user.getUserID())});

    }

//    public int updateEvent(Events events){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_EVENTNAME, events.getEventName());
//        values.put(COLUMN_LOCATION, events.getLocation());
//        values.put(COLUMN_STARTTIME, events.getStartTime());
//        values.put(COLUMN_ENDTIME, events.getEndTime());
//        values.put(COLUMN_DATE, events.getDate());
//        values.put(COLUMN_BIO, events.getBio());
//        values.put(COLUMN_PHOTO, events.getPhoto());
//        values.put(COLUMN_CLUBID, events.getClubID());
//        values.put(COLUMN_ROOMNUMBER, events.getRoomNumber());
//
//        return db.update(TABLE_EVENTS, values, COLUMN_EVENTID + " = ?",
//                new String[] {String.valueOf(events.getEventID())});
//
//    }
//
    public int updateApproval(Club club){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FACULTYEMAIL, club.getFacultyEmail());
        values.put(COLUMN_PHOTO, club.getPhoto());
        values.put(COLUMN_CLUBNAME, club.getClubName());
        values.put(COLUMN_USERNAME, club.getUsername());
        values.put(COLUMN_PASSWORD, club.getPassword());
        values.put(COLUMN_CLUBEMAIL, club.getClubEmail());
        values.put(COLUMN_ROLE, club.getRole());
        values.put(COLUMN_BIO, club.getBio());
        values.put(COLUMN_APPROVED, club.getApproved());

        return db.update(TABLE_CLUB, values, COLUMN_CLUBNAME + " = ?", new String[] {String.valueOf(club.getClubName())});
    }

    public void deleteEvent(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_CLUBID + " = '" + id + "'" + " AND " + COLUMN_EVENTNAME + " = '" + name + "'";
            db.execSQL(query);
            db.close();

    }

    public void deleteClub(String club){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CLUB + " WHERE " +
                COLUMN_CLUBNAME + " = '" + club + "'" ;
        db.execSQL(query);
        db.close();

    }
    //endregion

    //region check Methods
    public boolean checkUser(String username){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        System.out.println(cursorCount);
        return cursorCount > 0;
    }
    public boolean checkUser(String username, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username , password};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0; 
    }

    public Cursor getClub(String username) {
        String query = "SELECT *"  + " FROM " + TABLE_CLUB +
                " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getClub(int clubId, int x) {
        String club = "" + clubId + " ";
        String query = "SELECT " + COLUMN_CLUBID + " FROM " + TABLE_CLUB +
                " WHERE " + COLUMN_CLUBID + " = '" + club + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }



    public boolean checkClub(String username, String password){
        String[] columns = {
                COLUMN_CLUBID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_CLUB,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        System.out.println(cursorCount);
        return cursorCount > 0;
    }
    public Cursor getAllClub (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor query = db.rawQuery("SELECT * FROM " + TABLE_CLUB, null);
        return query;

    }
    //endregion

    //region insert methods
    //endregion
}
