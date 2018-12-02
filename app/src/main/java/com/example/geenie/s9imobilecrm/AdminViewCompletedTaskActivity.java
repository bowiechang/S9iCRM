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
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminViewCompletedTaskActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCompleted;
    private ArrayList<Task> listCompleted;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Task");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_completed_task);

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

        recyclerViewCompleted = findViewById(R.id.rvComplete);
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompleted.setHasFixedSize(true);

        listCompleted = new ArrayList<>();
        read();
    }


    public void read(){

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Task task = dataSnapshot.getValue(Task.class);
                System.out.println("in");
                if(task!=null){

//                    Calendar c = Calendar.getInstance();
//                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                    String todayDate = df.format(c.getTime());
//
//                    String receivedDate = appointment.getDate();
//                    if (receivedDate.isEmpty()) {
//                        Log.d("receivedDate", "isempty");
//                    } else {
//                        Log.d("receivedDate", receivedDate);
//                    }
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                    Date dateAppointment = null;
//                    Date dateToday = null;
//
//                    try {
//                        dateToday = sdf.parse(todayDate);
//                        dateAppointment = sdf.parse(receivedDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    if (dateAppointment.after(dateToday)) {
//                        listUpcoming.add(appointment);
//                        getMyAppointmentUpcomingList(listUpcoming);
//                    }
//                    else{
//                        listPast.add(appointment);
//                        getMyAppointmentPastList(listPast);
//                    }

                    System.out.println("viewmytask: " + task.getStatus());

                    if(task.getStatus().equalsIgnoreCase("completed")){
                        listCompleted.add(task);
                        getMyTaskCompleteList(listCompleted);

                        System.out.println("viewmytask complete: in");
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

    private void getMyTaskCompleteList(ArrayList list){
        ViewMyCompletedTaskAdapter viewMyTaskAdapter = new ViewMyCompletedTaskAdapter(AdminViewCompletedTaskActivity.this, list);
        recyclerViewCompleted.setAdapter(viewMyTaskAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent i = new Intent(AdminViewCompletedTaskActivity.this, ViewMyTaskActivity.class);
        AdminViewCompletedTaskActivity.this.startActivity(i);
        return true;
    }
}
