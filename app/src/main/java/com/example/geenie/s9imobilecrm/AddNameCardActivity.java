package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNameCardActivity extends AppCompatActivity implements View.OnClickListener {

    //company
    private EditText etCompanyName;
    private EditText etPostalCode;
    private EditText etUnitNo;
    private EditText etOfficeNumber;
    private Spinner spIndustry;

    //contact
    private EditText etName;
    private EditText etTitle;
    private EditText etMobileNumber;
    private EditText etoffNumber;
    private EditText etEmail;
    private Switch switchIC;

    //copier
    private TextView tvAddCopier;
    private LinearLayout linearLayoutCopier;
    private Calendar calendar = Calendar.getInstance();

    //other info
    private CheckBox cbCopier, cbScanner, cbShredder;
    private RadioGroup rgPriorityLevel;
    private RadioButton rbNormal, rbFollowUp, rbUrgent;
    private EditText comment;


    private TextView tvAdd;
    private LinearLayout linearLayoutContact;
    private Button btnAddANameCard;

    private int contactCount = 2;
    private int copierCount = 1;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name_card);
        init();
    }

    public void init(){
        //company
        etCompanyName = findViewById(R.id.etCompanyName);
        etPostalCode = findViewById(R.id.etPostalCode);
        etUnitNo = findViewById(R.id.etUnitNumber);
        etOfficeNumber = findViewById(R.id.etOfficeNumber);
        spIndustry = findViewById(R.id.spinnerIndustry);

        //contact
        etName = findViewById(R.id.etContactName);
        etTitle = findViewById(R.id.etContactTitle);
        etMobileNumber = findViewById(R.id.etContactMobile);
        etoffNumber = findViewById(R.id.etContactOffice);
        etEmail = findViewById(R.id.etContactEmail);
        switchIC = findViewById(R.id.switchContactIC);
        switchIC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ICswitchChecker();
            }
        });

        linearLayoutContact = findViewById(R.id.llcontact);
        tvAdd = findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(this);

        //copier
        tvAddCopier = findViewById(R.id.tvAddCopier);
        linearLayoutCopier = findViewById(R.id.llcopier);
        tvAddCopier.setOnClickListener(this);

        //otherinfo
        cbCopier = findViewById(R.id.cbCopier);
        cbScanner = findViewById(R.id.cbScanner);
        cbShredder = findViewById(R.id.cbShredder);
        rgPriorityLevel = findViewById(R.id.rgPriorityLevel);
        rbNormal = findViewById(R.id.rbNormal);
        rbFollowUp = findViewById(R.id.rbFollowUp);
        rbUrgent = findViewById(R.id.rbUrgent);
        comment = findViewById(R.id.etComment);

        btnAddANameCard = findViewById(R.id.btnAddNameCard);
        btnAddANameCard.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == tvAdd){
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

            linearLayoutContact.addView(addView, linearLayoutContact.getChildCount());

        }
        else if(view == btnAddANameCard){
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

            linearLayoutCopier.addView(addView, linearLayoutCopier.getChildCount());
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
                et.setText(day+"/"+month+"/"+year);
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
        String commentText = comment.getText().toString().trim();

        String comdetails = companyName + " " + companyPostalCode + " " + companyUnitNo + " " + companyOfficeNumber + " " + companyIndustry;

        String otherinfodetails = arrayListLack + " " + rbSelectedText + " " + commentText;
        System.out.println("company details: " + "( " + comdetails + " )");
        System.out.println("otherinfo details: " + "( " + otherinfodetails + " )");
        System.out.println("uid: " + uid);

        final Company company = new Company(companyName, companyPostalCode, companyUnitNo, companyOfficeNumber, companyIndustry,
                lack, rbSelectedText, commentText, uid, 0);
        final String dbKey = databaseReference.push().getKey();
        databaseReference.child("Company").child(dbKey).setValue(company).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "name card added!", Toast.LENGTH_SHORT).show();
            }
        });

        addContacts(dbKey);
        addCopier(dbKey);
        finish();
    }

    public void addContacts(String companyid){
        // name, title mobile, ic must be extracted

        //contact
        String contactName = etName.getText().toString().trim();
        String contactTitle = etTitle.getText().toString().trim();
        String contactMobileNumber = etMobileNumber.getText().toString().trim();
        String contactOfficeNumber = etoffNumber.getText().toString().trim();
        String contactEmail = etEmail.getText().toString().trim();

        Boolean contactIC = false;
        if(switchIC.isChecked()){
            contactIC = true;
        }

        Contact contact = new Contact(contactName, contactTitle, contactMobileNumber, contactOfficeNumber, contactEmail, contactIC, companyid);
        databaseReference.child("Contact").push().setValue(contact);

        if(linearLayoutContact.getChildCount() > 0){

            int childCount = linearLayoutContact.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = linearLayoutContact.getChildAt(c);
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

        FollowUp followUp = new FollowUp(calculateFollowUpDate(), "greeting", "incomplete", companyid);
        databaseReference.child("FollowUp").push().setValue(followUp);
    }

    public void addCopier(String companyid){

        if(linearLayoutCopier.getChildCount() > 0){
            int childCount = linearLayoutCopier.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = linearLayoutCopier.getChildAt(c);

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

        FollowUp followUp = new FollowUp(calculateFollowUpDate(), "greeting", "incomplete", companyid);
        databaseReference.child("FollowUp").push().setValue(followUp);
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

        int childCount = linearLayoutContact.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = linearLayoutContact.getChildAt(c);
            TextView tvContactCount = (childView.findViewById(R.id.tvContactCount));
            tvContactCount.setText("Contact #" + (c + 2));

        }
    }

    public void rearrangeCopierCount(){

        int childCount = linearLayoutCopier.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = linearLayoutCopier.getChildAt(c);
            TextView tvContactCount = (childView.findViewById(R.id.tvCopierCount));
            tvContactCount.setText("Copier #" + (c + 1));

        }
    }

    public void ICswitchChecker(){

        int childCount = linearLayoutContact.getChildCount();
        int icChecker = 0;

        if(switchIC.isChecked()){
            icChecker ++;
        }

        for(int c=0; c<childCount; c++){

            View childView = linearLayoutContact.getChildAt(c);
            Switch switchIC = (childView.findViewById(R.id.switchContactIC));

            if(switchIC.isChecked()){
                icChecker ++;
            }
            if(icChecker > 1){
                Toast.makeText(getApplicationContext(), "Warning, more than one IC found!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
