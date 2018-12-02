package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ViewMyDetailedTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle, tvDesc, tvWithCompany, tvDueDate, tvStatus, tvToolbarTitle;
    private LinearLayout containerLog;
    private EditText etLog;
    private RelativeLayout btnLog;
    private RelativeLayout btnCompleteTask;
    private String title, companyid, companyname, key;
    private Task task;
    private String taskDbKey;

//    private HashMap<String, String> log;
    private ArrayList<String> log;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerTask, childEventListenerContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_detailed_task);

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

        companyname = getIntent().getExtras().getString("companyname");
        companyid = getIntent().getExtras().getString("companyid");
        title = getIntent().getExtras().getString("title");
        key = getIntent().getExtras().getString("key");

        tvTitle = findViewById(R.id.tvTaskTitle);
        tvDesc = findViewById(R.id.tvTaskDesc);
        tvWithCompany = findViewById(R.id.tvWithCompany);
        tvDueDate = findViewById(R.id.tvTaskDueDate);
        tvStatus = findViewById(R.id.tvTaskStatus);
        etLog = findViewById(R.id.etLog);
        btnLog = findViewById(R.id.btnLog);
        btnCompleteTask = findViewById(R.id.btnCompleteTask);
        containerLog = findViewById(R.id.containerLog);
        tvToolbarTitle = findViewById(R.id.toolbar_title);

        if(key.equalsIgnoreCase("admin")){
            btnCompleteTask.setVisibility(View.GONE);
        }

        btnLog.setOnClickListener(this);
        btnCompleteTask.setOnClickListener(this);
        retrieveTaskDetails();


    }



    public void retrieveTaskDetails(){

        childEventListenerTask = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                task = dataSnapshot.getValue(Task.class);
                if(task!=null){
                    if(task.getTitle().equals(title) && task.getCompanyid().equals(companyid)) {

                        if(task.getStatus().equalsIgnoreCase("completed")){
                            btnCompleteTask.setVisibility(View.GONE);
                            btnLog.setVisibility(View.GONE);
                            etLog.setVisibility(View.GONE);
                            tvToolbarTitle.setText("Completed Task");
                        }

                        taskDbKey = dataSnapshot.getKey();

                        tvTitle.setText(tvTitle.getText().toString().concat(task.getTitle()));
                        tvDesc.setText(tvDesc.getText().toString().concat(task.getDesc()));
                        tvDueDate.setText(tvDueDate.getText().toString().concat(task.getDueDate()));
                        tvStatus.setText(tvStatus.getText().toString().concat(task.getStatus()));
                        tvWithCompany.setText(tvWithCompany.getText().toString().concat(task.getCompanyName()));

                        if(task.getLog()!=null) {
                            log = task.getLog();
                            for (int i = 0; i < log.size(); i++) {
                                String currentlog = log.get(i);
                                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View addView = layoutInflater.inflate(R.layout.rowlog, null);
                                final TextView tvLog = addView.findViewById(R.id.tvLog);
                                tvLog.setText(currentlog);
                                containerLog.addView(addView, containerLog.getChildCount());
                            }
                        }

//                        Iterator myVeryOwnIterator = log.keySet().iterator();
//                        while(myVeryOwnIterator.hasNext()) {
//                            String key=(String)myVeryOwnIterator.next();
//                            String value=(String)log.get(key);
//
//                            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            final View addView = layoutInflater.inflate(R.layout.rowlog, null);
//                            final TextView tvLog = addView.findViewById(R.id.tvLog);
//                            tvLog.setText(key.concat(": " + value));
//                            containerLog.addView(addView, containerLog.getChildCount());
//                        }

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
        };
        databaseReference.child("Task").orderByChild("title").equalTo(title).addChildEventListener(childEventListenerTask);
    }


    @Override
    public void onClick(View view) {

        if(view.equals(btnLog)){

            String newlog = getName().concat(": ") + etLog.getText().toString();
            log.add(newlog);
            etLog.setText("");

            databaseReference.child("Task").child(taskDbKey).child("log").setValue(log);
            readLogs(log);
        }
        else if(view.equals(btnCompleteTask)){
            databaseReference.child("Task").child(taskDbKey).child("status").setValue("completed");
            databaseReference.child("Task").child(taskDbKey).child("dateCompleted").setValue(getDateCreateNow());
            Toast.makeText(getApplicationContext(), "TASK COMPLETED!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ViewMyDetailedTaskActivity.this, ViewMyTaskActivity.class);
            ViewMyDetailedTaskActivity.this.startActivity(i);
        }
    }

    public void readLogs(ArrayList<String> arrayList){

        containerLog.removeAllViews();

        for(int i=0; i<log.size(); i++) {
            String currentlog = arrayList.get(i);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.rowlog, null);
            final TextView tvLog = addView.findViewById(R.id.tvLog);
            tvLog.setText(currentlog);
            containerLog.addView(addView, containerLog.getChildCount());
        }

    }

    public String getName(){
        String[] split = user.getEmail().split("@");
        return split[0].substring(0, 1).toUpperCase()+split[0].substring(1);
    }

    public String getDateCreateNow(){

        String dt = Calendar.getInstance().getTime().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String output = sdf1.format(c.getTime());

        return output;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        if(key.equalsIgnoreCase("admin")){
            Intent i = new Intent(ViewMyDetailedTaskActivity.this, AdminViewAllTaskAssignment.class);
            ViewMyDetailedTaskActivity.this.startActivity(i);
        }
        else{
            Intent i = new Intent(ViewMyDetailedTaskActivity.this, ViewMyTaskActivity.class);
            ViewMyDetailedTaskActivity.this.startActivity(i);
        }
        return true;
    }
}
