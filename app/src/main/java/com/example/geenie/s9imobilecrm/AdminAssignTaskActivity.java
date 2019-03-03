package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public class AdminAssignTaskActivity extends AppCompatActivity implements View.OnClickListener {



    private Spinner spNameCard, spUser;
    private EditText etDate, etTitle, etDesc;

    private RelativeLayout rlSave;

    Boolean exist = false;
    Boolean add = false;
    Company company;

    private Calendar calendar = Calendar.getInstance();

    private ArrayList<Company> companyArrayList= new ArrayList<>();
    private ArrayList<String> arraylistCompanyName = new ArrayList<>(), arrayListCompanyid = new ArrayList<>(), arrayListUsername = new ArrayList<>(), arrayListUserid = new ArrayList<>();

    private TreeMap<String, String> treeMapCompany =new TreeMap<>();

    //firebase init
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assign_task);

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

//        databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Company company = dataSnapshot.getValue(Company.class);
//                if(company.getName().equalsIgnoreCase("A A Khan Law Practice")){
//                    databaseReference.child("Company").child(dataSnapshot.getKey()).removeValue();
//                    System.out.println("removed");
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        treeMapCompany.put("No Company Selected", "0");

        spUser = findViewById(R.id.spinnerUser);
        spNameCard = findViewById(R.id.spinnerNamecard);

        etDate = findViewById(R.id.etDueDate);
        etDesc = findViewById(R.id.etDesc);
        etTitle = findViewById(R.id.etTitle);

        rlSave = findViewById(R.id.btnAddTask);
        rlSave.setOnClickListener(this);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(etDate)) {
                    getDate(etDate);
                }
            }
        });
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    getDate(etDate);
                }
            }
        });

        retrieveMyNameCardsAndUsers();
    }

    public void saveTask(){

        if(etDate.getText().toString()!="" && etTitle.getText().toString()!=""){

            ArrayList<String> log = new ArrayList<>();

            String desc = "";
            desc = etDesc.getText().toString();

            final String companyid, companyname;

            if(spNameCard.getSelectedItem().toString().equalsIgnoreCase("no company selected")){
                companyid = "0";
                companyname = "No company selected";
            }
            else{
                int selectedCompany = spNameCard.getSelectedItemPosition()-1;

                String string1 = treeMapCompany.entrySet().toArray()[selectedCompany].toString();
                String [] spliter = string1.split("=");

                companyid = spliter[1];
                companyname = spliter[0];
            }

            int selectedUser = spUser.getSelectedItemPosition();
            final String userid = arrayListUserid.get(selectedUser);

            Task task = new Task(etTitle.getText().toString(), desc, getDateCreateNow(), etDate.getText().toString(),"incomplete", "",companyid, companyname, userid, log);
            databaseReference.child("Task").push().setValue(task);



            databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.getKey().equalsIgnoreCase(companyid)) {
                        final Company c = dataSnapshot.getValue(Company.class);
                        final String dbkeyCompany = dataSnapshot.getKey();
                        if (c.getCreateBy().equalsIgnoreCase(userid)) {
                            exist = true;
                            System.out.println("adminassigntask:: no need add");
                        } else {
                            company = c;
                            add = true;
                            Company c2 = new Company(company.getName(), company.getAddress(), company.getPostalCode(), company.getUnitNo(), company.getOfficeTel(),
                                    company.getIndustry(), company.getCompanyLackOf(), company.getPriorityLevel(), company.getComment(), userid, getDateCreateNow(),0);

                            final String newkey = databaseReference.child("Company").push().getKey();
                            databaseReference.child("Company").child(newkey).setValue(c2);
                            System.out.println("adminassigntask:: added");

                            databaseReference.child("Contact").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Contact contact = dataSnapshot.getValue(Contact.class);

                                    if(contact.getCompany_id().equalsIgnoreCase(dbkeyCompany)){
                                        Contact newContact = new Contact(contact.getName(), contact.getTitle(), contact.getMobileNumber(), contact.getOfficeNumber(),
                                                contact.getEmail(), contact.getIc(), newkey);

                                        databaseReference.child("Contact").push().setValue(newContact);

                                    }
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
                    }
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
    }

    public void retrieveMyNameCardsAndUsers(){

        databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                treeMapCompany.put(company.getName(), dataSnapshot.getKey());

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

        databaseReference.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> arraylistCompanyName2 = new ArrayList<>();

                arraylistCompanyName2.add("No Company Selected");

//                Set<String> keys = treeMapCompany.keySet();
//                for(String key: keys){
//                    arraylistCompanyName2.add(treeMapCompany.get(key));
//                }

//                for (int i = 0; i < treeMapCompany.size(); i++) {
//                    arraylistCompanyName2.add(treeMapCompany.get(i));
//                }

                for (Map.Entry<String, String> entry : treeMapCompany.entrySet()) {
//                    System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
                    arraylistCompanyName2.add(entry.getKey());
                }

                System.out.println("admintask: arraylistcompanyname: " + arraylistCompanyName2.toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminAssignTaskActivity.this,
                        android.R.layout.simple_spinner_item, arraylistCompanyName2);
                adapter.setDropDownViewResource(R.layout.custom_spinner_item);
                spNameCard.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);

                String[] split = user.getEmail().split("@");
                String name = split[0];
                arrayListUsername.add(name);
                arrayListUserid.add(dataSnapshot.getKey());

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
        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("admintask: arrayListUsername: " + arrayListUsername.toString());


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminAssignTaskActivity.this,
                        android.R.layout.simple_spinner_item, arrayListUsername);
                adapter.setDropDownViewResource(R.layout.custom_spinner_item);
                spUser.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AdminAssignTaskActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                et.setText(day+"/"+(month+1)+"/"+year);
            }
        }
                ,day,month,year);
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(rlSave)){
            saveTask();
            Intent i = new Intent(AdminAssignTaskActivity.this, AdminViewAllTaskAssignment.class);
            AdminAssignTaskActivity.this.startActivity(i);
            Toast.makeText(getApplicationContext(), "Task Assigned!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminAssignTaskActivity.this, AdminViewAllTaskAssignment.class);
        AdminAssignTaskActivity.this.startActivity(i);
    }
}
