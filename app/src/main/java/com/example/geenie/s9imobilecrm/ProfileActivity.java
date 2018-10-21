package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName;
    Button btnSave;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private String uid = user.getUid();
    private String name = getName();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

    }

    public void init(){

        etName = findViewById(R.id.etName);
        btnSave = findViewById(R.id.btnSaveProfile);

        btnSave.setOnClickListener(this);
        etName.setText(name);
    }

    public String getName(){

        String email  = user.getEmail();
        String[] split = email.split("@");

        String name = split[0];
        System.out.println("name: " + name);

        String formattedName = name.substring(0,1).toUpperCase() + name.substring(1);

        return formattedName;
    }

    @Override
    public void onClick(View view) {

        String email = user.getEmail();
        String role = "employee";

        User user = new User(name, email, role);

        databaseReference.child("User").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "SAVE SUCCESSFUL!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                ProfileActivity.this.startActivity(i);
            }
        });



    }
}
