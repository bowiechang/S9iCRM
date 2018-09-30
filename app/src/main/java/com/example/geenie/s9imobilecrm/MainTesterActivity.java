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
    private Button btnViewTask;
    private Button btnViewFollowUp;
    private Button btnViewCopier;
    private Button btnAddCoWorkingCompany;
    private Button btnViewCoWorkingCompany;
    private Button btnTakePhotoActivity;
    private Button btnViewAlerts;
    private Button btnViewDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tester);

        btnLOGINSIGNUP = findViewById(R.id.btnLOGINSIGNUP);
        btnAddNameCard = findViewById(R.id.btnAddNameCard);
        btnViewNameCard = findViewById(R.id.btnViewNameCard);
        btnViewAppointment = findViewById(R.id.btnViewAppointment);
        btnViewTask = findViewById(R.id.btnViewTask);
        btnViewFollowUp = findViewById(R.id.btnViewFollowUp);
        btnViewCopier = findViewById(R.id.btnViewCopier);
        btnAddCoWorkingCompany = findViewById(R.id.btnAddSharedCompany);
        btnViewCoWorkingCompany = findViewById(R.id.btnViewSharedCompany);
        btnTakePhotoActivity = findViewById(R.id.btnTakePhotoActivity);
        btnViewAlerts = findViewById(R.id.btnViewAlerts);
        btnViewDashboard = findViewById(R.id.btnViewDashBoard);

        btnLOGINSIGNUP.setOnClickListener(this);
        btnAddNameCard.setOnClickListener(this);
        btnViewNameCard.setOnClickListener(this);
        btnViewAppointment.setOnClickListener(this);
        btnViewTask.setOnClickListener(this);
        btnViewFollowUp.setOnClickListener(this);
        btnViewCopier.setOnClickListener(this);
        btnAddCoWorkingCompany.setOnClickListener(this);
        btnViewCoWorkingCompany.setOnClickListener(this);
        btnTakePhotoActivity.setOnClickListener(this);
        btnViewAlerts.setOnClickListener(this);
        btnViewDashboard.setOnClickListener(this);
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
        else if(view == btnViewTask){
            Intent i = new Intent(MainTesterActivity.this, ViewMyTaskActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnViewFollowUp){
            Intent i = new Intent(MainTesterActivity.this, ViewMyFollowUpActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnViewCopier){
            Intent i = new Intent(MainTesterActivity.this, ViewMyCopierActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnAddCoWorkingCompany){
            Intent i = new Intent(MainTesterActivity.this, AddSharedCoWorkingCompany.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnViewCoWorkingCompany){
            Intent i = new Intent(MainTesterActivity.this, ViewMySharedCoWorkingActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnTakePhotoActivity){
            Intent i = new Intent(MainTesterActivity.this, CapturePhotoActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnViewAlerts){
            Intent i = new Intent(MainTesterActivity.this, TesterForAlertActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
        else if(view == btnViewDashboard){
            Intent i = new Intent(MainTesterActivity.this, MainActivity.class);
            MainTesterActivity.this.startActivity(i);
        }
    }
}
