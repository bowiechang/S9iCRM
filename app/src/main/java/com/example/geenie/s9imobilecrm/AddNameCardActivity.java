package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddNameCardActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    //company
    private EditText etCompanyName;
    private EditText etPostalCode;
    private EditText etAddress;
    private EditText etUnitNo;
    private EditText etOfficeNumber;
    private Spinner spIndustry;

    //contact
    private TextView tvAddContact;
    private LinearLayout containerContact;

    //copier
    private TextView tvAddCopier;
    private LinearLayout containerCopier;
    private Calendar calendar = Calendar.getInstance();


    //photos
    private RecyclerView recyclerView;
    private RelativeLayout btnCapturePhoto;

    private Boolean exist = false;
    private int count, counter;
    private String dbkey = "";

    //other info
    private CheckBox cbCopier, cbScanner, cbShredder;
    private RadioGroup rgPriorityLevel;
    private RadioButton rbNormal, rbFollowUp, rbUrgent;
    private EditText comment;


//    private TextView tvAdd;
//    private LinearLayout linearLayoutContact;
    private RelativeLayout btnAddANameCard;

    private int contactCount = 1;
    private int copierCount = 1;

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
        setContentView(R.layout.redesign_activity_add_name_card);
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

        recyclerView = findViewById(R.id.rvPhotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        btnCapturePhoto = findViewById(R.id.btnLaunchPhotoCaptureLib);
        btnCapturePhoto.setOnClickListener(this);

        //company
        etCompanyName = findViewById(R.id.etCompanyName);
        etAddress = findViewById(R.id.etCompanyAddress);
        etPostalCode = findViewById(R.id.etCompanyPostalCode);
        etPostalCode.addTextChangedListener(this);
        etUnitNo = findViewById(R.id.etCompanyUnit);
        etOfficeNumber = findViewById(R.id.etCompanyOfficeNumber);
        spIndustry = findViewById(R.id.spinnerIndustry);

        //contact
        containerContact = findViewById(R.id.containerContact);
        tvAddContact = findViewById(R.id.tvAddContact);
        tvAddContact.setOnClickListener(this);

        //copier
        containerCopier = findViewById(R.id.containerCopier);
        tvAddCopier = findViewById(R.id.tvAddCopier);
        tvAddCopier.setOnClickListener(this);

        //otherinfo
        cbCopier = findViewById(R.id.cbCopier);
        cbScanner = findViewById(R.id.cbScanner);
        cbShredder = findViewById(R.id.cbShredder);
        rgPriorityLevel = findViewById(R.id.rgPriorityLevel);
        rbNormal = findViewById(R.id.rbNormal);
        rbFollowUp = findViewById(R.id.rbFollowUp);
        rbUrgent = findViewById(R.id.rbUrgent);
        comment = findViewById(R.id.etCompanyComment);

        btnAddANameCard = findViewById(R.id.btnAddNameCard);
        btnAddANameCard.setOnClickListener(this);

//        initForContact();

    }

    public void initForContact(){

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

//                    if(contactKeyArrayList.size() >= contactCount){
//                        String key = contactKeyArrayList.get(contactCount);
//                        databaseReference.child("Contact").child(key).removeValue();
//                        contactKeyArrayList.remove(contactCount);
//                    }

            }
        });

        //NAME
        TextView tvRowName = addView.findViewById(R.id.tvContactName);
        tvRowName.setVisibility(View.GONE);

        //TITLE
        TextView tvRowTitle = addView.findViewById(R.id.tvContactTitle);
        tvRowTitle.setVisibility(View.GONE);

        //MOBILE
        TextView tvRowMobile = addView.findViewById(R.id.tvContactMobile);
        tvRowMobile.setVisibility(View.GONE);

        //OFFICE
        TextView tvRowOffice = addView.findViewById(R.id.tvContactOffice);
        tvRowOffice.setVisibility(View.GONE);

        //EMAIL
        TextView tvRowEmail = addView.findViewById(R.id.tvContactEmail);
        tvRowEmail.setVisibility(View.GONE);

        //IC
        TextView tvRowIC = addView.findViewById(R.id.tvContactIC);
        Switch icSwitch2 = addView.findViewById(R.id.switchContactIC);
        icSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ICswitchChecker();
            }
        });

        containerContact.addView(addView, containerContact.getChildCount());

    }

    @Override
    public void onClick(View view) {
//        if(view == tvAdd){
//            //perform rowcontact insert
//            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            final View addView = layoutInflater.inflate(R.layout.rowcontact, null);
//
//            TextView tvContactCount = addView.findViewById(R.id.tvContactCount);
//            tvContactCount.setText("Contact #" + contactCount);
//            contactCount ++;
//
//            //icSwitchChecker
//            Switch icSwitch = addView.findViewById(R.id.switchContactIC);
//            icSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    ICswitchChecker();
//                }
//            });
//
//            //remove contact
//            TextView tvContactRemove = addView.findViewById(R.id.tvRemove);
//            tvContactRemove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    TextView tvCurrentCount = addView.findViewById(R.id.tvContactCount);
//                    if(!tvCurrentCount.getText().toString().equals(String.valueOf(contactCount))){
//                        ((LinearLayout)addView.getParent()).removeView(addView);
//                        contactCount --;
//                        rearrangeContactCount();
//                    }
//                    else{
//                        ((LinearLayout)addView.getParent()).removeView(addView);
//                        contactCount --;
//                    }
//
//                }
//            });
//
//            linearLayoutContact.addView(addView, linearLayoutContact.getChildCount());
//
//        }
//        else

        if(view == btnAddANameCard){

            insertCompanyDetails();
        }
        else if(view == tvAddCopier){
            //perform rowcopier insert
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
        else if(view.equals(tvAddContact)){

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

//                    if(contactKeyArrayList.size() >= contactCount){
//                        String key = contactKeyArrayList.get(contactCount);
//                        databaseReference.child("Contact").child(key).removeValue();
//                        contactKeyArrayList.remove(contactCount);
//                    }

                }
            });

            //NAME
            TextView tvRowName = addView.findViewById(R.id.tvContactName);
            tvRowName.setVisibility(View.GONE);

            //TITLE
            TextView tvRowTitle = addView.findViewById(R.id.tvContactTitle);
            tvRowTitle.setVisibility(View.GONE);

            //MOBILE
            TextView tvRowMobile = addView.findViewById(R.id.tvContactMobile);
            tvRowMobile.setVisibility(View.GONE);

            //OFFICE
            TextView tvRowOffice = addView.findViewById(R.id.tvContactOffice);
            tvRowOffice.setVisibility(View.GONE);

            //EMAIL
            TextView tvRowEmail = addView.findViewById(R.id.tvContactEmail);
            tvRowEmail.setVisibility(View.GONE);

            //IC
            TextView tvRowIC = addView.findViewById(R.id.tvContactIC);
            Switch icSwitch2 = addView.findViewById(R.id.switchContactIC);
            icSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ICswitchChecker();
                }
            });

            containerContact.addView(addView, containerContact.getChildCount());
        }
        else if(view.equals(btnCapturePhoto)){
            ImagePicker.with(AddNameCardActivity.this)                         //  Initialize ImagePicker with activity or fragment context
                    .setToolbarColor("#212121")         //  Toolbar color
                    .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                    .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                    .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                    .setProgressBarColor("#4CAF50")     //  ProgressBar color
                    .setBackgroundColor("#212121")      //  Background color
                    .setCameraOnly(false)               //  Camera mode
                    .setMultipleMode(true)              //  Select multiple images or single image
                    .setFolderMode(true)                //  Folder mode
                    .setShowCamera(true)                //  Show camera button
                    .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                    .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                    .setDoneTitle("Done")               //  Done button title
                    .setLimitMessage("You have reached selection limit")    // Selection limit message
                    .setMaxSize(10)                     //  Max images can be selected
                    .setSavePath("ImagePicker")             //  Selected images
                    .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                    .setKeepScreenOn(true)              //  Keep screen on when selecting images
                    .start();
        }
    }

    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddNameCardActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                et.setText(day+"/"+(month+1)+"/"+year);
            }
        }
                ,day,month,year);
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.show();
    }

    public void insertCompanyDetails(){
//
        //company
        String companyName = etCompanyName.getText().toString().trim();
        String companyPostalCode = etPostalCode.getText().toString().trim();
        String companyUnitNo = etUnitNo.getText().toString().trim();
        char firstletterUnitchecker = companyUnitNo.charAt(0);
        if(!companyUnitNo.contains("#")){
            companyUnitNo = "#".concat(companyUnitNo);
        }
        else if(firstletterUnitchecker != '#'){
            companyUnitNo = companyUnitNo.replaceAll("#", "");
            companyUnitNo = "#".concat(companyUnitNo);
        }
        String companyOfficeNumber = etOfficeNumber.getText().toString().trim();
        String companyIndustry = spIndustry.getSelectedItem().toString();

        //otherinfo
        String lack = "";
        ArrayList<String> arrayListLack = new ArrayList<String>();
        int[] checkboxes = {R.id.cbCopier, R.id.cbScanner, R.id.cbShredder};
        int length = checkboxes.length;
        for(int i = 0; i<length; i ++){
            CheckBox cb = findViewById(checkboxes[i]);
            if(cb.isChecked()){
                lack = lack.concat(cb.getText().toString() + " ");
//                arrayListLack.add(cb.getText().toString());
            }
        }

        int rgCheckednumber = rgPriorityLevel.getCheckedRadioButtonId();
        View radioButton = rgPriorityLevel.findViewById(rgCheckednumber);
        int idx = rgPriorityLevel.indexOfChild(radioButton);
        RadioButton rbChecked = (RadioButton)  rgPriorityLevel.getChildAt(idx);
        String rbSelectedText = rbChecked.getText().toString();
        String newPL = "";
        if(rbSelectedText.equals("Urgent")){
            newPL = "a.Urgent";
        }
        else if(rbSelectedText.equals("Follow Up")){
            newPL = "b.Follow up";
        }
        else if(rbSelectedText.equals("Normal")){
            newPL = "c.Normal";
        }
        String commentText = comment.getText().toString().trim();

        String comdetails = companyName + " " + companyPostalCode + " " + companyUnitNo + " " + companyOfficeNumber + " " + companyIndustry;

        String otherinfodetails = arrayListLack + " " + rbSelectedText + " " + commentText;
        System.out.println("company details: " + "( " + comdetails + " )");
        System.out.println("otherinfo details: " + "( " + otherinfodetails + " )");
        System.out.println("uid: " + uid);

        String dateCreate = getDateCreateNow();

        final Company company = new Company(companyName, companyPostalCode, companyUnitNo, companyOfficeNumber, companyIndustry,
                lack, newPL, commentText, uid, dateCreate, 0);

        final String dbkeyhere;
        if(dbkey.equals("")) {
            dbkeyhere = databaseReference.push().getKey();
        }
        else{
            dbkeyhere = dbkey;
        }
        databaseReference.child("Company").child(dbkeyhere).setValue(company).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Name Card Added!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddNameCardActivity.this, ViewMyNameCardDetailedActivity.class);
                Bundle extras = new Bundle();
                extras.putString("dbkey", dbkeyhere);
                intent.putExtras(extras);
                AddNameCardActivity.this.startActivity(intent);
            }
        });

        addContacts(dbkeyhere);
        addCopier(dbkeyhere);
    }

    public void addContacts(String companyid){
//        // name, title mobile, ic must be extracted
//
//        //contact
//        String contactName = etName.getText().toString().trim();
//        String contactTitle = etTitle.getText().toString().trim();
//        String contactMobileNumber = etMobileNumber.getText().toString().trim();
//        String contactOfficeNumber = etoffNumber.getText().toString().trim();
//        String contactEmail = etEmail.getText().toString().trim();
//
//        Boolean contactIC = false;
//        if(switchIC.isChecked()){
//            contactIC = true;
//        }
//
//        Contact contact = new Contact(contactName, contactTitle, contactMobileNumber, contactOfficeNumber, contactEmail, contactIC, companyid);
//        databaseReference.child("Contact").push().setValue(contact);

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

                String etContactNameText;
                String etTitleText;
                String etMobileText;
                String etOfficeText;
                String etEmailText;

                etContactNameText = etContactName.getText().toString().trim();
                etTitleText = etTitle.getText().toString().trim();
                etMobileText = etMobile.getText().toString().trim();
                etOfficeText = etOffice.getText().toString().trim();
                etEmailText = etEmail.getText().toString().trim();
                Boolean contactIC2 = false;
                if(switchIC.isChecked()){
                    contactIC2 = true;
                }

                Contact contact1 = new Contact(etContactNameText, etTitleText, etMobileText, etOfficeText, etEmailText, contactIC2, companyid);
                databaseReference.child("Contact").push().setValue(contact1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "contacts added!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        //insert to follow up
        //seek for the COMPANY ID and insert into follow up
        FollowUp followUp = new FollowUp(calculateFollowUpDate(), "greeting", "incomplete", companyid, uid);
        databaseReference.child("FollowUp").push().setValue(followUp);
    }

    public void addCopier(String companyid){

        if(containerCopier.getChildCount() > 0){
            int childCount = containerCopier.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = containerCopier.getChildAt(c);

                Spinner spinnerCopierBrand = (childView.findViewById(R.id.spinnerCopierBrand));
                EditText etCopierModel = (childView.findViewById(R.id.etCopierModel));
                EditText etCopierAge= (childView.findViewById(R.id.etCopierAge));
                RadioGroup rgRorP = (childView.findViewById(R.id.rgRorP));
                EditText etCopierContractLength = (childView.findViewById(R.id.etCopierContLength));
                EditText etCopierContractStartDate = (childView.findViewById(R.id.etCopierContStartDate));
                EditText etCopierContractExpiryDate = (childView.findViewById(R.id.etCopierContExpiryDate));
                EditText etCopierContractMP  = (childView.findViewById(R.id.etCopierContMonthlyPayment));
                EditText etCopierContractFP  = (childView.findViewById(R.id.etCopierContFinalPayment));

                //get problem face by copier
                String problem = "";
                int[] checkboxes = {R.id.cbPrint, R.id.cbScan, R.id.cbPaperJam};
                int length = checkboxes.length;
                for(int i = 0; i<length; i ++){
                    CheckBox cb = findViewById(checkboxes[i]);
                    if(cb.isChecked()){
                        problem = problem.concat(cb.getText().toString() + " ");
                    }
                }

                //copier renter or purchase or unsure
                int rgCheckednumber = rgRorP.getCheckedRadioButtonId();
                View radioButton = rgRorP.findViewById(rgCheckednumber);
                int idx = rgRorP.indexOfChild(radioButton);
                RadioButton rbChecked = (RadioButton)  rgRorP.getChildAt(idx);
                String rbSelectedText = rbChecked.getText().toString();

                String brandText = "";
                String modelText = "";
                String age = "";
                String rOrP = "";
                String startDate = "";
                String expiryDate = "";
                int lengthCont;
                String contMP = "";
                String contFP = "";

                brandText = spinnerCopierBrand.getSelectedItem().toString();
                modelText = etCopierModel.getText().toString().trim();
                age = etCopierAge.getText().toString().trim();
                rOrP = rbSelectedText;
                startDate = etCopierContractStartDate.getText().toString().trim();
                expiryDate = etCopierContractExpiryDate.getText().toString().trim();
                lengthCont = Integer.parseInt(etCopierContractLength.getText().toString().trim());
                contMP = etCopierContractMP.getText().toString().trim();
                contFP = etCopierContractFP.getText().toString().trim();

                Copier copier = new Copier(brandText, modelText, age, problem, rOrP, lengthCont, startDate, expiryDate, contMP, contFP, companyid);
                databaseReference.child("Copier").push().setValue(copier).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "copiers added!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        //insert to follow up
        //seek for the COMPANY ID and insert into follow up
    }

    public String calculateFollowUpDate(){

        String dt = Calendar.getInstance().getTime().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 2);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        if(c.get(Calendar.DAY_OF_WEEK) == 7){
            c.add(Calendar.DATE, 2);
        }
        else if(c.get(Calendar.DAY_OF_WEEK) == 1){
            c.add(Calendar.DATE, 1);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String output = sdf1.format(c.getTime());

        return output;
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

    public void rearrangeContactCount(){

        int childCount = containerContact.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = containerContact.getChildAt(c);
            TextView tvContactCount = (childView.findViewById(R.id.tvContactCount));
            tvContactCount.setText("Contact #" + (c + 2));

        }
    }

    public void rearrangeCopierCount(){

        int childCount = containerCopier.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = containerCopier.getChildAt(c);
            TextView tvContactCount = (childView.findViewById(R.id.tvCopierCount));
            tvContactCount.setText("Copier #" + (c + 1));

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

    //photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {

            dbkey = databaseReference.child("Photo").push().getKey();

            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            for (int i = 0; i < images.size(); i++) {
                storageReference.child(dbkey.concat("/pic" + i + ".jpg")).putFile(Uri.fromFile(new File(images.get(i).getPath())));
            }

            Photo photo2 = new Photo(String.valueOf(images.size()));
            databaseReference.child("Photo").child(dbkey).push().setValue(photo2);
            System.out.println("exist pushing child: in (!exist checker)");

            final Handler handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(2000);
                    }
                    catch (Exception e) { }
                    handler.post(new Runnable() {
                        public void run() {
                            retrieveStoragePath();
                        }
                    });
                }
            }).start();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void retrieveStoragePath(){
        databaseReference.child("Photo").child(dbkey).addChildEventListener(new ChildEventListener() {
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
            String address = addresses.get(0).getAddressLine(0); // If any additial address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getPhotos(ArrayList<String> list){

        PhotoAdapter photoAdapter = new PhotoAdapter(list,AddNameCardActivity.this, dbkey);
        recyclerView.setAdapter(photoAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        etAddress.setText(getLatLng(String.valueOf(charSequence)));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
