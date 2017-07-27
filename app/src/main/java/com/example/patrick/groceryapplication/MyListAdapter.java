package com.example.patrick.groceryapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Barry on 7/26/2017.
 */

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ItemHolder>{

    public MyListAdapter(){

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_item_list, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView category;

        ItemHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.my_list_title);
            category = (TextView) view.findViewById(R.id.my_list_category);
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos){
            title.setText("My Big List of Colorful Dildos");
            category.setText("Category: gaygay gay");
        }

        @Override
        public void onClick(View v) {
            //goto grocerylist list thing. i think.
        }
    }

}
