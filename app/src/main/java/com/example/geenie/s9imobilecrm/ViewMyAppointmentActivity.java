package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class ViewMyAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUpcoming, recyclerViewPast;
    private ArrayList<Appointment> listUpcoming, listPast;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Appointment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_appointment);

        init();
    }

    public void init(){

        recyclerViewUpcoming = findViewById(R.id.rvUpcoming);
        recyclerViewPast = findViewById(R.id.rvPast);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPast.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUpcoming.setHasFixedSize(true);
        recyclerViewPast.setHasFixedSize(true);

        listUpcoming = new ArrayList<>();
        listPast = new ArrayList<>();
        read();
    }

    public void read(){

        listUpcoming.clear();

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
                    if (dateAppointment.after(dateToday)) {
                        listUpcoming.add(appointment);
                        getMyAppointmentUpcomingList(listUpcoming);
                    }
                    else{
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

    private void getMyAppointmentUpcomingList(ArrayList list){
        ViewMyAppointmentAdapter viewMyAppointmentAdapter = new ViewMyAppointmentAdapter(ViewMyAppointmentActivity.this, list);
        recyclerViewUpcoming.setAdapter(viewMyAppointmentAdapter);
    }

    private void getMyAppointmentPastList(ArrayList list){
        ViewMyAppointmentAdapter viewMyAppointmentAdapter = new ViewMyAppointmentAdapter(ViewMyAppointmentActivity.this, list);
        recyclerViewPast.setAdapter(viewMyAppointmentAdapter);
    }
}
