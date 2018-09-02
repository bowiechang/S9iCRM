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

public class ViewMyCopierActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCopier;
    private ArrayList<Copier> listCopier;
    private ArrayList<String> listCompanyDbKey, listCopierDbKey;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReferenceCompany = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReferenceCopier = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerCopier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_copier);

        init();
    }

    public void init(){
        recyclerViewCopier = findViewById(R.id.rvCopier);
        recyclerViewCopier.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCopier.setHasFixedSize(true);

        listCopier = new ArrayList<>();
        listCopierDbKey = new ArrayList<>();
        listCompanyDbKey = new ArrayList<>();
        read();
    }

    public void read() {

        databaseReferenceCompany.child("Company").orderByChild("createBy").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Company company = dataSnapshot.getValue(Company.class);
                if (company != null) {
                    System.out.println("viewmycopier:: company details:" + company.getName());
                    System.out.println("viewmycopier:: key value:" + dataSnapshot.getKey());

                    final String companydbkey = dataSnapshot.getKey();


                    databaseReferenceCopier.child("Copier").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Copier copier = dataSnapshot.getValue(Copier.class);
                            if(copier!=null) {
                                if(copier.getCompany_id().equals(companydbkey))
                                    listCopier.add(copier);
                                getMyCopierList(listCopier);
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

    private void getMyCopierList(ArrayList list){
        ViewMyCopierAdapter viewMyCopierAdapter = new ViewMyCopierAdapter(ViewMyCopierActivity.this, list);
        recyclerViewCopier.setAdapter(viewMyCopierAdapter);
    }
}
