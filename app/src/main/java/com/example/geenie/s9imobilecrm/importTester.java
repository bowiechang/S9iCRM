package com.example.geenie.s9imobilecrm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_tester);
//        readCsv();
        dbcounter();
//        delete();
    }

    public void delete(){
        databaseReference.child("Company").removeValue();
        databaseReference.child("Contact").removeValue();

    }

    public void dbcounter(){
        databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("importer:: count: " + i);
                i++;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readCsv(){

        /*HOW TO USE IMPORTER
          1) CONVERT .XLSX FILE INTO UNICODE TEXT FILE
          2) OPEN NOTEPAD AND REPLACE SPACE WITH COMMA
          3) THEN CONVERT IT BACK TO TEXT DOCUMENT WITH UTF-8 ENCODING
          4) DRAG IT INTO THE RAW FOLDER AND SET THE PATH FOR IT.*/


        InputStream is = getResources().openRawResource(R.raw.geenieimporter3);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        try{
            while ((line = reader.readLine()) != null){

                line = line.replaceAll("\"", "");
                String [] tokens = line.split(",");

                System.out.println("importer:: " + "0 line token: " + tokens[0]);
                System.out.println("importer:: " + "1 line token: " + tokens[1]);
                System.out.println("importer:: " + "2 line token: " + tokens[2]);
                System.out.println("importer:: " + "3 line token: " + tokens[3]);
                System.out.println("importer:: " + "4 line token: " + tokens[4]);
                System.out.println("importer:: " + "5 line token: " + tokens[5]);
                System.out.println("importer:: " + "6 line token: " + tokens[6]);
                System.out.println("importer:: " + "7 line token: " + tokens[7]);
                System.out.println("importer:: " + "8 line token: " + tokens[8]);
                System.out.println("importer:: " + "9 line token: " + tokens[9]);
                System.out.println("importer:: " + "10 line token: " + tokens[10]);
                System.out.println("importer:: " + "11 line token: " + tokens[11]);
                System.out.println("importer:: " + "12 line token: " + tokens[12]);
                System.out.println("importer:: " + "13 line token: " + tokens[13]);
                System.out.println("importer:: " + "14 line token: " + tokens[14]);
                System.out.println("importer:: " + "15 line token: " + tokens[15]);

                String dateCreate = getDateCreateNow();

                Company c = new Company(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4],
                    tokens[5], tokens[6], tokens[7], tokens[8], tokens[9], dateCreate, Integer.parseInt(tokens[10].trim()));


                String key = databaseReference.child("Company").push().getKey();
                databaseReference.child("Company").child(key).setValue(c);

                Boolean torf = null;
                if(tokens[16].equals("TRUE")){
                    torf = true;
                }
                else if(tokens[16].equals("FALSE")){
                    torf = false;
                }

                Contact ct = new Contact(tokens[11], tokens[12], tokens[13], tokens[14], tokens[15], torf, key);

                databaseReference.child("Contact").push().setValue(ct);
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
