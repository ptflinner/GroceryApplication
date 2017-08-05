package com.example.patrick.groceryapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.patrick.groceryapplication.MainActivity;
import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.adapters.ExpandableListItemAdapter;
import com.example.patrick.groceryapplication.models.FeaturedItem;
import com.example.patrick.groceryapplication.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FeaturedListFragment extends Fragment {

    private HashMap<String, FeaturedItem> featuredItemHashMap = new HashMap<String, FeaturedItem>();
    private ArrayList<FeaturedItem> featuredList = new ArrayList<FeaturedItem>();
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    private static final String TAG = "featured";
    public FeaturedListFragment() {
        // Required empty public constructor
    }

    public static FeaturedListFragment newInstance() {
        FeaturedListFragment fragment = new FeaturedListFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_featured_list, container, false);
        initView(view);
        loadData();
        return view;
    }

    private void initView(View view) {
        expandableListAdapter = new ExpandableListItemAdapter(getContext(), featuredList);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_view);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void loadData() {
        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=mFirebaseDatabase.getReference("featureList");
        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                featuredItemHashMap = (HashMap) dataSnapshot.getValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        if(featuredList == null){
            featuredList = new ArrayList(featuredItemHashMap.entrySet());
        }
    }
}
