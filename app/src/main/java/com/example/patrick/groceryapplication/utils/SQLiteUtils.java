package com.example.jose.sqlitedemo.data;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * @author Jose
 *
 */

import java.util.Calendar;

public class SQLiteUtils {

    public static final String TAG = "Running SQLiteUtils";

    //adding a new item into the database
    //taking the name, quantity, price, picture, and status of the item being inserting
    //the new item would be inserted into the list table
    //added TAG see debugger
    private long addItem(SQLiteDatabase db,String name, int quantity, int price, String status, String picture, String category){
        ContentValues value = new ContentValues();
        Log.d(TAG, " - Updating item: " + name);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME, name);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY, quantity);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PRICE, price);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY,category);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PICTURE, picture);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PURCHASE_STATUS, status);
        return db.insert(Contract.TABLE_ITEM.TABLE_NAME, null,value);
    }

    //adding a new list into the database
    //taking the name of the list and the category for the list and inserting
    //it into the table name list
    private long addList(SQLiteDatabase db, String category, String name){
        ContentValues value = new ContentValues();
        Log.d(TAG, " - Updating item: " + name);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME,name);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY,category);
        return db.insert(Contract.TABLE_LIST.TABLE_NAME, null,value);
    }

    //update item by grabbing its id and updating the items properties and its assigned id
    private int updateItem(SQLiteDatabase db, String name, int quantity, int price, String status, String picture,String category, long id){
        Log.d(TAG, " - Updating item: " + id);
        ContentValues value = new ContentValues();
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME, name);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY, quantity);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PRICE, price);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY,category);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PICTURE, picture);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PURCHASE_STATUS, status);
        return db.update(Contract.TABLE_ITEM.TABLE_NAME, value, Contract.TABLE_ITEM._ID + "=" + id,null);
    }

    //update list grabbing the list properties and its assigned id
    private int updateList(SQLiteDatabase db, String category, String name, long id){
        ContentValues value = new ContentValues();
        Log.d(TAG, " - Updating list: " + id);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME,name);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY,category);
        return db.update(Contract.TABLE_LIST.TABLE_NAME, value, Contract.TABLE_LIST._ID + "=" + id,null);
    }

    //grabs all items
    private Cursor getAllItems(SQLiteDatabase db) {
        Log.d(TAG, "- Getting all items");
        return db.query(Contract.TABLE_ITEM.TABLE_NAME,null,null,null,null,null,Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME);
    }

    //grabs all list
    private Cursor getAllList(SQLiteDatabase db) {
        Log.d(TAG, "- Getting all list");
        return db.query(Contract.TABLE_LIST.TABLE_NAME, null, null,null,null,Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME, Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY);

    }

    /**
     *
     * remove item and remove list based on it ID
     * TAG included
     *
     */
    private boolean removeItem(SQLiteDatabase db,long id){
        Log.d(TAG, " - Deleting item with id:" + id);
        return db.delete(Contract.TABLE_ITEM.TABLE_NAME, Contract.TABLE_ITEM._ID + "=" + id, null) > 0;
    }
    private boolean removeList(SQLiteDatabase db, long id){
        Log.d(TAG, " - Deleting List with id:" + id);
        return db.delete(Contract.TABLE_LIST.TABLE_NAME, Contract.TABLE_LIST._ID + "=" + id, null) > 0;
    }


    //returns cursor for the item base on category added a tag
    //to check if theirs any issues
    private Cursor getItemsByCategory(SQLiteDatabase db, String category) {
        Log.d(TAG, "Returning item by category" + category);
        return db.query(Contract.TABLE_ITEM.TABLE_NAME,null,Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY +
                "= '" + category + "'", null,null,null,Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME);
    }


    //returns cursor for the list base on category added a tag
    //to check if theirs any issues
    private Cursor getListByCategory(SQLiteDatabase db, String category){
        Log.d(TAG, "Returning list by category" + category);
        return db.query(Contract.TABLE_LIST.TABLE_NAME,null,Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY +
         "= '" + category + "'", null,null,null,Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME);
    }
}
