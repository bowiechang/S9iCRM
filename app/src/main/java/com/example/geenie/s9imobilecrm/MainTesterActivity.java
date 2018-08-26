package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainTesterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLOGINSIGNUP;
    private Button btnAddNameCard;
    private Button btnViewNameCard;
    private Button btnViewAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tester);

        btnLOGINSIGNUP = findViewById(R.id.btnLOGINSIGNUP);
        btnAddNameCard = findViewById(R.id.btnAddNameCard);
        btnViewNameCard = findViewById(R.id.btnViewNameCard);
        btnViewAppointment = findViewById(R.id.btnViewAppointment);

        btnLOGINSIGNUP.setOnClickListener(this);
        btnAddNameCard.setOnClickListener(this);
        btnViewNameCard.setOnClickListener(this);
        btnViewAppointment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnLOGINSIGNUP){
            Intent i = new Intent(MainTesterActivity.this, SignUpLoginActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnAddNameCard){
            Intent i = new Intent(MainTesterActivity.this, AddNameCardActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnViewNameCard){
            Intent i = new Intent(MainTesterActivity.this, ViewMyNameCardActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnViewAppointment){
            Intent i = new Intent(MainTesterActivity.this, ViewMyAppointmentActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
    }
}
