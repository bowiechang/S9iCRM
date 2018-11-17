package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardCompanyActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCompany;
    private ArrayList<Company> companyArrayList;
    private TextView tvNone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_company);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        companyArrayList = (ArrayList<Company>) args.getSerializable("ARRAYLIST");

        init();
    }

    public void init(){

        tvNone = findViewById(R.id.tvNone);
        recyclerViewCompany = findViewById(R.id.rvCompany);
        recyclerViewCompany.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompany.setHasFixedSize(true);

        if(!companyArrayList.isEmpty()){
            getCompany(companyArrayList);
        }
        else{
            tvNone.setVisibility(View.VISIBLE);
            recyclerViewCompany.setVisibility(View.GONE);
        }

    }


    private void getCompany(ArrayList list){

        ViewDashboardNameCardAdapter viewDashboardNameCardAdapter = new ViewDashboardNameCardAdapter(DashboardCompanyActivity.this, list, list);
        recyclerViewCompany.setAdapter(viewDashboardNameCardAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
