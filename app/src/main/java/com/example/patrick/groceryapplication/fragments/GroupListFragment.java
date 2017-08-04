package com.example.patrick.groceryapplication.fragments;

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

import com.example.patrick.groceryapplication.MainActivity;
import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.adapters.GroupListAdapter;
import com.example.patrick.groceryapplication.models.GroupList;
import com.example.patrick.groceryapplication.models.Item;
import com.example.patrick.groceryapplication.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;

public class GroupListFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView groupListRecyclerView;
    private ArrayList<GroupList> groupArray;
    private static final String TAG="GROUPLISTFRAGMENT";
    private FirebaseRecyclerAdapter<GroupList,ItemHolder> mGroupListAdapter;
    private DatabaseReference groupRef;

    public GroupListFragment(){

    }

    public static GroupListFragment newInstance() {
        GroupListFragment fragment = new GroupListFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupArray=new ArrayList<>();
        View view=inflater.inflate(R.layout.fragment_group_list, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab_group_list);

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                FragmentManager fm = getSupportFragmentManager();
//                AddMyListFragment frag = new AddMyListFragment();
//                frag.show(fm, "addMyListFragment");
                firebaseGroupAdd(FirebaseDatabase.getInstance());
                Log.d(TAG,"BUTTON CLICKED");
            }
        });

        groupListRecyclerView=(RecyclerView) view.findViewById(R.id.frag_group_list_recycler);
        groupListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupRef=FirebaseDatabase.getInstance().getReference("groupList");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createAdapter();
    }

    public void createAdapter(){
        mGroupListAdapter = new FirebaseRecyclerAdapter<GroupList, ItemHolder>(
                GroupList.class,
                R.layout.group_list_recycle_item,
                ItemHolder.class,
                groupRef
        ) {
            @Override
            protected void populateViewHolder(ItemHolder viewHolder, GroupList model, final int position) {

//                mGroupListAdapter.getRef(position).getKey();
                Log.d(TAG,"FIREBASE");
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment groupListItems =GroupListItemFragment.newInstance(mGroupListAdapter.getRef(position).getKey());
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, groupListItems);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                viewHolder.bind(model,position);
            }
        };
        groupListRecyclerView.setAdapter(mGroupListAdapter);
    }

    public void firebaseGroupAdd(FirebaseDatabase fdb){
        DatabaseReference groupRef = fdb.getReference("groupList");
        ArrayList<Item> itemsArr = new ArrayList<>();
//        itemsArr.add(new Item("Hamburger Patty","Meat","8","It is hamburger meat"));
//        itemsArr.add(new Item("Hamburger Buns","Wheat","8","It is a hamburger bun"));
//        itemsArr.add(new Item("Slammers Gift Card","Monetary","1","It is a gift card"));
        GroupList groupList = new GroupList("Best Buy","Need cheap tech",itemsArr);
        groupRef.push().setValue(groupList);
    }
    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        private TextView listName;

        public ItemHolder(View itemView) {
            super(itemView);
            this.mView=itemView;
            listName=(TextView) itemView.findViewById(R.id.group_list_name);
            itemView.setOnClickListener(this);

        }

        public void bind(GroupList groupList, int position){
            Log.d(TAG,"LIST NAME:"+groupList.getName());
            listName.setText(groupList.getName());
        }

        @Override
        public void onClick(View view) {
//            int pos=getAdapterPosition();
//            mItemClick.onItemClickListener(pos,groupLists.get(pos));
        }
    }
}
