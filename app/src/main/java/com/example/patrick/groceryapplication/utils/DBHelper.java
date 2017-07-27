package com.example.jose.sqlitedemo.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * @author Jose
 *
 */

public class DBHelper extends SQLiteOpenHelper{


	private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_NAME = "shopping.db";
		private static final String TAG ="database helper tag";
		
		
		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null ,DATABASE_VERSION);
		}

		//creates table for the items and the list creating the join between both
		@Override
		public void onCreate(SQLiteDatabase db) {
			String table_item_query = "CREATE TABLE " + Contract.TABLE_ITEM.TABLE_NAME
					+ " (" + Contract.TABLE_ITEM._ID + " INTEGER," +
					Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME + " TEXT NOT NULL," +
					Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY + " INTEGER," +
					Contract.TABLE_ITEM.COLUMN_NAME_PRICE + " INTEGER," +
					Contract.TABLE_ITEM.COLUMN_NAME_PICTURE + " TEXT NOT NULL," +
					Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY + "TEXT NOT NULL," +
					Contract.TABLE_ITEM.COLUMN_NAME_PURCHASE_STATUS + " TEXT NOT NULL DEFAULT 'Not purchased'" +
					"FOREIGN KEY(table_item_id) REFERENCES list(table_list_id)" +
					"); ";
			Log.d(TAG, "Create table one SQL:" + table_item_query);
			db.execSQL(table_item_query);

			String table_list_query = "CREATE TABLE " + Contract.TABLE_LIST.TABLE_NAME +
					" (" + Contract.TABLE_LIST._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME + " TEXT NOT NULL," +
					Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY + "TEXT NOT NULL," +
					"); ";
			Log.d(TAG, "Create table two SQL:" + table_list_query);
			db.execSQL(table_list_query);
		}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + Contract.TABLE_ITEM.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS" + Contract.TABLE_LIST.TABLE_NAME);
	}
		
}