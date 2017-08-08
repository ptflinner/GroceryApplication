package com.example.patrick.groceryapplication.models;

/**
 * Created by Patrick on 8/8/2017.
 */

public class AdminHolder {
    boolean admin;

    public AdminHolder(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
