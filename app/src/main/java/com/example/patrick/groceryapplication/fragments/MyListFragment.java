package com.example.patrick.groceryapplication.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.adapters.MyListAdapter;
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
    private final static int REQUEST_CODE = 1;


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
                FragmentManager fm = getFragmentManager();
                AddPersonalList frag = new AddPersonalList();
                frag.setTargetFragment(MyListFragment.this, REQUEST_CODE);
                frag.show(fm, "addPersonalList");
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                Log.d(TAG, "removing id: " + id);
                //remove list function call here
                SQLiteUtils.removeList(db, id);
                adapter.swapCursor(getAllList(db));
            }
        }).attachToRecyclerView(myListRecyclerView);

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
    //insert new list
    private long addList(SQLiteDatabase db, String title, String category){
        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_NAME, title);
        cv.put(Contract.TABLE_LIST.COLUMN_NAME_LIST_CATEGORY, category);
        Log.d(TAG, title + " inserted into db");
        Log.d(TAG, category + " inserted into db");
        return db.insert(Contract.TABLE_LIST.TABLE_NAME, null, cv);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== AddPersonalList.REQUEST_CODE){
            Bundle extras=data.getBundleExtra("args");
            Log.d(TAG,"BUNDLE: "+extras.getString("title"));
            Log.d(TAG,"BUNDLE: "+extras.getString("category"));
            addList(db, extras.getString("title"), extras.getString("category"));
            createAdapter();

            Log.d(TAG, "BUTTON CLICKED");
        }
    }


}
