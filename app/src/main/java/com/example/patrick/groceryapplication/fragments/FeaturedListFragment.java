package com.example.patrick.groceryapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.R;

public class FeaturedListFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_featured_list, container, false);
    }

}
