package com.example.patrick.groceryapplication.utils;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * @author Jose
 *
 */


public class SQLiteUtils {

    public static final String TAG = "Running SQLiteUtils";
    Cursor cursor;
    //adding a new item into the database
    //taking the name, quantity, price, picture, and status of the item being inserting
    //the new item would be inserted into the list table
    //added TAG see debugger
    public long addItem(SQLiteDatabase db,String name, int quantity, double price, String picture, String category, String status){
        ContentValues value = new ContentValues();
        Log.d(TAG, " - Adding item: " + name + quantity + price + picture + category + status + db);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME, name);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY, quantity);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PRICE, price);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PICTURE, picture);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY, category);
        value.put(Contract.TABLE_ITEM.COLUMN_NAME_PURCHASE_STATUS, status);
        return db.insert(Contract.TABLE_ITEM.TABLE_NAME,null,value);

    }

    //adding a new list into the database
    //taking the name of the list and the category for the list and inserting
    //it into the table name list
    public long addList(SQLiteDatabase db, String name, String category){
        ContentValues value = new ContentValues();
        Log.d(TAG, " - Adding to list item: " + name + " into db " + db);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME,name);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY,category);
        return db.insert(Contract.TABLE_LIST.TABLE_NAME, null,value);
    }

    //update item by grabbing its id and updating the items properties and its assigned id
    public int updateItem(SQLiteDatabase db, String name, int quantity, double price, String status, String picture,String category, long id){
        Log.d(TAG, " - Updating item: " + id + name + quantity + price + picture + category + status + db);
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
    public int updateList(SQLiteDatabase db, String category, String name, long id){
        ContentValues value = new ContentValues();
        Log.d(TAG, " - Updating list: " + id + category + name);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME,name);
        value.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY,category);
        return db.update(Contract.TABLE_LIST.TABLE_NAME, value, Contract.TABLE_LIST._ID + "=" + id,null);
    }

    //adds your personalist using the ref table
    public long addMyList(SQLiteDatabase db, int listID, int itemID){
        Log.d(TAG," Adding your personal list " + listID + " - " + itemID + db);
        ContentValues value = new ContentValues();
        value.put(Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_LIST_ID, listID);
        value.put(Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_ITEM_ID, itemID);
        return db.insert(Contract.TABLE_COMPLETED_LIST.TABLE_NAME, null, value);
    }

    //grabs all items
    public Cursor getAllItems(SQLiteDatabase db) {
        Log.d(TAG, "- Getting all items");
        return db.query(Contract.TABLE_ITEM.TABLE_NAME,null,null,null,null,null,Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME);
    }

    //grabs all list
    public Cursor getAllList(SQLiteDatabase db) {
        Log.d(TAG, "- Getting all list");
        return db.query(Contract.TABLE_LIST.TABLE_NAME, null, null,null,null,Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME, Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY);

    }

    /**
     *
     * remove item and remove list based on it ID
     * TAG included
     *
     */
    public static boolean removeItem(SQLiteDatabase db,long id){
        Log.d(TAG, " - Deleting item with id:" + id + db);
        db.delete(Contract.TABLE_COMPLETED_LIST.TABLE_NAME, Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_ITEM_ID + "=" + id, null);
        return db.delete(Contract.TABLE_ITEM.TABLE_NAME, Contract.TABLE_ITEM._ID + "=" + id, null) >= 0;
    }
    public static boolean removeList(SQLiteDatabase db, long id){
        Log.d(TAG, " - Deleting List with id:" + id);
        db.delete(Contract.TABLE_COMPLETED_LIST.TABLE_NAME, Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_LIST_ID + "=" + id, null);
        return db.delete(Contract.TABLE_LIST.TABLE_NAME, Contract.TABLE_LIST._ID + "=" + id, null) >= 0;
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
