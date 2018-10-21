package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ViewMyDetailedFollowUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFollowUpName, tvFollowUpDate, tvFollowUpType, tvFollowUpStatus, tvFollowUpPhone;
    private RelativeLayout btnRemove, btnComplete, btnCall;
    private String companyname, companyid, date, type, status;
    private FollowUp followUp;
    private String followUpDBkey, phoneNumber;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FollowUp");
    private DatabaseReference databaseReferenceCompany = FirebaseDatabase.getInstance().getReference().child("Company");
    private ChildEventListener childEventListenerFollowUp;

    private Calendar calendar = Calendar.getInstance();

    private Boolean editable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redesign_activity_view_my_detailed_follow_up);

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

    public void getCompanyDetails(final String companyid){

        databaseReferenceCompany.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company c = dataSnapshot.getValue(Company.class);
                if(dataSnapshot.getKey().equals(companyid)){
                    phoneNumber = c.getOfficeTel();
                    tvFollowUpPhone.setText(phoneNumber);
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

    public void init(){

        companyname = getIntent().getExtras().getString("companyname");
        companyid = getIntent().getExtras().getString("companyid");
        date = getIntent().getExtras().getString("date");
        type = getIntent().getExtras().getString("type");
        status = getIntent().getExtras().getString("status");

        System.out.println("detailed follow up:: companyid: " + companyid);

        tvFollowUpDate = findViewById(R.id.tvFollowUpDate);
        tvFollowUpName = findViewById(R.id.tvFollowUpName);
        tvFollowUpType = findViewById(R.id.tvFollowUpType);
        tvFollowUpStatus = findViewById(R.id.tvFollowUpStatus);
        tvFollowUpPhone = findViewById(R.id.tvFollowupNumber);

        if(companyid!=null){
            getCompanyDetails(companyid);
        }

        btnComplete = findViewById(R.id.btnFollowUpComplete);
        btnRemove = findViewById(R.id.btnFollowUpRemove);
        btnCall = findViewById(R.id.btnCall);

        btnRemove.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        if(status.equalsIgnoreCase("completed")){
            editable = false;
            btnRemove.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
        }

        tvFollowUpDate.setText(date);
        tvFollowUpName.setText(companyname);
        tvFollowUpType.setText(type);
        tvFollowUpStatus.setText(firstletterCaps(status));

        getDBKey();
    }

    public String firstletterCaps(String data){
        String firstLetter = data.substring(0,1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnComplete)){
            databaseReference.child(followUpDBkey).child("followUpStatus").setValue("completed").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Follow Up Compeleted", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        }
        else if(view.equals(btnRemove)){
            databaseReference.child(followUpDBkey).removeValue();
            Intent i = new Intent(ViewMyDetailedFollowUpActivity.this, ViewMyFollowUpActivity.class);
            ViewMyDetailedFollowUpActivity.this.startActivity(i);
        }
        else if(view.equals(btnCall)){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }
}
