package com.example.patrick.groceryapplication.fragments;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.BarCodeItems;
import com.example.patrick.groceryapplication.utils.JsonUtils;
import com.example.patrick.groceryapplication.utils.NetworkUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import java.util.HashMap;

/**
 * Created by Nomis on 7/25/2017.
 */

public class ItemFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Void> {

    private EditText name;
    private EditText quantity;
    private EditText price;
    private Button bar_code;
    private Spinner item_spinner;
    private EditText store;
    private EditText camera;
    private Button add;
    //private Spinner userSpinner;
    private HashMap<String, String> usersWithPermission;
    private final String TAG = "itemFragment";
    private String toast;
    private String content;
    private BarCodeItems results;
    private Button gallery;
    private static final int GALLERY_INTENT = 2;
    private StorageReference mStorage;
    private Button cancel;
    public static final int REQUEST_CODE = 420;

    private static final int BAR_LOADER = 1;
    public final static String SEARCH_QUERY_EXTRA = "query";

    public ItemFragment() {
    }

    public static ItemFragment newInstance() {

        Bundle args = new Bundle();
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }
//creating the view for adding an item
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        name = (EditText) view.findViewById(R.id.itemName);
        quantity = (EditText) view.findViewById(R.id.itemQuantity);
        price = (EditText) view.findViewById(R.id.item_price);
        store = (EditText) view.findViewById(R.id.item_store);
        camera = (EditText) view.findViewById(R.id.item_picture);
        cancel= (Button) view.findViewById(R.id.cancel_button);
        item_spinner = (Spinner) view.findViewById(R.id.categories_item_spinner);
        //userSpinner = (Spinner) view.findViewById(R.id.user_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.categories_array, android.R.layout.simple_spinner_item);
        item_spinner.setAdapter(adapter);
        mStorage = FirebaseStorage.getInstance().getReference();
        //opens up your gallery to choose a picture to be uploaded to the firebase database
        gallery = (Button) view.findViewById(R.id.gallery_image);
        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        //open up a scanner to scan a barcode
        bar_code = (Button) view.findViewById(R.id.scan_button);
        bar_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });
        //add the item to your current list/group list
        add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passing the data from this fragment to the activity
                //uncomment this block of code
                String itemname = name.getText().toString();
                int itemquantity = Integer.parseInt(quantity.getText().toString());
                double itemprice = Double.parseDouble(price.getText().toString());
                String itemCategory = item_spinner.getSelectedItem().toString();

                Bundle b = new Bundle();
                b.putString("Name", itemname);
                b.putInt("Quantity", itemquantity);
                b.putDouble("Price", itemprice);
                b.putString("Category", itemCategory);

                Intent intent = new Intent();
                intent.putExtra("args", b);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), REQUEST_CODE, intent);

                ItemFragment.this.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemFragment.this.dismiss();
            }
        });
        return view;
    }
    //function for the scanner to scan barcodes
    public void scanFromFragment() {
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(ItemFragment.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(true);  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }
//show toast on successful/unsuccessful scan
    private void displayToast() {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }
//stuff with toast
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //uploading images or displaying the toast
        if(result != null) {
            if(result.getContents() == null) {
                //toast = "Cancelled from fragment";
                Uri uri = data.getData();
                // may need to change if multiple users
                StorageReference path = mStorage.child("Photos").child(uri.getLastPathSegment());
                path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        toast = "Upload done";
                    }
                });
            } else {
                content = result.getContents();
                load();
                toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }
//connection to the upcdatabase.org's api
    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this.getActivity()) {

            //Loader starts and shows that the screen is refreshing by enabling progressbar
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                Log.d(TAG, "LOD STARTING");
            }

            //Runs internet refresh off the UI thread
            @Override
            public Void loadInBackground() {
                URL url = NetworkUtils.makeUrl(content);
                try {
                    Log.d(TAG, url + "");
                    String jsonBarcode = NetworkUtils.getResponseFromHttpUrl(url);
                    results = JsonUtils.parseJson(jsonBarcode);
                } catch (IOException e) {
                    Log.d(TAG, "IO EXCEPTOIN OCCURRED");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d(TAG, "JSON EXCEPTION OCCURRED");
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
//auto fill name and price when you scan a barcode when adding an item
    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        Log.d(TAG, "AM I DONE");
        if(results!=null){
            name.setText(results.getItemName());
            price.setText(results.getAvg_price());
        }

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    private void load() {
        LoaderManager lm = this.getActivity().getSupportLoaderManager();
        lm.restartLoader(BAR_LOADER, null, this).forceLoad();
    }
}