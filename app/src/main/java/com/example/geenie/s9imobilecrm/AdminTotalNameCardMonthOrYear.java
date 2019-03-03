package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class AdminTotalNameCardMonthOrYear extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlMonth, rlYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_total_name_card_month_or_year);
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
            Intent i = new Intent(AdminTotalNameCardMonthOrYear.this, AdminViewNameCardAddedMonthSelector.class);
            AdminTotalNameCardMonthOrYear.this.startActivity(i);
        }
        else if(view.equals(rlYear)){
            Intent i = new Intent(AdminTotalNameCardMonthOrYear.this, AdminViewTotalNameCardByYear.class);
            AdminTotalNameCardMonthOrYear.this.startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(AdminTotalNameCardMonthOrYear.this, AdminMainActivity.class);
        AdminTotalNameCardMonthOrYear.this.startActivity(i);
        return true;
    }
}
