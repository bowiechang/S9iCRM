package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMyAppointmentDetailedActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private TextView tvDate, tvTime, tvLocation, tvWith, tvComment;
    private EditText etDate, etTime, etLocationName, etLocationAddress, etComment;
    private Spinner spWith;
    private Button btnApptEditSave, btnDeleteAppt;

    private String companyName, apptTime, apptDate;
    private Appointment appointment;
    private Contact contact;

    private ArrayList<String> arrayListContactkey;
    private ArrayList<String> arrayListContactName;

    private String appointmentDbKey;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerAppointment, childEventListenerContact;

    //placepicker init
    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_appointment_detailed);
        init();


    }

    public void init(){

        arrayListContactkey = new ArrayList<>();
        arrayListContactName = new ArrayList<>();

        companyName = getIntent().getExtras().getString("companyName");
        apptTime = getIntent().getExtras().getString("time");
        apptDate = getIntent().getExtras().getString("date");

        tvDate = findViewById(R.id.tvApptDate);
        tvTime = findViewById(R.id.tvApptTime);
        tvLocation = findViewById(R.id.tvApptLocation);
        tvWith = findViewById(R.id.tvApptContact);
        tvComment = findViewById(R.id.tvApptComment);

        btnApptEditSave = findViewById(R.id.btnApptEditSave);
        btnDeleteAppt = findViewById(R.id.btnDeleteAppt);
        btnApptEditSave.setOnClickListener(this);
        btnDeleteAppt.setOnClickListener(this);

        etDate = findViewById(R.id.etApptDate);
        etTime = findViewById(R.id.etApptTime);
        etLocationName = findViewById(R.id.etApptLocation);
        etLocationAddress = findViewById(R.id.etApptLocationAddress);
        etComment = findViewById(R.id.etApptComment);
        spWith = findViewById(R.id.spApptContact);

        etLocationName.setOnFocusChangeListener(this);

        etDate.setVisibility(View.GONE);
        etTime.setVisibility(View.GONE);
        etLocationName.setVisibility(View.GONE);
        etLocationAddress.setVisibility(View.GONE);
        etComment.setVisibility(View.GONE);
        spWith.setVisibility(View.GONE);

        retrieveAppointmentDetails();
    }

    public void retrieveAppointmentDetails(){

        childEventListenerAppointment = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                appointment = dataSnapshot.getValue(Appointment.class);
                if(appointment!=null){
                    if(appointment.getDate().equals(apptDate) && appointment.getTime().equals(apptTime)){

                        appointmentDbKey = dataSnapshot.getKey();

                        tvDate.setText(tvDate.getText().toString().concat(appointment.getDate()));
                        tvTime.setText(tvTime.getText().toString().concat(appointment.getTime()));
                        tvLocation.setText(tvLocation.getText().toString().concat(appointment.getLocationName().concat(", " + appointment.getLocationAddress())));
                        tvComment.setText(tvComment.getText().toString().concat(appointment.getComments()));

                        etDate.setText(appointment.getDate());
                        etTime.setText(appointment.getTime());
                        etComment.setText(appointment.getComments());


                        if(!appointment.getLocationName().equals("")){
                            tvLocation.setText(tvLocation.getText().toString().concat(appointment.getLocationName().concat(", " + appointment.getLocationAddress())));
                            etLocationName.setText(appointment.getLocationName());
                            etLocationAddress.setText(appointment.getLocationAddress());
                        }
                        else{
                            tvLocation.setText(tvLocation.getText().toString().concat(appointment.getLocationAddress()));
                            etLocationAddress.setText(appointment.getLocationAddress());
                        }

                        etDate.setText(appointment.getDate());
                        etTime.setText(appointment.getTime());
                        etComment.setText(appointment.getComments());
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
        };
        databaseReference.child("Appointment").orderByChild("companyName").equalTo(companyName).addChildEventListener(childEventListenerAppointment);
        databaseReference.child("Appointment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("Appointment").removeEventListener(childEventListenerAppointment);
                System.out.println("child event removed");

               setUpSpinnerContact(appointment.getCompany_id());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUpSpinnerContact(String companyDbKey){

        childEventListenerContact = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                contact = dataSnapshot.getValue(Contact.class);

                arrayListContactkey.add(dataSnapshot.getKey());
                String contactValue = contact.getName().concat(", " + contact.getTitle());
                if(contact.getIc()){
                    contactValue = contact.getName().concat("(IC)");
                }
                arrayListContactName.add(contactValue);
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

        databaseReference.child("Contact").orderByChild("company_id").equalTo(companyDbKey).addChildEventListener(childEventListenerContact);
        databaseReference.child("Contact").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("Contact").removeEventListener(childEventListenerContact);
                System.out.println("child event removed");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewMyAppointmentDetailedActivity.this,
                        android.R.layout.simple_spinner_item, arrayListContactName);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                System.out.println("arraylistcn: " + arrayListContactName);
                spWith.setAdapter(adapter);

                for(int i=0; i < arrayListContactkey.size(); i ++){
                    if(arrayListContactkey.get(i).equals(appointment.getContact_id())){
                        spWith.setSelection(i);
                        tvWith.setText(tvWith.getText().toString().concat(arrayListContactName.get(i)));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnApptEditSave)){
            if(btnApptEditSave.getText().toString().equals("Edit")){
                setAppointmentEditable();
            }
            else{

                String date = etDate.getText().toString();
                String time = etTime.getText().toString();
                String locationName = etLocationName.getText().toString();
                String locationAddress = etLocationAddress.getText().toString();

                int selecteditem = spWith.getSelectedItemPosition();
                String contact_id = arrayListContactkey.get(selecteditem);

                String comment = etComment.getText().toString();

                Appointment appointment2 = new Appointment(companyName, time, date, locationName, locationAddress,
                        comment, uid, appointment.getDateCreated(), contact_id,  appointment.getCompany_id());
                databaseReference.child("Appointment").child(appointmentDbKey).setValue(appointment2);
            }
        }
        else if(view.equals(btnDeleteAppt)){
            databaseReference.child("Appointment").child(appointmentDbKey).removeValue();
        }
    }

    public void setAppointmentEditable(){

        tvDate = findViewById(R.id.tvApptDate);
        tvTime = findViewById(R.id.tvApptTime);
        tvLocation = findViewById(R.id.tvApptLocation);
        tvWith = findViewById(R.id.tvApptContact);
        tvComment = findViewById(R.id.tvApptComment);

        tvDate.setText("APPOINTMENT ON: ");
        tvTime.setText("APPOINTMENT AT: ");
        tvLocation.setText("APPOINTMENT LOCATION: ");
        tvWith.setText("APPOINTMENT WITH: ");
        tvComment.setText("APPOINTMENT COMMENT: ");

        etDate.setVisibility(View.VISIBLE);
        etTime.setVisibility(View.VISIBLE);
        etLocationName.setVisibility(View.VISIBLE);
        etLocationAddress.setVisibility(View.VISIBLE);
        etComment.setVisibility(View.VISIBLE);
        spWith.setVisibility(View.VISIBLE);

        btnApptEditSave.setText("SAVE");

    }

    public void selectLocation(){

        Log.d("selectLocation", "reached");
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(ViewMyAppointmentDetailedActivity.this);
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
                etLocationName.setText(locationName);
                etLocationAddress.setText(address);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b){
            selectLocation();
        }
    }
}
