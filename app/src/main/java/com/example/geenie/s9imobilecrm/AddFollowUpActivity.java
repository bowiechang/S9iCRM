package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddFollowUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName;
    private EditText etDate;
    private Spinner spinnerType;
    private RelativeLayout btnAddFollowUp;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("FollowUp");
    private ChildEventListener childEventListenerFollowUp;

    private Calendar calendar = Calendar.getInstance();

    private String companyDbkey, companyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_follow_up);

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

        companyDbkey = getIntent().getExtras().getString("companydbkey");
        companyName = getIntent().getExtras().getString("companyName");

        tvName = findViewById(R.id.tvFollowUpName);
        etDate = findViewById(R.id.etFollowUpDate);
        spinnerType = findViewById(R.id.spinnerFollowUp);
        btnAddFollowUp = findViewById(R.id.btnAddFollowUp);
        btnAddFollowUp.setOnClickListener(this);

        tvName.setText(companyName);

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
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnAddFollowUp)){

            String date = etDate.getText().toString();
            String type = spinnerType.getSelectedItem().toString();
            String followupstatus = "incomplete";

            FollowUp followUp = new FollowUp(date, type, followupstatus, companyDbkey, uid);
            databaseReference.push().setValue(followUp).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent i = new Intent(AddFollowUpActivity.this, ViewMyFollowUpActivity.class);
                    AddFollowUpActivity.this.startActivity(i);
                }
            });
        }
    }

    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddFollowUpActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                et.setText(day+"/"+(month+1)+"/"+year);

                //hide keyboard
                View view = AddFollowUpActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
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
}
