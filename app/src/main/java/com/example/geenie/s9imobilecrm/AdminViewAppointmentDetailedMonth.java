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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminViewAppointmentDetailedMonth extends AppCompatActivity{

    private HashMap<String, User> userHashMap = new HashMap<>();
    private HashMap<String, String> userCount = new HashMap<>();
    private ArrayList<Appointment> apptList = new ArrayList<>();

    private ArrayList<String> counterlist = new ArrayList<>();

    private String monthreceivedkey;

    private RelativeLayout rlLoading, rlNone;

    // get current year、month and day
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);

    List<DataEntry> data = new ArrayList<>();


    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_appointment_made);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        monthreceivedkey = getIntent().getExtras().getString("month");

        retrieveUsers();

        init();
    }

    public void init(){

        rlLoading = findViewById(R.id.rlLoading);
        rlNone = findViewById(R.id.rlNone);

    }

    public void retrieveUsers(){
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User u = dataSnapshot.getValue(User.class);
                userHashMap.put(dataSnapshot.getKey(), u);
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
                retrieveAppt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void retrieveAppt(){
        databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                apptList.add(appointment);
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

        databaseReference.child("Appointment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retrieveData(monthreceivedkey, String.valueOf(year));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrieveData(String monthkey, String yearkey){

        System.out.println("apptmade:: counterlist size: " + counterlist.size());


        for (int i = 0; i < apptList.size(); i++) {
            //set the month here

            //get the month out
            String [] dateSplit = apptList.get(i).getDate().split("/");
            System.out.println("apptmade:: year from split: " + dateSplit[2]);
            if(dateSplit[1].equalsIgnoreCase(monthkey) && dateSplit[2].equalsIgnoreCase(yearkey)){
                counterlist.add(apptList.get(i).getCreateby());
                System.out.println("apptmade:: added into counterlist");
            }
        }

        if(counterlist.size() == 0){
            rlNone.setVisibility(View.VISIBLE);
        }

        System.out.println("apptmade:: userhashmap: " + userHashMap.size());
        System.out.println("apptmade:: apptlist: " + apptList.size());
        System.out.println("apptmade:: counterlist: " + counterlist.size());

        for (Map.Entry<String, User> entry : userHashMap.entrySet()) {
            String key = entry.getKey();
            User user = entry.getValue();

            int count = 0;
            for (int i = 0; i < counterlist.size() ; i++) {

                if(counterlist.get(i).equalsIgnoreCase(key)){
                    count ++;
                }
            }
            userCount.put(user.getName(), String.valueOf(count));
        }

        System.out.println("apptmade:: userCount: " + userCount.toString());

        for (Map.Entry<String, String> entry : userCount.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            data.add(new ValueDataEntry(key, Integer.parseInt(value)));
        }

        System.out.println("apptmade:: pie creation");


        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(AdminViewAppointmentDetailedMonth.this, event.getData().get("x") + " Made " + event.getData().get("value") + " Appointment", Toast.LENGTH_SHORT).show();
            }
        });

        pie.data(data);
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Sales Team")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        AnyChartView anyChartView = findViewById(R.id.piechart);
        anyChartView.setChart(pie);

        rlLoading.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(AdminViewAppointmentDetailedMonth.this, AdminViewAppointmentMonthOrYear.class);
        AdminViewAppointmentDetailedMonth.this.startActivity(i);
        return true;
    }




































    //THIS IS THE CODE FOR COLUMN GRAPH//


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_view_appointment_made);
//
//        //status bar
//        Window window = this.getWindow();
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
//
//        //toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//
//        AnyChartView anyChartView = findViewById(R.id.piechart);
//
//        Cartesian cartesian = AnyChart.column();
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("How", 3));
//        data.add(new ValueDataEntry("Gen", 4));
//        data.add(new ValueDataEntry("Jod", 1));
//        data.add(new ValueDataEntry("Yun", 0));
//        data.add(new ValueDataEntry("Alv", 3));
//        data.add(new ValueDataEntry("Pie", 6));
//        data.add(new ValueDataEntry("Yop", 2));
//
//        Column column = cartesian.column(data);
//
//        column.tooltip()
//                .position(Position.CENTER_BOTTOM)
//                .anchor(Anchor.CENTER_BOTTOM)
//                .offsetX(0d)
//                .offsetY(5d)
//                .format("{%Value}");
//
//        cartesian.animation(true);
//
//        cartesian.yScale().minimum(0d);
//
//        cartesian.yAxis(0).labels().format("{%Value}");
//
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//        cartesian.interactivity().hoverMode(HoverMode.BY_X);
//
//        cartesian.xAxis(0).title("Sales Rep");
//        cartesian.yAxis(0).title("Appointments");
//
//        anyChartView.setChart(cartesian);
//    }
}
