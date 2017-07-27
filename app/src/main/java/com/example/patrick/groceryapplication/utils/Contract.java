/**
 * 
 */
package com.example.jose.sqlitedemo.data;
import android.provider.BaseColumns;
/**
 * @author Jose
 *
 */
public class Contract {
		
		public static class TABLE_ITEM implements BaseColumns{
			
			public static final String TABLE_NAME = "items";
			public static final String COLUMN_NAME_ITEM_NAME ="name";
			public static final String COLUMN_NAME_QUANTITY = "quantity";
			public static final String COLUMN_NAME_PRICE = "price";
			public static final String COLUMN_NAME_PURCHASE_STATUS = "status";
			public static final String COLUMN_NAME_PICTURE = "picture";
			public static final String COLUMN_NAME_CATEGORY = "category";
		}
		public static class TABLE_LIST implements BaseColumns{
			
			public static final String TABLE_NAME = "list";
			public static final String COLUMN_NAME_LIST_NAME = "listname";
			public static final String COLUMN_NAME_LIST_CATEGORY = "category";
			
			
			
		}
}
