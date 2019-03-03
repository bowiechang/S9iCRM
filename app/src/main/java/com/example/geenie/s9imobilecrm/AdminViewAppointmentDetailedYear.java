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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class AdminViewAppointmentDetailedYear extends AppCompatActivity implements View.OnClickListener{

    private HashMap<String, User> userHashMap = new HashMap<>();
    private HashMap<String, String> userCount = new HashMap<>();
    private ArrayList<Appointment> apptList = new ArrayList<>();

    private ArrayList<String> counterlist = new ArrayList<>();
    private String yearreceivedkey;
    private RelativeLayout rlLoading, rlNone;
    private EditText etYear;
    private TextView tvGo, tvLoading;

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
        setContentView(R.layout.activity_admin_view_appointment_detailed_year);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("year")) {
                yearreceivedkey = getIntent().getExtras().getString("year");
                retrieveUsers();
            }
        }
        else{
            tvLoading.setText("Please select year");
        }


    }

    public void init(){

        rlLoading = findViewById(R.id.rlLoading);
        rlNone = findViewById(R.id.rlNone);
        etYear = findViewById(R.id.etYear);
        tvGo = findViewById(R.id.tvGo);
        tvLoading = findViewById(R.id.tvLoading);

        tvGo.setOnClickListener(this);

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
                retrieveData(yearreceivedkey);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrieveData(String yearkey){

        System.out.println("apptmade:: counterlist size: " + counterlist.size());


        for (int i = 0; i < apptList.size(); i++) {
            //set the month here

            //get the month out
            String [] dateSplit = apptList.get(i).getDate().split("/");
            System.out.println("apptmade:: year from split: " + dateSplit[2]);
            if(dateSplit[2].equalsIgnoreCase(yearkey)){
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
                Toast.makeText(AdminViewAppointmentDetailedYear.this, event.getData().get("x") + " Made " + event.getData().get("value") + " Appointment", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(AdminViewAppointmentDetailedYear.this, AdminViewAppointmentMonthOrYear.class);
        AdminViewAppointmentDetailedYear.this.startActivity(i);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(tvGo)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedYear.class);
            Bundle extras = new Bundle();
            extras.putString("year", etYear.getText().toString().trim());
            intent.putExtras(extras);
            this.startActivity(intent);
        }
    }
}