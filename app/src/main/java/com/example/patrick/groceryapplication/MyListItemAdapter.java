package com.example.patrick.groceryapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Barry on 7/26/2017.
 */

public class MyListItemAdapter {
    public MyListItemAdapter(){

    }





    class GroceryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView name;
        TextView quantity;
        TextView price;
        CheckBox checkBox;

        GroceryItemHolder(View view){
            super(view);

            image = (ImageView) view.findViewById(R.id.grocery_image);
            name = (TextView) view.findViewById(R.id.grocery_name);
            quantity = (TextView) view.findViewById(R.id.grocery_quantity);
            price = (TextView) view.findViewById(R.id.grocery_price);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            view.setOnClickListener(this);
        }

        public void bind(MyListAdapter.ItemHolder holder, int pos){
            //bind codecodecodecodecodecodecodecodecodecodecode
        }

        @Override
        public void onClick(View v) {

        }
    }
}
