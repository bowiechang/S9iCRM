package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPassword;
    private RelativeLayout relativeLayoutAdminLogin;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

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

//        sp = getSharedPreferences("login", MODE_PRIVATE);
//        if(sp.getBoolean("logged", true)){
//            //user is admin already
//            Intent i = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
//            AdminLoginActivity.this.startActivity(i);
//        }

        etPassword = findViewById(R.id.adminLogin);
        relativeLayoutAdminLogin = findViewById(R.id.btnAdminLogin);
        relativeLayoutAdminLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.equals(relativeLayoutAdminLogin)){
            if(etPassword.getText().toString().trim().equalsIgnoreCase("517550517")){

                //add to sp
//                sp.edit().putBoolean("logged",true).apply();

                //and then proceed to admin activity
                Intent i = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                AdminLoginActivity.this.startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
