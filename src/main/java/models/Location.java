package models;

public class Location {
    private int locationID;
    private String address;
    private String name;
    private String campus;
    private double Long;
    private double Lat;

    public Location(double aLong, double lat, String name, String campus) {
        this.name = name;
        this.campus = campus;
        Long = aLong;
        Lat = lat;
    }

    public Location(String address, String name, String campus) {
        this.address = address;
        this.name = name;
        this.campus = campus;
    }
    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
