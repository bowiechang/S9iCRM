package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewDetailedSharedCoWorkingCompanyActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout containerContacts, containerCopier, containertAppt, containerFollowUp, containerLog;
    private TextView tvDetailedCoWorkingCompanyName, tvDetailedCoWorkingCompanyAddress, tvDetailedCoWorkingCompanyUnit, tvDetailedCoWorkingCompanyPostalCode,
            tvDetailedCoWorkingCompanyNumber, tvDetailedCoWorkingCompanyIndustry, tvDetailedCoWorkingCompanyLack, tvDetailedCoWorkingCompanyNumOfCalls,
            tvDetailedCoWorkingCompanyPL, tvDetailedCoWorkingCompanyComments, tvDetailedCoWorkingCompanyIShare, tvDetailedCoWorkingCompanySharedWith;
    private EditText etLog;
    private Button btnSubmitLog, btnTerminateSharing;

    private String dbSharedCoWorkingKey;
    private String companyid;
    private Company company;
    private ArrayList<FollowUp> followUpArrayList = new ArrayList<>();
    private ArrayList<Copier> copierArrayList= new ArrayList<>();
    private ArrayList<Appointment> appointmentArrayList= new ArrayList<>();
    private ArrayList<Contact> contactArrayList= new ArrayList<>();
    private ArrayList<String> log = new ArrayList<>();

    //photos
    private RecyclerView recyclerView;
    private Button btnCapturePhoto;

    private Boolean exist = false;
    private int count, counter;
    private String dbkey = "";

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerChecker;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detailed_shared_co_working_company);

        init();

