package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewMyTaskActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIncomplete, recyclerViewCompleted;
    private ArrayList<Task> listIncomplete, listCompleted;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Task");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_task);

//        ArrayList<String> log = new ArrayList<>();
//        log.add("Howie: Show them Model 256M if they are still looking");
//
//        Task task = new Task("Meet up with client and proceed to close", "Client is interested in mdk20v", "20/08/2018",
//                "incomplete", "", "-LKkPuTjiV5s7M_4zTkl", "Back", "2NRwrKKCwybNIO0bqBMCrL2CASk2", log);
//
//        databaseReference.push().setValue(task);
        init();

    }

    public void init(){

        recyclerViewIncomplete = findViewById(R.id.rvIncomplete);
        recyclerViewCompleted = findViewById(R.id.rvComplete);
        recyclerViewIncomplete.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIncomplete.setHasFixedSize(true);
        recyclerViewCompleted.setHasFixedSize(true);

        listIncomplete = new ArrayList<>();
        listCompleted = new ArrayList<>();
        read();
    }


    public void read(){

        databaseReference.orderByChild("assigned_uid").equalTo(uid).addChildEventListener(new ChildEventListener() {
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

                    if(task.getStatus().equals("incomplete")){
                        listIncomplete.add(task);
                        getMyTaskIncompleteList(listIncomplete);

                        System.out.println("viewmytask incomplete: in");
                    }
                    else{
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

    private void getMyTaskIncompleteList(ArrayList list){
        ViewMyTaskAdapter viewMyTaskAdapter = new ViewMyTaskAdapter(ViewMyTaskActivity.this, list);
        recyclerViewIncomplete.setAdapter(viewMyTaskAdapter);
    }

    private void getMyTaskCompleteList(ArrayList list){
        ViewMyTaskAdapter viewMyTaskAdapter = new ViewMyTaskAdapter(ViewMyTaskActivity.this, list);
        recyclerViewCompleted.setAdapter(viewMyTaskAdapter);
    }
}
