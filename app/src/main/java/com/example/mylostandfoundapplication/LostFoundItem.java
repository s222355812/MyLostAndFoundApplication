package com.example.mylostandfoundapplication;

public class LostFoundItem {
    private int id;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private double latitude; // Add latitude field
    private double longitude; // Add longitude field

    public LostFoundItem(int id, String name, String phone, String description, String date, String location, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
