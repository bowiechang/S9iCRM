package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class AdminViewAllRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeLayoutViewAllNameCard, relativeLayoutViewAllAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_record);

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

        relativeLayoutViewAllAppointment = findViewById(R.id.rlAdminViewAllAppointment);
        relativeLayoutViewAllNameCard = findViewById(R.id.rlAdminViewAllNameCard);

        relativeLayoutViewAllNameCard.setOnClickListener(this);
        relativeLayoutViewAllAppointment.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(AdminViewAllRecordActivity.this, AdminMainActivity.class);
        AdminViewAllRecordActivity.this.startActivity(i);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(relativeLayoutViewAllNameCard)){
            System.out.println("rlAdminViewAllNameCard clicked2");
            Intent i = new Intent(AdminViewAllRecordActivity.this, AdminViewAllNameCardActivity.class);
            AdminViewAllRecordActivity.this.startActivity(i);
        }
        else if(view.equals(relativeLayoutViewAllAppointment)){
            Intent i = new Intent(AdminViewAllRecordActivity.this, AdminViewAllAppointment.class);
            AdminViewAllRecordActivity.this.startActivity(i);
        }
    }
}
