package com.example.patrick.groceryapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Patrick on 8/3/2017.
 */

public class GroupListItemListAdapter extends RecyclerView.Adapter<GroupListItemListAdapter.ItemHolder>{


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface ItemClickListener{
        void onItemClickListener();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        public ItemHolder(View itemView) {
            super(itemView);
        }

        public void bind(){

        }
    }
}
