package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AddSharedCoWorkingCompany extends AppCompatActivity {

    private RelativeLayout btnAddSharedWorkingCompany;

    private Spinner spNameCard, spUser;

    private ArrayList<Company> companyArrayList= new ArrayList<>();
    private ArrayList<String> arraylistCompanyName = new ArrayList<>(), arrayListCompanyid = new ArrayList<>(), arrayListUsername = new ArrayList<>(), arrayListUserid = new ArrayList<>();

    private TreeMap<String, String> treeMapCompany =new TreeMap<>();

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shared_co_working_company);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        retrieveMyNameCardsAndUsers();

    }

    public void init(){

        spNameCard = findViewById(R.id.spinnerShareNameCard);
        spUser = findViewById(R.id.spinnerShareWith);
        btnAddSharedWorkingCompany = findViewById(R.id.btnAddSharedWorkingCompany);

        btnAddSharedWorkingCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedCompany = spNameCard.getSelectedItemPosition();

                String string1 = treeMapCompany.entrySet().toArray()[selectedCompany].toString();
                String [] spliter = string1.split("=");
                System.out.println("sharedworkingcoy:: details: " + spliter[1]);

                final String companyid = spliter[1];

                int selectedUser = spUser.getSelectedItemPosition();
                final String userid = arrayListUserid.get(selectedUser);

                ArrayList<String> arrayList = new ArrayList<>();

                SharedCoWorkingCompany sharedCoWorkingCompany = new SharedCoWorkingCompany(companyid, uid, userid, "ongoing", arrayList);
                databaseReference.child("SharedCoWorkingDetails").push().setValue(sharedCoWorkingCompany).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Shared!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddSharedCoWorkingCompany.this, ViewMyCoWorkingSharedToActivity.class);
                        AddSharedCoWorkingCompany.this.startActivity(i);

                    }
                });


            }
        });
    }

    public void retrieveMyNameCardsAndUsers(){

        databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                if(company.getCreateBy().equals(uid)){
//
//                    arrayListCompanyid.add(dataSnapshot.getKey());
//                    arraylistCompanyName.add(company.getName());

                    treeMapCompany.put(company.getName(), dataSnapshot.getKey());
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

        databaseReference.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> arraylistCompanyName2 = new ArrayList<>();

//                Set<String> keys = treeMapCompany.keySet();
//                for(String key: keys){
//                    arraylistCompanyName2.add(treeMapCompany.get(key));
//                }

//                for (int i = 0; i < treeMapCompany.size(); i++) {
//                    arraylistCompanyName2.add(treeMapCompany.get(i));
//                }

                for (Map.Entry<String, String> entry : treeMapCompany.entrySet()) {
//                    System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
                    arraylistCompanyName2.add(entry.getKey());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSharedCoWorkingCompany.this,
                        android.R.layout.simple_spinner_item, arraylistCompanyName2);
                adapter.setDropDownViewResource(R.layout.custom_spinner_item);
                spNameCard.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);

                String[] split = user.getEmail().split("@");
                String name = split[0];
                if(!dataSnapshot.getKey().equals(uid)) {
                    arrayListUsername.add(name);
                    arrayListUserid.add(dataSnapshot.getKey());
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
        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSharedCoWorkingCompany.this,
                        android.R.layout.simple_spinner_item, arrayListUsername);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spUser.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void retrieveTest(){

        databaseReference.child("SharedCoWorkingDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final SharedCoWorkingCompany sharedCoWorkingCompany = dataSnapshot.getValue(SharedCoWorkingCompany.class);
                if(sharedCoWorkingCompany.getCompanyid().equals("-LKkPuTjiV5s7M_4zTkl")){

                    //for Appointment
                    databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Appointment appointment = dataSnapshot.getValue(Appointment.class);
                            if(appointment.getCompany_id().equals(sharedCoWorkingCompany.getCompanyid())){
                                System.out.println("tester(): appointment found" );
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

                    //for Contact
                    databaseReference.child("Contact").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Contact contact = dataSnapshot.getValue(Contact.class);
                            if(contact.getCompany_id().equals(sharedCoWorkingCompany.getCompanyid())){
                                System.out.println("tester(): contact found" );
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

                    //for Copier
                    databaseReference.child("Copier").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Copier copier = dataSnapshot.getValue(Copier.class);
                            if(copier.getCompany_id().equals(sharedCoWorkingCompany.getCompanyid())){
                                System.out.println("tester(): copier found" );
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

                    //for FollowUp
                    databaseReference.child("FollowUp").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            FollowUp followUp = dataSnapshot.getValue(FollowUp.class);
                            if(followUp.getCompanyid().equals(sharedCoWorkingCompany.getCompanyid())){
                                System.out.println("tester(): followUp found" );
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent i = new Intent(AddSharedCoWorkingCompany.this, ViewMySharedCoWorkingActivity.class);
        AddSharedCoWorkingCompany.this.startActivity(i);
        return true;
    }

}
