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

public class SignUpLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etSUName, etSUpassword, etLemail, etLpassword;
    Button btnSU, btnLogin;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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


        //listens to auth on changes, if not null proceed
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent i = new Intent(SignUpLoginActivity.this, ProfileActivity.class);
                    SignUpLoginActivity.this.startActivity(i);
                    Toast.makeText(getApplicationContext(), "YOU ARE ALREADY LOGGED IN!", Toast.LENGTH_SHORT).show();

                }
            }
        };
        //add the listener its important!
        mAuth.addAuthStateListener(authStateListener);

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
                    Intent i = new Intent(SignUpLoginActivity.this, ProfileActivity.class);
                    SignUpLoginActivity.this.startActivity(i);
                }
                else{
                    Log.d("Login failed", task.getException().getMessage().toString());
                    Toast.makeText(getApplicationContext(), "FAILED: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    //methods for validation here
    // Check Validation before login
//    private void checkLoginValidation() {
//        // Get email id and password
//        String getEmailId = emailid.getText().toString();
//        String getPassword = password.getText().toString();
//
//        // Check patter for email id
//        Pattern p = Pattern.compile(Utils.regEx);
//
//        Matcher m = p.matcher(getEmailId);
//
//        // Check for both field is empty or not
//        if (getEmailId.equals("") || getEmailId.length() == 0
//                || getPassword.equals("") || getPassword.length() == 0) {
//            loginLayout.startAnimation(shakeAnimation);
////			new CustomToast().Show_Toast(getActivity(), view,
////					"Enter both credentials.");
//
//            Toasty.Config.getInstance()
//                    .setInfoColor(getResources().getColor(R.color.yellow1))
//                    .apply(); // required
//            Toasty.info(getActivity().getBaseContext(), "Enter both credentials", Toast.LENGTH_LONG, true).show();
//
//        }
//        // Check if email id is valid or not
//        else if (!m.find())
////			new CustomToast().Show_Toast(getActivity(), view,
////					"Your Email Id is Invalid.");
//
//            Toasty.error(getActivity().getBaseContext(), "Your Email is invalid", Toast.LENGTH_LONG, true).show();
//
//
//            // Else do login and do your stuff
//        else
//            login();
//
//    }




    // Check Validation Method
//    private void checkSignUpValidation() {
//
//        // Get all edittext texts
//
//        String getEmailId = emailId.getText().toString();
//        String getPassword = password.getText().toString();
//        String getConfirmPassword = confirmPassword.getText().toString();
//
//        // Pattern match for email id
//        Pattern p = Pattern.compile(Utils.regEx);
//        Matcher m = p.matcher(getEmailId);
//
//        Toasty.Config.getInstance()
//                .setInfoColor(getResources().getColor(R.color.yellow1))
//                .apply(); // required
//
//        // Check if all strings are null or not
//        if (getEmailId.equals("") || getEmailId.length() == 0
//                || getPassword.equals("") || getPassword.length() == 0
//                || getConfirmPassword.equals("")
//                || getConfirmPassword.length() == 0)
//
//
//
////			new CustomToast().Show_Toast(getActivity(), view,
////					"All fields are required.");
//
//            Toasty.info(getActivity().getBaseContext(), "All fields are required", Toast.LENGTH_LONG, true).show();
//
//
//            // Check if email id valid or not
//        else if (!m.find())
////			new CustomToast().Show_Toast(getActivity(), view,
////					"Your Email is invalid.");
//
//            Toasty.info(getActivity().getBaseContext(), "Your Email is invalid", Toast.LENGTH_LONG, true).show();
//
//            // Check if both password should be equal
//        else if (!getConfirmPassword.equals(getPassword))
////			new CustomToast().Show_Toast(getActivity(), view,
////					"Passwords doesn't match.");
//            Toasty.info(getActivity().getBaseContext(), "Passwords doesn't match", Toast.LENGTH_LONG, true).show();
//
//            // Make sure user should check Terms and Conditions checkbox
//        else if (!terms_conditions.isChecked())
////			new CustomToast().Show_Toast(getActivity(), view,
////					"Please check Terms and Conditions.");
//            Toasty.info(getActivity().getBaseContext(), "Please check Terms and Conditions", Toast.LENGTH_LONG, true).show();
//
//            // Else do signup or do your stuff
//        else
//            register();
//
//    }

}
