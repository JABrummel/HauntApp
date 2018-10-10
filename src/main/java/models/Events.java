package models;

import java.io.Serializable;

public class Events implements Serializable {
    private int eventID;
    private String eventName;
    private String location;
    private String startTime;
    private String endTime;
    private String date;
    private String bio;
    private byte[] photo;
    private int clubID;
    private int roomNumber;

    public Events() {}
    public Events(String eName, String locat, String starttime, String endtime,
                  String dateStr, String biog, byte[] photoData, int     clubid) {
        eventName = eName;
        location = locat;
        startTime = starttime;
        endTime = endtime;
        date = dateStr;
        bio = biog;
        photo = photoData;
        clubID = clubid;

    };

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
