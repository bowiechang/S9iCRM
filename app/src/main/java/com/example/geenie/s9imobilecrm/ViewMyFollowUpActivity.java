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

public class ViewMyFollowUpActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFollowUp, recyclerViewPastFollowUp;
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
        init();
    }

    public void init(){

        recyclerViewFollowUp = findViewById(R.id.rvFollowUp);
        recyclerViewPastFollowUp = findViewById(R.id.rvPastFollowUp);
        recyclerViewFollowUp.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPastFollowUp.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFollowUp.setHasFixedSize(true);
        recyclerViewPastFollowUp.setHasFixedSize(true);

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
                    else{
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

    private void getMyFollowUp(ArrayList list){
        ViewMyFollowUpAdapter viewMyFollowUpAdapter = new ViewMyFollowUpAdapter(ViewMyFollowUpActivity.this, list);
        recyclerViewFollowUp.setAdapter(viewMyFollowUpAdapter);
    }

    private void getMyPastFollowUp(ArrayList list){
        ViewMyFollowUpAdapter viewMyFollowUpAdapter = new ViewMyFollowUpAdapter(ViewMyFollowUpActivity.this, list);
        recyclerViewPastFollowUp.setAdapter(viewMyFollowUpAdapter);
    }
}
