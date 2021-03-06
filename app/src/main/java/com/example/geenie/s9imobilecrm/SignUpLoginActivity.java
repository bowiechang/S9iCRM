package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etSUName, etSUpassword, etLemail, etLpassword;
    Button btnSU, btnLogin;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);

        init();
    }

    public void init(){

        etSUName = findViewById(R.id.emailSignup);
        etSUpassword = findViewById(R.id.passwordSignup);
        btnSU = findViewById(R.id.btnSignUp);

        etLemail = findViewById(R.id.emailLogin);
        etLpassword = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);

        btnSU.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == btnSU){
            //perform SU here
            performSignUp();
        }
        else if(view == btnLogin){
            //perform login here
            performLogin();
        }
    }

    public void performSignUp(){

        String email = etSUName.getText().toString().trim();
        String password = etSUpassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
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
                                Intent i = new Intent(SignUpLoginActivity.this, MainActivity.class);
                                SignUpLoginActivity.this.startActivity(i);
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

    public void performLogin(){

        final String email = etLemail.getText().toString().trim();
        final String password = etLpassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "LOGIN SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpLoginActivity.this, MainActivity.class);
                    SignUpLoginActivity.this.startActivity(i);
                }
                else{
                    Log.d("Login failed", task.getException().getMessage().toString());
                    Toast.makeText(getApplicationContext(), "FAILED: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
