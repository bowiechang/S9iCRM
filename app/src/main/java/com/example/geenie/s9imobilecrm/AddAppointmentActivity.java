package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class AddAppointmentActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCompanyName, tvNoContact;
    private EditText etTime, etDate, etLocation, etLocationAddress, etComment;
    private Spinner spinnerContact;
    private RelativeLayout btnAddApointment;

    private String companyDbkey, companyName, companyAddress;
    private Calendar calendar = Calendar.getInstance();

    private ArrayList<String> arrayListContactkey;
    private ArrayList<String> arrayListContactName;
    private static final String[] paths = {};

    //placepicker init
    int PLACE_PICKER_REQUEST = 1;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redesign_activity_appointment);
        init();

        //status bar
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void init(){

        arrayListContactkey = new ArrayList<>();
        arrayListContactName = new ArrayList<>();

        companyDbkey = getIntent().getExtras().getString("companydbkey");
        companyName = getIntent().getExtras().getString("companyName");
        companyAddress = getIntent().getExtras().getString("companyAddress");

        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvNoContact = findViewById(R.id.tvNoContact);
        etTime = findViewById(R.id.etApptTime);
        etDate = findViewById(R.id.etApptDate);
        etLocation = findViewById(R.id.etApptLocation);
        etLocationAddress = findViewById(R.id.etApptLocationAddress);
        etComment = findViewById(R.id.etApptComment);
        spinnerContact = findViewById(R.id.spApptContact);
        btnAddApointment = findViewById(R.id.btnAddAppointment);
        btnAddApointment.setOnClickListener(this);

        retrieveContact(companyDbkey);
        retrieveCompanyDetails();

        tvCompanyName.setText(companyName);
        etLocationAddress.setText(companyAddress);
        etLocation.setVisibility(View.GONE);

        //get date
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

        //get time
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(etTime)) {
                    getTime(etTime);
                }
            }
        });
        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    getTime(etTime);
                }
            }
        });

        etLocationAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    etLocation.setText("");
                    selectLocation();
                }
            }
        });

    }

    public void retrieveCompanyDetails(){

        databaseReference.child("Company").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company c = dataSnapshot.getValue(Company.class);
                if(dataSnapshot.getKey().equalsIgnoreCase(companyDbkey) && c.getName().equalsIgnoreCase(companyName)){
                    uid = c.getCreateBy();
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


    public void retrieveContact(final String companyDbkey){

        childEventListenerContacts = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Contact contact = dataSnapshot.getValue(Contact.class);
                if(contact!=null){
                    arrayListContactkey.add(dataSnapshot.getKey());
                    String contactValue = contact.getName().concat(", " + contact.getTitle());
                    if(contact.getIc()){
                        contactValue = contact.getName().concat("(IC)");
                    }
                    arrayListContactName.add(contactValue);
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
        };

        databaseReference.child("Contact").orderByChild("company_id").equalTo(companyDbkey).addChildEventListener(childEventListenerContacts);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("Contact").removeEventListener(childEventListenerContacts);
                System.out.println("child event removed");

                if(arrayListContactkey.size() == 0){
                    spinnerContact.setVisibility(View.GONE);
                    tvNoContact.setVisibility(View.VISIBLE);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddAppointmentActivity.this,
                        R.layout.custom_spinner_item, arrayListContactName);

                adapter.setDropDownViewResource(R.layout.custom_spinner_item);
                spinnerContact.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                if(month == 0 || month == 1 || month == 2 || month == 3 || month == 4 || month == 5
                        || month == 6 || month == 7 || month == 8) {
                    et.setText(day + "/0" + (month + 1) + "/" + year);
                }
                else{
                    et.setText(day + "/" + (month + 1) + "/" + year);
                }

            }
        }
                ,day,month,year);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.show();
    }

    //start time
    public void getTime(final EditText et){
        int hour, mins;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                if(hour > 12){
                    hour = hour - 12;
                    et.setText(String.format("%01d:%02d PM", hour, minute));
                }
                else{
                    et.setText(String.format("%01d:%02d AM", hour, minute));
                }

            }
        },hour, mins, false);
        timePickerDialog.show();
    }

    public void selectLocation(){

        Log.d("selectLocation", "reached");
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(AddAppointmentActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("onActivityResult", "reached");
        if(requestCode == PLACE_PICKER_REQUEST){
            Log.d("requestCode", "reached");
            if(resultCode == RESULT_OK){

                Place place = PlaceAutocomplete.getPlace(this, data);
                String locationName = String.format("%s", place.getName());
                String address = String.format("%s", place.getAddress());
                LatLng latLng = place.getLatLng();

                String placeid = place.getId();
//                LatLng latlng = latLng;
//                tvLocation.setText(locationName);
                System.out.println("locationname: " + locationName);
                etLocation.setText(locationName);
                etLocationAddress.setText(address);

                etLocation.setVisibility(View.VISIBLE);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addAppointment(){

        String apptDate = etDate.getText().toString();
        String apptTime = etTime.getText().toString();
        String apptLocationName = etLocation.getText().toString();
        String apptLocationAddress = etLocationAddress.getText().toString();
        String apptComment = etComment.getText().toString();

        String dateCreated = getDateCreateNow();
        int selecteditem = spinnerContact.getSelectedItemPosition();
        String contact_id;
        if(arrayListContactkey == null || arrayListContactkey.size() == 0){
            contact_id = "empty";
        }
        else {
            contact_id = arrayListContactkey.get(selecteditem);
        }

        Appointment appointment = new Appointment(companyName, apptTime, apptDate, apptLocationName, apptLocationAddress, apptComment, uid, getDateCreateNow(), contact_id, companyDbkey);
        databaseReference.child("Appointment").push().setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Appointment Added", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddAppointmentActivity.this, ViewMyAppointmentActivity.class);
                AddAppointmentActivity.this.startActivity(i);
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

    @Override
    public void onClick(View view) {
        if(view.equals(btnAddApointment)){
            addAppointment();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
