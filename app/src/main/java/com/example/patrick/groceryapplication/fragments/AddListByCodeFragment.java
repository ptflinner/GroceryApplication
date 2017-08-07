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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

                String key=listID.getText().toString();
                Log.d(TAG,key);
                String fuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d(TAG,fuid);
                DatabaseReference userRef=(FirebaseDatabase.getInstance()).getReference("userList").child(fuid);
                userRef.child("groupLists").push().setValue(key);

                AddListByCodeFragment.this.dismiss();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
