package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewMyCoWorkingSharedToActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewIshare;

    private ArrayList<SharedCoWorkingCompany> listIShare;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SharedCoWorkingDetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_co_working_shared_to);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
    }

    public void init(){

        recyclerViewIshare = findViewById(R.id.rvIshare);
        recyclerViewIshare.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIshare.setHasFixedSize(true);

        listIShare = new ArrayList<>();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent i = new Intent(ViewMyCoWorkingSharedToActivity.this, ViewMySharedCoWorkingActivity.class);
        ViewMyCoWorkingSharedToActivity.this.startActivity(i);
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}
