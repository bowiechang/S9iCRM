package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

public class AdminTaskAssignment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_task_assignment);

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //idea of placing TASK
//         ArrayList<String> log = new ArrayList<>();
//        log.add("Howie: Show them Model 256M if they are still looking");
//
//        Task task = new Task("Meet up with client and proceed to close", "Client is interested in mdk20v", "20/12/2018",
//                "incomplete", "", "-LPK5VRDdzAp2HHu-yRZ", "CUBIT PTE LTD", "PKjjIXFJsldUFOi13AfQb8eS0ih1", log);
//
//        databaseReference.push().setValue(task);
    }
}
