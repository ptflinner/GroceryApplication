package com.example.patrick.groceryapplication.models;

/**
 * Created by Jose
 */
public class User {
    private String id = "";
    private String name;
    private String dob;
    private String location;

    public User() {
    }

    public User(String id,String name, String dob, String location) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

