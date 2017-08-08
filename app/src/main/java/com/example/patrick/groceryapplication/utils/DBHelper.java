package com.example.patrick.groceryapplication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/*
 * @author Jose
 *
 */

public class DBHelper extends SQLiteOpenHelper{


	private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_NAME = "shopping.db";
		private static final String TAG ="database helper tag";


    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        Log.d(TAG, "Enabling Foreign Ket "  + db);
    }

    public DBHelper(Context context) {
			super(context, DATABASE_NAME, null ,DATABASE_VERSION);
		}

    //creates table for the items and the list creating the join between both
    /*
    * Change (1)
    * made price into a double and change the column id
    * Change (2)
    * update the reference list to link both tables ides to query with the
    * reference list
    *
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String table_item_query = "CREATE TABLE " + Contract.TABLE_ITEM.TABLE_NAME
            + " (" + Contract.TABLE_ITEM.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME + " TEXT NOT NULL, " +
            Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY + " INTEGER, " +
            Contract.TABLE_ITEM.COLUMN_NAME_PRICE + " DOUBLE, " +
            Contract.TABLE_ITEM.COLUMN_NAME_PICTURE + " TEXT, " +
            Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY + " TEXT NOT NULL, " +
            Contract.TABLE_ITEM.COLUMN_NAME_PURCHASE_STATUS + " INTEGER NOT NULL" +
            "); ";
        Log.d(TAG, "Create table one SQL:" + table_item_query);
        db.execSQL(table_item_query);

        String table_list_query = "CREATE TABLE " + Contract.TABLE_LIST.TABLE_NAME +
            " (" + Contract.TABLE_LIST.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME + " INTEGER, " +
            Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY + " INTEGER " +
            "); ";
        Log.d(TAG, "Create table two SQL:" + table_list_query);
        db.execSQL(table_list_query);

        String reference_list_to_item = "CREATE TABLE " + Contract.TABLE_COMPLETED_LIST.TABLE_NAME + " ("
            + Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_LIST_ID + " INTEGER,"
            + Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_ITEM_ID + " INTEGER,"
            + " FOREIGN KEY(" + Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_LIST_ID + ") "
            + " REFERENCES " + Contract.TABLE_LIST.TABLE_NAME + " (" + Contract.TABLE_LIST.COLUMN_NAME_ID + "),"
            + " FOREIGN KEY(" + Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_ITEM_ID + ") "
            + " REFERENCES " + Contract.TABLE_ITEM.TABLE_NAME + " (" + Contract.TABLE_ITEM.COLUMN_NAME_ID + ")); ";
        Log.d(TAG, " - Merging both tables execSQL " + reference_list_to_item);
        db.execSQL(reference_list_to_item);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + Contract.TABLE_ITEM.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS" + Contract.TABLE_LIST.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + Contract.TABLE_COMPLETED_LIST.TABLE_NAME);
	}
}
