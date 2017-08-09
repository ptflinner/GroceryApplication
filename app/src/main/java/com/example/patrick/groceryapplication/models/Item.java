package com.example.patrick.groceryapplication.models;


import android.graphics.Bitmap;

public class Item {
    private String name;
    private String category;
    private String count;
    private String description;
    //private Bitmap image;
    private String price;

    public Item() {
        this.name="";
        this.category="";
        this.count="";
        this.price="";
    }

    public Item(String name, String category, String count, String price) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.price = price;
    }

    public Item(String name){
        this.name = name;
        this.category="";
        this.count="";
        this.price="";
    }
/*    public Item(String name, String category, String count, String description,Bitmap image) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.description = description;
        this.image = image;
    }*/

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String description) {
        this.price = description;
    }
/*    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }*/
}
