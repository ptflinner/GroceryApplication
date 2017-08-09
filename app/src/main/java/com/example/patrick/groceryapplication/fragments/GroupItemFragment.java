package com.example.patrick.groceryapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
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
    private TextView descriptionTV;
    //private ImageView imageTV;
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
        descriptionTV=(TextView) view.findViewById(R.id.description_group_item_tv);
        //imageTV = (ImageView) view.findViewById(R.id.item_detail_picture);
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

                Item item=(dataSnapshot.getValue(Item.class));
                nameTV.setText("Name: "+item.getName());
                countTV.setText("Number of Items: "+item.getCount());
                categoryTV.setText("Item Category: "+item.getCategory());
                descriptionTV.setText("Item Description: "+item.getDescription());
                //imageTV.setImageBitmap(item.getImage());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