//        databaseReference.child("SharedCoWorkingDetails").child("-LLqzXnV4zJt5C-8h5cz").child("copierArrayList").child("1").child("new").setValue("new");
    }

    public void init(){

        companyid = getIntent().getExtras().getString("companyid");

        recyclerView = findViewById(R.id.rvPhotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        retrieveStoragePath();

        etLog = findViewById(R.id.etLog);
        btnSubmitLog = findViewById(R.id.btnLog);
        btnTerminateSharing = findViewById(R.id.btnTerminateSharing);
        btnSubmitLog.setOnClickListener(this);
        btnTerminateSharing.setOnClickListener(this);

        containertAppt = findViewById(R.id.containerAppointment);
        containerCopier = findViewById(R.id.containerCopier);
        containerContacts = findViewById(R.id.containerContact);
        containerFollowUp = findViewById(R.id.containerFollowUp);
        containerLog = findViewById(R.id.containerLog);

        tvDetailedCoWorkingCompanyName = findViewById(R.id.tvDetailedSharedCoWorkingCompanyName);
        tvDetailedCoWorkingCompanyAddress = findViewById(R.id.tvDetailedSharedCoWorkingCompanyAddress);
        tvDetailedCoWorkingCompanyPostalCode = findViewById(R.id.tvDetailedSharedCoWorkingCompanyPostalCode);
        tvDetailedCoWorkingCompanyUnit = findViewById(R.id.tvDetailedSharedCoWorkingCompanyUnit);
        tvDetailedCoWorkingCompanyNumber = findViewById(R.id.tvDetailedSharedCoWorkingCompanyNumber);
        tvDetailedCoWorkingCompanyIndustry = findViewById(R.id.tvDetailedSharedCoWorkingCompanyIndustry);
        tvDetailedCoWorkingCompanyLack = findViewById(R.id.tvDetailedSharedCoWorkingCompanyLacking);
        tvDetailedCoWorkingCompanyNumOfCalls = findViewById(R.id.tvDetailedSharedCoWorkingCompanyNumberOfCall);
        tvDetailedCoWorkingCompanyPL = findViewById(R.id.tvDetailedSharedCoWorkingCompanyPriorityLevel);
        tvDetailedCoWorkingCompanyComments = findViewById(R.id.tvDetailedSharedCoWorkingCompanyComments);
        tvDetailedCoWorkingCompanyIShare = findViewById(R.id.tvDetailedSharedCoWorkingSharerName);
        tvDetailedCoWorkingCompanySharedWith = findViewById(R.id.tvDetailedSharedCoWorkingSharedToName);


        getAndSetName();
        retrieveCompany(companyid);
        retrievingContacts();
        retrievingCopiers();
        retrievingAppointments();
        retrievingFollowUps();
        retrievingLog();

    }

    public void retrieveCompany(final String companyid){

        databaseReference.child("Company").orderByKey().equalTo(companyid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                company = dataSnapshot.getValue(Company.class);
                //company
                tvDetailedCoWorkingCompanyName.setText(company.getName());
                tvDetailedCoWorkingCompanyAddress.setText(tvDetailedCoWorkingCompanyAddress.getText().toString().concat(getLatLng(company.getPostalCode().trim())));
                tvDetailedCoWorkingCompanyPostalCode.setText(tvDetailedCoWorkingCompanyPostalCode.getText().toString().concat(company.getPostalCode()));
                tvDetailedCoWorkingCompanyUnit.setText(tvDetailedCoWorkingCompanyUnit.getText().toString().concat(company.getUnitNo()));
                tvDetailedCoWorkingCompanyNumber.setText(tvDetailedCoWorkingCompanyNumber.getText().toString().concat(company.getOfficeTel()));
                tvDetailedCoWorkingCompanyIndustry.setText(tvDetailedCoWorkingCompanyIndustry.getText().toString().concat(company.getIndustry()));
                tvDetailedCoWorkingCompanyLack.setText(tvDetailedCoWorkingCompanyLack.getText().toString().concat(company.getCompanyLackOf()));
                tvDetailedCoWorkingCompanyNumOfCalls.setText(tvDetailedCoWorkingCompanyNumOfCalls.getText().toString().concat(String.valueOf(company.getNumberOfTimesCalled())));;
                tvDetailedCoWorkingCompanyPL.setText(tvDetailedCoWorkingCompanyPL.getText().toString().concat(company.getPriorityLevel()));
                tvDetailedCoWorkingCompanyComments.setText(tvDetailedCoWorkingCompanyComments.getText().toString().concat(company.getComment()));
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

    public void retrievingContacts(){

        databaseReference.child("Contacts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                if(contact.getCompany_id().equals(companyid)){
                    contactArrayList.add(contact);
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
        databaseReference.child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int contactCount = 1;

                for (int i = 0; i < contactArrayList.size(); i++) {

                    Contact contact = contactArrayList.get(i);


                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.rowcontact, null);

                    final TextView tvContactCount = addView.findViewById(R.id.tvContactCount);
                    tvContactCount.setText("Contact #" + contactCount);
                    contactCount ++;

                    //remove contact
                    TextView tvContactRemove = addView.findViewById(R.id.tvRemove);
                    tvContactRemove.setVisibility(View.GONE);

                    //NAME
                    TextView tvRowName = addView.findViewById(R.id.tvContactName);
                    tvRowName.setText(tvRowName.getText().toString().concat(contact.getName()));
                    EditText etRowName = addView.findViewById(R.id.etContactName);
                    etRowName.setText(contact.getName());
                    etRowName.setVisibility(View.GONE);

                    //TITLE
                    TextView tvRowTitle = addView.findViewById(R.id.tvContactTitle);
                    tvRowTitle.setText(tvRowTitle.getText().toString().concat(contact.getTitle()));
                    EditText etRowTitle = addView.findViewById(R.id.etContactTitle);
                    etRowTitle.setText(contact.getTitle());
                    etRowTitle.setVisibility(View.GONE);

                    //MOBILE
                    TextView tvRowMobile = addView.findViewById(R.id.tvContactMobile);
                    tvRowMobile.setText(tvRowMobile.getText().toString().concat(contact.getMobileNumber()));
                    EditText etRowMobile = addView.findViewById(R.id.etContactMobile);
                    etRowMobile.setText(contact.getMobileNumber());
                    etRowMobile.setVisibility(View.GONE);

                    //OFFICE
                    TextView tvRowOffice = addView.findViewById(R.id.tvContactOffice);
                    tvRowOffice.setText(tvRowOffice.getText().toString().concat(contact.getOfficeNumber()));
                    EditText etRowOffice = addView.findViewById(R.id.etContactOffice);
                    etRowOffice.setText(contact.getOfficeNumber());
                    etRowOffice.setVisibility(View.GONE);

                    //EMAIL
                    TextView tvRowEmail = addView.findViewById(R.id.tvContactEmail);
                    tvRowEmail.setText(tvRowEmail.getText().toString().concat(contact.getEmail()));
                    EditText etRowEmail= addView.findViewById(R.id.etContactEmail);
                    etRowEmail.setText(contact.getEmail());
                    etRowEmail.setVisibility(View.GONE);

                    //IC
                    TextView tvRowIC = addView.findViewById(R.id.tvContactIC);
                    tvRowIC.setText(tvRowIC.getText().toString().concat(String.valueOf(contact.getIc())));
                    Switch icSwitch2 = addView.findViewById(R.id.switchContactIC);
                    icSwitch2.setVisibility(View.GONE);

                    containerContacts.addView(addView, containerContacts.getChildCount());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void retrievingCopiers(){

        databaseReference.child("Copier").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Copier copier = dataSnapshot.getValue(Copier.class);
                if(copier.getCompany_id().equals(companyid)){
                    copierArrayList.add(copier);
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
        databaseReference.child("Copier").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int copierCount = 1;

                for (int i = 0; i < copierArrayList.size(); i++) {
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.rowcopier, null);

                    Copier copier = copierArrayList.get(i);

                    final TextView tvCopierCount = addView.findViewById(R.id.tvCopierCount);
                    tvCopierCount.setText("Copier #" + copierCount);
                    copierCount++;

                    //remove contact
                    TextView tvCopierRemove = addView.findViewById(R.id.tvRemove);
                    tvCopierRemove.setVisibility(View.GONE);

                    //Brand
                    TextView tvRowBrand = addView.findViewById(R.id.tvCopierBrand);
                    tvRowBrand.setText(tvRowBrand.getText().toString().concat(copier.getBrand()));
                    Spinner spinnerBrand = addView.findViewById(R.id.spinnerCopierBrand);
                    spinnerBrand.setVisibility(View.GONE);

                    //Model
                    TextView tvRowModel = addView.findViewById(R.id.tvCopierModel);
                    tvRowModel.setText(tvRowModel.getText().toString().concat(copier.getModel()));
                    EditText etModel = addView.findViewById(R.id.etCopierModel);
                    etModel.setText(copier.getModel());
                    etModel.setVisibility(View.GONE);

                    //Age
                    TextView tvRowAge = addView.findViewById(R.id.tvCopierAge);
                    tvRowAge.setText(tvRowAge.getText().toString().concat(copier.getAge()));
                    EditText etAge = addView.findViewById(R.id.etCopierAge);
                    etAge.setText(copier.getAge());
                    etAge.setVisibility(View.GONE);


                    //Problem
                    TextView tvRowProblem = addView.findViewById(R.id.tvCopierProblem);
                    tvRowProblem.setText(tvRowProblem.getText().toString().concat(copier.getProblemfaced()));
                    CheckBox cbPrint = addView.findViewById(R.id.cbPrint);
                    CheckBox cbScan = addView.findViewById(R.id.cbScan);
                    CheckBox cbPaperJam = addView.findViewById(R.id.cbPaperJam);

                    cbPrint.setVisibility(View.GONE);
                    cbScan.setVisibility(View.GONE);
                    cbPaperJam.setVisibility(View.GONE);

                    //RorP
                    TextView tvRorP = addView.findViewById(R.id.tvCopierRorP);
                    RadioButton rbRented = addView.findViewById(R.id.rbRented);
                    RadioButton rbPurchased = addView.findViewById(R.id.rbPurchased);
                    RadioButton rbUnsure = addView.findViewById(R.id.rbUnsure);
                    tvRorP.setText(tvRorP.getText().toString().concat(copier.getRentedOrPurchased()));

                    rbRented.setVisibility(View.GONE);
                    rbPurchased.setVisibility(View.GONE);
                    rbUnsure.setVisibility(View.GONE);

                    //contractLength
                    TextView tvRowcontLength = addView.findViewById(R.id.tvCopierContLength);
                    tvRowcontLength.setText(tvRowcontLength.getText().toString().concat(String.valueOf(copier.getContractLength())));
                    EditText etContLength = addView.findViewById(R.id.etCopierContLength);
                    etContLength.setText(String.valueOf(copier.getContractLength()));
                    etContLength.setVisibility(View.GONE);

                    //contractStartDate
                    TextView tvRowStartDate = addView.findViewById(R.id.tvCopierContStartDate);
                    tvRowStartDate.setText(tvRowStartDate.getText().toString().concat(copier.getContractStartDate()));
                    EditText etContStartDate = addView.findViewById(R.id.etCopierContStartDate);
                    etContStartDate.setText(copier.getContractStartDate());
                    etContStartDate.setVisibility(View.GONE);

                    //contractExpiryDate
                    TextView tvRowExpiryDate = addView.findViewById(R.id.tvCopierContExpiryDate);
                    tvRowExpiryDate.setText(tvRowExpiryDate.getText().toString().concat(copier.getContractExpiryDate()));
                    EditText etContExpiryDate = addView.findViewById(R.id.etCopierContExpiryDate);
                    etContExpiryDate.setText(copier.getContractExpiryDate());
                    etContExpiryDate.setVisibility(View.GONE);

                    //contractMonthlyPayment
                    TextView tvRowContMP = addView.findViewById(R.id.tvCopierContMonthlyPayment);
                    tvRowContMP.setText(tvRowContMP.getText().toString().concat(copier.getContractMonthlyPayment()));
                    EditText etContMP = addView.findViewById(R.id.etCopierContMonthlyPayment);
                    etContMP.setText(copier.getContractMonthlyPayment());
                    etContMP.setVisibility(View.GONE);

                    //contractFinalPayment
                    TextView tvRowContFP = addView.findViewById(R.id.tvCopierContFinalPayment);
                    tvRowContFP.setText(tvRowContFP.getText().toString().concat(copier.getContractFinalPayment()));
                    EditText etContFP = addView.findViewById(R.id.etCopierContFinalPayment);
                    etContFP.setText(copier.getContractFinalPayment());
                    etContFP.setVisibility(View.GONE);


                    containerCopier.addView(addView, containerCopier.getChildCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void retrievingAppointments(){

        databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                if(appointment.getCompany_id().equals(companyid)){
                    appointmentArrayList.add(appointment);
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
        databaseReference.child("Appointment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int apptCount = 1;

                for (int i = 0; i < appointmentArrayList.size(); i++) {

                    Appointment appointment = appointmentArrayList.get(i);

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.rowappointment, null);


                    final TextView tvApptCount = addView.findViewById(R.id.tvApptCount);
                    tvApptCount.setText(tvApptCount.getText().toString().concat(String.valueOf(apptCount)));
                    apptCount++;

                    //date
                    TextView tvApptDate = addView.findViewById(R.id.tvApptDate);
                    tvApptDate.setText(tvApptDate.getText().toString().concat(" " + appointment.getDate()));

                    //time
                    TextView tvApptTime = addView.findViewById(R.id.tvApptTime);
                    tvApptTime.setText(tvApptTime.getText().toString().concat(" " + appointment.getTime()));

                    //contact
                    final TextView tvApptContact = addView.findViewById(R.id.tvApptContact);

                    databaseReference.child("User").orderByKey().equalTo(appointment.getContact_id()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Contact contact = dataSnapshot.getValue(Contact.class);
                            tvApptContact.setText(tvApptContact.getText().toString().concat(" " + contact.getName()));

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

                    //locatonName
                    TextView tvApptLocation = addView.findViewById(R.id.tvApptLocation);
                    tvApptLocation.setText(tvApptLocation.getText().toString().concat(" " + appointment.getLocationName()));
                    if(appointment.getLocationName().equals("")){
                        tvApptLocation.setVisibility(View.GONE);
                    }

                    TextView tvApptLocationAddress = addView.findViewById(R.id.tvApptLocationAddress);
                    tvApptLocationAddress.setText(tvApptLocationAddress.getText().toString().concat(" " + appointment.getLocationAddress()));

                    containertAppt.addView(addView, containertAppt.getChildCount());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void retrievingFollowUps(){

        databaseReference.child("FollowUp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FollowUp followUp = dataSnapshot.getValue(FollowUp.class);
                if(followUp.getCompanyid().equals(companyid)){
                    followUpArrayList.add(followUp);
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
        databaseReference.child("FollowUp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int fuCount = 1;

                for (int i = 0; i < followUpArrayList.size(); i++) {

                    FollowUp followUp = followUpArrayList.get(i);

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.rowfollowup, null);


                    final TextView tvFUCount = addView.findViewById(R.id.tvFollowUpCount);
                    tvFUCount.setText(tvFUCount.getText().toString().concat(String.valueOf(fuCount)));
                    fuCount++;

                    //due date
                    TextView tvFUDueDate = addView.findViewById(R.id.tvFUDate);
                    tvFUDueDate.setText(tvFUDueDate.getText().toString().concat(" " + followUp.getFollowupDueDate()));

                    //type
                    TextView tvFUType = addView.findViewById(R.id.tvFUtype);
                    tvFUType.setText(tvFUType.getText().toString().concat(" " + followUp.getTypeOfFollowup()));

                    //status
                    TextView tvFUStatus = addView.findViewById(R.id.tvFUStatus);
                    tvFUStatus.setText(tvFUStatus.getText().toString().concat(" " + followUp.getFollowUpStatus()));

                    containerFollowUp.addView(addView, containerFollowUp.getChildCount());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrievingLog(){

        databaseReference.child("SharedCoWorkingDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SharedCoWorkingCompany sharedCoWorkingCompany = dataSnapshot.getValue(SharedCoWorkingCompany.class);
                if(sharedCoWorkingCompany.getCompanyid().equals(companyid) && sharedCoWorkingCompany.getStatus().equals("ongoing")){
                    dbSharedCoWorkingKey = dataSnapshot.getKey();
                    if(sharedCoWorkingCompany.getLog()!=null) {
                        log = sharedCoWorkingCompany.getLog();
                        for (int i = 0; i < log.size(); i++) {
                            String currentlog = log.get(i);
                            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View addView = layoutInflater.inflate(R.layout.rowlog, null);
                            final TextView tvLog = addView.findViewById(R.id.tvLog);
                            tvLog.setText(currentlog);
                            containerLog.addView(addView, containerLog.getChildCount());
                        }
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


    public String getLatLng(String zip){

        String finaladdress = "";

        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                getAddress(address.getLatitude(), address.getLongitude());

                List<Address> address2 = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1);
                finaladdress = address2.get(0).getAddressLine(0);

            }
        } catch (IOException e) {
            // handle exception
        }
        return finaladdress;
    }
    public void getAddress(double lat, double lng){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void getAndSetName(){

        databaseReference.child("SharedCoWorkingDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SharedCoWorkingCompany sharedCoWorkingCompany = dataSnapshot.getValue(SharedCoWorkingCompany.class);
                if(sharedCoWorkingCompany.getCompanyid().equals(companyid) && sharedCoWorkingCompany.getStatus().equals("ongoing")){

                    if(!sharedCoWorkingCompany.getCreatedby().equals(uid)){
                        btnTerminateSharing.setVisibility(View.GONE);
                    }
                    //set sharer
                    databaseReference.child("User").orderByKey().equalTo(sharedCoWorkingCompany.getCreatedby()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            User user = dataSnapshot.getValue(User.class);
                            tvDetailedCoWorkingCompanyIShare.setText(user.getName());
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
                    //set sharedto
                    databaseReference.child("User").orderByKey().equalTo(sharedCoWorkingCompany.getSharedto()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            User user = dataSnapshot.getValue(User.class);
                            tvDetailedCoWorkingCompanySharedWith.setText(user.getName());
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

    public String getName(){
        String[] split = user.getEmail().split("@");
        return split[0];
    }

    public void readLogs(ArrayList<String> arrayList){

        containerLog.removeAllViews();

        for(int i=0; i<log.size(); i++) {
            String currentlog = arrayList.get(i);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.rowlog, null);
            final TextView tvLog = addView.findViewById(R.id.tvLog);
            tvLog.setText(currentlog);
            containerLog.addView(addView, containerLog.getChildCount());
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnSubmitLog)){
            String newlog = getName().concat(": ") + etLog.getText().toString();
            log.add(newlog);
            etLog.setText("");

            databaseReference.child("SharedCoWorkingDetails").child(dbSharedCoWorkingKey).child("log").setValue(log);
            readLogs(log);

        }
        else if(view.equals(btnTerminateSharing)){
            databaseReference.child("SharedCoWorkingDetails").child(dbSharedCoWorkingKey).child("status").setValue("terminated");
        }
    }

    public void retrieveStoragePath(){
        databaseReference.child("Photo").child(companyid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                System.out.println("exist storage count: " + photo.getCount());
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < Integer.parseInt(photo.getCount()); i++) {
                    arrayList.add(String.valueOf(i));
                }
                getPhotos(arrayList);
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

    private void getPhotos(ArrayList<String> list){

        PhotoAdapter photoAdapter = new PhotoAdapter(list,ViewDetailedSharedCoWorkingCompanyActivity.this);
        recyclerView.setAdapter(photoAdapter);
    }
}
