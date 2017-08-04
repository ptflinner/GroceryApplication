package layout;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

    public AddPersonalList(){}

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
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                OnDialogCloseListener activity = (OnDialogCloseListener) getActivity();
                activity.closeDialog(title.getText().toString(), categoriesSpinner.getSelectedItem().toString());
                AddPersonalList.this.dismiss();
            }
        });
        return view;
    }
}
