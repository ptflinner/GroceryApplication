package com.example.patrick.groceryapplication.fragments;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.patrick.groceryapplication.MainActivity;
import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.BarCodeItems;
import com.example.patrick.groceryapplication.utils.JsonUtils;
import com.example.patrick.groceryapplication.utils.NetworkUtils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Nomis on 7/25/2017.
 */
public class AddGroupListItemFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Void>{
    private EditText name;
    private EditText quantity;
    private EditText price;
    private Spinner item_spinner;
    private EditText store;
    private ImageView imageHolder;
    //private EditText camera;
    private Button bar_code;
    private Button gallery;
    private Button add;
    private Spinner userSpinner;
    private HashMap<String,String> usersWithPermission;
    private final String TAG = "itemFragment";
    private String toast;
    private ImageView itemImage;
    private ProgressDialog progress;
    private static final int CAMERA_REQUEST_CODE =2;

    //private static final int GALLERY_INTENT = 2;
    private StorageReference mStorage;
    private String content;
    private Button cancel;
    private static final int BAR_LOADER=1;
    public static final int REQUEST_CODE=123;
    BarCodeItems results;
    public AddGroupListItemFragment(){}
//sending information to other fragments
    public static AddGroupListItemFragment newInstance(String groupKey) {
        Bundle args = new Bundle();
        args.putString("key",groupKey);
        AddGroupListItemFragment fragment = new AddGroupListItemFragment();
        fragment.setArguments(args);
        return fragment;

    }
//get key for group list you belong too
    public String getGroupKey(){return getArguments().getString("key");}
    //camera stuff_____________________________________--------------------------------------------------
  /*  String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this.getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }*/

    //the view when you are adding an item to the list
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_adding_group_list_item, container, false);
        name = (EditText) view.findViewById(R.id.itemName);
        quantity = (EditText) view.findViewById(R.id.itemQuantity);
        price = (EditText) view.findViewById(R.id.item_price);
        store = (EditText) view.findViewById(R.id.item_store);
        imageHolder = (ImageView) view.findViewById(R.id.item_picture);
        progress = new ProgressDialog(getActivity());
        cancel = (Button) view.findViewById(R.id.cancel_button);
        item_spinner = (Spinner) view.findViewById(R.id.categories_item_spinner);
        userSpinner=(Spinner) view.findViewById(R.id.user_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.categories_array,android.R.layout.simple_spinner_item);
        item_spinner.setAdapter(adapter);
        //show the memebers of the current group list you are on
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
        //opens up your gallery to upload an image to the firebase database
        mStorage = FirebaseStorage.getInstance().getReference();
        gallery = (Button) view.findViewById(R.id.gallery_image);
        //itemImage = (ImageView) view.findViewById(R.id.item_picture);
        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.setType("image/*");
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
                //dispatchTakePictureIntent();
            }
        });
        // open up the scanner to scan barcodes
        bar_code = (Button) view.findViewById(R.id.scan_button);
        bar_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });
        //add button to add your current item into your list
        add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passing the data from this fragment to the activity
                //uncomment this block of code
                //BitmapDrawable drawable = (BitmapDrawable) imageHolder.getDrawable();
                //Bitmap itembitmap = drawable.getBitmap();

                String itemname = name.getText().toString();
                String itemquantity = quantity.getText().toString();
                String itemprice = price.getText().toString();
                String itemProvider=userSpinner.getSelectedItem().toString();
                String itemCategory=item_spinner.getSelectedItem().toString();
               // Bitmap itemImage = (Bitmap) data.getExtras().get("data");
                //String itemimages = itembitmap.toString();

                Bundle b = new Bundle();
                b.putString("Name",itemname);
                b.putString("Quantity",itemprice);
                b.putString("Price",itemquantity);
                b.putString("provider",itemProvider);
                b.putString("category",itemCategory);

                Intent intent = new Intent();
                intent.putExtra("args",b);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), REQUEST_CODE, intent);

                AddGroupListItemFragment.this.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGroupListItemFragment.this.dismiss();
            }
        });
        return view;
    }
//setting up the barcode scanner
    public void scanFromFragment() {
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(AddGroupListItemFragment.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(true);  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }
    private void displayToast() {
        if(this.getActivity() != null && toast != null) {
            Toast.makeText(this.getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {

                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataBAOS = baos.toByteArray();
                imageHolder.setImageBitmap(bitmap);

                GroupListItemFragment fragment = new GroupListItemFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("image",bitmap);
                fragment.setArguments(bundle);

                StorageReference storageRef = FirebaseStorage.getInstance().getReference("Photos");

                StorageReference imagesRef = storageRef.child("filename" + new Date().getTime());

                UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //progress.dismiss();
                        //taskSnapshot.getMetadata();
                        toast = "Upload done";
                    }
                });

            } else {
                content=result.getContents();
                load();
                toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this.getActivity()) {

            //Loader starts and shows that the screen is refreshing by enabling progressbar
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                Log.d(TAG,"LOD STARTING");
            }

            //Runs internet refresh off the UI thread
            @Override
            public Void loadInBackground() {
                //URL url= NetworkUtils.makeUrl("0f0cb14a14b7134d22586414523c975d");
                URL url= NetworkUtils.makeUrl(content);
                try{
                    Log.d(TAG,url+"");
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
            Log.d(TAG,"AM I DONE");
        if(results!=null){
            name.setText(results.getItemName());
            price.setText(results.getAvg_price());
        }

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }
    private void load(){
        LoaderManager lm = this.getActivity().getSupportLoaderManager();
        lm.restartLoader(BAR_LOADER,null,this).forceLoad();
    }
//this not working
//    @Override
//    public Loader<Void> onCreateLoader(int d, final Bundle args){
//        return new AsyncTaskLoader<Void>(this.getActivity()) {
//
//            //Loader starts and shows that the screen is refreshing by enabling progressbar
//            @Override
//            protected void onStartLoading() {
//                super.onStartLoading();
//                Log.d(TAG,"LOD STARTING");
//            }
//
//            //Runs internet refresh off the UI thread
//            @Override
//            public Void loadInBackground() {
//                //URL url= NetworkUtils.makeUrl("0f0cb14a14b7134d22586414523c975d");
//                URL url= NetworkUtils.makeUrl(content);
//                try{
//                    Log.d(TAG,url+"");
//                    String jsonBarcode=NetworkUtils.getResponseFromHttpUrl(url);
//                    results= JsonUtils.parseJson(jsonBarcode);
//                }
//                catch (IOException e){
//                    Log.d(TAG,"IO EXCEPTOIN OCCURRED");
//                    e.printStackTrace();
//                }
//                catch (JSONException e){
//                    Log.d(TAG,"JSON EXCEPTION OCCURRED");
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//    }
//    @Override
//    public void onLoadFinished(Loader<Void> loader, Void data) {
//        Log.d(TAG,"AM I DONE");
//       // name.setText("testing");
//        //price.setText("another test");
//        //name.setText(results.getItemName());
//        price.setText(results.getAvg_price());
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Void> loader) {
//
//    }
//    private void load(){
//        LoaderManager lm = this.getActivity().getSupportLoaderManager();
//        lm.restartLoader(BAR_LOADER,null,this).forceLoad();
//    }
}
