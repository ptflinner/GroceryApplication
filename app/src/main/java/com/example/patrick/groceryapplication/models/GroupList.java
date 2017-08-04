package com.example.patrick.groceryapplication.models;

import java.util.ArrayList;

/**
 *
 * Created by Jose
 *
 */

public class GroupList {

    private String name = "";
    private String description = "";
    private ArrayList<Item> items;

    public GroupList() {
    }

    public GroupList(String name, String description, ArrayList<Item> items) {
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

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
