package com.example.patrick.groceryapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrick.groceryapplication.utils.Contract;

import java.util.List;

/**
 * Created by Barry on 7/26/2017.
 */

public class MyListItemAdapter extends RecyclerView.Adapter<MyListItemAdapter.GroceryItemHolder>{

    public static final String TAG = "MyListItemAdapter";
    private Cursor cursor;
    private Context context;


    public MyListItemAdapter(Cursor cursor){
        this.cursor = cursor;
    }


    @Override
    public GroceryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.my_list_items, parent, false);
        GroceryItemHolder holder = new GroceryItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(GroceryItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class GroceryItemHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView quantity;
        CheckBox checkBox;

        long id;
        GroceryItemHolder(View view){
            super(view);

            image = (ImageView) view.findViewById(R.id.grocery_image);
            name = (TextView) view.findViewById(R.id.grocery_name);
            quantity = (TextView) view.findViewById(R.id.grocery_quantity);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        }

        public void bind(GroceryItemHolder holder, int pos){
//            cursor.moveToPosition(pos);
//            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_ID));
//            name.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME)));
//            quantity.setText(cursor.getInt(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY)));
            //image code is supposed to be here

            name.setText("gaaaaaaaaaaaaaaaaaaaaay");
            quantity.setText( ""+ 21);


        }


    }
}
