package com.example.patrick.groceryapplication.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jose
 */
public class User {
    private String id = "";
    private String name;
    private String dob;
    private String location;
    private ArrayList<String> groupLists;
    public User() {
    }

    public User(String id,String name, String dob, String location,ArrayList<String> groupLists) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.location = location;
        this.groupLists=groupLists;

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

    public void setUser(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.dob = user.getDob();
        this.location = user.getLocation();
        this.groupLists=user.getGroupLists();
    }

    public ArrayList<String> getGroupLists() {
        return groupLists;
    }

    public void setGroupLists(ArrayList<String> groupLists) {
        this.groupLists = groupLists;
    }
}

