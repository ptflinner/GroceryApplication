package com.example.patrick.groceryapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeaturedListFragment extends Fragment {

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

        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=mFirebaseDatabase.getReference("featureList");
        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                String item1 = dataSnapshot.child("Barbeque").child("1").getValue(String.class);
                Log.d(TAG, item1.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return inflater.inflate(R.layout.fragment_featured_list, container, false);
    }

}
