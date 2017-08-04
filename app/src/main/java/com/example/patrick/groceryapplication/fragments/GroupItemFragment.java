package com.example.patrick.groceryapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.R;

public class GroupItemFragment extends Fragment {
    private FloatingActionButton fab;
    private RecyclerView groupListItemRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_group_item, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab_my_list);

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//            FragmentManager fm = getSupportFragmentManager();
//            AddMyListFragment frag = new AddMyListFragment();
//            frag.show(fm, "addMyListFragment");
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
