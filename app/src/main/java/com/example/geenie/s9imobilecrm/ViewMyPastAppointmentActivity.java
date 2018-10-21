package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewMyPastAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPast;
    private ArrayList<Appointment> listPast;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Appointment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_appointment);

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

        recyclerViewPast = findViewById(R.id.rvPast);
        recyclerViewPast.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPast.setHasFixedSize(true);
        listPast = new ArrayList<>();
        read();
    }

    public void read(){

        databaseReference.orderByChild("createby").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                if(appointment!=null){

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String todayDate = df.format(c.getTime());

                    String receivedDate = appointment.getDate();
                    if (receivedDate.isEmpty()) {
                        Log.d("receivedDate", "isempty");
                    } else {
                        Log.d("receivedDate", receivedDate);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateAppointment = null;
                    Date dateToday = null;

                    try {
                        dateToday = sdf.parse(todayDate);
                        dateAppointment = sdf.parse(receivedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (dateAppointment.before(dateToday)) {
                        listPast.add(appointment);
                        getMyAppointmentPastList(listPast);
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
    private void getMyAppointmentPastList(ArrayList list){
        ViewMyAppointmentAdapter viewMyAppointmentAdapter = new ViewMyAppointmentAdapter(ViewMyPastAppointmentActivity.this, list);
        recyclerViewPast.setAdapter(viewMyAppointmentAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
