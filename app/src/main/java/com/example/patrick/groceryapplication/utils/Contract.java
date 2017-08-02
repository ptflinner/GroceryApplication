/**
 * 
 */
package com.example.patrick.groceryapplication.utils;
import android.provider.BaseColumns;
/**
 * @author Jose
 *
 */
public class Contract {

	public static class TABLE_ITEM implements BaseColumns{

		public static final String TABLE_NAME = "items";
		public static final String COLUMN_NAME_ID="_ID";
		public static final String COLUMN_NAME_ITEM_NAME ="name";
		public static final String COLUMN_NAME_QUANTITY = "quantity";
		public static final String COLUMN_NAME_PRICE = "price";
		public static final String COLUMN_NAME_PICTURE = "picture";
		public static final String COLUMN_NAME_CATEGORY = "category";
		public static final String COLUMN_NAME_PURCHASE_STATUS = "status";
	}
	public static class TABLE_LIST implements BaseColumns{

		public static final String TABLE_NAME = "list";
		public static final String COLUMN_NAME_ID="_ID";
		public static final String COLUMN_NAME_LIST_NAME = "listname";
		public static final String COLUMN_NAME_LIST_CATEGORY = "category";

	}

	public static class TABLE_COMPLETED_LIST implements BaseColumns {

		public static final String TABLE_NAME = "complete_list";
		public static final String COLUMN_NAME_LIST_ID = "list_id";
		public static final String COLUMN_NAME_ITEM_ID = "item_id";

	}
}
