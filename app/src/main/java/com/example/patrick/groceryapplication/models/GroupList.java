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
    private HashMap<String,String> users;

    public GroupList() {
    }

    public GroupList(String name, String category) {
        this.name = name;
        this.category = category;
        this.items = new HashMap<>();
        this.users =new HashMap<>();
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

    public HashMap<String, String> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, String> users) {
        this.users = users;
    }
}
