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

public class ViewMyFollowUpActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeLayoutViewPastFollowUp;
    private TextView tvNo;
    private RecyclerView recyclerViewFollowUp;
    private ArrayList<FollowUp> listFollowUp, listPastFollowUp;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FollowUp");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_follow_up);

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

        tvNo = findViewById(R.id.tvNone);
        relativeLayoutViewPastFollowUp = findViewById(R.id.relativeLayoutViewPastFollowUp);
        relativeLayoutViewPastFollowUp.setOnClickListener(this);
        recyclerViewFollowUp = findViewById(R.id.rvFollowUp);
        recyclerViewFollowUp.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFollowUp.setHasFixedSize(true);

        listFollowUp = new ArrayList<>();
        listPastFollowUp = new ArrayList<>();
        read();
    }

    public void read(){

        databaseReference.orderByChild("createdBy").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FollowUp followUp = dataSnapshot.getValue(FollowUp.class);
                System.out.println("in");
                if(followUp!=null){

                    if(followUp.getFollowUpStatus().equals("incomplete")){
                        listFollowUp.add(followUp);
                        getMyFollowUp(listFollowUp);
                    }
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

        databaseReference.orderByChild("createdBy").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listFollowUp.size() == 0){
                    tvNo.setVisibility(View.VISIBLE);
                    recyclerViewFollowUp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getMyFollowUp(ArrayList list){
        ViewMyFollowUpAdapter viewMyFollowUpAdapter = new ViewMyFollowUpAdapter(ViewMyFollowUpActivity.this, list);
        recyclerViewFollowUp.setAdapter(viewMyFollowUpAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(ViewMyFollowUpActivity.this, MainActivity.class);
        startActivity(i);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(relativeLayoutViewPastFollowUp)){
            Intent i = new Intent(ViewMyFollowUpActivity.this, ViewMyPastFollowUpActivity.class);
            startActivity(i);
        }
    }
}
