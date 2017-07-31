package com.example.patrick.groceryapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.patrick.groceryapplication.fragments.*;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private BottomNavigationView mBottomNavView;
    private static final String TAG="MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] drawerItems;
    private ActionBarDrawerToggle mActionToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=mFirebaseDatabase.getReference("message");

        databaseReference.setValue("Hello, Database");

        Log.d(TAG,"In Main");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value=dataSnapshot.getValue(String.class);
                Log.d(TAG,"Value is: "+value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG,"Failed to read value. ", databaseError.toException());
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        mDrawerLayout=(DrawerLayout) findViewById(R.id.navigation_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList=(ListView) findViewById(R.id.left_drawer);
        drawerItems=getResources().getStringArray(R.array.drawer_items);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list,drawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mActionToggle=new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.open,
                R.string.close
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(getTitle());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getTitle());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
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

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MyListFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen=mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.sign_out).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId=item.getItemId();
        if(mActionToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case R.id.sign_out:
                signOutOfGoogle();
                break;
            default:
                return onOptionsItemSelected(item);
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

    private void selectItem(int pos){
        if(pos==1){
            signOutOfGoogle();
        }
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
            selectItem(pos);
        }
    }
}
