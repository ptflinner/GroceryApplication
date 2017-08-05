package com.example.patrick.groceryapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.GroupList;
import com.example.patrick.groceryapplication.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupListItemFragment extends Fragment {

    private static final String TAG="GROUPLISTITEMFRAGMENT";
    private FloatingActionButton fab;
    private RecyclerView itemListRecyclerView;
    private FirebaseRecyclerAdapter<Item,GroupListItemFragment.ItemHolder> mItemListAdapter;
    private DatabaseReference itemRef;

    public GroupListItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        createAdapter();
    }

    public static GroupListItemFragment newInstance(String groupKey) {
        Bundle args = new Bundle();
        args.putString("key",groupKey);
        GroupListItemFragment fragment = new GroupListItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getGroupKey(){return getArguments().getString("key");}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_group_list, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab_group_list);

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//            FragmentManager fm = getSupportFragmentManager();
//            AddMyListFragment frag = new AddMyListFragment();
//            frag.show(fm, "addMyListFragment");
            firebaseGroupAdd(FirebaseDatabase.getInstance());
            Log.d(TAG,"RABBLE");
            }
        });

        itemListRecyclerView=(RecyclerView) view.findViewById(R.id.frag_group_list_recycler);
        itemListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemRef= FirebaseDatabase.getInstance().getReference("groupList").child(getGroupKey()).child("items");

        // Inflate the layout for this fragment
        return view;
    }

    public void createAdapter(){
        mItemListAdapter = new FirebaseRecyclerAdapter<Item, GroupListItemFragment.ItemHolder>(
                Item.class,
                R.layout.group_item_list_recycle,
                GroupListItemFragment.ItemHolder.class,
                itemRef) {
            @Override
            protected void populateViewHolder(ItemHolder viewHolder, Item model, final int position) {

                Log.d(TAG,"FIREBASE");
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment groupItems =GroupItemFragment.newInstance(getGroupKey(),mItemListAdapter.getRef(position).getKey());
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, groupItems);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });
                viewHolder.bind(model,position);
            }
        };
        itemListRecyclerView.setAdapter(mItemListAdapter);
    }

    public void firebaseGroupAdd(FirebaseDatabase fdb){
        DatabaseReference itemRef = fdb.getReference("groupList").child(getGroupKey()).child("items");
        Item item=new Item("Computer","Tech","1","Beep Boop");

//        itemsArr.add(new Item("Hamburger Patty","Meat","8","It is hamburger meat"));
//        itemsArr.add(new Item("Hamburger Buns","Wheat","8","It is a hamburger bun"));
//        itemsArr.add(new Item("Slammers Gift Card","Monetary","1","It is a gift card"));
//        GroupList groupList = new GroupList("Best Buy","Need cheap tech",itemsArr);
        itemRef.push().setValue(item);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        private TextView itemName;

        public ItemHolder(View itemView) {
            super(itemView);
            this.mView=itemView;
            itemName=(TextView) itemView.findViewById(R.id.group_item_name);
            itemView.setOnClickListener(this);

        }

        public void bind(Item item, int position){
            Log.d(TAG,"LIST NAME:"+item.getName());
            itemName.setText(item.getName());
        }

        @Override
        public void onClick(View view) {
//            int pos=getAdapterPosition();
//            mItemClick.onItemClickListener(pos,groupLists.get(pos));
        }
    }
}
