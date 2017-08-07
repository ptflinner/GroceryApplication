package com.example.patrick.groceryapplication.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.patrick.groceryapplication.MainActivity;
import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.BarCodeItems;
import com.example.patrick.groceryapplication.utils.JsonUtils;
import com.example.patrick.groceryapplication.utils.NetworkUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import java.util.ArrayList;
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
        private Spinner userSpinner;
        private HashMap<String,String> usersWithPermission;
        private final String TAG = "itemFragment";
        private String toast;
        private String content;
        private BarCodeItems results;
        public static final int REQUEST_CODE=349;
        private static final int BAR_LOADER=1;
        public final static String SEARCH_QUERY_EXTRA = "query";
        public ItemFragment(){}

    public static ItemFragment newInstance() {

        Bundle args = new Bundle();
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
            View view = inflater.inflate(R.layout.fragment_item, container, false);
            name = (EditText) view.findViewById(R.id.itemName);
            quantity = (EditText) view.findViewById(R.id.itemQuantity);
            price = (EditText) view.findViewById(R.id.item_price);
            store = (EditText) view.findViewById(R.id.item_store);
            camera = (EditText) view.findViewById(R.id.item_picture);

            item_spinner = (Spinner) view.findViewById(R.id.categories_item_spinner);
            userSpinner=(Spinner) view.findViewById(R.id.user_spinner);

            ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.categories_array1,android.R.layout.simple_spinner_item);
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
                    String itemCategory=item_spinner.getSelectedItem().toString();

                    Bundle b = new Bundle();
                    b.putString("Name",itemname);
                    b.putString("Quantity",itemprice);
                    b.putString("Price",itemquantity);
                    b.putString("category",itemCategory);

                    Intent intent = new Intent();
                    intent.putExtra("args",b);
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(), REQUEST_CODE, intent);

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
                    content=result.getContents();
                    load();
                    toast = "Scanned from fragment: " + result.getContents();
                }

                // At this point we may or may not have a reference to the activity
                displayToast();
            }
        }
//    //@Override
//    public AsyncTaskLoader<ArrayList<BarCodeItems>> onCreateLoader(int id, final Bundle args) {
//        return new AsyncTaskLoader<ArrayList<BarCodeItems>>(this) {
//
//            @Override
//            protected void onStartLoading() {
//                super.onStartLoading();
//                if(args == null) return;
//            }
//
//            @Override
//            public ArrayList<BarCodeItems> loadInBackground() {
//                String query = args.getString(SEARCH_QUERY_EXTRA);
//
//                if(query == null || TextUtils.isEmpty(query)) return null;
//
//                ArrayList<BarCodeItems> result = null;
//                URL url = NetworkUtils.makeUrl();
//                //Log.d(TAG, "url: " + url.toString());
//                try {
//                    String json = NetworkUtils.getResponseFromHttpUrl(url);
//                    result = NetworkUtils.parseJson(json);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return null;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//                return result;
//
//            }
//        };
//    }

        @Override
        public Loader<Void> onCreateLoader(int d, Bundle args){
            return new AsyncTaskLoader<Void>(this.getActivity()) {

                //Loader starts and shows that the screen is refreshing by enabling progressbar
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                }

                //Runs internet refresh off the UI thread
                @Override
                public Void loadInBackground() {
                    URL url= NetworkUtils.makeUrl();
                    try{
                        String jsonBarcode=NetworkUtils.getResponseFromHttpUrl(url);
                        results= JsonUtils.parseJson(jsonBarcode);
                    }
                    catch (IOException e){
                        Log.d(TAG,"IO EXCEPTOIN OCCURRED");
                        e.printStackTrace();
                    }
                    catch (JSONException e){
                        Log.d(TAG,"JSON EXCEPTION OCCURRED");
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
        @Override
        public void onLoadFinished(Loader<Void> loader, Void data) {
            name.setText(results.getItemName());
            price.setText(results.getAvg_price());
        }

        @Override
        public void onLoaderReset(Loader<Void> loader) {

        }
        private void load(){
            LoaderManager lm = getLoaderManager();
            lm.restartLoader(BAR_LOADER,null,this).forceLoad();
        }
    }
