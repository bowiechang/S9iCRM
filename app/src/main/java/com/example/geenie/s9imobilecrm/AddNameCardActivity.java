package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

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

    private int contactCount = 2;

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
    }

    public void insertCompanyDetails(){
//
        //company
        String companyName = etCompanyName.getText().toString().trim();
        String companyPostalCode = etPostalCode.getText().toString().trim();
        String companyUnitNo = etUnitNo.getText().toString().trim();
        String companyOfficeNumber = etOfficeNumber.getText().toString().trim();
        String companyIndustry = spIndustry.getSelectedItem().toString();

        //contact
        String contactName = etName.getText().toString().trim();
        String contactTitle = etTitle.getText().toString().trim();
        String contactMobileNumber = etMobileNumber.getText().toString().trim();

        if(switchIC.isChecked()){
            String contactIC = "yes";
        }
        else{
            String contactIC = "no";
        }

        //otherinfo
        String lack = "";
        int[] checkboxes = {R.id.cbCopier, R.id.cbScanner, R.id.cbShredder};
        int length = checkboxes.length;
        for(int i = 0; i<length; i ++){
            CheckBox cb = findViewById(checkboxes[i]);
            if(cb.isChecked()){
                lack = lack.concat(cb.getText().toString());
            }
        }

        int rgCheckednumber = rgPriorityLevel.getCheckedRadioButtonId();
        View radioButton = rgPriorityLevel.findViewById(rgCheckednumber);
        int idx = rgPriorityLevel.indexOfChild(radioButton);
        RadioButton rbChecked = (RadioButton)  rgPriorityLevel.getChildAt(idx);
        String rbSelectedText = rbChecked.getText().toString();
        String commentText = comment.getText().toString().trim();
    }

    public void rearrangeContactCount(){

        int childCount = linearLayoutContact.getChildCount();

        for(int c=0; c<childCount; c++){
            View childView = linearLayoutContact.getChildAt(c);
            TextView tvContactCount = (childView.findViewById(R.id.tvContactCount));
            tvContactCount.setText("Contact #" + (c + 2));

        }
    }
}
