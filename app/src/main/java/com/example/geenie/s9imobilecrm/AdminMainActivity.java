package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlMainViewAllRecords, rlMainViewApptMade, rlMainViewNameCardAdded, rlUserManagement, rlTaskAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rlMainViewAllRecords = findViewById(R.id.rlAdminMainViewAllRecords);
        rlMainViewAllRecords.setOnClickListener(this);

        rlMainViewApptMade = findViewById(R.id.rlAdminMainViewApptMade);
        rlMainViewApptMade.setOnClickListener(this);

        rlMainViewNameCardAdded = findViewById(R.id.rlAdminMainViewNameCardAdded);
        rlMainViewNameCardAdded.setOnClickListener(this);

        rlUserManagement = findViewById(R.id.rlAdminUserManagement);
        rlUserManagement.setOnClickListener(this);

        rlTaskAssignment = findViewById(R.id.rlAdminTaskAssignment);
        rlTaskAssignment.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(AdminMainActivity.this, MainActivity.class);
        AdminMainActivity.this.startActivity(i);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(rlMainViewAllRecords)){
            Intent i = new Intent(AdminMainActivity.this, AdminViewAllRecordActivity.class);
            AdminMainActivity.this.startActivity(i);
        }
        else if(view.equals(rlMainViewApptMade)){
            Intent i = new Intent(AdminMainActivity.this, AdminViewAppointmentMonthOrYear.class);
            AdminMainActivity.this.startActivity(i);
        }
        else if(view.equals(rlMainViewNameCardAdded)){
            Intent i = new Intent(AdminMainActivity.this, AdminTotalNameCardMonthOrYear.class);
            AdminMainActivity.this.startActivity(i);
        }
        else if(view.equals(rlUserManagement)){
            Intent i = new Intent(AdminMainActivity.this, AdminUserManagement.class);
            AdminMainActivity.this.startActivity(i);
        }
        else if(view.equals(rlTaskAssignment)){
            Intent i = new Intent(AdminMainActivity.this, AdminViewAllTaskAssignment.class);
            AdminMainActivity.this.startActivity(i);
        }
    }
}
