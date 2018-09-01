package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ViewMyDetailedFollowUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFollowUpName, tvFollowUpDate, tvFollowUpType, tvFollowUpStatus;
    private EditText etDate;
    private Button btnEdit, btnRemove, btnComplete;
    private String companyname, companyid, date, type, status;
    private FollowUp followUp;
    private String followUpDBkey;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FollowUp");
    private ChildEventListener childEventListenerFollowUp;

    private Calendar calendar = Calendar.getInstance();

    private Boolean editable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_detailed_follow_up);

        init();
    }

    public void getDBKey(){

        childEventListenerFollowUp = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                followUp = dataSnapshot.getValue(FollowUp.class);
                if(followUp!=null){

                    if(followUp.getCompanyid().equals(companyid) && followUp.getFollowupDueDate().equals(date) && followUp.getTypeOfFollowup().equals(type)){

                        followUpDBkey = dataSnapshot.getKey();
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
        databaseReference.addChildEventListener(childEventListenerFollowUp);
    }

    public void init(){

        companyname = getIntent().getExtras().getString("companyname");
        companyid = getIntent().getExtras().getString("companyid");
        date = getIntent().getExtras().getString("date");
        type = getIntent().getExtras().getString("type");
        status = getIntent().getExtras().getString("status");



        tvFollowUpDate = findViewById(R.id.tvFollowUpDate);
        tvFollowUpName = findViewById(R.id.tvFollowUpName);
        tvFollowUpType = findViewById(R.id.tvFollowUpType);
        tvFollowUpStatus = findViewById(R.id.tvFollowUpStatus);

        etDate = findViewById(R.id.etFollowUpDate);
        etDate.setVisibility(View.GONE);

        //get date
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(etDate)) {
                    getDate(etDate);
                }
            }
        });
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    getDate(etDate);
                }
            }
        });

        btnComplete = findViewById(R.id.btnFollowUpComplete);
        btnEdit = findViewById(R.id.btnFollowUpEdit);
        btnRemove = findViewById(R.id.btnFollowUpRemove);

        btnRemove.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnComplete.setOnClickListener(this);

        if(status.equals("completed")){
            editable = false;
            btnRemove.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
        }

        tvFollowUpDate.setText(date);
        etDate.setText(date);
        tvFollowUpName.setText(companyname);
        tvFollowUpType.setText(type);
        tvFollowUpStatus.setText(status);

        getDBKey();
    }

    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ViewMyDetailedFollowUpActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                et.setText(day+"/"+(month+1)+"/"+year);
            }
        }
                ,day,month,year);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.show();
    }

    public void setEditable(){
        etDate.setVisibility(View.VISIBLE);
        tvFollowUpDate.setText("Follow up Date: ");
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnEdit)){
            if(btnEdit.getText().toString().equals("Edit")) {
                setEditable();
                btnEdit.setText("Save");
            }
            else{
                String newDate = etDate.getText().toString();
                databaseReference.child(followUpDBkey).child("followupDueDate").setValue(newDate);
                btnEdit.setText("Edit");
            }
        }
        else if(view.equals(btnComplete)){
            databaseReference.child(followUpDBkey).child("followUpStatus").setValue("completed");
            Intent i = new Intent(ViewMyDetailedFollowUpActivity.this, ViewMyFollowUpActivity.class);
            ViewMyDetailedFollowUpActivity.this.startActivity(i);
        }
        else if(view.equals(btnRemove)){
            databaseReference.child(followUpDBkey).removeValue();
            Intent i = new Intent(ViewMyDetailedFollowUpActivity.this, ViewMyFollowUpActivity.class);
            ViewMyDetailedFollowUpActivity.this.startActivity(i);
        }
    }
}
