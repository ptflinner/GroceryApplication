package com.example.patrick.groceryapplication.models;

/**
 * Created by Patrick on 7/9/2017.
 */

public class Item {
    private String name;
    private String category;
    private String count;
    private String description;

    public Item() {
    }

    public Item(String name, String category, String count, String description) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.description = description;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
