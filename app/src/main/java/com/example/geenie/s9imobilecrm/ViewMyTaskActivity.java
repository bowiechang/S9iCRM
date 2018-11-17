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

import java.util.ArrayList;

public class ViewMyTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewIncomplete;
    private ArrayList<Task> listIncomplete;
    private RelativeLayout relativeLayoutViewCompeletdTask;

    private TextView tvNo;

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
//        Task task = new Task("Meet up with client and proceed to close", "Client is interested in mdk20v", "20/12/2018",
//                "incomplete", "", "-LPK5VRDdzAp2HHu-yRZ", "CUBIT PTE LTD", "PKjjIXFJsldUFOi13AfQb8eS0ih1", log);
//
//        databaseReference.push().setValue(task);

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

        relativeLayoutViewCompeletdTask = findViewById(R.id.relativeLayoutViewCompeletdTask);
        relativeLayoutViewCompeletdTask.setOnClickListener(this);
        recyclerViewIncomplete = findViewById(R.id.rvIncomplete);
        recyclerViewIncomplete.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIncomplete.setHasFixedSize(true);

        tvNo = findViewById(R.id.tvNone);

        listIncomplete = new ArrayList<>();
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

        databaseReference.orderByChild("assigned_uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listIncomplete.size() == 0){
                    tvNo.setVisibility(View.VISIBLE);
                    recyclerViewIncomplete.setVisibility(View.GONE);
                }
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

    @Override
    public void onClick(View view) {
        if(view.equals(relativeLayoutViewCompeletdTask)){
            Intent i = new Intent(ViewMyTaskActivity.this, ViewMyCompletedTaskActivity.class);
            ViewMyTaskActivity.this.startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent i = new Intent(ViewMyTaskActivity.this, MainActivity.class);
        ViewMyTaskActivity.this.startActivity(i);
        return true;
    }
}
