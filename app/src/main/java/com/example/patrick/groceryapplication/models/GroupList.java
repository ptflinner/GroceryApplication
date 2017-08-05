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
    private String category = "";
    private HashMap<String,Item> items;

    public GroupList() {
    }

    public GroupList(String name, String category, HashMap<String,Item> items) {
        this.name = name;
        this.category = category;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public HashMap<String,Item> getItems() {
        return items;
    }

    public void setItems(HashMap<String,Item> items) {
        this.items = items;
    }
}
