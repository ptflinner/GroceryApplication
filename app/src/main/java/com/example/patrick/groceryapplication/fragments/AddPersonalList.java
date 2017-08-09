package com.example.patrick.groceryapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.patrick.groceryapplication.R;

public class AddPersonalList extends DialogFragment {
    private EditText title;
    private Spinner categoriesSpinner;
    private Button add;
    private final String TAG = "AddPersonalList";
    public static final int REQUEST_CODE = 1;


    public interface OnDialogCloseListener{
        void closeDialog(String title, String categoriesSpinner);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_add_personal_list, container, false);
        title = (EditText) view.findViewById(R.id.title);
        categoriesSpinner = (Spinner) view.findViewById(R.id.categories_spinner);
        add = (Button) view.findViewById(R.id.add);

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
                args.putString("category",categoriesSpinner.getSelectedItem().toString());

                intent.putExtra("args",args);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), REQUEST_CODE, intent);

                AddPersonalList.this.dismiss();
            }
        });
        return view;
    }
}


