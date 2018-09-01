package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewMyNameCardDetailedActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView tvCompanyName, tvCompanyAddress, tvCompanyUnitNumber, tvCompanyPostalCode,
            tvCompanyNumber, tvCompanyIndustry, tvCompanyLack, tvCompanyNumOfCalls, tvCompanyPl, tvCompanyComments, tvAddContact, tvAddCopier;

    private EditText etCompanyName, etCompanyAddress, etCompanyNumber, etCompanyNumOfCalls,
            etCompanyComments, etCompanyPostalCode, etCompanyUnitNo;

    private Spinner spinnerIndustry;
    private CheckBox checkBoxPrinter, checkBoxScanner, checkBoxShredder;
    private RadioGroup rgPriorityLevel;
    private RadioButton rbNormal, rbFollowUp, rbUrgent;
    private LinearLayout containerContact;
    private LinearLayout containerCopier;
    private Button btnEditandSave;
    private Button btnAddAppt, btnAddFollowUp;

    private Company company;
    private Contact contact;
    private Copier copier;
    private String postalcodekey, namekey, companyDbKey;
    private ArrayList<String> contactKeyArrayList;
    private ArrayList<String> copierKeyArrayList;
    private int contactCount = 1;
    private int copierCount = 1;

    //for company vals
    String companyDateCreated;

    //for contact vals
    String contactName, contactTitle, contactMobile, contactOffice, contactEmail;
    Boolean contactIC;

    //for copier vals
    String copierBrand, copierModel, copierAge, copierProblems, copierROP, copierContLength, copierStartDate, copierEndDate, copierMP, copierFP;

    private Calendar calendar = Calendar.getInstance();

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerContacts;
    private ChildEventListener childEventListenerCopier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_name_card_detailed);
        init();

        namekey = getIntent().getExtras().getString("name");
        postalcodekey = getIntent().getExtras().getString("postalcode");

        if(namekey!=null && postalcodekey!=null){
            seekCompanyDetails();
        }
    }

    public void init(){
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanyAddress = findViewById(R.id.tvCompanyAddress);
        tvCompanyUnitNumber = findViewById(R.id.tvCompanyUnitNo);
        tvCompanyPostalCode = findViewById(R.id.tvCompanyPostalCode);
        tvCompanyNumber = findViewById(R.id.tvCompanyOfficeNumber);
        tvCompanyIndustry = findViewById(R.id.tvCompanyIndustry);
        tvCompanyLack = findViewById(R.id.tvCompanyLack);
        tvCompanyNumOfCalls = findViewById(R.id.tvCompanyNumOfCalls);
        tvCompanyPl = findViewById(R.id.tvCompanyPriorityLevel);
        tvCompanyComments = findViewById(R.id.tvCompanyComment);
        tvAddContact = findViewById(R.id.tvAddContact);
        tvAddContact.setOnClickListener(this);

        tvAddCopier = findViewById(R.id.tvAddCopier);
        tvAddCopier.setOnClickListener(this);

        containerContact = findViewById(R.id.containerContact);
        containerCopier = findViewById(R.id.containerCopier);

        etCompanyName = findViewById(R.id.etCompanyName);
        etCompanyAddress = findViewById(R.id.etCompanyAddress);
        etCompanyPostalCode = findViewById(R.id.etCompanyPostalCode);
        etCompanyUnitNo = findViewById(R.id.etCompanyUnit);
        etCompanyPostalCode.addTextChangedListener(this);
        etCompanyNumber = findViewById(R.id.etCompanyOfficeNumber);
        spinnerIndustry = findViewById(R.id.spinnerIndustry);
        checkBoxPrinter = findViewById(R.id.cbCopier);
        checkBoxScanner = findViewById(R.id.cbScanner);
        checkBoxShredder = findViewById(R.id.cbShredder);
        etCompanyNumOfCalls = findViewById(R.id.etCompanyNumOfCalls);
        rgPriorityLevel = findViewById(R.id.rgPriorityLevel);
        rbUrgent = findViewById(R.id.rbUrgent);
        rbFollowUp = findViewById(R.id.rbFollowUp);
        rbNormal = findViewById(R.id.rbNormal);
        etCompanyComments = findViewById(R.id.etCompanyComment);
        btnEditandSave = findViewById(R.id.btnEditandSave);
        btnEditandSave.setOnClickListener(this);
        btnAddAppt = findViewById(R.id.btnAddAppointment);
        btnAddFollowUp = findViewById(R.id.btnAddFollowUp);
        btnAddAppt.setOnClickListener(this);
        btnAddFollowUp.setOnClickListener(this);

        etCompanyName.setVisibility(View.GONE);
        etCompanyAddress.setVisibility(View.GONE);
        etCompanyPostalCode.setVisibility(View.GONE);
        etCompanyUnitNo.setVisibility(View.GONE);
        etCompanyNumber.setVisibility(View.GONE);
        etCompanyNumOfCalls.setVisibility(View.GONE);
        etCompanyComments.setVisibility(View.GONE);
        spinnerIndustry.setVisibility(View.GONE);
        checkBoxPrinter.setVisibility(View.GONE);
        checkBoxScanner.setVisibility(View.GONE);
        checkBoxShredder.setVisibility(View.GONE);
        rgPriorityLevel.setVisibility(View.GONE);
        rbUrgent.setVisibility(View.GONE);
        rbFollowUp.setVisibility(View.GONE);
        rbNormal.setVisibility(View.GONE);


        contactKeyArrayList = new ArrayList<>();
        copierKeyArrayList = new ArrayList<>();
    }

    public void seekCompanyDetails(){

        databaseReference.child("Company").orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                company = dataSnapshot.getValue(Company.class);
                if(company!=null) {
                    if (company.getPostalCode().equals(postalcodekey)) {
                        inputCompanyDetails(company);
                        companyDbKey = dataSnapshot.getKey();
                        seekContactsFromCompany(companyDbKey);
                        seekCopierFromCompany(companyDbKey);
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

    public void inputCompanyDetails(Company company){

        tvCompanyName.setText(tvCompanyName.getText().toString().concat(company.getName()));
        tvCompanyAddress.setText(tvCompanyAddress.getText().toString().concat(getLatLng(company.getPostalCode().trim())));
        tvCompanyUnitNumber.setText(tvCompanyUnitNumber.getText().toString().concat(company.getUnitNo()));
        tvCompanyPostalCode.setText(tvCompanyPostalCode.getText().toString().concat(company.getPostalCode().trim()));
        tvCompanyNumber.setText(tvCompanyNumber.getText().toString().concat(company.getOfficeTel()));
        tvCompanyIndustry.setText(tvCompanyIndustry.getText().toString().concat(company.getIndustry()));
        tvCompanyLack.setText(tvCompanyLack.getText().toString().concat(company.getCompanyLackOf()));
        tvCompanyNumOfCalls.setText(tvCompanyNumOfCalls.getText().toString().concat(String.valueOf(company.getNumberOfTimesCalled())));
        tvCompanyPl.setText(tvCompanyPl.getText().toString().concat(company.getPriorityLevel()));
        tvCompanyComments.setText(tvCompanyComments.getText().toString().concat(company.getComment()));

        etCompanyName.setText(company.getName());
        etCompanyUnitNo.setText(company.getUnitNo());
        etCompanyAddress.setText(getLatLng(company.getPostalCode()));
        etCompanyPostalCode.setText(company.getPostalCode());
        etCompanyNumber.setText(company.getOfficeTel());
        etCompanyNumOfCalls.setText(String.valueOf(company.getNumberOfTimesCalled()));
        etCompanyComments.setText(company.getComment());

        //spinner selection
        spinnerSetter(company.getIndustry());

        //checkbox selection
        checkBoxCompanySetter(company.getCompanyLackOf());

        //radiobutton slection
        radioCompanySetter(company.getPriorityLevel());

        rgPriorityLevel.setVisibility(View.GONE);
        rbUrgent.setVisibility(View.GONE);
        rbFollowUp.setVisibility(View.GONE);
        rbNormal.setVisibility(View.GONE);

        companyDateCreated = company.getDateCreated();

    }

    public void seekContactsFromCompany(String key){

        childEventListenerContacts = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                contact = dataSnapshot.getValue(Contact.class);
                if(contact!=null){

                    System.out.println("child addded");

                    contactKeyArrayList.add(dataSnapshot.getKey());

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.rowcontact, null);

                    final TextView tvContactCount = addView.findViewById(R.id.tvContactCount);
                    tvContactCount.setText("Contact #" + contactCount);
                    contactCount ++;

                    //remove contact
                    TextView tvContactRemove = addView.findViewById(R.id.tvRemove);
                    tvContactRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            TextView tvCurrentCount = addView.findViewById(R.id.tvContactCount);
                            if(!tvCurrentCount.getText().toString().equals(String.valueOf(contactCount))){
                                ((LinearLayout)addView.getParent()).removeView(addView);
                                contactCount --;
                                rearrangeContactCount();
                            }
                            else{
                                ((LinearLayout)addView.getParent()).removeView(addView);
                                contactCount --;
                            }

                            String contactcounter  = tvContactCount.getText().toString();
                            String[] split = contactcounter.split("#");
                            int contactCount = Integer.parseInt(split[1].trim()) - 1;

                            if(contactKeyArrayList.size() >= contactCount){
                                String key = contactKeyArrayList.get(contactCount);
                                databaseReference.child("Contact").child(key).removeValue();
                                contactKeyArrayList.remove(contactCount);
                            }

                        }
                    });

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
                    Switch icSwitch2 = addView.findViewById(R.id.switchContactIC);
                    icSwitch2.setVisibility(View.GONE);
                    icSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            ICswitchChecker();
                        }
                    });
                    if(contact.getIc().equals(true)){
                        tvRowIC.setText(tvRowIC.getText().toString().concat("yes"));
                        icSwitch2.isChecked();
                    }
                    else{
                        tvRowIC.setText(tvRowIC.getText().toString().concat("no"));
                        icSwitch2.setChecked(false);
                    }

                    containerContact.addView(addView, containerContact.getChildCount());
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
        databaseReference.child("Contact").orderByChild("company_id").equalTo(key).addChildEventListener(childEventListenerContacts);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("Contact").removeEventListener(childEventListenerContacts);
                System.out.println("child event removed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void seekCopierFromCompany(String key){

        childEventListenerCopier = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                copier = dataSnapshot.getValue(Copier.class);
                if(copier!=null){

                    System.out.println("child addded");

                    copierKeyArrayList.add(dataSnapshot.getKey());

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.rowcopier, null);

                    final TextView tvCopierCount = addView.findViewById(R.id.tvCopierCount);
                    tvCopierCount.setText("Copier #" + copierCount);
                    copierCount ++;

                    //remove contact
                    TextView tvCopierRemove = addView.findViewById(R.id.tvRemove);
                    tvCopierRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            TextView tvCurrentCount = addView.findViewById(R.id.tvCopierCount);
                            if(!tvCurrentCount.getText().toString().equals(String.valueOf(copierCount))){
                                ((LinearLayout)addView.getParent()).removeView(addView);
                                copierCount --;
                                rearrangeCopierCount();
                            }
                            else{
                                ((LinearLayout)addView.getParent()).removeView(addView);
                                copierCount --;
                            }

                            String copiercounter  = tvCopierCount.getText().toString();
                            String[] split = copiercounter.split("#");
                            int copierCount = Integer.parseInt(split[1].trim()) - 1;

                            if(copierKeyArrayList.size() >= copierCount){
                                String key = copierKeyArrayList.get(copierCount);
                                databaseReference.child("Copier").child(key).removeValue();
                                copierKeyArrayList.remove(copierCount);
                            }

                        }
                    });

                    //Brand
                    TextView tvRowBrand = addView.findViewById(R.id.tvCopierBrand);

                    System.out.println("brand: " + copier.getBrand());
                    System.out.println("tvBrand: " + tvRowBrand.getText().toString());

                    String copierBrand = copier.getBrand();
                    tvRowBrand.setText(tvRowBrand.getText().toString().concat(copier.getBrand()));
                    Spinner spinnerBrand = addView.findViewById(R.id.spinnerCopierBrand);

                    List<String> lines = Arrays.asList(getResources().getStringArray(R.array.copierBrand));
                    for(int i=0; i < lines.size(); i++){
                        if(lines.get(i).equals(copierBrand)){
                            spinnerBrand.setSelection(i);
                        }
                    }
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

                    String[] split = copier.getProblemfaced().split(" ");
                    for(int i=0; i < split.length; i++){
                        if(split[i].equals("Print")){
                            cbPrint.setChecked(true);
                        }
                        else if(split[i].equals("Scan")){
                            cbScan.setChecked(true);
                        }
                        else if(split[i].equals("PaperJam")){
                            cbPaperJam.setChecked(true);
                        }
                    }
                    cbPrint.setVisibility(View.GONE);
                    cbScan.setVisibility(View.GONE);
                    cbPaperJam.setVisibility(View.GONE);

                    //RorP
                    TextView tvRorP = addView.findViewById(R.id.tvCopierRorP);
                    RadioButton rbRented = addView.findViewById(R.id.rbRented);
                    RadioButton rbPurchased = addView.findViewById(R.id.rbPurchased);
                    RadioButton rbUnsure = addView.findViewById(R.id.rbUnsure);
                    tvRorP.setText(tvRorP.getText().toString().concat(copier.getRentedOrPurchased()));

                    if(copier.getRentedOrPurchased().equals("Rented")){
                        rbRented.setChecked(true);
                    }
                    else if(copier.getRentedOrPurchased().equals("Purchased")){
                        rbPurchased.setChecked(true);
                    }
                    else if(copier.getRentedOrPurchased().equals("Unsure")){
                        rbUnsure.setChecked(true);
                    }
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
        databaseReference.child("Copier").orderByChild("company_id").equalTo(key).addChildEventListener(childEventListenerCopier);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("Copier").removeEventListener(childEventListenerCopier);
                System.out.println("child event removed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void rearrangeContactCount(){

        int childCount = containerContact.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = containerContact.getChildAt(c);
            TextView tvContactCount = (childView.findViewById(R.id.tvContactCount));
            tvContactCount.setText("Contact #" + (c + 1));

        }
    }

    public void rearrangeCopierCount(){

        int childCount = containerCopier.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = containerCopier.getChildAt(c);
            TextView tvCopierCount = (childView.findViewById(R.id.tvCopierCount));
            tvCopierCount.setText("Copier #" + (c + 1));

        }
    }

    public void ICswitchChecker(){

        int childCount = containerContact.getChildCount();
        int icChecker = 0;

        for(int c=0; c<childCount; c++){

            View childView = containerContact.getChildAt(c);
            Switch switchIC = (childView.findViewById(R.id.switchContactIC));

            if(switchIC.isChecked()){
                icChecker ++;
            }
            if(icChecker > 1){
                Toast.makeText(getApplicationContext(), "Warning, more than one IC found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void spinnerSetter(String key){

        switch (key) {
            case "IT":
                spinnerIndustry.setSelection(0);
                break;
            case "Agriculture":
                spinnerIndustry.setSelection(1);
                break;
            case "Health":
                spinnerIndustry.setSelection(2);
                break;
        }
    }

    public void checkBoxCompanySetter(String key){

        String[] split = key.split(" ");
        for(int i=0; i < split.length; i++){
            if(split[i].equals("Copier")){
                checkBoxPrinter.setChecked(true);
            }
            else if(split[i].equals("Scanner")){
                checkBoxScanner.setChecked(true);
            }
            else if(split[i].equals("Shredder")){
                checkBoxShredder.setChecked(true);
            }
        }
    }

    public void radioCompanySetter(String key){

        if(key.equals("a.Urgent")){
            rbUrgent.isChecked();
        }
        else if(key.equals("b.Follow Up")){
            rbFollowUp.isChecked();
        }
        else if(key.equals("c.Normal")){
            rbNormal.isChecked();
        }

    }

    @Override
    public void onClick(View view) {

        if(view == btnEditandSave){
            if(btnEditandSave.getText().toString().equals("EDIT")){
                setCompanyEditable();
                setContactEditable();
                setCopierEditable();
            }
            else if(btnEditandSave.getText().toString().equals("SAVE")){

                String industry = spinnerIndustry.getSelectedItem().toString();

                String lack = "";
                int[] checkboxes = {R.id.cbCopier, R.id.cbScanner, R.id.cbShredder};
                int length = checkboxes.length;
                for(int i = 0; i<length; i ++){
                    CheckBox cb = findViewById(checkboxes[i]);
                    if(cb.isChecked()){
                        lack = lack.concat(cb.getText().toString() + " ");
                    }
                }

                int rgCheckednumber = rgPriorityLevel.getCheckedRadioButtonId();
                View radioButton = rgPriorityLevel.findViewById(rgCheckednumber);
                int idx = rgPriorityLevel.indexOfChild(radioButton);
                RadioButton rbChecked = (RadioButton)  rgPriorityLevel.getChildAt(idx);
                String rbSelectedText = rbChecked.getText().toString();

                String name = etCompanyName.getText().toString().trim();
                String postalCode = etCompanyPostalCode.getText().toString().trim();
                String unitNo = etCompanyUnitNo.getText().toString().trim();
                String officeTel = etCompanyNumber.getText().toString().trim();
                String comment = etCompanyComments.getText().toString().trim();
                String createBy = uid;
                int numberOfTimesCalled = Integer.parseInt(etCompanyNumOfCalls.getText().toString().trim());

                Company company = new Company(name, postalCode, unitNo, officeTel, industry, lack, rbSelectedText, comment, createBy, companyDateCreated, numberOfTimesCalled);
                databaseReference.child("Company").child(companyDbKey).setValue(company);

                saveContact();
                saveCopier();
                setCompanyNotEditable();
                setContactNotEditable();
                setCopierNotEditable();

            }
        }
        else if(view.equals(tvAddContact)){

            setCompanyEditable();
            setContactEditable();
            setCopierEditable();

            System.out.println("tvAddContact");
            //perform rowcontact insert
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.rowcontact, null);

            TextView tvContactCount = addView.findViewById(R.id.tvContactCount);
            tvContactCount.setText("Contact #" + contactCount);
            contactCount ++;

            //icSwitchChecker
            Switch icSwitch = addView.findViewById(R.id.switchContactIC);
            icSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ICswitchChecker();
                }
            });

            //remove contact
            TextView tvContactRemove = addView.findViewById(R.id.tvRemove);
            tvContactRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    TextView tvCurrentCount = addView.findViewById(R.id.tvContactCount);
                    if(!tvCurrentCount.getText().toString().equals(String.valueOf(contactCount))){
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        contactCount --;
                        rearrangeContactCount();
                    }
                    else{
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        contactCount --;
                    }

                }
            });

            containerContact.addView(addView, containerContact.getChildCount());
        }
        else if(view.equals(tvAddCopier)){

            setCompanyEditable();
            setContactEditable();
            setCopierEditable();

            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.rowcopier, null);

            TextView tvCopierCount = addView.findViewById(R.id.tvCopierCount);
            tvCopierCount.setText("Copier #" + copierCount);
            copierCount ++;

            //remove copier
            TextView tvCopierRemove = addView.findViewById(R.id.tvRemove);
            tvCopierRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView tvCurrentCount = addView.findViewById(R.id.tvCopierCount);
                    if(!tvCurrentCount.getText().toString().equals(String.valueOf(copierCount))){
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        copierCount --;
                        rearrangeCopierCount();
                    }
                    else{
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        copierCount --;
                    }

                }
            });

            //get Start Date
            final EditText etStartDateContract = addView.findViewById(R.id.etCopierContStartDate);
            etStartDateContract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.equals(etStartDateContract)) {
                        getDate(etStartDateContract);
                    }
                }
            });
            etStartDateContract.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b) {
                        getDate(etStartDateContract);
                    }
                }
            });

            //get End Date
            final EditText etEndDateContract = addView.findViewById(R.id.etCopierContExpiryDate);
            etEndDateContract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.equals(etEndDateContract)) {
                        getDate(etEndDateContract);
                    }
                }
            });
            etEndDateContract.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b) {
                        getDate(etEndDateContract);
                    }
                }
            });

            //calculate expiry date
            final EditText etContLength = addView.findViewById(R.id.etCopierContLength);
            etContLength.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!etStartDateContract.getText().toString().equals("")){

                        //
                        final Handler handler = new Handler();
                        new Thread(new Runnable() {
                            public void run() {
                                try{
                                    Thread.sleep(300);
                                }
                                catch (Exception e) { } // Just catch the InterruptedException

                                // Now we use the Handler to post back to the main thread
                                handler.post(new Runnable() {
                                    public void run() {
                                        // Set the View's visibility back on the main UI Thread

                                        if(!etContLength.getText().equals("")) {
                                            String date = calculateExpiryDate(etStartDateContract.getText().toString(), Integer.parseInt(etContLength.getText().toString()));
                                            etEndDateContract.setText(date);
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                }
            });

            containerCopier.addView(addView, containerCopier.getChildCount());
        }
        else if(view.equals(btnAddAppt)){

            Intent intent = new Intent(this, AddAppointmentActivity.class);
            Bundle extras = new Bundle();
            extras.putString("companydbkey", companyDbKey);
            extras.putString("companyName", company.getName());
            extras.putString("companyAddress", etCompanyAddress.getText().toString());
            intent.putExtras(extras);
            this.startActivity(intent);
        }
        else if(view.equals(btnAddFollowUp)){

            Intent intent = new Intent(this, AddFollowUpActivity.class);
            Bundle extras = new Bundle();
            extras.putString("companydbkey", companyDbKey);
            extras.putString("companyName", company.getName());
            intent.putExtras(extras);
            this.startActivity(intent);
        }
    }

    public void setCompanyEditable(){

        tvCompanyName.setText("Company Name: ");
        tvCompanyAddress.setText("Company Address: ");
        tvCompanyPostalCode.setText("Company Postal Code: ");
        tvCompanyUnitNumber.setText("Company Unit Number: ");
        tvCompanyNumber .setText("Company Number: ");
        tvCompanyIndustry.setText("Company Industry: ");
        tvCompanyLack.setText("Company Lacking of: ");
        tvCompanyNumOfCalls.setText("Number Of Times Called: ");
        tvCompanyPl.setText("Company Priority Level: ");
        tvCompanyComments.setText("Company Comment: ");

        etCompanyName.setVisibility(View.VISIBLE);
        etCompanyAddress.setVisibility(View.VISIBLE);
        etCompanyPostalCode.setVisibility(View.VISIBLE);
        etCompanyUnitNo.setVisibility(View.VISIBLE);
        etCompanyNumber.setVisibility(View.VISIBLE);
        etCompanyNumOfCalls.setVisibility(View.VISIBLE);
        etCompanyComments.setVisibility(View.VISIBLE);
        spinnerIndustry.setVisibility(View.VISIBLE);
        checkBoxPrinter.setVisibility(View.VISIBLE);
        checkBoxScanner.setVisibility(View.VISIBLE);
        checkBoxShredder.setVisibility(View.VISIBLE);
        rgPriorityLevel.setVisibility(View.VISIBLE);
        rbUrgent.setVisibility(View.VISIBLE);
        rbFollowUp.setVisibility(View.VISIBLE);
        rbNormal.setVisibility(View.VISIBLE);

        btnEditandSave.setText("SAVE");
    }
    public void setCompanyNotEditable(){

        String lack = "";
        int[] checkboxes = {R.id.cbCopier, R.id.cbScanner, R.id.cbShredder};
        int length = checkboxes.length;
        for(int i = 0; i<length; i ++){
            CheckBox cb = findViewById(checkboxes[i]);
            if(cb.isChecked()){
                lack = lack.concat(cb.getText().toString() + " ");
            }
        }

        int rgCheckednumber = rgPriorityLevel.getCheckedRadioButtonId();
        View radioButton = rgPriorityLevel.findViewById(rgCheckednumber);
        int idx = rgPriorityLevel.indexOfChild(radioButton);
        RadioButton rbChecked = (RadioButton)  rgPriorityLevel.getChildAt(idx);
        String rbSelectedText = rbChecked.getText().toString();

        tvCompanyName.setText("Company Name: " + etCompanyName.getText().toString());
        tvCompanyAddress.setText("Company Address: "+ etCompanyAddress.getText().toString());
        tvCompanyPostalCode.setText("Company Postal Code: "+ etCompanyPostalCode.getText().toString());
        tvCompanyUnitNumber.setText("Company Unit Number: "+ etCompanyUnitNo.getText().toString());
        tvCompanyNumber .setText("Company Number: "+ etCompanyNumber.getText().toString());
        tvCompanyIndustry.setText("Company Industry: "+ spinnerIndustry.getSelectedItem().toString());
        tvCompanyLack.setText("Company Lacking of: "+ lack);
        tvCompanyNumOfCalls.setText("Number Of Times Called: "+ etCompanyNumOfCalls.getText().toString());
        tvCompanyPl.setText("Company Priority Level: "+ rbSelectedText);
        tvCompanyComments.setText("Company Comment: "+ etCompanyComments.getText().toString());

        etCompanyName.setVisibility(View.GONE);
        etCompanyAddress.setVisibility(View.GONE);
        etCompanyPostalCode.setVisibility(View.GONE);
        etCompanyUnitNo.setVisibility(View.GONE);
        etCompanyNumber.setVisibility(View.GONE);
        etCompanyNumOfCalls.setVisibility(View.GONE);
        etCompanyComments.setVisibility(View.GONE);
        spinnerIndustry.setVisibility(View.GONE);
        checkBoxPrinter.setVisibility(View.GONE);
        checkBoxScanner.setVisibility(View.GONE);
        checkBoxShredder.setVisibility(View.GONE);
        rgPriorityLevel.setVisibility(View.GONE);
        rbUrgent.setVisibility(View.GONE);
        rbFollowUp.setVisibility(View.GONE);
        rbNormal.setVisibility(View.GONE);

        btnEditandSave.setText("EDIT");
    }

    public void setContactEditable(){

        if(containerContact.getChildCount() > 0){

            int childCount = containerContact.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = containerContact.getChildAt(c);

                TextView tvRowName = (childView.findViewById(R.id.tvContactName));
                TextView tvRowTitle = (childView.findViewById(R.id.tvContactTitle));
                TextView tvRowMobile = (childView.findViewById(R.id.tvContactMobile));
                TextView tvRowOffice = (childView.findViewById(R.id.tvContactOffice));
                TextView tvRowEmail = (childView.findViewById(R.id.tvContactEmail));
                TextView tvRowIC = (childView.findViewById(R.id.tvContactIC));

                tvRowName.setText("Contact Name: ");
                tvRowTitle.setText("Contact Title: ");
                tvRowMobile.setText("Contact Mobile: ");
                tvRowOffice.setText("Contact Office: ");
                tvRowEmail.setText("Contact Email: ");
                tvRowIC.setText("Contact IC: ");

                EditText etContactName = (childView.findViewById(R.id.etContactName));
                EditText etTitle = (childView.findViewById(R.id.etContactTitle));
                EditText etMobile = (childView.findViewById(R.id.etContactMobile));
                EditText etOffice = (childView.findViewById(R.id.etContactOffice));
                EditText etEmail = (childView.findViewById(R.id.etContactEmail));
                Switch switchIC = (childView.findViewById(R.id.switchContactIC));

                etContactName.setVisibility(View.VISIBLE);
                etTitle.setVisibility(View.VISIBLE);
                etMobile.setVisibility(View.VISIBLE);
                etOffice.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                switchIC.setVisibility(View.VISIBLE);
            }
        }
    }
    public void setContactNotEditable(){

        if(containerContact.getChildCount() > 0){

            int childCount = containerContact.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = containerContact.getChildAt(c);

                TextView tvRowName = (childView.findViewById(R.id.tvContactName));
                TextView tvRowTitle = (childView.findViewById(R.id.tvContactTitle));
                TextView tvRowMobile = (childView.findViewById(R.id.tvContactMobile));
                TextView tvRowOffice = (childView.findViewById(R.id.tvContactOffice));
                TextView tvRowEmail = (childView.findViewById(R.id.tvContactEmail));
                TextView tvRowIC = (childView.findViewById(R.id.tvContactIC));

                EditText etContactName = (childView.findViewById(R.id.etContactName));
                EditText etTitle = (childView.findViewById(R.id.etContactTitle));
                EditText etMobile = (childView.findViewById(R.id.etContactMobile));
                EditText etOffice = (childView.findViewById(R.id.etContactOffice));
                EditText etEmail = (childView.findViewById(R.id.etContactEmail));
                Switch switchIC = (childView.findViewById(R.id.switchContactIC));
                String switchICchecked = "false";

                if(switchIC.isChecked()){
                    switchICchecked = "true";
                }

                tvRowName.setText("Contact Name: " + etContactName.getText().toString());
                tvRowTitle.setText("Contact Title: "+ etTitle.getText().toString());
                tvRowMobile.setText("Contact Mobile: "+ etMobile.getText().toString());
                tvRowOffice.setText("Contact Office: "+ etOffice.getText().toString());
                tvRowEmail.setText("Contact Email: "+ etEmail.getText().toString());
                tvRowIC.setText("Contact IC: "+ switchICchecked);

                etContactName.setVisibility(View.GONE);
                etTitle.setVisibility(View.GONE);
                etMobile.setVisibility(View.GONE);
                switchIC.setVisibility(View.GONE);
            }
        }
    }

    public void setCopierEditable(){

        if(containerCopier.getChildCount() > 0){

            int childCount = containerCopier.getChildCount();
            for(int c=0; c<childCount; c++){

                View childView = containerCopier.getChildAt(c);
                TextView tvBrand = (childView.findViewById(R.id.tvCopierBrand));
                TextView tvModel = (childView.findViewById(R.id.tvCopierModel));
                TextView tvAge = (childView.findViewById(R.id.tvCopierAge));
                TextView tvProblem = (childView.findViewById(R.id.tvCopierProblem));
                TextView tvROP = (childView.findViewById(R.id.tvCopierRorP));
                TextView tvStartDate = (childView.findViewById(R.id.tvCopierContStartDate));
                TextView tvLength = (childView.findViewById(R.id.tvCopierContLength));
                TextView tvEndDate = (childView.findViewById(R.id.tvCopierContExpiryDate));
                TextView tvMP = (childView.findViewById(R.id.tvCopierContMonthlyPayment));
                TextView tvFP = (childView.findViewById(R.id.tvCopierContFinalPayment));

                tvBrand.setText("Copier Brand: ");
                tvModel.setText("Copier Model: ");
                tvAge.setText("Copier Age: ");
                tvProblem.setText("Problem Face by Copier: ");
                tvROP.setText("Copier Rented or Purchased: ");
                tvStartDate.setText("Copier Contract Start Date: ");
                tvLength.setText("Copier contract Length(MONTHS): ");
                tvEndDate.setText("Copier Contract End Date: ");
                tvMP.setText("Copier Contract Monthly Payment: ");
                tvFP.setText("Copier Contract Final Payment: ");

                Spinner spBrand = (childView.findViewById(R.id.spinnerCopierBrand));
                EditText etModel = (childView.findViewById(R.id.etCopierModel));
                EditText etAge = (childView.findViewById(R.id.etCopierAge));
                CheckBox cbPrint = (childView.findViewById(R.id.cbPrint));
                CheckBox cbScan = (childView.findViewById(R.id.cbScan));
                CheckBox cbPaperJam = (childView.findViewById(R.id.cbPaperJam));
                RadioGroup rgROP = (childView.findViewById(R.id.rgRorP));
                RadioButton rbrented = (childView.findViewById(R.id.rbRented));
                RadioButton rbPurchased = (childView.findViewById(R.id.rbPurchased));
                RadioButton rbUnsure = (childView.findViewById(R.id.rbUnsure));
                EditText etStartDate = (childView.findViewById(R.id.etCopierContStartDate));
                EditText etlength = (childView.findViewById(R.id.etCopierContLength));
                EditText etExpiryDate = (childView.findViewById(R.id.etCopierContExpiryDate));
                EditText etMP = (childView.findViewById(R.id.etCopierContMonthlyPayment));
                EditText etFP = (childView.findViewById(R.id.etCopierContFinalPayment));

                spBrand.setVisibility(View.VISIBLE);
                etModel.setVisibility(View.VISIBLE);
                etAge.setVisibility(View.VISIBLE);
                cbPrint.setVisibility(View.VISIBLE);
                cbScan.setVisibility(View.VISIBLE);
                cbPaperJam.setVisibility(View.VISIBLE);
                rgROP.setVisibility(View.VISIBLE);
                rbPurchased.setVisibility(View.VISIBLE);
                rbrented.setVisibility(View.VISIBLE);
                rbUnsure.setVisibility(View.VISIBLE);
                etStartDate.setVisibility(View.VISIBLE);
                etlength.setVisibility(View.VISIBLE);
                etExpiryDate.setVisibility(View.VISIBLE);
                etMP.setVisibility(View.VISIBLE);
                etFP.setVisibility(View.VISIBLE);

            }
        }
    }

    public void setCopierNotEditable(){

        if(containerCopier.getChildCount() > 0){

            int childCount = containerCopier.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = containerCopier.getChildAt(c);

                TextView tvBrand = (childView.findViewById(R.id.tvCopierBrand));
                TextView tvModel = (childView.findViewById(R.id.tvCopierModel));
                TextView tvAge = (childView.findViewById(R.id.tvCopierAge));
                TextView tvProblem = (childView.findViewById(R.id.tvCopierProblem));
                TextView tvROP = (childView.findViewById(R.id.tvCopierRorP));
                TextView tvStartDate = (childView.findViewById(R.id.tvCopierContStartDate));
                TextView tvLength = (childView.findViewById(R.id.tvCopierContLength));
                TextView tvEndDate = (childView.findViewById(R.id.tvCopierContExpiryDate));
                TextView tvMP = (childView.findViewById(R.id.tvCopierContMonthlyPayment));
                TextView tvFP = (childView.findViewById(R.id.tvCopierContFinalPayment));

                Spinner spBrand = (childView.findViewById(R.id.spinnerCopierBrand));
                EditText etModel = (childView.findViewById(R.id.etCopierModel));
                EditText etAge = (childView.findViewById(R.id.etCopierAge));
                CheckBox cbPrint = (childView.findViewById(R.id.cbPrint));
                CheckBox cbScan = (childView.findViewById(R.id.cbScan));
                CheckBox cbPaperJam = (childView.findViewById(R.id.cbPaperJam));
                RadioGroup rgROP = (childView.findViewById(R.id.rgRorP));
                EditText etStartDate = (childView.findViewById(R.id.etCopierContStartDate));
                EditText etlength = (childView.findViewById(R.id.etCopierContLength));
                EditText etExpiryDate = (childView.findViewById(R.id.etCopierContExpiryDate));
                EditText etMP = (childView.findViewById(R.id.etCopierContMonthlyPayment));
                EditText etFP = (childView.findViewById(R.id.etCopierContFinalPayment));

                String problem = "";
                int[] checkboxes = {R.id.cbPrint, R.id.cbScan, R.id.cbPaperJam};
                int length = checkboxes.length;
                for(int i = 0; i<length; i ++){
                    CheckBox cb = findViewById(checkboxes[i]);
                    if(cb.isChecked()){
                        problem = problem.concat(cb.getText().toString() + " ");
                    }
                }

                //rgROP
                RadioGroup rgROP2 = (childView.findViewById(R.id.rgRorP));
                int rgCheckednumber = rgROP2.getCheckedRadioButtonId();
                View radioButton = rgROP2.findViewById(rgCheckednumber);
                int idx = rgROP2.indexOfChild(radioButton);
                RadioButton rbChecked = (RadioButton)  rgROP2.getChildAt(idx);
                String rbROPtext = rbChecked.getText().toString();

                tvBrand.setText("Copier Brand: " + spBrand.getSelectedItem().toString());
                tvModel.setText("Copier Model: "+ etModel.getText().toString());
                tvAge.setText("Copier Age: "+ etAge.getText().toString());
                tvProblem.setText("Problem Face by Copier: "+ problem);
                tvROP.setText("Copier Rented Or Purchased: "+ rbROPtext);
                tvStartDate.setText("Copier Contract Start Date: "+ etStartDate.getText().toString());
                tvLength.setText("Copier Contract length: "+ etlength.getText().toString());
                tvEndDate.setText("Copier Contract End Date: "+ etExpiryDate.getText().toString());
                tvMP.setText("Copier Contract Monthly Payment: "+ etMP.getText().toString());
                tvFP.setText("Copier Contract Final Payment: "+ etFP.getText().toString());

                spBrand.setVisibility(View.GONE);
                etModel.setVisibility(View.GONE);
                etAge.setVisibility(View.GONE);
                cbPrint.setVisibility(View.GONE);
                cbScan.setVisibility(View.GONE);
                cbPaperJam.setVisibility(View.GONE);
                rgROP.setVisibility(View.GONE);
                etStartDate.setVisibility(View.GONE);
                etlength.setVisibility(View.GONE);
                etExpiryDate.setVisibility(View.GONE);
                etMP.setVisibility(View.GONE);
                etFP.setVisibility(View.GONE);

            }
        }
    }

    public void saveContact(){
        if(containerContact.getChildCount() > 0){

            int childCount = containerContact.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = containerContact.getChildAt(c);

                EditText etContactName = (childView.findViewById(R.id.etContactName));
                EditText etTitle = (childView.findViewById(R.id.etContactTitle));
                EditText etMobile = (childView.findViewById(R.id.etContactMobile));
                EditText etOffice = (childView.findViewById(R.id.etContactOffice));
                EditText etEmail = (childView.findViewById(R.id.etContactEmail));
                Switch switchIC = (childView.findViewById(R.id.switchContactIC));

                String companyid = companyDbKey;
                contactName = etContactName.getText().toString().trim();
                contactTitle = etTitle.getText().toString().trim();
                contactMobile = etMobile.getText().toString().trim();
                contactOffice = etOffice.getText().toString().trim();
                contactEmail = etEmail.getText().toString().trim();
                contactIC = false;
                if(switchIC.isChecked()){
                    contactIC = true;
                }

                Contact contact1 = new Contact(contactName, contactTitle, contactMobile, contactOffice, contactEmail, contactIC, companyid);

                if(contactKeyArrayList.size() > c) {
                    System.out.println("checker: C: " + c);
                    System.out.println("checker: array size: " + contactKeyArrayList.size());

                    String key = contactKeyArrayList.get(c);
                    databaseReference.child("Contact").child(key).setValue(contact1);
                }
                else{
                    String key = databaseReference.push().getKey();
                    contactKeyArrayList.add(key);
                    databaseReference.child("Contact").child(key).setValue(contact1);
                }

            }
        }
    }

    public void saveCopier(){
        if(containerCopier.getChildCount() > 0){

            int childCount = containerCopier.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = containerCopier.getChildAt(c);

                Spinner spBrand = (childView.findViewById(R.id.spinnerCopierBrand));
                EditText etModel = (childView.findViewById(R.id.etCopierModel));
                EditText etAge = (childView.findViewById(R.id.etCopierAge));
                CheckBox cbPrint = (childView.findViewById(R.id.cbPrint));
                CheckBox cbScan = (childView.findViewById(R.id.cbScan));
                CheckBox cbPaperJam = (childView.findViewById(R.id.cbPaperJam));
                String problem = "";
                int[] checkboxes = {R.id.cbPrint, R.id.cbScan, R.id.cbPaperJam};
                int length = checkboxes.length;
                for(int i = 0; i<length; i ++){
                    CheckBox cb = findViewById(checkboxes[i]);
                    if(cb.isChecked()){
                        problem = problem.concat(cb.getText().toString() + " ");
                    }
                }

                //rgROP
                RadioGroup rgROP = (childView.findViewById(R.id.rgRorP));
                int rgCheckednumber = rgROP.getCheckedRadioButtonId();
                View radioButton = rgROP.findViewById(rgCheckednumber);
                int idx = rgROP.indexOfChild(radioButton);
                RadioButton rbChecked = (RadioButton)rgROP.getChildAt(idx);
                String rbROPtext = rbChecked.getText().toString();

                //
                EditText etStartDate = (childView.findViewById(R.id.etCopierContStartDate));
                EditText etlength = (childView.findViewById(R.id.etCopierContLength));
                EditText etExpiryDate = (childView.findViewById(R.id.etCopierContExpiryDate));
                EditText etMP = (childView.findViewById(R.id.etCopierContMonthlyPayment));
                EditText etFP = (childView.findViewById(R.id.etCopierContFinalPayment));

                String companyid = companyDbKey;
                copierBrand = spBrand.getSelectedItem().toString();
                copierModel = etModel.getText().toString().trim();
                copierAge = etAge.getText().toString().trim();
                copierROP = rbROPtext;
                copierProblems = problem;
                copierStartDate = etStartDate.getText().toString().trim();
                copierContLength = etlength.getText().toString().trim();
                copierEndDate = etExpiryDate.getText().toString().trim();
                copierMP = etMP.getText().toString().trim();
                copierFP = etFP.getText().toString().trim();

                Copier copier = new Copier(copierBrand, copierModel, copierAge, copierProblems, copierROP, Integer.parseInt(copierContLength), copierStartDate, copierEndDate, copierMP, copierFP, companyid);

                if(copierKeyArrayList.size() > c) {
                    System.out.println("checker: C: " + c);
                    System.out.println("checker: array size: " + copierKeyArrayList.size());

                    String key = copierKeyArrayList.get(c);
                    databaseReference.child("Copier").child(key).setValue(copier);
                }
                else{
                    String key = databaseReference.push().getKey();
                    copierKeyArrayList.add(key);
                    databaseReference.child("Copier").child(key).setValue(copier);
                }

            }
        }
    }

    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ViewMyNameCardDetailedActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                et.setText(day+"/"+(month+1)+"/"+year);
            }
        }
                ,day,month,year);
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.show();
    }

    public String calculateExpiryDate(String dt, int months){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, months);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String output = sdf1.format(c.getTime());

        System.out.println("output date: " + output);
        return output;
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

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        etCompanyAddress.setText(getLatLng(String.valueOf(charSequence)));
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void afterTextChanged(Editable editable) {

    }

}
