package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddSharedCoWorkingCompany extends AppCompatActivity {

    private Button btnAddSharedWorkingCompany, btnRetrieveSharedWorkingCompany;

    private Spinner spNameCard, spUser;

    private Company company;
    private ArrayList<Company> companyArrayList= new ArrayList<>();
    private ArrayList<Appointment> appointmentArrayList= new ArrayList<>();
    private ArrayList<Contact> contactArrayList= new ArrayList<>();
    private ArrayList<Copier> copierArrayList = new ArrayList<>();
    private ArrayList<FollowUp> followUpArrayList= new ArrayList<>();

    private ArrayList<String> arraylistCompanyName = new ArrayList<>(), arrayListCompanyid = new ArrayList<>(), arrayListUsername = new ArrayList<>(), arrayListUserid = new ArrayList<>();

    private SharedCoWorkingCompany sharedCoWorkingCompany;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shared_co_working_company);

        spNameCard = findViewById(R.id.spinnerShareNameCard);
        spUser = findViewById(R.id.spinnerShareWith);

        retrieveMyNameCardsAndUsers();

        btnAddSharedWorkingCompany = findViewById(R.id.btnAddSharedWorkingCompany);
        btnRetrieveSharedWorkingCompany = findViewById(R.id.btnRetrieveSharedWorkingCompany);

        btnRetrieveSharedWorkingCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child("SharedCoWorkingDetails").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        sharedCoWorkingCompany = dataSnapshot.getValue(SharedCoWorkingCompany.class);
                        if(sharedCoWorkingCompany.getCompanyid().equals("-LKkPuTjiV5s7M_4zTkl")){
                            ArrayList<Contact> contactArrayList1;
                            contactArrayList1 = sharedCoWorkingCompany.getContactArrayList();

                            System.out.println("contactarraylist1: " + contactArrayList1.size());
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
        });


        btnAddSharedWorkingCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedCompany = spNameCard.getSelectedItemPosition();
                final String companyid = arrayListCompanyid.get(selectedCompany);

                int selectedContact = spUser.getSelectedItemPosition();
                final String contactid = arrayListUserid.get(selectedContact);

                //retrieve company
                databaseReference.child("Company").child(companyid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        company = dataSnapshot.getValue(Company.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //retrieve Contact
                databaseReference.child("Contact").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Contact contactCurrent = dataSnapshot.getValue(Contact.class);
                        if(contactCurrent.getCompany_id().equals("-LKkPuTjiV5s7M_4zTkl")) {
                            contactArrayList.add(contactCurrent);
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

                //retrieve appointment
                databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Appointment appointmentCurrent = dataSnapshot.getValue(Appointment.class);
                        if(appointmentCurrent.getCompany_id().equals("-LKkPuTjiV5s7M_4zTkl")) {
                            appointmentArrayList.add(appointmentCurrent);
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

                //retrieve copier
                databaseReference.child("Copier").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Copier copierCurrent = dataSnapshot.getValue(Copier.class);
                        if(copierCurrent.getCompany_id().equals("-LKkPuTjiV5s7M_4zTkl")){
                            copierArrayList.add(copierCurrent);
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

                //retrieve follow up
                databaseReference.child("FollowUp").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        FollowUp followUpCurrent = dataSnapshot.getValue(FollowUp.class);
                        if(followUpCurrent.getCompanyid().equals("-LKkPuTjiV5s7M_4zTkl")) {
                            followUpArrayList.add(followUpCurrent);
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

                //
                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            Thread.sleep(3000);
                        }
                        catch (Exception e) { } // Just catch the InterruptedException

                        // Now we use the Handler to post back to the main thread
                        handler.post(new Runnable() {
                            public void run() {
                                // Set the View's visibility back on the main UI Thread

                                    SharedCoWorkingCompany sharedCoWorkingCompany = new SharedCoWorkingCompany(company, copierArrayList, appointmentArrayList,
                                            followUpArrayList, contactArrayList, uid, contactid, companyid);

                                    databaseReference.child("SharedCoWorkingDetails").push().setValue(sharedCoWorkingCompany);

                            }
                        });
                    }
                }).start();


            }
        });
    }

    public void retrieveMyNameCardsAndUsers(){

        databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                if(company.getCreateBy().equals(uid)){
                    companyArrayList.add(company);
                    arrayListCompanyid.add(dataSnapshot.getKey());
                    arraylistCompanyName.add(company.getName());
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSharedCoWorkingCompany.this,
                        android.R.layout.simple_spinner_item, arraylistCompanyName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                arrayListUsername.add(name);
                arrayListUserid.add(dataSnapshot.getKey());
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

}
