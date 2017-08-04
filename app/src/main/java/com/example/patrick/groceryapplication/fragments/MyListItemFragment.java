package com.example.patrick.groceryapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.MyListItemAdapter;
import com.example.patrick.groceryapplication.R;


/**
 * Created by Barry on 8/1/2017.
 */

public class MyListItemFragment extends Fragment{

    public final String TAG = "MyListItemFragment";

    public MyListItemFragment(){}

    public static MyListItemFragment newInstance(int id) {

        Bundle args = new Bundle();

        MyListItemFragment fragment = new MyListItemFragment();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    public int getListId(){
        return getArguments().getInt("id", 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_my_list_items, container, false);
        Log.d(TAG, "" + getListId());
        return view;
    }
}
