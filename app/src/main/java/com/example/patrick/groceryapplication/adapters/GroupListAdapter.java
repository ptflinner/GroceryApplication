package com.example.patrick.groceryapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.GroupList;

import java.util.ArrayList;

/**
 * Created by Patrick on 8/3/2017.
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ItemHolder>{
    private ArrayList<GroupList> groupLists;
    private ItemClickListener mItemClick;
    private final String TAG="GROUP LIST ADAPTER";

    public GroupListAdapter(ArrayList<GroupList> groupLists,ItemClickListener listener){
        this.groupLists=groupLists;
        this.mItemClick=listener;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.group_list_recycle_item,parent,false);
        ItemHolder holder=new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder,position);
    }

    @Override
    public int getItemCount() {
        return groupLists.size();
    }

    public interface ItemClickListener{
        void onItemClickListener(int pos,GroupList list);
    }
    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView listName;

        public ItemHolder(View itemView) {
            super(itemView);
            listName=(TextView) itemView.findViewById(R.id.group_list_name);
            itemView.setOnClickListener(this);

        }

        public void bind(ItemHolder holder, int position){
            Log.d(TAG,"LIST NAME:"+groupLists.get(position).getName());
            listName.setText(groupLists.get(position).getName());
        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            mItemClick.onItemClickListener(pos,groupLists.get(pos));
        }
    }
}
