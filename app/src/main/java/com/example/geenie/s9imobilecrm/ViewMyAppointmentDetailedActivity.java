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
import java.util.Date;

public class ViewMyAppointmentDetailedActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private RelativeLayout containerRead, containerEdit;

    private TextView tvDate, tvTime, tvLocation, tvWith, tvComment, tvCompanyName, tvCompanyNameEdit;
    private EditText etDate, etTime, etLocationName, etLocationAddress, etComment;
    private Spinner spWith;
    private RelativeLayout btnApptEditSave, btnDeleteAppt;
    private TextView tvbtnTextEditSave, tvbtnTextDelete;

    private String companyName, apptTime, apptDate;
    private Appointment appointment;
    private Contact contact;

    private ArrayList<String> arrayListContactkey;
    private ArrayList<String> arrayListContactName;

    private String appointmentDbKey;

    private Calendar calendar = Calendar.getInstance();
    private Boolean editable = false;

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
        setContentView(R.layout.redesign_activity_view_my_appointment_detailed);

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

        containerRead = findViewById(R.id.containerRead);
        containerEdit = findViewById(R.id.containerEdit);

        arrayListContactkey = new ArrayList<>();
        arrayListContactName = new ArrayList<>();

        companyName = getIntent().getExtras().getString("companyname");
        apptTime = getIntent().getExtras().getString("time");
        apptDate = getIntent().getExtras().getString("date");

        tvDate = findViewById(R.id.tvApptDate);
        tvTime = findViewById(R.id.tvApptTime);
        tvLocation = findViewById(R.id.tvApptLocation);
        tvWith = findViewById(R.id.tvApptContact);
        tvComment = findViewById(R.id.tvApptComment);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanyNameEdit = findViewById(R.id.tvCompanyNameEdit);

        tvCompanyName.setText(companyName);
        tvCompanyNameEdit.setText(companyName);

        btnApptEditSave = findViewById(R.id.btnApptEditSave);
        btnDeleteAppt = findViewById(R.id.btnDeleteAppt);
        btnApptEditSave.setOnClickListener(this);
        btnDeleteAppt.setOnClickListener(this);

        tvbtnTextDelete = findViewById(R.id.tvBtnDelete);
        tvbtnTextEditSave = findViewById(R.id.tvBtnEditSave);

        etDate = findViewById(R.id.etApptDate);
        etTime = findViewById(R.id.etApptTime);
        etLocationName = findViewById(R.id.etApptLocation);
        etLocationAddress = findViewById(R.id.etApptLocationAddress);
        etComment = findViewById(R.id.etApptComment);
        spWith = findViewById(R.id.spApptContact);

        etLocationName.setOnFocusChangeListener(this);
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
                if(view.equals(etDate)) {
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
                    if(appointment.getDate().equals(apptDate) && appointment.getTime().equals(apptTime)) {
                        appointmentDbKey = dataSnapshot.getKey();

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String todayDate = df.format(c.getTime());

                        String receivedDate = appointment.getDate();
                        if (receivedDate.isEmpty()) {
                            Log.d("receivedDate", "isempty");
                        } else {
                            Log.d("receivedDate", receivedDate);
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateAppointment = null;
                        Date dateToday = null;

                        try {
                            dateToday = sdf.parse(todayDate);
                            dateAppointment = sdf.parse(receivedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (dateAppointment.after(dateToday)) {
                            editable = true;
                        }
                        else{
                            btnApptEditSave.setVisibility(View.GONE);
                            btnDeleteAppt.setVisibility(View.GONE);
                        }

                        tvDate.setText(tvDate.getText().toString().concat(appointment.getDate()));
                        tvTime.setText(tvTime.getText().toString().concat(appointment.getTime()));
                        tvComment.setText(tvComment.getText().toString().concat(appointment.getComments()));

                        etDate.setText(appointment.getDate());
                        etTime.setText(appointment.getTime());
                        etComment.setText(appointment.getComments());

                        if(appointment.getComments().equalsIgnoreCase("")){
                            tvComment.setText("No Comments");
                        }


                        if (!appointment.getLocationName().equals("")) {
                            tvLocation.setText(tvLocation.getText().toString().concat(appointment.getLocationName().concat(", " + appointment.getLocationAddress())));
                            etLocationName.setText(appointment.getLocationName());
                            etLocationAddress.setText(appointment.getLocationAddress());
                        } else {
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
        databaseReference.child("Appointment").orderByChild("name").equalTo(companyName).addChildEventListener(childEventListenerAppointment);
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
                        R.layout.custom_spinner_item, arrayListContactName);

                adapter.setDropDownViewResource(R.layout.custom_spinner_item);
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
            if(tvbtnTextEditSave.getText().toString().equalsIgnoreCase("Edit")){
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
                databaseReference.child("Appointment").child(appointmentDbKey).setValue(appointment2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Save Successful!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ViewMyAppointmentDetailedActivity.this, ViewMyAppointmentActivity.class);
                        ViewMyAppointmentDetailedActivity.this.startActivity(i);
                    }
                });


            }
        }
        else if(view.equals(btnDeleteAppt)){
            databaseReference.child("Appointment").child(appointmentDbKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Delete Successful!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ViewMyAppointmentDetailedActivity.this, ViewMyAppointmentActivity.class);
                    ViewMyAppointmentDetailedActivity.this.startActivity(i);
                }
            });
        }
    }

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

    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);



        DatePickerDialog datePickerDialog = new DatePickerDialog(ViewMyAppointmentDetailedActivity.this, new DatePickerDialog.OnDateSetListener() {

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

    public void setAppointmentEditable(){

        containerRead.setVisibility(View.GONE);
        containerEdit.setVisibility(View.VISIBLE);

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

        tvbtnTextEditSave.setText("SAVE");

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

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(ViewMyAppointmentDetailedActivity.this, ViewMyAppointmentActivity.class);
        ViewMyAppointmentDetailedActivity.this.startActivity(i);
        return true;
    }
}
