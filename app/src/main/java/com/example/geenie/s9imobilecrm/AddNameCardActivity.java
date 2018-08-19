package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private Switch switchIC;

    //other info
    private CheckBox cbCopier, cbScanner, cbShredder;
    private RadioGroup rgPriorityLevel;
    private RadioButton rbNormal, rbFollowUp, rbUrgent;
    private EditText comment;


    private TextView tvAdd;
    private LinearLayout linearLayoutContact;
    private Button btnAddANameCard;

    private int contactCount = 2;

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
            //perform row insert
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.row, null);

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
        finish();
    }

    public void addContacts(String companyid){
        // name, title mobile, ic must be extracted

        //contact
        String contactName = etName.getText().toString().trim();
        String contactTitle = etTitle.getText().toString().trim();
        String contactMobileNumber = etMobileNumber.getText().toString().trim();

        Boolean contactIC = false;
        if(switchIC.isChecked()){
            contactIC = true;
        }

        Contact contact = new Contact(contactName, contactTitle, contactMobileNumber, contactIC, companyid);
        databaseReference.child("Contact").push().setValue(contact);

        if(linearLayoutContact.getChildCount() > 0){

            int childCount = linearLayoutContact.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = linearLayoutContact.getChildAt(c);
                EditText etContactName = (childView.findViewById(R.id.etContactName));
                EditText etTitle = (childView.findViewById(R.id.etContactTitle));
                EditText etMobile = (childView.findViewById(R.id.etContactMobile));
                Switch switchIC = (childView.findViewById(R.id.switchContactIC));

                String etContactNameText;
                String etTitleText;
                String etMobileText;

                etContactNameText = etContactName.getText().toString().trim();
                etTitleText = etTitle.getText().toString().trim();
                etMobileText = etMobile.getText().toString().trim();
                Boolean contactIC2 = false;
                if(switchIC.isChecked()){
                    contactIC2 = true;
                }

                Contact contact1 = new Contact(etContactNameText, etTitleText, etMobileText, contactIC2, companyid);
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

    public void rearrangeContactCount(){

        int childCount = linearLayoutContact.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = linearLayoutContact.getChildAt(c);
            TextView tvContactCount = (childView.findViewById(R.id.tvContactCount));
            tvContactCount.setText("Contact #" + (c + 2));

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
