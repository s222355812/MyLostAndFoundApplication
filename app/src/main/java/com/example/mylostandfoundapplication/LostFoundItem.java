package com.example.mylostandfoundapplication;

public class LostFoundItem {
    private int id;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;

    public LostFoundItem(int id, String name, String phone, String description, String date, String location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
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
}
