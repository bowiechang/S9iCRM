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

public class DashboardTaskActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTask;
    private ArrayList<Task> taskArrayList;
    private TextView tvNone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_task);//status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        taskArrayList = (ArrayList<Task>) args.getSerializable("ARRAYLIST");
        System.out.println("upcomingappt:: arraylist received size: " + taskArrayList.size());

        init();
    }

    public void init(){

        tvNone = findViewById(R.id.tvNone);
        recyclerViewTask = findViewById(R.id.rvTask);
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTask.setHasFixedSize(true);

        if(!taskArrayList.isEmpty()){

            //date sorter
            Collections.sort(taskArrayList, new Comparator<Task>() {
                public int compare(Task o1, Task o2) {
                    if (o1.getDueDate() == null || o2.getDueDate() == null)
                        return 0;
                    return o1.getDueDate().compareTo(o2.getDueDate());
                }
            });

            getMyTaskIncompleteList(taskArrayList);
        }
        else{
            tvNone.setVisibility(View.VISIBLE);
            recyclerViewTask.setVisibility(View.GONE);
        }
    }

    private void getMyTaskIncompleteList(ArrayList list){
        ViewMyTaskAdapter viewMyTaskAdapter = new ViewMyTaskAdapter(DashboardTaskActivity.this, list);
        recyclerViewTask.setAdapter(viewMyTaskAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
