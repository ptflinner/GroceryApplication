package com.example.patrick.groceryapplication.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.Contract;


/**
 * Created by Barry on 7/26/2017.
 */

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ItemHolder>{

    public static final String TAG = "MyListAdapter";
    private Cursor cursor;
    private Context context;
    private ItemClickListener listener;



    public MyListAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.my_lists, parent, false);
        ItemHolder viewHolder = new ItemHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface ItemClickListener{
        void onItemClick(Cursor cursor, int clickedItemIndex);
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView category;
        long id;
        ItemHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.my_list_title);
            category = (TextView) view.findViewById(R.id.my_list_category);
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos){
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_LIST.COLUMN_NAME_ID));
            title.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME)));
            category.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY)));
            holder.itemView.setTag(id);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(cursor, pos);

        }
    }

}

