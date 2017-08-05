package com.example.patrick.groceryapplication.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Jose
 *
 */

public class GroupList {

    private String name = "";
    private String description = "";
    private HashMap<String,Item> items;

    public GroupList() {
    }

    public GroupList(String name, String description, HashMap<String,Item> items) {
        this.name = name;
        this.description = description;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String,Item> getItems() {
        return items;
    }

    public void setItems(HashMap<String,Item> items) {
        this.items = items;
    }
}