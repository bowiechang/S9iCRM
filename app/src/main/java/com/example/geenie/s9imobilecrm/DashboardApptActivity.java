package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DashboardApptActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUpcoming;
    private ArrayList<Appointment> appointmentArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_appt);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        appointmentArrayList = (ArrayList<Appointment>) args.getSerializable("ARRAYLIST");
        System.out.println("upcomingappt:: arraylist received size: " + appointmentArrayList.size());

        init();
    }

    public void init(){

        recyclerViewUpcoming = findViewById(R.id.rvUpcoming);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUpcoming.setHasFixedSize(true);

        if(!appointmentArrayList.isEmpty()){

            //date sorter
            Collections.sort(appointmentArrayList, new Comparator<Appointment>() {
                public int compare(Appointment o1, Appointment o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o1.getDate().compareTo(o2.getDate());
                }
            });

            getMyAppointmentUpcomingList(appointmentArrayList);
        }

    }

    private void getMyAppointmentUpcomingList(ArrayList list){
        ViewMyAppointmentAdapter viewMyAppointmentAdapter = new ViewMyAppointmentAdapter(DashboardApptActivity.this, list);
        recyclerViewUpcoming.setAdapter(viewMyAppointmentAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
