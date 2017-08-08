package com.example.patrick.groceryapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.FeaturedItem;
import com.example.patrick.groceryapplication.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class FeaturedListFragment extends Fragment {

    private LinkedHashMap<String, ArrayList<Item>> listHashMap = new LinkedHashMap<String, ArrayList<Item>>();
    private ArrayList<FeaturedItem> featuredList = new ArrayList<FeaturedItem>();
    private ExpandableListView expandableListView;
    private ExpandableListItemAdapter expandableListAdapter;

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
        expandableListAdapter = new ExpandableListItemAdapter();
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_view);
        expandableListView.setAdapter(expandableListAdapter);

    }

    private void loadData() {
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = mFirebaseDatabase.getReference("featureList");
        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Count " + dataSnapshot.getChildrenCount());
                GenericTypeIndicator<HashMap<String, ArrayList<String>>> t = new GenericTypeIndicator <HashMap<String, ArrayList<String>>>(){};
                HashMap<String, ArrayList<String>> dbFeaturedList = dataSnapshot.getValue(t);
                Iterator it = dbFeaturedList.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                    if(listHashMap.get(pair.getKey()) == null) {
                        FeaturedItem group = new FeaturedItem();
                        group.setTitle(pair.getKey().toString());
                        ArrayList<Item> items = new ArrayList<Item>();
                        for (String s : dbFeaturedList.get(pair.getKey())) {
                            items.add(new Item(s));
                        }
                        group.setGroceryList(items);
                        listHashMap.put(pair.getKey().toString(), items);
                        featuredList.add(group);
                    }
                }
                expandableListAdapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    protected class ExpandableListItemAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return featuredList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return featuredList.get(groupPosition).getGroceryList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return featuredList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return featuredList.get(groupPosition).getGroceryList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.list_item, parent, false);
            }
            TextView title = (TextView) v.findViewById(R.id.featured_list_title_view);
            FeaturedItem featuredGrocery = featuredList.get(groupPosition);
            title.setText(featuredGrocery.getTitle());
            return v;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.child_list_item, parent, false);
            }
            TextView subtitle = (TextView) v.findViewById(R.id.featured_list_subtitle_view);
            Item featuredItem = featuredList.get(groupPosition).getGroceryList().get(childPosition);
            subtitle.setText(featuredItem.getName());
            return v;
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
