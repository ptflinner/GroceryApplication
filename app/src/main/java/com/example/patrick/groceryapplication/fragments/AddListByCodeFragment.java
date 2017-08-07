package com.example.patrick.groceryapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.GroupList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class AddListByCodeFragment extends DialogFragment {
    private EditText listID;
    private Button addButton;
    private final String TAG="CodeFragment";

    public static int REQUEST_CODE=346;

    public AddListByCodeFragment() {
        // Required empty public constructor
    }

    public static AddListByCodeFragment newInstance(String param1) {
        AddListByCodeFragment fragment = new AddListByCodeFragment();
        Bundle args = new Bundle();
        args.putString("key", param1);
        fragment.setArguments(args);
        return fragment;
    }

    private String getListKey(){
        return getArguments().getString("key");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_add_list_by_code, container, false);

        listID=(EditText) view.findViewById(R.id.add_code_edit_text);
        addButton=(Button) view.findViewById(R.id.add_code_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent();
//                Bundle args=new Bundle();
//                args.putString("key",key);
//
//                intent.putExtra("args",args);
//                getTargetFragment().onActivityResult(
//                        getTargetRequestCode(), REQUEST_CODE, intent);

                FirebaseDatabase fdb=FirebaseDatabase.getInstance();
                firebaseGroupAdd(fdb);

                AddListByCodeFragment.this.dismiss();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    public void firebaseGroupAdd(FirebaseDatabase fdb){
        final String pushKey=listID.getText().toString();
        final DatabaseReference groupRef = fdb.getReference("groupList").child(pushKey);
        final String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userReference=(FirebaseDatabase.getInstance().getReference("userList").child(firebaseUid));
        final DatabaseReference userNameReference=(FirebaseDatabase.getInstance().getReference("userList").child(firebaseUid).child("name"));

        userNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.getValue(String.class);
//                HashMap<String, String> users=new HashMap<>();
//                users.put(firebaseUid,name);

                userReference.child("groupLists").push().setValue(pushKey);
                groupRef.child("users").child(firebaseUid).setValue(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
