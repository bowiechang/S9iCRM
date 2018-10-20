package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class importTester extends AppCompatActivity {

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_tester);
        readCsv();

    }

    public void readCsv(){

        InputStream is = getResources().openRawResource(R.raw.csvtext);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        try{
            while ((line = reader.readLine()) != null){

                line = line.replaceAll("\"", "");
                String [] tokens = line.split(",");

                System.out.println("importer:: line token: " + tokens[0]);

                String dateCreate = getDateCreateNow();

                Company c = new Company(tokens[0], tokens[1], tokens[2], tokens[3],
                    tokens[4], tokens[5], tokens[6], tokens[7], tokens[8], dateCreate, Integer.parseInt(tokens[9]));


                String key = databaseReference.child("Tester").child("Company").push().getKey();
                databaseReference.child("Tester").child("Company").child(key).setValue(c);

                Boolean torf = null;
                if(tokens[15].equals("TRUE")){
                    torf = true;
                }
                else if(tokens[15].equals("FALSE")){
                    torf = false;
                }

                Contact ct = new Contact(tokens[10], tokens[11], tokens[12], tokens[13], tokens[14], torf, key);

                databaseReference.child("Tester").child("Contact").push().setValue(ct);
            }
        }
        catch (IOException e){
            System.out.println("importer:: error reading");
        }
    }

    public String getDateCreateNow(){

        String dt = Calendar.getInstance().getTime().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String output = sdf1.format(c.getTime());

        return output;
    }
}
