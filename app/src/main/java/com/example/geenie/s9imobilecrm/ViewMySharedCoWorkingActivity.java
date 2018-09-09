package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewMySharedCoWorkingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIshare, recyclerViewSharedWithMe;

    private ArrayList<SharedCoWorkingCompany> listIShare, listSharedWithMe;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SharedCoWorkingDetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_shared_co_working);

        init();
    }

    public void init(){

        recyclerViewIshare = findViewById(R.id.rvIshare);
        recyclerViewSharedWithMe = findViewById(R.id.rvSharedWithMe);
        recyclerViewIshare.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSharedWithMe.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIshare.setHasFixedSize(true);
        recyclerViewSharedWithMe.setHasFixedSize(true);

        listIShare = new ArrayList<>();
        listSharedWithMe = new ArrayList<>();
        read();
    }

    public void read(){

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SharedCoWorkingCompany sharedCoWorkingCompany = dataSnapshot.getValue(SharedCoWorkingCompany.class);

                if(sharedCoWorkingCompany.getCreatedby().equals(uid) && sharedCoWorkingCompany.getStatus().equals("ongoing")){
                    listIShare.add(sharedCoWorkingCompany);
                    getIShared(listIShare);
                }
                else if(sharedCoWorkingCompany.getSharedto().equals(uid) && sharedCoWorkingCompany.getStatus().equals("ongoing")){
                    listSharedWithMe.add(sharedCoWorkingCompany);
                    getSharedWith(listSharedWithMe);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getIShared(ArrayList list){
        ViewMySharedCoWorkingAdapter viewMySharedCoWorkingAdapter = new ViewMySharedCoWorkingAdapter(this, list);
        recyclerViewIshare.setAdapter(viewMySharedCoWorkingAdapter);
    }

    private void getSharedWith(ArrayList list){
        ViewMySharedCoWorkingAdapter viewMySharedCoWorkingAdapter = new ViewMySharedCoWorkingAdapter(this, list);
        recyclerViewSharedWithMe.setAdapter(viewMySharedCoWorkingAdapter);
    }

}
