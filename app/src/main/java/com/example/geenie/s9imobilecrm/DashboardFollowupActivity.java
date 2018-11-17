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

public class DashboardFollowupActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFollowup;
    private ArrayList<FollowUp> followUpArrayList;
    private TextView tvNone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);

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
        followUpArrayList = (ArrayList<FollowUp>) args.getSerializable("ARRAYLIST");
        System.out.println("upcomingappt:: arraylist received size: " + followUpArrayList.size());

        init();
    }

    public void init(){

        tvNone = findViewById(R.id.tvNone);
        recyclerViewFollowup = findViewById(R.id.rvFollowUp);
        recyclerViewFollowup.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFollowup.setHasFixedSize(true);

        if(!followUpArrayList.isEmpty()){

            //date sorter
            Collections.sort(followUpArrayList, new Comparator<FollowUp>() {
                public int compare(FollowUp o1, FollowUp o2) {
                    if (o1.getFollowupDueDate() == null || o2.getFollowupDueDate() == null)
                        return 0;
                    return o1.getFollowupDueDate().compareTo(o2.getFollowupDueDate());
                }
            });

            getMyFollowUp(followUpArrayList);
        }
        else{
            tvNone.setVisibility(View.VISIBLE);
            recyclerViewFollowup.setVisibility(View.GONE);
        }

    }

    private void getMyFollowUp(ArrayList list){
        ViewMyFollowUpAdapter viewMyFollowUpAdapter = new ViewMyFollowUpAdapter(DashboardFollowupActivity.this, list);
        recyclerViewFollowup.setAdapter(viewMyFollowUpAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
