package com.example.patrick.groceryapplication.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.R;

public class MyListFragment extends Fragment {
    public MyListFragment(){

    }

    public static MyListFragment newInstance() {
        MyListFragment fragment = new MyListFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_list, container, false);

        FloatingActionButton fab=(FloatingActionButton) view.findViewById(R.id.fab_my_list);

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                FragmentManager fm = getSupportFragmentManager();
//                AddMyListFragment frag = new AddMyListFragment();
//                frag.show(fm, "addMyListFragment");
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
