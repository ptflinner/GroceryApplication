package com.example.patrick.groceryapplication.models;


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
