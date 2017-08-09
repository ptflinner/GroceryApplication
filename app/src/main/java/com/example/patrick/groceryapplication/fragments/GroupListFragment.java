package com.example.patrick.groceryapplication.fragments;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
import com.example.patrick.groceryapplication.models.AdminHolder;
import com.example.patrick.groceryapplication.models.GroupList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ClipboardManager;
import android.widget.Toast;

public class GroupListFragment extends Fragment{

    private FloatingActionButton fab;
    private RecyclerView groupListRecyclerView;
    private ArrayList<GroupList> groupArray;
    private static final String TAG="GROUPLISTFRAGMENT";
    private FirebaseRecyclerAdapter<GroupList,ItemHolder> mGroupListAdapter;
    private FirebaseRecyclerAdapter<String,ItemHolder> mStringAdapter;
    private DatabaseReference groupRef;
    private final static int REQUEST_CODE=245;

    public GroupListFragment(){

    }

    public static GroupListFragment newInstance() {
        GroupListFragment fragment = new GroupListFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupArray=new ArrayList<>();
        View view=inflater.inflate(R.layout.fragment_group_list, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab_group_list);


        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                AddGroupListFragment frag = new AddGroupListFragment();
                frag.setTargetFragment(GroupListFragment.this,REQUEST_CODE);
                frag.show(fm, "addPersonalList");
            }
        });

        groupListRecyclerView=(RecyclerView) view.findViewById(R.id.frag_group_list_recycler);
        groupListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        groupRef=FirebaseDatabase.getInstance().getReference("userList").child(uid).child("groupLists");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createAdapter();
    }

    public void createAdapter(){
        mStringAdapter = new FirebaseRecyclerAdapter<String, ItemHolder>(
                String.class,
                R.layout.group_list_recycle_item,
                ItemHolder.class,
                groupRef
        ) {
            @Override
            protected void populateViewHolder(final ItemHolder viewHolder, final String model, final int position) {

                Log.d(TAG,model);
                DatabaseReference gRef=FirebaseDatabase.getInstance().getReference("groupList").
                        child(model);

                final AdminHolder adminHolder=new AdminHolder(false);
                gRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            boolean admin=false;

                            GroupList gModel=(dataSnapshot.getValue(GroupList.class));
                            if((gModel.getAdmin().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){
                                admin=true;
                                adminHolder.setAdmin(true);
                                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        ClipboardManager clipboardManager=(ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip=ClipData.newPlainText("label",model);
                                        clipboardManager.setPrimaryClip(clip);
                                        Toast.makeText(getActivity(),"Copied to Clipboard", Toast.LENGTH_LONG).show();
                                        return true;
                                    }
                                });
                            }
                           viewHolder.bind(gModel,position,admin,model);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Fragment groupListItems =GroupListItemFragment.newInstance(model,adminHolder.isAdmin());
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, groupListItems);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });



            }

        };
        groupListRecyclerView.setAdapter(mStringAdapter);
    }

    public void firebaseGroupAdd(FirebaseDatabase fdb,final GroupList groupList){
        final DatabaseReference groupRef = fdb.getReference("groupList");
        final String pushKey=groupRef.push().getKey();

        final String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userReference=(FirebaseDatabase.getInstance().getReference("userList").child(firebaseUid));
        DatabaseReference userNameReference=(FirebaseDatabase.getInstance().getReference("userList").child(firebaseUid).child("displayName"));

        userNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.getValue(String.class);
                HashMap<String, String> users=new HashMap<>();
                users.put(firebaseUid,name);
                groupList.setUsers(users);
                groupList.setAdmin(firebaseUid);

                userReference.child("groupLists").push().setValue(pushKey);
                groupRef.child(pushKey).setValue(groupList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== AddGroupListFragment.REQUEST_CODE){
            Bundle extras=data.getBundleExtra("args");

            Log.d(TAG,"BUNDLE: "+extras.getString("title"));
            Log.d(TAG,"BUNDLE: "+extras.getString("cat"));
            GroupList groupList = new GroupList(extras.getString("title"),extras.getString("cat"));
            firebaseGroupAdd(FirebaseDatabase.getInstance(), groupList);
            Log.d(TAG, "BUTTON CLICKED");
        }
    }
    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        private TextView listName;
        private TextView listId;
        private TextView listCategory;
        public ItemHolder(View itemView) {
            super(itemView);
            this.mView=itemView;
            listName=(TextView) itemView.findViewById(R.id.group_list_name);
            itemView.setOnClickListener(this);
            listId=(TextView) itemView.findViewById(R.id.group_list_id);
            listCategory=(TextView) itemView.findViewById(R.id.group_list_category);
        }

        public void bind(GroupList groupList, int position,boolean admin,String key){
            Log.d(TAG,"LIST NAME:"+groupList.getName());
            listName.setText(groupList.getName());
            listCategory.setText(groupList.getCategory());
            if(admin){
                listId.setText(key);
                listId.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG,"DOES THIS WORK????");
        }
    }
}
