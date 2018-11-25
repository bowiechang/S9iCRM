package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class AdminViewAppointmentMadeMonthSelector extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlJan, rlFeb, rlMar, rlApr, rlMay, rlJun, rlJul, rlAug, rlSep, rlOct, rlNov, rlDec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_appointment_made_month_selector);

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

        rlJan = findViewById(R.id.rlJAN);
        rlFeb = findViewById(R.id.rlFEB);
        rlMar = findViewById(R.id.rlMAR);
        rlApr = findViewById(R.id.rlAPR);
        rlMay = findViewById(R.id.rlMAY);
        rlJun = findViewById(R.id.rlJUN);
        rlJul = findViewById(R.id.rlJUL);
        rlAug = findViewById(R.id.rlAUG);
        rlSep = findViewById(R.id.rlSEP);
        rlOct = findViewById(R.id.rlOCT);
        rlNov = findViewById(R.id.rlNOV);
        rlDec = findViewById(R.id.rlDEC);

        rlJan.setOnClickListener(this);
        rlFeb.setOnClickListener(this);
        rlMar.setOnClickListener(this);
        rlApr.setOnClickListener(this);
        rlMay.setOnClickListener(this);
        rlJun.setOnClickListener(this);
        rlJul.setOnClickListener(this);
        rlAug.setOnClickListener(this);
        rlSep.setOnClickListener(this);
        rlOct.setOnClickListener(this);
        rlNov.setOnClickListener(this);
        rlDec.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(rlJan)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "01");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlFeb)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "02");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlMar)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "03");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlApr)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "04");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlMay)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "05");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlJun)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "06");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlJul)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "07");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlAug)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "08");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlSep)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "09");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlOct)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "10");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlNov)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "11");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(rlDec)){
            Intent intent = new Intent(this, AdminViewAppointmentDetailedMonth.class);
            Bundle extras = new Bundle();
            extras.putString("month", "12");
            intent.putExtras(extras);
            this.startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(AdminViewAppointmentMadeMonthSelector.this, AdminViewAppointmentMonthOrYear.class);
        AdminViewAppointmentMadeMonthSelector.this.startActivity(i);
        return true;
    }
}
