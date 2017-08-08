package com.example.patrick.groceryapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patrick.groceryapplication.R;
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
                
                gRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                           boolean admin=true;
                           GroupList gModel=(dataSnapshot.getValue(GroupList.class));
                            if(!(gModel.getAdmin().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){
                                admin=false;
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
                        Fragment groupListItems =GroupListItemFragment.newInstance(model);
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
//        if(requestCode==AddListByCodeFragment.REQUEST_CODE){
//            Bundle extras=data.getBundleExtra("args");
//
//            DatabaseReference groupRef=(FirebaseDatabase.getInstance()).getReference("groupList").child(extras.getString("key"));
//
//
//        }
    }
    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        private TextView listName;
        private TextView listId;
        public ItemHolder(View itemView) {
            super(itemView);
            this.mView=itemView;
            listName=(TextView) itemView.findViewById(R.id.group_list_name);
            itemView.setOnClickListener(this);
            listId=(TextView) itemView.findViewById(R.id.group_list_id);
        }

        public void bind(GroupList groupList, int position,boolean admin,String key){
            Log.d(TAG,"LIST NAME:"+groupList.getName());
            listName.setText(groupList.getName());
            if(admin){
                listId.setText(key);
                listId.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
//            int pos=getAdapterPosition();
//            mItemClick.onItemClickListener(pos,groupLists.get(pos));
        }
    }
}
