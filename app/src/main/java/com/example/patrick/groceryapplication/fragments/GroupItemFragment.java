package com.example.patrick.groceryapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.GroupItem;
import com.example.patrick.groceryapplication.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GroupItemFragment extends Fragment {

    private TextView nameTV;
    private TextView countTV;
    private TextView categoryTV;
    private TextView priceTV;
    private TextView providerTV;

    private Button updateButton;

    private final static String TAG="ITEM FRAG";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static GroupItemFragment newInstance(String groupKey,String itemKey) {

        Bundle args = new Bundle();
        args.putString("groupKey",groupKey);
        args.putString("itemKey",itemKey);
        GroupItemFragment fragment = new GroupItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getGroupKey(){return getArguments().getString("groupKey");}
    public String getItemKey(){return getArguments().getString("itemKey");}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_group_item, container, false);
        nameTV=(TextView) view.findViewById(R.id.name_group_item_tv);
        countTV=(TextView) view.findViewById(R.id.count_group_item_tv);
        categoryTV=(TextView) view.findViewById(R.id.category_group_item_tv);
        priceTV=(TextView) view.findViewById(R.id.group_item_price);
        updateButton=(Button) view.findViewById(R.id.update_item);
        providerTV=(TextView) view.findViewById(R.id.provider_group_item_tv);
        DatabaseReference itemRef=(FirebaseDatabase.getInstance())
                .getReference("groupList")
                .child(getGroupKey())
                .child("items")
                .child(getItemKey());

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ITEM FRAG",getGroupKey());
                Log.d("ITEM FRAG",getItemKey());

                GroupItem item=(dataSnapshot.getValue(GroupItem.class));
                nameTV.setText(item.getName());
                countTV.setText(item.getPrice());
                categoryTV.setText(item.getCategory());
                priceTV.setText("$"+item.getCount());
                providerTV.setText(item.getProvider());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update fragment click listener
                Fragment updateItem = UpdateGroupItem.newInstance(getGroupKey(),getItemKey());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, updateItem);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
