package com.example.patrick.groceryapplication.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.Item;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by Nomis on 7/25/2017.
 */

public class ItemFragment extends DialogFragment {
    private EditText name;
    private EditText quantity;
    private EditText price;
    private Button bar_code;
    private Spinner item_spinner;
    private EditText store;
    private EditText camera;
    private Button add;
    private final String TAG = "itemFragment";
    private String toast;

    public ItemFragment(){}

    public interface OnDialogCloseListener{

        void closeDialog(String toString, String s, String name, String quantity, String price);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.adding_item_fragment, container, false);
        name = (EditText) view.findViewById(R.id.itemName);
        quantity = (EditText) view.findViewById(R.id.itemQuantity);
        price = (EditText) view.findViewById(R.id.item_price);
        store = (EditText) view.findViewById(R.id.item_store);
        camera = (EditText) view.findViewById(R.id.item_picture);

        item_spinner = (Spinner) view.findViewById(R.id.categories_item_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.categories_array,android.R.layout.simple_spinner_item);
        item_spinner.setAdapter(adapter);
        bar_code = (Button) view.findViewById(R.id.scan_button);
        bar_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });
        add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passing the data from this fragment to the activity
                //uncomment this block of code

               String itemname = name.getText().toString();
                String itemquantity = quantity.getText().toString();
                String itemprice = price.getText().toString();
                Bundle b = new Bundle();
                b.putString("Name",itemname);
                b.putString("Quantity",itemprice);
                b.putString("Price",itemquantity);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

/*                nameOfYourItemFragment itemFrag = new nameOfYourItemFragment();
                itemFrag.setArguments(b);
                ft.replace(R.id.frame_container,itemFragment);
                ft.commit();*/


                //copy pasta this code into the your item fragment
/*                Bundle b = getArguments();
                String iname = b.getString("Name");
                String iquantity = b.getString("Quantity");
                String iprice = b.getString("Price");

                TextView yourNameView = (TextView) view.findViewById(R.id.itemName);
                TextView yourQuantityView = (TextView) view.findViewById(R.id.itemQuantity;
                TextView yourPriceView = (TextView) view.findViewById(R.id.item_price);

                yourNameView.setText(iname);
                yourQuantityView.setText(iquantity);
                yourPriceView.setText(iprice);*/


                //-----------------
                OnDialogCloseListener activity = (OnDialogCloseListener) getActivity();
                activity.closeDialog(name.getText().toString(),quantity.getText().toString(),price.getText().toString(),
                        store.getText().toString(),camera.getText().toString());
                ItemFragment.this.dismiss();
            }
        });

        return view;
    }

    public void scanFromFragment() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(true);  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }
    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }
}
