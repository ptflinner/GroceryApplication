package com.example.patrick.groceryapplication.models;

/**
 * Created by Patrick on 7/9/2017.
 */

public class Item {
    private String itemName;

    public Item(){
        this.itemName="";
    }

    public Item(String itemName){
        this.itemName=itemName;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
