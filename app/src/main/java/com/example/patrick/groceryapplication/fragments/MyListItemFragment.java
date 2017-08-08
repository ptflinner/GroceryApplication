package com.example.patrick.groceryapplication.fragments;

import android.content.ContentValues;
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

import com.example.patrick.groceryapplication.adapters.MyListItemAdapter;
import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.Contract;
import com.example.patrick.groceryapplication.utils.DBHelper;
import com.example.patrick.groceryapplication.utils.SQLiteUtils;


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
    private MyListItemAdapter adapter;
    private final static int REQUEST_CODE = 420;


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
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ItemFragment frag = new ItemFragment();
                frag.setTargetFragment(MyListItemFragment.this, REQUEST_CODE);
                frag.show(fm, "itemFragment");
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
                SQLiteUtils.removeItem(db, id);
                adapter.swapCursor(getGroceryList(db));
            }
        }).attachToRecyclerView(myListItemRecyclerView);
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

    //crossreference handler here
    //check if there's any item id for the passed on list id
    //if not then have it ready to handle it and insert a new one
    //it there is then have it ready to


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if(requestCode == ItemFragment.REQUEST_CODE){
            Bundle extras = data.getBundleExtra("args");
            long id = addItem(db, extras.getString("Name"), extras.getInt("Quantity"), extras.getDouble("Price"), extras.getString("Category"));
            Log.d(TAG, id+"LOVE YOU BARRY");
            addXRef(db,getListId(), id);
            createAdapter();
        }
    }

    private long addItem(SQLiteDatabase db, String name, int quantity, double price, String category){
        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY, quantity);
        cv.put(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME, name);
        cv.put(Contract.TABLE_ITEM.COLUMN_NAME_PRICE, price);
        cv.put(Contract.TABLE_ITEM.COLUMN_NAME_CATEGORY, category);
        cv.put(Contract.TABLE_ITEM.COLUMN_NAME_PURCHASE_STATUS,0);

        Log.d(TAG, category + " inserted into db");
        return db.insert(Contract.TABLE_ITEM.TABLE_NAME, null, cv);
    }

    private long addXRef(SQLiteDatabase db, long listID, long itemID){
        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_LIST_ID, listID);
        cv.put(Contract.TABLE_COMPLETED_LIST.COLUMN_NAME_ITEM_ID,itemID);


        return db.insert(Contract.TABLE_COMPLETED_LIST.TABLE_NAME,null,cv);
    }
}
