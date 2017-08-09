package com.example.patrick.groceryapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.Contract;

/**
 * Created by Barry on 7/26/2017.
 */

public class MyListItemAdapter extends RecyclerView.Adapter<MyListItemAdapter.GroceryItemHolder>{

    public static final String TAG = "MyListItemAdapter";
    private Cursor cursor;
    private Context context;
    private ItemClickListener listener;


    public MyListItemAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener = listener;
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

    public interface ItemClickListener{
        void onItemClick(Cursor cursor, int clickedItemIndex, long id);
    }

    class GroceryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        TextView quantity;
        TextView price;
        TextView category;
        CheckBox checkBox;

        long id;
        GroceryItemHolder(View view){
            super(view);

            image = (ImageView) view.findViewById(R.id.grocery_image);
            name = (TextView) view.findViewById(R.id.grocery_name);
            quantity = (TextView) view.findViewById(R.id.grocery_quantity);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            price = (TextView) view.findViewById(R.id.grocery_price);
//            category = (TextView) view.findViewById(R.id.grocery_category);
            view.setOnClickListener(this);

        }

        public void bind(GroceryItemHolder holder, int pos){
            cursor.moveToPosition(pos);

            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_ID));
            name.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME)));
            quantity.setText("Quantity: x" + cursor.getInt(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY)));
            String priceFormat = String.format("Price: $%,.2f", cursor.getDouble(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_PRICE)));
            price.setText(priceFormat);
//            category.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY)));
            //image code is supposed to be here
            holder.itemView.setTag(id);

        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(cursor, pos, id);
            Log.d(TAG, "" + pos);
        }
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
