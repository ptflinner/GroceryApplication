package com.example.patrick.groceryapplication.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.Contract;
import com.example.patrick.groceryapplication.utils.DBHelper;
import com.example.patrick.groceryapplication.utils.SQLiteUtils;

/**
 * Created by Barry on 8/4/2017.
 */

public class MyListItemDetailFragment extends Fragment{
    private static final String TAG = "MyListItemDetailFragment";
    private Cursor cursor;
    private DBHelper helper;
    private SQLiteDatabase db;
    private TextView itemName;
    private TextView itemPrice;
    private TextView itemQuantity;
    private Button updateButton;
    private Button deleteButton;
    public MyListItemDetailFragment(){}

    public static MyListItemDetailFragment newInstance(long id){
        Bundle args = new Bundle();
        MyListItemDetailFragment fragment = new MyListItemDetailFragment();
        args.putLong("id", id);
        fragment.setArguments(args);

        return fragment;
    }

    public long getItemId(){ return getArguments().getLong("id");}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_list_item_detail, container, false);


        itemName = (TextView) view.findViewById(R.id.item_detail_name);
        itemPrice = (TextView) view.findViewById(R.id.item_detail_price);
        itemQuantity = (TextView) view.findViewById(R.id.item_detail_quantity);
        updateButton = (Button) view.findViewById(R.id.update_button);
        deleteButton = (Button) view.findViewById(R.id.delete_button);


        updateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //update fragment click listener
                Fragment updateItem = UpdateMyListItem.newInstance(getItemId());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, updateItem);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SQLiteUtils.removeItem(db, getItemId());
                getFragmentManager().popBackStackImmediate();
            }
        });

        return view;
    }public Cursor getItemDetails(SQLiteDatabase db){
        String[] selectionArgs = {String.valueOf(getItemId())};
        String query = "select items._id, items.name, items.price, items.quantity" +
                " from items " +
                "where items._ID = ?";

        return db.rawQuery(query, selectionArgs);
    }

    @Override
    public void onStart() {
        super.onStart();
        helper = new DBHelper(getActivity());
        db = helper.getWritableDatabase();
        cursor = getItemDetails(db);



        cursor.moveToFirst();
        String price = String.format("$%,.2f", cursor.getDouble(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_PRICE)));
        itemName.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME)));
        itemPrice.setText(price);
        itemQuantity.setText("x" + cursor.getInt(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY)));

    }

    @Override
    public void onStop() {
        super.onStop();
        if(db != null) db.close();
        if(cursor != null) cursor.close();
    }
}