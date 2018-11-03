package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewMyPastFollowUpActivity extends AppCompatActivity{

    private RecyclerView recyclerViewPastFollowUp;
    private ArrayList<FollowUp> listPastFollowUp;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FollowUp");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_past_follow_up);


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

        recyclerViewPastFollowUp = findViewById(R.id.rvPastFollowUp);
        recyclerViewPastFollowUp.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPastFollowUp.setHasFixedSize(true);

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

                    if(followUp.getFollowUpStatus().equals("completed")){
                        listPastFollowUp.add(followUp);
                        getMyPastFollowUp(listPastFollowUp);
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
    }

    private void getMyPastFollowUp(ArrayList list){
        ViewMyPastFollowUpAdapter viewMyPastFollowUpAdapter = new ViewMyPastFollowUpAdapter(ViewMyPastFollowUpActivity.this, list);
        recyclerViewPastFollowUp.setAdapter(viewMyPastFollowUpAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
