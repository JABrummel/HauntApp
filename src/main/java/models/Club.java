package models;

import java.io.Serializable;
import java.sql.Blob;

public class Club implements Serializable {

    private String clubEmail;
    private byte[] photo;
    private int userID;
    private String clubName;
    private String username;
    private String facultyEmail;
    private String password;
    private String role;
    private String bio;
    private String approved;

    public Club() {}

    public Club ( String fac, String clubn, byte[] photoimg, String un, String em,
                 String pw, String role, String bio, String approved) {

        facultyEmail = fac;
        clubName = clubn;
        photo = photoimg;
        username = un;
        password = pw;
        this.role = role;
        this.bio= bio;
        this.approved = approved;
        clubEmail = em;

    }

 //"CREATE TABLE " + TABLE_CLUB + "("
    //            + COLUMN_CLUBID + " INTEGER PRIMARY KEY ," + COLUMN_FACULTYEMAIL + " TEXT,"
    //            + COLUMN_CLUBNAME + " TEXT," + COLUMN_PHOTO + " BLOB," + COLUMN_USERNAME + " TEXT,"
    //            + COLUMN_CLUBEMAIL + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_ROLE + " TEXT,"
    //            + COLUMN_BIO + " TEXT," + COLUMN_APPROVED + " TEXT" + ")";

    public String getFacultyEmail() {
        return facultyEmail;
    }

    public void setFacultyEmail(String facultyEmail) {
        this.facultyEmail = facultyEmail;
    }

    public String getClubEmail() {
        return clubEmail;
    }

    public void setClubEmail(String clubEmail) {
        this.clubEmail = clubEmail;
    }



    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String a) {
        approved = a;
    }
}
