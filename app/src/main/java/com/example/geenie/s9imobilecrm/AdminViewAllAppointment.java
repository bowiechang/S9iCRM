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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdminViewAllAppointment extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeLayoutViewPastAppointment;
    private TextView tvNo;
    private RecyclerView recyclerViewUpcoming;
    private ArrayList<Appointment> listUpcoming;

    //firebase init
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private FirebaseUser user = mAuth.getCurrentUser();
//    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Appointment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_appointment);

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
        relativeLayoutViewPastAppointment = findViewById(R.id.relativeLayoutViewPastAppointment);
        relativeLayoutViewPastAppointment.setOnClickListener(this);
        recyclerViewUpcoming = findViewById(R.id.rvUpcoming);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUpcoming.setHasFixedSize(true);

        listUpcoming = new ArrayList<>();
        read();
    }

    public void read(){

        listUpcoming.clear();

        databaseReference.addChildEventListener(new ChildEventListener() {
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
                if(listUpcoming.size() == 0) {
                    tvNo.setVisibility(View.VISIBLE);
                    recyclerViewUpcoming.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMyAppointmentUpcomingList(ArrayList list){
        AdminViewAllAppointmentAdapter adminViewAllAppointmentAdapter = new AdminViewAllAppointmentAdapter(AdminViewAllAppointment.this, list);
        recyclerViewUpcoming.setAdapter(adminViewAllAppointmentAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(relativeLayoutViewPastAppointment)){
//            Intent i = new Intent(AdminViewAllAppointment.this, ViewMyPastAppointmentActivity.class);
//            AdminViewAllAppointment.this.startActivity(i);

            Intent intent = new Intent(this, ViewMyPastAppointmentActivity.class);
            Bundle extras = new Bundle();
            extras.putString("powers", "admin");

            intent.putExtras(extras);
            this.startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent i = new Intent(AdminViewAllAppointment.this, AdminViewAllRecordActivity.class);
        AdminViewAllAppointment.this.startActivity(i);
        return true;
    }
}
