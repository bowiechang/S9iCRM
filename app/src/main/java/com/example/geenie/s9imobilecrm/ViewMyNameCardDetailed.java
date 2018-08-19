package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewMyNameCardDetailed extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView tvCompanyName, tvCompanyAddress, tvCompanyUnitNumber, tvCompanyPostalCode, tvCompanyNumber, tvCompanyIndustry, tvCompanyLack, tvCompanyNumOfCalls, tvCompanyPl, tvCompanyComments;
    private EditText etCompanyName, etCompanyAddress, etCompanyNumber, etCompanyNumOfCalls, etCompanyComments, etCompanyPostalCode, etCompanyUnitNo;

    private Spinner spinnerIndustry;
    private CheckBox checkBoxPrinter, checkBoxScanner, checkBoxShredder;
    private RadioGroup rgPriorityLevel;
    private RadioButton rbNormal, rbFollowUp, rbUrgent;
    private LinearLayout containerContact;
    private Button btnEditandSave;

    private Company company;
    private Contact contact;
    private String postalcodekey, namekey, companyDbKey;
    private ArrayList<String> contactKeyArrayList;
    private int contactCount = 1;

    //for contact vals
    String contactName, contactTitle, contactMobile;
    Boolean contactIC;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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
        containerContact = findViewById(R.id.containerContact);

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
        checkBoxSetter(company.getCompanyLackOf());

        //radiobutton slection
        radioSetter(company.getPriorityLevel());

        rgPriorityLevel.setVisibility(View.GONE);
        rbUrgent.setVisibility(View.GONE);
        rbFollowUp.setVisibility(View.GONE);
        rbNormal.setVisibility(View.GONE);

    }

    public void seekContactsFromCompany(String key){

        databaseReference.child("Contact").orderByChild("company_id").equalTo(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                contact = dataSnapshot.getValue(Contact.class);
                if(contact!=null){

                    contactKeyArrayList.add(dataSnapshot.getKey());

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.namecarddetailed_contact_row, null);

                    TextView tvContactCount = addView.findViewById(R.id.tvContactCount);
                    tvContactCount.setText("Contact #" + contactCount);
                    contactCount ++;

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
                    tvRowMobile.setText(tvRowMobile.getText().toString().concat(contact.getTitle()));
                    EditText etRowMobile = addView.findViewById(R.id.etContactMobile);
                    etRowMobile.setText(contact.getMobileNumber());
                    etRowMobile.setVisibility(View.GONE);

                    //IC
                    TextView tvRowIC = addView.findViewById(R.id.tvContactIC);
                    Switch icSwitch = addView.findViewById(R.id.switchContactIC);
                    icSwitch.setVisibility(View.GONE);
                    if(contact.getIc().equals(true)){
                        tvRowIC.setText(tvRowIC.getText().toString().concat("yes"));
                        icSwitch.isChecked();
                    }
                    else{
                        tvRowIC.setText(tvRowIC.getText().toString().concat("no"));
                        icSwitch.setChecked(false);
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
        });
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

    public void checkBoxSetter(String key){

        if(key.contains("Copier")){
            checkBoxPrinter.isChecked();
        }
        else if(key.contains("Shredder")){
            checkBoxShredder.isChecked();
        }
        else if(key.contains("Scanner")){
            checkBoxScanner.isChecked();
        }
    }

    public void radioSetter(String key){

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

        if(btnEditandSave.getText().toString().equals("EDIT")){

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
            setContactEditable();
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

            Company company = new Company(name, postalCode, unitNo, officeTel, industry, lack, rbSelectedText, comment, createBy, numberOfTimesCalled);
            databaseReference.child("Company").child(companyDbKey).setValue(company);

            saveContact();

        }
    }

    public void setContactEditable(){

        if(containerContact.getChildCount() > 0){

            int childCount = containerContact.getChildCount();
            for(int c=0; c<childCount; c++){
                View childView = containerContact.getChildAt(c);

                TextView tvRowName = (childView.findViewById(R.id.tvContactName));
                TextView tvRowTitle = (childView.findViewById(R.id.tvContactTitle));
                TextView tvRowMobile = (childView.findViewById(R.id.tvContactMobile));
                TextView tvRowIC = (childView.findViewById(R.id.tvContactIC));

                tvRowName.setText("Contact Name: ");
                tvRowTitle.setText("Contact Title: ");
                tvRowMobile.setText("Contact Mobile: ");
                tvRowIC.setText("Contact IC: ");

                EditText etContactName = (childView.findViewById(R.id.etContactName));
                EditText etTitle = (childView.findViewById(R.id.etContactTitle));
                EditText etMobile = (childView.findViewById(R.id.etContactMobile));
                Switch switchIC = (childView.findViewById(R.id.switchContactIC));

                etContactName.setVisibility(View.VISIBLE);
                etTitle.setVisibility(View.VISIBLE);
                etMobile.setVisibility(View.VISIBLE);
                switchIC.setVisibility(View.VISIBLE);
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
                Switch switchIC = (childView.findViewById(R.id.switchContactIC));

                String companyid = companyDbKey;
                contactName = etContactName.getText().toString().trim();
                contactTitle = etTitle.getText().toString().trim();
                contactMobile = etMobile.getText().toString().trim();
                contactIC = false;
                if(switchIC.isChecked()){
                    contactIC = true;
                }

                Contact contact1 = new Contact(contactName, contactTitle, contactMobile, contactIC, companyid);
                String key = contactKeyArrayList.get(c);
                databaseReference.child("Contact").child(key).setValue(contact1);
            }
        }
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
