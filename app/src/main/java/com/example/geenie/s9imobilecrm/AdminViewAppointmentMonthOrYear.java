package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class AdminViewAppointmentMonthOrYear extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlMonth, rlYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_appointment_month_or_year);

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

        rlMonth = findViewById(R.id.rlMonth);
        rlYear = findViewById(R.id.rlYear);

        rlMonth.setOnClickListener(this);
        rlYear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(rlMonth)){
            Intent i = new Intent(AdminViewAppointmentMonthOrYear.this, AdminViewAppointmentMadeMonthSelector.class);
            AdminViewAppointmentMonthOrYear.this.startActivity(i);
        }
        else if(view.equals(rlYear)){
            Intent i = new Intent(AdminViewAppointmentMonthOrYear.this, AdminViewAppointmentDetailedYear.class);
            AdminViewAppointmentMonthOrYear.this.startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(AdminViewAppointmentMonthOrYear.this, AdminMainActivity.class);
        AdminViewAppointmentMonthOrYear.this.startActivity(i);
        return true;
    }
}
