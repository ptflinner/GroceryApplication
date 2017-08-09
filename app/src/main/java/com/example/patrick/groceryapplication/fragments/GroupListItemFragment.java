package com.example.patrick.groceryapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.GroupItem;
import com.example.patrick.groceryapplication.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupListItemFragment extends Fragment {

    private static final String TAG="GROUPLISTITEMFRAGMENT";
    private FloatingActionButton fab;
    private RecyclerView itemListRecyclerView;
    private FirebaseRecyclerAdapter<Item,GroupListItemFragment.ItemHolder> mItemListAdapter;
    private DatabaseReference itemRef;
    private static final int REQUEST_CODE=123;

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

    public static GroupListItemFragment newInstance(String groupKey,boolean admin) {
        Bundle args = new Bundle();
        args.putString("key",groupKey);
        args.putBoolean("admin",admin);
        GroupListItemFragment fragment = new GroupListItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getGroupKey(){return getArguments().getString("key");}

    public Boolean getAdminRights(){return getArguments().getBoolean("admin");}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_group_list, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab_group_list);

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                AddGroupListItemFragment frag = new AddGroupListItemFragment().newInstance(getGroupKey());
                frag.setTargetFragment(GroupListItemFragment.this,REQUEST_CODE);
                frag.show(fm, "addGroupListItemFragment");
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
                viewHolder.bind(viewHolder,model,position);
            }
        };
        itemListRecyclerView.setAdapter(mItemListAdapter);

        Log.d(TAG,"Admin: "+getAdminRights());
        if(getAdminRights()){
            //Allows for items to be deleted by swiping
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    int id = (int)viewHolder.itemView.getTag();
                    String itemKey=mItemListAdapter.getRef(id).getKey();
                    itemRef.child(itemKey).removeValue();
                    createAdapter();
                    Log.d(TAG, "passing id: " + itemKey);
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View itemView=viewHolder.itemView;

                    Paint p = new Paint();
                    p.setColor(getResources().getColor(R.color.colorAccent));
                    if (dX > 0) {
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);
                    } else {
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(itemListRecyclerView);
        }
    }


    public void firebaseGroupAdd(FirebaseDatabase fdb,GroupItem item){
        DatabaseReference itemRef = fdb.getReference("groupList").child(getGroupKey()).child("items");
        itemRef.push().setValue(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== AddGroupListItemFragment.REQUEST_CODE){
            Bundle extras=data.getBundleExtra("args");
            //Bitmap bitmap = getArguments().getParcelable("image");
            Bundle bundle = getArguments();
            Bitmap bitmap = bundle.getParcelable("image");
            GroupItem item= new GroupItem(extras.getString("Name"),extras.getString("category"),extras.getString("Quantity"),extras.getString("Price"),extras.getString("provider"),
                    bitmap);
            firebaseGroupAdd(FirebaseDatabase.getInstance(), item);
            Log.d(TAG, "BUTTONCLICKED");
        }
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        private TextView itemName;
        private TextView itemQuantity;

        public ItemHolder(View itemView) {
            super(itemView);
            this.mView=itemView;
            itemName=(TextView) itemView.findViewById(R.id.group_item_name);
            itemQuantity=(TextView) itemView.findViewById(R.id.group_item_quantity);
            itemView.setOnClickListener(this);

        }

        public void bind(ItemHolder holder,Item item, int position){
            Log.d(TAG,"LIST NAME:"+item.getName());
            itemName.setText(item.getName());
            itemQuantity.setText("$"+item.getCount());
            holder.itemView.setTag(position);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
