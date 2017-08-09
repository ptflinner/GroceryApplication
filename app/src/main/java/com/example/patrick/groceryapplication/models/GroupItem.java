package com.example.patrick.groceryapplication.models;

import android.graphics.Bitmap;

/**
 * Created by Patrick on 8/6/2017.
 */

public class GroupItem {
    private String name;
    private String category;
    private String count;
    private String price;
    private String provider;
    private String imageName;
    private Bitmap image;

    public GroupItem() {
        this.name="";
        this.category="";
        this.count="";
        this.price="";
        this.provider="";
    }

    public GroupItem(String name, String category, String count, String price,String provider) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.price = price;
        this.provider=provider;
    }
    public GroupItem(String name, String category, String count, String description,String provider, Bitmap image) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.price = description;
        this.provider=provider;
        this.image=image;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String description) {
        this.price = description;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
