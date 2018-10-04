package sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = DatabaseHelper.class.getName();
    //DB version
    private static final int DATABASE_VERSION = 1;

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
    //USER COLUMNS

    /*EVENTS TABLE*/
    private static final String COLUMN_EVENTID = "eventID"; //EVENT CATEGORY SHARES THIS
    private static final String COLUMN_EVENTNAME = "eventName";
    private static final String COLUMN_LOCATIONID = "locationID";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_BIO = "bio";
    //PHOTO COLUMN
    //CLUB ID
    private static final String COLUMN_ROOMNUMBER = "roomNumber";

    /*EVENTCATEGORIES TABLE*/
    //EventID
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
    private static final String COLUMN_CAMPUS = "campus";

    //endregion

    //region CREATE TABLES
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY ," + COLUMN_USERNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_ROLE + " TEXT" + ")";

    private String CREATE_CLUB_TABLE = "CREATE TABLE " + TABLE_CLUB + "("
            + COLUMN_CLUBID + " INTEGER PRIMARY KEY ," + COLUMN_FACULTYEMAIL + " TEXT,"
            + COLUMN_CLUBNAME + " TEXT," + COLUMN_PHOTO + " BLOB," + COLUMN_USERNAME + " TEXT,"
            + COLUMN_CLUBEMAIL + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_ROLE + " TEXT," + COLUMN_USER_ID
            + " INTEGER NOT NULL," + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(ID) " + ")";

    private String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
            + COLUMN_EVENTID + " INTEGER PRIMARY KEY ," + COLUMN_EVENTNAME + " TEXT,"
            + COLUMN_LOCATIONID + " INTEGER," + COLUMN_TIME + " TEXT," + COLUMN_DATE + " INTEGER,"
            + COLUMN_BIO + " TEXT," + COLUMN_PHOTO + " BLOB," + COLUMN_CLUBID + " INTEGER,"
            + COLUMN_ROOMNUMBER + " INTEGER," + "FOREIGN KEY(" + COLUMN_LOCATIONID + ") REFERENCES " + TABLE_LOCATION + "(ID), "
            + "FOREIGN KEY(" + COLUMN_CLUBID + ") REFERENCES " + TABLE_CLUB + "(clubID) " +")";

    private String CREATE_EVENT_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_EVENTCATEGORIES + "("
            + COLUMN_EVENTID + " INTEGER," + COLUMN_CATEGORYID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_CATEGORYID + ") REFERENCES " + TABLE_CATEGORY + "(categoryID)," +
            "FOREIGN KEY(" + COLUMN_EVENTID + ") REFERENCES " + TABLE_EVENTS + "(eventID)" + ")";

    private String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORYID + " INTEGER PRIMARY KEY ," + COLUMN_PHOTO + " BLOB" + ")";

    private String CREATE_ACCOUNT_APPROVAL_TABLE = "CREATE TABLE " + TABLE_ACCOUNT_APPROVAL + "("
            + COLUMN_CLUBID +  " INTEGER PRIMARY KEY ," + COLUMN_ISAPPROVED + " INTEGER,"
            + COLUMN_REASON + " TEXT," + "FOREIGN KEY(" + COLUMN_CLUBID + ") REFERENCES " + TABLE_CLUB + "(clubID)" + ")";

    private String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
            + COLUMN_ADDRESS + " TEXT," + COLUMN_NAME + " TEXT," + COLUMN_CAMPUS + " TEXT" + ")";
    //endregion

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        //Default user values
        db.execSQL("insert into User (username, email, password, role) values ('jvice1', 'jvice1@kennesaw.edu', 'test', 'admin')");
        db.execSQL("insert into User (username, email, password, role) values ('jbrummel', 'jbrummel@kennesaw.edu', 'test', 'admin')");

        db.execSQL(CREATE_CLUB_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_EVENT_CATEGORIES_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);

        db.execSQL("insert into Category(categoryID, photo) values ('0, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('1, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('2, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('3, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('4, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('5, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('6, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('7, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('8, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('9, null')");
        db.execSQL("insert into Category(categoryID, photo) values ('10, null')");

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
        values.put(COLUMN_USER_ID, club.getUserID());
        values.put(COLUMN_FACULTYEMAIL, club.getFacultyEmail());
        values.put(COLUMN_PHOTO, club.getPhoto());
        values.put(COLUMN_CLUBNAME, club.getClubName());
        values.put(COLUMN_USERNAME, club.getUsername());
        values.put(COLUMN_PASSWORD, club.getPassword());
        values.put(COLUMN_EMAIL, club.getClubEmail());
        values.put(COLUMN_ROLE, club.getRole());

        db.insert(TABLE_CLUB, null, values);
        db.close();
    }

    public void addEvents(Events events){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENTNAME, events.getEventName());
        values.put(COLUMN_LOCATIONID, events.getLocationID());
        values.put(COLUMN_TIME, events.getTime());
        values.put(COLUMN_DATE, events.getDate());
        values.put(COLUMN_BIO, events.getBio());
        values.put(COLUMN_PHOTO, events.getPhoto());
        values.put(COLUMN_CLUBID, events.getClubID());
        values.put(COLUMN_ROOMNUMBER, events.getRoomNumber());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public void addEventCategories(EventCategories ec)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENTID, ec.getEventID());
        values.put(COLUMN_CATEGORYID, ec.getCategoryID());

        db.insert(TABLE_EVENTCATEGORIES, null, values);
        db.close();
    }

    public void addCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO, category.getImage());

        db.insert(TABLE_CATEGORY, null, values);
        db.close();
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
    //endregion

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.setUserID(c.getInt((c.getColumnIndex(COLUMN_USER_ID))));
                user.setEmail(c.getString((c.getColumnIndex(COLUMN_EMAIL))));
                user.setPassword(c.getString((c.getColumnIndex(COLUMN_EMAIL))));
                user.setRole(c.getString((c.getColumnIndex(COLUMN_ROLE))));

                users.add(user);
            } while (c.moveToNext());
        }
        return users;
    }

    public List<Club> getAllClubs() {
        List<Club> clubs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CLUB;

        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Club club = new Club();
                club.setClubID(c.getInt((c.getColumnIndex(COLUMN_CLUBID))));
                club.setUserID(c.getInt((c.getColumnIndex(COLUMN_USER_ID))));
                club.setFacultyEmail(c.getString((c.getColumnIndex(COLUMN_FACULTYEMAIL))));
                club.setClubEmail(c.getString((c.getColumnIndex(COLUMN_CLUBEMAIL))));
                club.setClubName(c.getString((c.getColumnIndex(COLUMN_CLUBNAME))));
                club.setUsername(c.getString((c.getColumnIndex(COLUMN_USERNAME))));
//                club.setPhoto(c.getBlob((c.getColumnIndex(COLUMN_PHOTO)))); Gotta figure out how pictures are saved properly into DB more
                club.setPassword(c.getString((c.getColumnIndex(COLUMN_EMAIL))));
                club.setRole(c.getString((c.getColumnIndex(COLUMN_ROLE))));

                clubs.add(club);
            } while (c.moveToNext());
        }
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
                event.setLocationID(c.getInt((c.getColumnIndex(COLUMN_LOCATIONID))));
                event.setTime(c.getString((c.getColumnIndex(COLUMN_TIME))));
                event.setDate(c.getInt((c.getColumnIndex(COLUMN_DATE))));
                event.setBio(c.getString((c.getColumnIndex(COLUMN_BIO))));
