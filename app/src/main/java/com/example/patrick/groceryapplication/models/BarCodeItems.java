package com.example.patrick.groceryapplication.models;

/**
 * Created by Nomis on 8/3/2017.
 */

public class BarCodeItems {

    private String number;
    private String itemname;
    private String description;
    private String avg_price;

    public BarCodeItems(String number,String itemName, String description, String avg_price){

    }
    public BarCodeItems(){
        this.number="";
        this.itemname="";
        this.description="";
        this.avg_price="";
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getItemName() {
        return itemname;
    }

    public void setItemName(String itemname) {
        this.itemname = itemname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }
}
