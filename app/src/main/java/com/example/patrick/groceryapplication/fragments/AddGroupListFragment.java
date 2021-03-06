package com.example.patrick.groceryapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.patrick.groceryapplication.R;

public class AddGroupListFragment extends DialogFragment {
    private EditText title;
    private Spinner categoriesSpinner;
    private Button add;
    private Button addByCode;
    private Button cancel;

    private final String TAG = "AddGroupListFragment";
    public  static final int REQUEST_CODE=245;
    public  static final int REQUEST_CODE_CODE=346;
    public AddGroupListFragment(){}

    public interface OnDialogCloseListener{
        void closeDialog(String title, String categoriesSpinner);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_add_group_list, container, false);
        title = (EditText) view.findViewById(R.id.title);
        categoriesSpinner = (Spinner) view.findViewById(R.id.categories_spinner);
        add = (Button) view.findViewById(R.id.add);
        addByCode=(Button) view.findViewById(R.id.add_code_transfer_button);
        cancel=(Button) view.findViewById(R.id.cancel_button);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.list_categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle args=new Bundle();
                Log.d(TAG,"TITLE: "+title.getText().toString());
                Log.d(TAG,"CAT: "+categoriesSpinner.getSelectedItem().toString());
                args.putString("title",title.getText().toString());
                args.putString("cat",categoriesSpinner.getSelectedItem().toString());

                intent.putExtra("args",args);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), REQUEST_CODE, intent);

                AddGroupListFragment.this.dismiss();
            }
        });

        addByCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG,"CLICK CLICK CLICK");
                FragmentManager fm = getFragmentManager();
                AddListByCodeFragment frag = new AddListByCodeFragment();
                frag.show(fm, "addListByCodeFragment");

                AddGroupListFragment.this.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGroupListFragment.this.dismiss();
            }
        });
        return view;
    }
}

