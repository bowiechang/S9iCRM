package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView tvAppt, tvFu, tvTask, tvCompany, tvCopier;
    private CardView cvAppt, cvTask, cvFollowup, cvCompany, cvCopier;

    private RelativeLayout relativeLayoutAddNameCard;

    private ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private ArrayList<FollowUp> followUpArrayList = new ArrayList<>();
    private ArrayList<Copier> copierArrayList = new ArrayList<>();
    private ArrayList<Company> companyArrayList = new ArrayList<>();

    private int apptCount = 0;
    private int fuCount = 0;
    private int taskCount = 0;
    private int companyCount = 0;
    private int copierCount = 0;
    private int copierCount2 = 0;


    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigationview
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        init();
    }

    public void init(){

        relativeLayoutAddNameCard = findViewById(R.id.relativeLayoutAddNameCard);
        relativeLayoutAddNameCard.setOnClickListener(this);

        tvAppt = findViewById(R.id.tvAppointment);
        tvFu = findViewById(R.id.tvFollowUp);
        tvTask = findViewById(R.id.tvTask);
        tvCompany = findViewById(R.id.tvCompany);
        tvCopier = findViewById(R.id.tvCopier);

        //copier retrieval has different setting of text, seems it doesnt set for 0 count so we set it here
        tvCopier.setText(String.valueOf(copierCount));

        cvAppt = findViewById(R.id.cardviewAppt);
        cvTask = findViewById(R.id.cardviewTask);
        cvFollowup = findViewById(R.id.cardviewFollowup);
        cvCompany = findViewById(R.id.cardviewCompany);
        cvCopier = findViewById(R.id.cardviewCopier);

        cvAppt.setOnClickListener(this);
        cvTask.setOnClickListener(this);
        cvFollowup.setOnClickListener(this);
        cvCompany.setOnClickListener(this);
        cvCopier.setOnClickListener(this);

        getAlertCountForAppt();
        getAlertCountForFollowUp();
        getAlertForTask();
        getAlertForCompany();
        getAlertForCopier();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = new Intent();
        if (id == R.id.namecard) {
            i = new Intent(MainActivity.this, ViewMyNameCardActivity.class);

        } else if (id == R.id.appt) {
            i = new Intent(MainActivity.this, ViewMyAppointmentActivity.class);

        } else if (id == R.id.followup) {
            i = new Intent(MainActivity.this, ViewMyFollowUpActivity.class);
        } else if (id == R.id.task) {
            i = new Intent(MainActivity.this, ViewMyTaskActivity.class);
        } else if(id == R.id.coworking) {
            i = new Intent(MainActivity.this, ViewMySharedCoWorkingActivity.class);
        } else if(id == R.id.admin) {
            i = new Intent(MainActivity.this, AdminLoginActivity.class);
        }

        MainActivity.this.startActivity(i);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getAlertCountForAppt(){
        databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                if(appointment.getCreateby().equals(uid)){

                    System.out.println("alert: appt: found");

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
                        System.out.println("alert: appt in the future");
                        appointmentArrayList.add(appointment);
                        apptCount++;
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
        databaseReference.child("Appointment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvAppt.setText((String.valueOf(apptCount)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAlertCountForFollowUp(){
        databaseReference.child("FollowUp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FollowUp followUp = dataSnapshot.getValue(FollowUp.class);
                if(followUp.getCreatedBy().equals(uid)) {
                    if (followUp.getFollowUpStatus().equals("incomplete")) {

                        System.out.println("alert: appt: found");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String todayDate = df.format(c.getTime());

                        String receivedDate = followUp.getFollowupDueDate();
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
                            System.out.println("alert: appt in the future");
                            followUpArrayList.add(followUp);
                            fuCount++;
                        }
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
        databaseReference.child("FollowUp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvFu.setText((String.valueOf(fuCount)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAlertForTask(){
        databaseReference.child("Task").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Task task = dataSnapshot.getValue(Task.class);
                if(task.getAssigned_uid().equals(uid)) {
                    if (task.getStatus().equals("incomplete")) {

                        System.out.println("alert: appt in the future");
                        taskArrayList.add(task);
                        taskCount++;

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
        databaseReference.child("Task").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvTask.setText((String.valueOf(taskCount)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAlertForCompany(){
        databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                if(company.getCreateBy().equals(uid)){
                    if(company.getPriorityLevel().equals("a.Urgent")){
                        companyCount++;
                        companyArrayList.add(company);
                        System.out.println("alert:: company PL count: " + company.getPriorityLevel());
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
        databaseReference.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvCompany.setText((String.valueOf(companyCount)));
                System.out.println("alert:: company PL added: ");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAlertForCopier(){
        databaseReference.child("Copier").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Copier copier = dataSnapshot.getValue(Copier.class);
                System.out.println("alert:: copier info: " + copier.getCompany_id());
                databaseReference.child("Company").child(copier.getCompany_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Company company = dataSnapshot.getValue(Company.class);
                        if (company.getCreateBy().equals(uid)) {
                            //do it here

                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            String dt = Calendar.getInstance().getTime().toString();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c = Calendar.getInstance();
                            try {
                                c.setTime(sdf.parse(dt));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            c.add(Calendar.MONTH, 5);
                            String date5monthString = df.format(c.getTime());
                            String expiryDateString = copier.getContractExpiryDate();
                            try {
                                Date date5monthDate = sdf.parse(date5monthString);
                                Date expiryDate = sdf.parse(expiryDateString);
                                System.out.println("alert:: Copier date: date of expiry: " + expiryDate.toString());
                                System.out.println("alert:: Copier date: date of date5monthDate: " + date5monthDate.toString());
                                if (date5monthDate.after(expiryDate)) {
                                    copierCount++;
                                    System.out.println("alert:: Copier count: " + copierCount);
                                    copierArrayList.add(copier);
                                    tvCopier.setText(String.valueOf(copierCount));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
    public void onClick(View view) {
        if(view.equals(cvAppt)){
            Intent intent = new Intent(MainActivity.this, DashboardApptActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",appointmentArrayList);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }
        else if(view.equals(cvTask)){
            Intent intent = new Intent(MainActivity.this, DashboardTaskActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",taskArrayList);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }
        else if(view.equals(cvFollowup)){
            Intent intent = new Intent(MainActivity.this, DashboardFollowupActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",followUpArrayList);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }
        else if(view.equals(cvCompany)){
            Intent intent = new Intent(MainActivity.this, DashboardCompanyActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",companyArrayList);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }
        else if(view.equals(cvCopier)){
            Intent intent = new Intent(MainActivity.this, DashboardCopierActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",copierArrayList);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }
        else if(view.equals(relativeLayoutAddNameCard)){
            Intent intent = new Intent(MainActivity.this, AddNameCardActivity.class);
            startActivity(intent);
        }
    }
}
