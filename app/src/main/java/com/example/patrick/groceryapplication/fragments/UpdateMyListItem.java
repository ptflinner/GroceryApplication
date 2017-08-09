package com.example.patrick.groceryapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.DBHelper;

public class UpdateMyListItem extends Fragment {
    private static final String TAG = "MyListItemDetailFragment";
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
    static  final int REQUEST_IMAGE_CAPTURE = 1;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_my_list_item, container, false);

        image = (ImageView) view.findViewById(R.id.item_detail_picture);

        editName = (EditText) view.findViewById(R.id.update_name);
        editPrice = (EditText) view.findViewById(R.id.update_price);
        editQuantity =(EditText) view.findViewById(R.id.update_quantity);

        selectImg = (Button) view.findViewById(R.id.update_item);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        return view;
    }

    public void selectImage(){
        final CharSequence[] options = {"Take Photo","Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           if(options[0].equals("Take Photo")){
               Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }else if (options[1].equals("Choose From Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,2);
                }
                else if(options[2].equals("Cancel")){
               dialogInterface.dismiss();
           }
           }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == Activity.RESULT_OK)
            extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            image.setImageBitmap(bitmap);
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
