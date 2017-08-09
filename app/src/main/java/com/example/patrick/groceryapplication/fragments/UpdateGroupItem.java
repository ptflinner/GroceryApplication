package com.example.patrick.groceryapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.GroupItem;
import com.example.patrick.groceryapplication.models.Item;
import com.example.patrick.groceryapplication.utils.SQLiteUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateGroupItem extends Fragment {
    private static final String TAG = "MyListItemDetailFragment";
    private ImageView image;
    private Button updateItem;
    private Button cancelButton;
    private EditText nameET;
    private EditText priceET;
    private EditText quantityET;
    private Spinner editCategory;
    private Spinner userSpinner;
    public static UpdateGroupItem newInstance(String groupKey,String itemKey){
        Bundle args = new Bundle();
        UpdateGroupItem fragment = new UpdateGroupItem();
        args.putString("gKey", groupKey);
        args.putString("iKey",itemKey);
        fragment.setArguments(args);

        return fragment;
    }

    public String getGroupKey(){ return getArguments().getString("gKey");}
    public String getItemKey(){ return getArguments().getString("iKey");}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_group_item, container, false);

        image = (ImageView) view.findViewById(R.id.item_detail_picture);
        cancelButton=(Button) view.findViewById(R.id.cancel_button);
        nameET = (EditText) view.findViewById(R.id.update_name);
        priceET = (EditText) view.findViewById(R.id.update_price);
        quantityET =(EditText) view.findViewById(R.id.update_quantity);
        userSpinner=(Spinner) view.findViewById(R.id.provider_group_item_tv);
        editCategory = (Spinner) view.findViewById(R.id.update_categories_item_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCategory.setAdapter(adapter);

        DatabaseReference itemRef=(FirebaseDatabase.getInstance())
                .getReference("groupList")
                .child(getGroupKey())
                .child("items")
                .child(getItemKey());

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ITEM FRAG",getGroupKey());
                Log.d("ITEM FRAG",getItemKey());

                GroupItem item=(dataSnapshot.getValue(GroupItem.class));
                nameET.setText(item.getName());
                quantityET.setText(item.getPrice());
                editCategory.setSelection(findSpinnerPosition(item.getCategory()));
                priceET.setText(item.getCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference userRef=(FirebaseDatabase.getInstance().getReference("groupList").child(getGroupKey()).child("users"));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> userPermission=new ArrayList<>();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    String user=snap.getValue(String.class);
                    userPermission.add(user);
                }
                ArrayAdapter<String> userAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,userPermission);
                userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        updateItem = (Button) view.findViewById(R.id.update_item);



        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupItem newItem=new GroupItem();

                newItem.setName(nameET.getText().toString());
                newItem.setCategory(editCategory.getSelectedItem().toString());
                newItem.setPrice(quantityET.getText().toString());
                newItem.setCount(priceET.getText().toString());
                newItem.setProvider(userSpinner.getSelectedItem().toString());
                DatabaseReference itemRef=FirebaseDatabase.getInstance().getReference("groupList")
                        .child(getGroupKey())
                        .child("items")
                        .child(getItemKey());

                itemRef.setValue(newItem);
                getFragmentManager().popBackStack();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }

    //Sets the spinner in UpdateFragment to the correct category choice
    private int findSpinnerPosition(String category){
        int pos=0;

        for(int i=0;i<editCategory.getCount();i++) {
            if (editCategory.getItemAtPosition(i).toString().toUpperCase().equals(category.toUpperCase())){
                pos = i;
            }
        }
        return pos;
    }

    private int findSpinnerUserPosition(String category){
        int pos=0;

        for(int i=0;i<userSpinner.getCount();i++) {
            if (userSpinner.getItemAtPosition(i).toString().toUpperCase().equals(category.toUpperCase())){
                pos = i;
            }
        }
        return pos;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}