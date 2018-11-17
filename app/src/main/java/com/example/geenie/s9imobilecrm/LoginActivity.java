package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etLemail, etLpassword;
    RelativeLayout btnLogin;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        init();
    }

    public void init(){

        etLemail = findViewById(R.id.emailLogin);
        etLpassword = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);

        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            Toast.makeText(getApplicationContext(), "ALREADY LOGGED IN!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(i);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnLogin)){
            performLogin();
        }
    }

    public void performLogin(){

        final String email = etLemail.getText().toString().trim();
        final String password = etLpassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "LOGIN SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(i);
                }
                else{
                    Log.d("Login failed", task.getException().getMessage().toString());
                    Toast.makeText(getApplicationContext(), "FAILED: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
