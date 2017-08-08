package com.example.patrick.groceryapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import android.widget.ImageView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.utils.Contract;
import com.example.patrick.groceryapplication.utils.DBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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


    //public MyListItemDetailFragment(){}

    public static MyListItemDetailFragment newInstance(long id){
        Bundle args = new Bundle();
        MyListItemDetailFragment fragment = new MyListItemDetailFragment();
        args.putLong("id", id);
        fragment.setArguments(args);

        return fragment;
    }

    public long getItemId(){ return getArguments().getLong("id");}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_my_list_item, container, false);

        image = (ImageView) view.findViewById(R.id.item_detail_picture);

        editName = (EditText) view.findViewById(R.id.update_name);
        editPrice = (EditText) view.findViewById(R.id.update_price);
        editQuantity =(EditText) view.findViewById(R.id.update_quantity);

        updateItem = (Button) view.findViewById(R.id.update_picture);
        selectImg = (Button) view.findViewById(R.id.update_item);

//        selectImg.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                selectImage();
//            }
//        });
        return view;
    }

    public void selectImage(){
        final CharSequence[] options = {"Take Photo","Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           if(options[i].equals("Take Photo")){
               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               File file = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
               intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent,1);
                }else if (options[i].equals("Choose From Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,2);
                }
                else if(options[i].equals("Cancel")){
               dialogInterface.dismiss();
           }
           }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 1){
                File file = new File((Environment.getExternalStorageDirectory().toString()));
                for(File temp : file.listFiles()){
                    if (temp.getName().equals("temp.jpg")){
                        file = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    String path = android.os.Environment.getExternalStorageState() + File.separator + "Phoenix"
                            + File.separator + "default";
                    file.delete();
                    OutputStream outfile = null;
                    File ile = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outfile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,85,outfile);
                        outfile.flush();
                        outfile.close();
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException a) {
                        a.printStackTrace();
                    } catch (Exception b){
                        b.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 2){
            Uri selectImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            //Cursor c = getContentResolver().query();
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



        cursor.moveToFirst();
        String price = String.format("$%,.2f", cursor.getDouble(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_PRICE)));
        itemName.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_ITEM_NAME)));
        itemPrice.setText(price);
        itemQuantity.setText("x" + cursor.getInt(cursor.getColumnIndex(Contract.TABLE_ITEM.COLUMN_NAME_QUANTITY)));

    }

    @Override
    public void onStop() {
        super.onStop();
        if(db != null) db.close();
        if(cursor != null) cursor.close();
    }
}
