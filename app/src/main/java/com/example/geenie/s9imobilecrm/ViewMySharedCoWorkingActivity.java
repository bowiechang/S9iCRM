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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMySharedCoWorkingActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewSharedWithMe;
    private RelativeLayout relativeLayoutshareCoworking, relativeLayoutViewIShared;
    private TextView tvNo;
    private ArrayList<SharedCoWorkingCompany> listSharedWithMe;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SharedCoWorkingDetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_shared_co_working);

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

        relativeLayoutshareCoworking = findViewById(R.id.relativeLayoutViewShare);
        relativeLayoutViewIShared = findViewById(R.id.relativeLayoutViewIShare);
        relativeLayoutViewIShared.setOnClickListener(this);
        relativeLayoutshareCoworking.setOnClickListener(this);

        recyclerViewSharedWithMe = findViewById(R.id.rvSharedWithMe);
        recyclerViewSharedWithMe.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSharedWithMe.setHasFixedSize(true);

        tvNo = findViewById(R.id.tvNone);

        listSharedWithMe = new ArrayList<>();
        read();
    }

    public void read(){

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SharedCoWorkingCompany sharedCoWorkingCompany = dataSnapshot.getValue(SharedCoWorkingCompany.class);

                if(sharedCoWorkingCompany.getSharedto().equals(uid) && sharedCoWorkingCompany.getStatus().equals("ongoing")){
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listSharedWithMe.size() == 0){
                    tvNo.setVisibility(View.VISIBLE);
                    recyclerViewSharedWithMe.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSharedWith(ArrayList list){
        ViewMySharedCoWorkingAdapter viewMySharedCoWorkingAdapter = new ViewMySharedCoWorkingAdapter(this, list);
        recyclerViewSharedWithMe.setAdapter(viewMySharedCoWorkingAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(relativeLayoutshareCoworking)){
            Intent i = new Intent(ViewMySharedCoWorkingActivity.this, AddSharedCoWorkingCompany.class);
            ViewMySharedCoWorkingActivity.this.startActivity(i);
        }
        else if(view.equals(relativeLayoutViewIShared)){
            Intent i = new Intent(ViewMySharedCoWorkingActivity.this, ViewMyCoWorkingSharedToActivity.class);
            ViewMySharedCoWorkingActivity.this.startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(ViewMySharedCoWorkingActivity.this, MainActivity.class);
        ViewMySharedCoWorkingActivity.this.startActivity(i);
        return true;
    }
}
