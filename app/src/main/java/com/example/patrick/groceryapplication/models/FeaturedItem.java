package com.example.patrick.groceryapplication.models;

import java.util.ArrayList;

public class FeaturedItem {
    private String Title;
    private ArrayList<Item> groceryList;
    private int itemCount;

    public FeaturedItem(String title, ArrayList<Item> groceryList, int itemCount) {
        Title = title;
        this.groceryList = groceryList;
        this.itemCount = itemCount;
    }
    public FeaturedItem(){}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public ArrayList<Item> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(ArrayList<Item> groceryList) {
        this.groceryList = groceryList;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
