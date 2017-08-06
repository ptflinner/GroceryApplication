package com.example.patrick.groceryapplication.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrick.groceryapplication.MyListItemAdapter;
import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.DBHelper;


/**
 * Created by Barry on 8/1/2017.
 */

public class MyListItemFragment extends Fragment{

    public final String TAG = "MyListItemFragment";
    private RecyclerView myListItemRecyclerView;
    private FloatingActionButton fab;
    private DBHelper helper;
    private Cursor cursor;
    private SQLiteDatabase db;
    MyListItemAdapter adapter;


    public MyListItemFragment(){}

    public static MyListItemFragment newInstance(long id) {

        Bundle args = new Bundle();

        MyListItemFragment fragment = new MyListItemFragment();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    public long getListId(){
        return getArguments().getLong("id");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_list_items, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_my_list_items);

        //put fab click listener here
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Log.d(TAG, "hey im faaabulous");

            }
        });

        Log.d(TAG, "" + getListId());
        myListItemRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_list_items);
        myListItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void createAdapter(){
        helper = new DBHelper(getActivity());
        db = helper.getWritableDatabase();
        cursor = getGroceryList(db);

        adapter = new MyListItemAdapter(cursor, new MyListItemAdapter.ItemClickListener(){

            @Override
            public void onItemClick(Cursor cursor, int clickedItemIndex, long id) {
                //item details fragment called
                Log.d(TAG, "" + clickedItemIndex);
                Fragment itemDetails = MyListItemDetailFragment.newInstance(id);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, itemDetails);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        myListItemRecyclerView.setAdapter(adapter);
    }

    public Cursor getGroceryList(SQLiteDatabase db){
        String[] selectionArgs = {String.valueOf(getListId())};
        String query = "select items._ID, items.name, items.quantity, items.price " +
                "from items inner join complete_list " +
                "where items._ID = complete_list.item_id and complete_list.list_id = ?";
        return db.rawQuery(query, selectionArgs);
    }

    @Override
    public void onStart() {
        super.onStart();
        createAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) db.close();
        if (cursor != null) cursor.close();
    }
}