//                event.setPhoto(c.getBlob((c.getColumnIndex(COLUMN_PHOTO)))); Gotta figure out how pictures are saved properly into DB more
                event.setRoomNumber(c.getInt((c.getColumnIndex(COLUMN_ROOMNUMBER))));

                events.add(event);
            } while (c.moveToNext());
        }
        return events;
    }

    public int getEventCount() {
        String countQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
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

    public int updateEvent(Events events){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENTNAME, events.getEventName());
        values.put(COLUMN_LOCATIONID, events.getLocationID());
        values.put(COLUMN_TIME, events.getTime());
        values.put(COLUMN_DATE, events.getDate());
        values.put(COLUMN_BIO, events.getBio());
        values.put(COLUMN_PHOTO, events.getPhoto());
        values.put(COLUMN_CLUBID, events.getClubID());
        values.put(COLUMN_ROOMNUMBER, events.getRoomNumber());

        return db.update(TABLE_EVENTS, values, COLUMN_EVENTID + " = ?",
                new String[] {String.valueOf(events.getEventID())});

    }

    public int updateClub(Club club){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, club.getUserID());
        values.put(COLUMN_FACULTYEMAIL, club.getFacultyEmail());
        values.put(COLUMN_PHOTO, club.getPhoto());
        values.put(COLUMN_CLUBNAME, club.getClubName());
        values.put(COLUMN_USERNAME, club.getUsername());
        values.put(COLUMN_PASSWORD, club.getPassword());
        values.put(COLUMN_EMAIL, club.getClubEmail());
        values.put(COLUMN_ROLE, club.getRole());

        return db.update(TABLE_CLUB, values, COLUMN_CLUBID + " = ?",
                new String[] {String.valueOf(club.getClubID())});
    }

    public void deleteEvent(Events event){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, COLUMN_EVENTID + " = ?",
                new String[] {String.valueOf(event)});
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

        return cursorCount > 0;
    }
    public boolean checkUser(String username, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
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

        return cursorCount > 0;
    }
    //endregion
}
