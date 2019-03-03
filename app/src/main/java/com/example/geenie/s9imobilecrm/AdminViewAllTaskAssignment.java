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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminViewAllTaskAssignment extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewTaskAssgin;
    private RelativeLayout rlTaskAssign, rlViewCompeletedTask;
    private TextView tvNo;
    private ArrayList<Task> taskArrayList ;

    //firebase init
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private FirebaseUser user = mAuth.getCurrentUser();
//    private String uid;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Task");
    private ChildEventListener childEventListenerContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_task_assignment);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //idea of placing TASK
//         ArrayList<String> log = new ArrayList<>();
//        log.add("Howie: Show them Model 256M if they are still looking");
//
//        Task task = new Task("Meet up with client and proceed to close", "Client is interested in mdk20v", "20/12/2018",
//                "incomplete", "", "-LPK5VRDdzAp2HHu-yRZ", "CUBIT PTE LTD", "PKjjIXFJsldUFOi13AfQb8eS0ih1", log);
//
//        databaseReference.push().setValue(task);

        init();
    }

    public void init(){

        recyclerViewTaskAssgin = findViewById(R.id.rvTaskAssignment);
        recyclerViewTaskAssgin.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTaskAssgin.setHasFixedSize(true);
        rlTaskAssign = findViewById(R.id.relativeLayoutAssignTask);
        rlViewCompeletedTask = findViewById(R.id.relativeLayoutViewCompletedTask);

        rlTaskAssign.setOnClickListener(this);
        rlViewCompeletedTask.setOnClickListener(this);

        tvNo = findViewById(R.id.tvNone);
        taskArrayList = new ArrayList<>();

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

                    if(task.getStatus().equals("incomplete")){
                        taskArrayList.add(task);
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(taskArrayList.size() == 0){
                    tvNo.setVisibility(View.VISIBLE);
                    recyclerViewTaskAssgin.setVisibility(View.GONE);
                }
                else{
                    getMyTaskIncompleteList(taskArrayList);
                    System.out.println("viewmytask rv generated");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMyTaskIncompleteList(ArrayList list){
        System.out.println("viewmytask getMyTaskIncompleteList");

        ViewMyTaskAdapter viewMyTaskAdapter = new ViewMyTaskAdapter(AdminViewAllTaskAssignment.this, list, "admin");
        recyclerViewTaskAssgin.setAdapter(viewMyTaskAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(rlTaskAssign)){
            Intent i = new Intent(AdminViewAllTaskAssignment.this, AdminAssignTaskActivity.class);
            AdminViewAllTaskAssignment.this.startActivity(i);
        }
        else if(view.equals(rlViewCompeletedTask)){
            Intent i = new Intent(AdminViewAllTaskAssignment.this, AdminViewCompletedTaskActivity.class);
            AdminViewAllTaskAssignment.this.startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(AdminViewAllTaskAssignment.this, AdminMainActivity.class);
        AdminViewAllTaskAssignment.this.startActivity(i);
        return true;
    }


}
