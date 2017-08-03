package com.example.patrick.groceryapplication;

import com.example.patrick.groceryapplication.models.*;
import android.content.Intent;
import android.os.Handler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.patrick.groceryapplication.models.User;
import com.example.patrick.groceryapplication.utils.DBHelper;
import com.example.patrick.groceryapplication.utils.SQLiteUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.patrick.groceryapplication.fragments.*;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private BottomNavigationView mBottomNavView;
    private static final String TAG="MainActivity";
    private SQLiteDatabase db;
    private DBHelper helper;
    private Cursor cursor;
    private DatabaseReference userReference;

    @Override
    protected void onStart() {
        super.onStart();
        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        insertDummy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(db != null){

            db.close();
        }
        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        //final DatabaseReference databaseReference=mFirebaseDatabase.getReference("groups");

//        databaseReference.push().setValue(list);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        mBottomNavView=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag=null;
                switch(item.getItemId()){
                    case R.id.my_list:
                        frag=MyListFragment.newInstance();
                        break;
                    case R.id.group_list:
                        frag=GroupListFragment.newInstance();
                        break;
                    case R.id.featured_list:
                        frag=FeaturedListFragment.newInstance();
                        break;
                    default:
                        Log.e(TAG,"Navigation Error Occurred");
                        Log.e(TAG,"Navigation ID:" +item.getItemId());
                    }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, frag);
                transaction.commit();
                return true;
            }
        });

        loadUserInformation();

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MyListFragment.newInstance());
        transaction.commit();
    }

    /*
    *
    * @Parameters firebase db
    * GroupList object takes the @Parameters
    * String List Name, ArrayList Items, String Description from the text fields the user inputs
    *
    * */
//    public void firebaseGroupAdd(FirebaseDatabase fdb){
//        DatabaseReference groupRef = fdb.getReference("groupList");
//        ArrayList<String> itemsArr = new ArrayList<>();
//        itemsArr.add(item.getText().toString());
//        GroupList groupList = new GroupList(name.getText().toString(), description.getText().toString(),itemsArr);
//        Log.d(TAG, "Loggin into db " + groupList);
//
//        groupRef.push().setValue(groupList);
//    }
    /*
    *
    *
    *
    *   Replace char witht he user values then for the update we can use a map or a has table
    *   but we can just use any adding method !Beware watch what method you use dont delete all the data!
        char extraItem;
     public void firebaseAddingMoreItems(FirebaseDatabase fdb){
       DatabaseReference itemRef = fdb.getReference("groupList").child("items");
         ArrayList<String> extraItems = new ArrayList<>();
         extraItems.add(extraItems.toString());
         itemRef.update().setValue();

     }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked=item.getItemId();
        if(itemThatWasClicked==R.id.sign_out){
            signOutOfGoogle();
            return true;
        }

        return onOptionsItemSelected(item);
    }

    private void signOutOfGoogle(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        FirebaseAuth auth=FirebaseAuth.getInstance();
                        auth.signOut();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }
                });
    }

    public void setUserReference(DatabaseReference userReference) {
        this.userReference = userReference;
    }

    private void loadUserInformation() {
        // Load Character from Database
        final String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setUserReference(FirebaseDatabase.getInstance().getReference("userList").child(firebaseUid));

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> groupLists = new ArrayList<String>();
                groupLists.add("39291d");
                groupLists.add("3923423123d");
                groupLists.add("124125511d");
                if (dataSnapshot.exists()) {
                    User userToAdd = new User(firebaseUid, "John Doe", "02/21/1992", "California", groupLists);
                    userReference.setValue(userToAdd);
                } else {
                    User userToAdd = new User(firebaseUid, "John Doe", "02/21/1992", "California", groupLists);
                    userReference.setValue(userToAdd);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    
    private void insertDummy(){
        SQLiteUtils add = new SQLiteUtils();

        add.addItem(db, "apples", 2, 12, "Purchased", "Immage", "Fruits");
        add.addList(db, "My List", "gaylist");
        add.addList(db, "My List1", "gaylist1");
        add.addList(db, "My List2", "gaylist2");
        add.addList(db, "My List3", "gaylist3");
        add.addList(db, "My List4", "gaylist4");
        add.addMyList(db,1,1);
    }

}
