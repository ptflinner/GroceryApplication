package com.example.patrick.groceryapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.DBHelper;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateMyListItem extends Fragment {
    private static final String TAG = "UpdateFragment";
    private Cursor cursor;
    private Context context;
    private DBHelper helper;
    private SQLiteDatabase db;
    private ImageView image;
    private Button updateItem;
    private Button selectImg;
    private EditText editName;
    private EditText editPrice;
    private EditText editQuantity;
    private Bundle extra;
    String mCurrentPhotoPath;
    int count = 0;
    static  final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_FIlE = 0;
    public static UpdateMyListItem newInstance(long id){
        Bundle args = new Bundle();
        UpdateMyListItem fragment = new UpdateMyListItem();
        args.putLong("id", id);
        fragment.setArguments(args);

        return fragment;
    }

    public long getItemId(){ return getArguments().getLong("id");}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_my_list_item, container, false);

        image = (ImageView) view.findViewById(R.id.item_detail_picture);

        editName = (EditText) view.findViewById(R.id.update_name);
        editPrice = (EditText) view.findViewById(R.id.update_price);
        editQuantity =(EditText) view.findViewById(R.id.update_quantity);

        selectImg = (Button) view.findViewById(R.id.update_item);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
//                startActivityForResult(intent,1);
                saveImage();
            }
        });
        return view;
    }

    public void saveImage(){

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(options[i].equals("Take Photo")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                    startActivityForResult(intent,1);
                }else if (options[i].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent,0);
                }
                else if(options[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
        Toast.makeText(getActivity(), "Image saved succefully" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
            if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK ) {
                extra = data.getExtras();
                Bitmap bitmap = (Bitmap) extra.get("data");
                image.setImageBitmap(bitmap);
                Log.d(TAG,"Saved Image");
                Toast.makeText(getContext(),"Image saved!", Toast.LENGTH_SHORT ).show();

        }
        else if(requestCode == SELECT_FIlE){
                Uri selectedImage = data.getData();
                image.setImageURI(selectedImage);
            }
        else if(resultCode == getActivity().RESULT_CANCELED){
            Toast.makeText(getContext(), "Cancel Image!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(),"Sorry failed to capture image,", Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor getItemDetails(SQLiteDatabase db){
        String[] selectionArgs = {String.valueOf(getItemId())};
        String query = "select items._id, items.name, items.price, items.quantity" +
                " from items " +
                "where items._ID = ?";

        return db.rawQuery(query, selectionArgs);
    }

    @Override
    public void onStart() {
        super.onStart();
        helper = new DBHelper(getActivity());
        db = helper.getWritableDatabase();
        cursor = getItemDetails(db);




    }

    @Override
    public void onStop() {
        super.onStop();
        if(db != null) db.close();
        if(cursor != null) cursor.close();
    }

}
