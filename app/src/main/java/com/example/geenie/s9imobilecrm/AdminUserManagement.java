package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserManagement extends AppCompatActivity implements View.OnClickListener {

    EditText etLemail, etLpassword, etLpassword2;
    RelativeLayout btnLogin;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_management);

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

        etLemail = findViewById(R.id.emailLogin);
        etLpassword = findViewById(R.id.passwordLogin);
        etLpassword2 = findViewById(R.id.passwordLogin2);
        btnLogin = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnLogin)){
            performSignUp();
        }
    }

    public void performSignUp(){

        String email = etLemail.getText().toString().trim();
        String password = etLpassword.getText().toString().trim();
        String password2 = etLpassword2.getText().toString().trim();

        if(password.equals(password2)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(), "SIGN UP SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                                FirebaseUser userFirebase = mAuth.getCurrentUser();

                                String uid = userFirebase.getUid();
                                String email1  = userFirebase.getEmail();
                                String[] split = email1.split("@");

                                String name = split[0];

                                String formattedName = name.substring(0,1).toUpperCase() + name.substring(1);


                                String email = userFirebase.getEmail();
                                String role;
                                if(name.equalsIgnoreCase("howie") || name.equalsIgnoreCase("geenie")){
                                    role = "employer";
                                }
                                else{
                                    role = "employee";
                                }

                                User user = new User(formattedName, email, role);

                                databaseReference.child("User").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "SAVE SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                                    }
                                });



                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("SignUp failed", task.getException().getMessage().toString());
                                Toast.makeText(getApplicationContext(), "FAILED: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "PASSWORD DO NOT MATCH!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        this.startActivity(intent);
        return true;
    }

}
