package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StarterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        //listens to auth on changes, if not null proceed
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent i = new Intent(StarterActivity.this, MainActivity.class);
                    StarterActivity.this.startActivity(i);

                }
                else{
                    Intent i = new Intent(StarterActivity.this, SignUpLoginActivity.class);
                    StarterActivity.this.startActivity(i);
                }
            }
        };
        //add the listener its important!
        mAuth.addAuthStateListener(authStateListener);
    }
}
