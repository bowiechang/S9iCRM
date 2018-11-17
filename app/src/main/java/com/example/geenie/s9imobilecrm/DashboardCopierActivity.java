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
import java.util.Collections;
import java.util.Comparator;

public class DashboardCopierActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCopier;
    private ArrayList<Copier> copierArrayList;
    private TextView tvNone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_copier);
        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        copierArrayList = (ArrayList<Copier>) args.getSerializable("ARRAYLIST");

        init();
    }

    public void init(){

        tvNone = findViewById(R.id.tvNone);
        recyclerViewCopier = findViewById(R.id.rvCopier);
        recyclerViewCopier.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCopier.setHasFixedSize(true);

        if(!copierArrayList.isEmpty()){

            //date sorter
            Collections.sort(copierArrayList, new Comparator<Copier>() {
                public int compare(Copier o1, Copier o2) {
                    if (o1.getContractExpiryDate() == null || o2.getContractExpiryDate() == null)
                        return 0;
                    return o1.getContractExpiryDate().compareTo(o2.getContractExpiryDate());
                }
            });

            getMyCopierList(copierArrayList);
        }
        else{
            tvNone.setVisibility(View.VISIBLE);
            recyclerViewCopier.setVisibility(View.GONE);
        }
    }

    private void getMyCopierList(ArrayList list){
        ViewDashboardCopierAdapter viewDashboardCopierAdapter = new ViewDashboardCopierAdapter(DashboardCopierActivity.this, list);
        recyclerViewCopier.setAdapter(viewDashboardCopierAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
