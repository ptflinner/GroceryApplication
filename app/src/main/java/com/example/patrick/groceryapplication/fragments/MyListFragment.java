package com.example.patrick.groceryapplication.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.MyListAdapter;
import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.Contract;
import com.example.patrick.groceryapplication.utils.DBHelper;
import com.example.patrick.groceryapplication.utils.SQLiteUtils;

public class MyListFragment extends Fragment {

    private RecyclerView myListRecyclerView;
    private FloatingActionButton fab;
    private DBHelper helper;
    private Cursor cursor;
    private SQLiteDatabase db;
    private MyListAdapter adapter;
    private final String TAG = "myListFRAGMENT";

    public MyListFragment(){}

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
        fab=(FloatingActionButton) view.findViewById(R.id.fab_my_list);

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Log.d(TAG, "hey im faaabulous");
            }
        });

        myListRecyclerView=(RecyclerView) view.findViewById(R.id.recycler_view_my_list);
        myListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inflate the layout for this fragment
        return view;
    }

    //When the app stops it closes connections to the db and to the cursor
    @Override
    public void onStop() {
        super.onStop();
        if (db != null) db.close();
        if (cursor != null) cursor.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        createAdapter();
    }

    //The adapter is created and begins making and placing everything
    private void createAdapter(){
        //Database is set up
        helper = new DBHelper(getActivity());
        db = helper.getWritableDatabase();

        cursor= getAllList(db);
        //Adapter is made using the cursor that was set above.
        adapter = new MyListAdapter(cursor,new MyListAdapter.ItemClickListener() {

            @Override
            public void onItemClick(Cursor cursor, int clickedItemIndex, long id) {
                Log.d(TAG, "" + clickedItemIndex);
                Fragment myListItems =MyListItemFragment.newInstance(id);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, myListItems);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });

        myListRecyclerView.setAdapter(adapter);
    }

    //grabs all list
    public Cursor getAllList(SQLiteDatabase db) {
        Log.d(TAG, "- Getting all list");
        return db.query(Contract.TABLE_LIST.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY);

    }

}
