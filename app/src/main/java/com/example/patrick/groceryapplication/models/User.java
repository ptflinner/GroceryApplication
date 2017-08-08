package com.example.patrick.groceryapplication.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jose
 */
public class User {
    private String id = "";
    private String firstName;
    private String lastName;
    private String displayName;
    private ArrayList<String> groupLists;
    public User() {
    }

    public User(String id,String firstName, String lastName, String displayName,ArrayList<String> groupLists) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.groupLists=groupLists;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUser(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.displayName = user.getDisplayName();
        this.groupLists=user.getGroupLists();
    }

    public ArrayList<String> getGroupLists() {
        return groupLists;
    }

    public void setGroupLists(ArrayList<String> groupLists) {
        this.groupLists = groupLists;
    }
}

