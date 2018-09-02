package com.example.geenie.s9imobilecrm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ViewDetailedCopierActivity extends AppCompatActivity implements View.OnClickListener {

    private String model, brand, companyid;

    private TextView tvBrand, tvModel, tvAge, tvProblem, tvRoP, tvContLength, tvContSD, tvContED, tvContMP, tvContFP;
    private EditText etModel, etAge, etContLength, etContSD, etContED, etContMP, etContFP;

    private Spinner spBrand;
    private CheckBox cbPrint, cbScan, cbPaperJam;
    private RadioGroup rgRoP;
    private RadioButton rbRented, rbPurchased, rbUnsure;

    private Button btnEdit, btnRecont, btnDelete;

    private String copierDbKey;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private Calendar calendar = Calendar.getInstance();


    private TextWatcher textWatcherContLength, textWatcherContSD, textWatcherRecontract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detailed_copier);

        init();
    }

    public void init(){

        model = getIntent().getExtras().getString("model");
        brand = getIntent().getExtras().getString("brand");
        companyid = getIntent().getExtras().getString("companyid");

        tvBrand = findViewById(R.id.tvCopierBrand);
        tvModel = findViewById(R.id.tvCopierModel);
        tvAge = findViewById(R.id.tvCopierAge);
        tvProblem = findViewById(R.id.tvCopierProblem);
        tvRoP = findViewById(R.id.tvCopierRorP);
        tvContLength = findViewById(R.id.tvCopierContLength);
        tvContSD = findViewById(R.id.tvCopierContStartDate);
        tvContED = findViewById(R.id.tvCopierContExpiryDate);
        tvContMP = findViewById(R.id.tvCopierContMonthlyPayment);
        tvContFP = findViewById(R.id.tvCopierContFinalPayment);

        btnEdit = findViewById(R.id.btnCopierEdit);
        btnRecont = findViewById(R.id.btnCopierRecont);
        btnDelete = findViewById(R.id.btnCopierDelete);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnRecont.setOnClickListener(this);

        etAge = findViewById(R.id.etCopierAge);
        etModel = findViewById(R.id.etCopierModel);
        etContLength = findViewById(R.id.etCopierContLength);
        etContSD = findViewById(R.id.etCopierContStartDate);
        etContED = findViewById(R.id.etCopierContExpiryDate);
        etContMP = findViewById(R.id.etCopierContMonthlyPayment);
        etContFP = findViewById(R.id.etCopierContFinalPayment);

        etContSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(etContSD)) {
                    getDate(etContSD);
                }
            }
        });
        etContSD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(etContSD.getVisibility() == View.VISIBLE) {
                        getDate(etContSD);
                    }
                }
            }
        });

        etContED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(etContED)) {
                    getDate(etContSD);
                }
            }
        });
        etContED.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(etContED.getVisibility() == View.VISIBLE) {
                        getDate(etContED);
                    }
                }
            }
        });

        textWatcherContLength = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!etContSD.getText().toString().equals("")){
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
                                        try{
                                            String date = calculateExpiryDate(etContSD.getText().toString(), Integer.parseInt(etContLength.getText().toString()));
                                            etContLength.removeTextChangedListener(textWatcherContLength);
                                            etContED.setText(date);
                                        }catch(NumberFormatException ex){
                                        }

                                    }
                                }
                            });
                        }
                    }).start();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                etContLength.addTextChangedListener(textWatcherContLength);
            }
        };

        textWatcherContSD = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!etContSD.getText().toString().equals("")){
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
                                        try{
                                            String date = calculateExpiryDate(etContSD.getText().toString(), Integer.parseInt(etContLength.getText().toString()));
                                            etContSD.removeTextChangedListener(textWatcherContSD);
                                            etContED.setText(date);
                                        }catch(NumberFormatException ex){
                                        }
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                etContSD.addTextChangedListener(textWatcherContSD);
            }
        };

        //calculate expiry date
        etContLength.addTextChangedListener(textWatcherContLength);
        etContSD.addTextChangedListener(textWatcherContSD);

        etAge.setVisibility(View.GONE);
        etModel.setVisibility(View.GONE);
        etContLength.setVisibility(View.GONE);
        etContSD.setVisibility(View.GONE);
        etContED.setVisibility(View.GONE);
        etContMP.setVisibility(View.GONE);
        etContFP.setVisibility(View.GONE);

        spBrand = findViewById(R.id.spinnerCopierBrand);
        cbPrint = findViewById(R.id.cbPrint);
        cbScan = findViewById(R.id.cbScan);
        cbPaperJam = findViewById(R.id.cbPaperJam);
        rgRoP = findViewById(R.id.rgRorP);
        rbRented = findViewById(R.id.rbRented);
        rbPurchased = findViewById(R.id.rbPurchased);
        rbUnsure = findViewById(R.id.rbUnsure);

        spBrand.setVisibility(View.GONE);
        cbPrint.setVisibility(View.GONE);
        cbScan.setVisibility(View.GONE);
        cbPaperJam.setVisibility(View.GONE);
        rgRoP.setVisibility(View.GONE);
        rbRented.setVisibility(View.GONE);
        rbUnsure.setVisibility(View.GONE);
        rbPurchased.setVisibility(View.GONE);

        retrieveCopierDetails();
    }

    public void retrieveCopierDetails(){

        databaseReference.child("Copier").orderByChild("company_id").equalTo(companyid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Copier copier = dataSnapshot.getValue(Copier.class);
                if(copier!=null){
                    if(copier.getBrand().equals(brand) && copier.getModel().equals(model)){

                        copierDbKey = dataSnapshot.getKey();

                        tvBrand.setText(tvBrand.getText().toString().concat(" " + copier.getBrand()));
                        tvModel.setText(tvModel.getText().toString().concat(" " + copier.getModel()));
                        tvAge.setText(tvAge.getText().toString().concat(" " + copier.getAge()));
                        tvProblem.setText(tvProblem.getText().toString().concat(" " + copier.getProblemfaced()));
                        tvRoP.setText(tvRoP.getText().toString().concat(" " + copier.getRentedOrPurchased()));
                        tvContLength.setText(tvContLength.getText().toString().concat(" " + copier.getContractLength()));
                        tvContSD.setText(tvContSD.getText().toString().concat(" " + copier.getContractStartDate()));
                        tvContED.setText(tvContED.getText().toString().concat(" " + copier.getContractExpiryDate()));
                        tvContMP.setText(tvContMP.getText().toString().concat(" " + copier.getContractMonthlyPayment()));
                        tvContFP.setText(tvContFP.getText().toString().concat(" " + copier.getContractFinalPayment()));

                        etAge.setText(copier.getAge());
                        etContLength.setText(String.valueOf(copier.getContractLength()));
                        etContSD.setText(copier.getContractStartDate());
                        etContED.setText(copier.getContractExpiryDate());
                        etContMP.setText(copier.getContractMonthlyPayment());
                        etContFP.setText(copier.getContractFinalPayment());

                        spBrand.setVisibility(View.GONE);
                        cbPrint.setVisibility(View.GONE);
                        cbScan.setVisibility(View.GONE);
                        cbPaperJam.setVisibility(View.GONE);
                        rgRoP.setVisibility(View.GONE);
                        rbRented.setVisibility(View.GONE);
                        rbUnsure.setVisibility(View.GONE);
                        rbPurchased.setVisibility(View.GONE);

                        List<String> lines = Arrays.asList(getResources().getStringArray(R.array.copierBrand));
                        String copierBrand = copier.getBrand();
                        for(int i=0; i < lines.size(); i++){
                            if(lines.get(i).equals(copierBrand)){
                                spBrand.setSelection(i);
                            }
                        }

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

                        if(copier.getRentedOrPurchased().equals("Rented")){
                            rbRented.setChecked(true);
                        }
                        else if(copier.getRentedOrPurchased().equals("Purchased")){
                            rbPurchased.setChecked(true);
                        }
                        else if(copier.getRentedOrPurchased().equals("Unsure")){
                            rbUnsure.setChecked(true);
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

    @Override
    public void onClick(View view) {
        if(view.equals(btnEdit)){
            if(btnEdit.getText().toString().equals("Edit")){
                setCopierEditable();
                btnEdit.setText("Save");
                btnRecont.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
            }
            else{
                saveCopier();

            }
        }
        else if(view.equals(btnRecont)){
            recontract();
        }
        else if(view.equals(btnDelete)){
            removeCopier();
        }
    }

    public void recontract(){

        etContSD.requestFocus();
        Toast.makeText(getApplicationContext(), "Input Start Date of Recontract!", Toast.LENGTH_SHORT).show();


        if(etContSD.getVisibility() == View.GONE){
            etContSD.setVisibility(View.VISIBLE);
            etContLength.setVisibility(View.VISIBLE);
            etContED.setVisibility(View.VISIBLE);
        }

        textWatcherRecontract = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            Thread.sleep(800);
                        }
                        catch (Exception e) { } // Just catch the InterruptedException
                        // Now we use the Handler to post back to the main thread
                        handler.post(new Runnable() {
                            public void run() {
                                // Set the View's visibility back on the main UI Thread
                                if(!etContED.getText().equals("")) {
                                    try{
                                        etContED.removeTextChangedListener(textWatcherRecontract);
                                        etContSD.removeTextChangedListener(textWatcherContSD);
                                        etContLength.removeTextChangedListener(textWatcherContLength);
                                        saveCopier();
                                    }catch(NumberFormatException ex){
                                    }
                                }
                            }
                        });
                    }
                }).start();
            }
        };

        etContED.addTextChangedListener(textWatcherRecontract);
    }

    public void setCopierEditable(){

        tvBrand.setText("Copier Brand: ");
        tvModel.setText("Copier Model: ");
        tvAge.setText("Copier Age: ");
        tvProblem.setText("Problem Face by Copier: ");
        tvRoP.setText("Copier Rented or Purchased: ");
        tvContSD.setText("Copier Contract Start Date: ");
        tvContLength.setText("Copier contract Length(MONTHS): ");
        tvContED.setText("Copier Contract End Date: ");
        tvContMP.setText("Copier Contract Monthly Payment: ");
        tvContFP.setText("Copier Contract Final Payment: ");


        spBrand.setVisibility(View.VISIBLE);
        cbPrint.setVisibility(View.VISIBLE);
        cbScan.setVisibility(View.VISIBLE);
        cbPaperJam.setVisibility(View.VISIBLE);
        rgRoP.setVisibility(View.VISIBLE);
        rbRented.setVisibility(View.VISIBLE);
        rbUnsure.setVisibility(View.VISIBLE);
        rbPurchased.setVisibility(View.VISIBLE);

        etAge.setVisibility(View.VISIBLE);
        etModel.setVisibility(View.VISIBLE);
        etContLength.setVisibility(View.VISIBLE);
        etContSD.setVisibility(View.VISIBLE);
        etContED.setVisibility(View.VISIBLE);
        etContMP.setVisibility(View.VISIBLE);
        etContFP.setVisibility(View.VISIBLE);
    }

    public void setCopierNotEditable(){

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
        int rgCheckednumber = rgRoP.getCheckedRadioButtonId();
        View radioButton = rgRoP.findViewById(rgCheckednumber);
        int idx = rgRoP.indexOfChild(radioButton);
        RadioButton rbChecked = (RadioButton)  rgRoP.getChildAt(idx);
        String rbROPtext = rbChecked.getText().toString();

        tvBrand.setText("Copier Brand: " + spBrand.getSelectedItem().toString());
        tvModel.setText("Copier Model: "+ etModel.getText().toString());
        tvAge.setText("Copier Age: "+ etAge.getText().toString());
        tvProblem.setText("Problem Face by Copier: "+ problem);
        tvRoP.setText("Copier Rented Or Purchased: "+ rbROPtext);
        tvContSD.setText("Copier Contract Start Date: "+ etContSD.getText().toString());
        tvContLength.setText("Copier Contract length: "+ etContLength.getText().toString());
        tvContED.setText("Copier Contract End Date: "+ etContED.getText().toString());
        tvContMP.setText("Copier Contract Monthly Payment: "+ etContMP.getText().toString());
        tvContFP.setText("Copier Contract Final Payment: "+ etContFP.getText().toString());

        spBrand.setVisibility(View.GONE);
        etModel.setVisibility(View.GONE);
        etAge.setVisibility(View.GONE);
        cbPrint.setVisibility(View.GONE);
        cbScan.setVisibility(View.GONE);
        cbPaperJam.setVisibility(View.GONE);
        rgRoP.setVisibility(View.GONE);
        etContSD.setVisibility(View.GONE);
        etContLength.setVisibility(View.GONE);
        etContED.setVisibility(View.GONE);
        etContMP.setVisibility(View.GONE);
        etContFP.setVisibility(View.GONE);

    }

    public void removeCopier(){

        databaseReference.child("Copier").child(copierDbKey).removeValue();
    }

    public void saveCopier(){

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
        int rgCheckednumber = rgRoP.getCheckedRadioButtonId();
        View radioButton = rgRoP.findViewById(rgCheckednumber);
        int idx = rgRoP.indexOfChild(radioButton);
        RadioButton rbChecked = (RadioButton)rgRoP.getChildAt(idx);
        String rbROPtext = rbChecked.getText().toString();

        String copierBrand = spBrand.getSelectedItem().toString();
        String copierModel = etModel.getText().toString().trim();
        String copierAge = etAge.getText().toString().trim();
        String copierROP = rbROPtext;
        String copierProblems = problem;
        String copierStartDate = etContSD.getText().toString().trim();
        int copierContLength = Integer.parseInt(etContLength.getText().toString().trim());
        String copierEndDate = etContED.getText().toString().trim();
        String copierMP = etContMP.getText().toString().trim();
        String copierFP = etContFP.getText().toString().trim();

        Copier copier = new Copier(copierBrand, copierModel, copierAge, copierProblems, copierROP, copierContLength, copierStartDate, copierEndDate, copierMP, copierFP, companyid);
        databaseReference.child("Copier").child(copierDbKey).setValue(copier).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Save Successful!", Toast.LENGTH_SHORT).show();
                setCopierNotEditable();
                btnEdit.setText("Edit");
                btnRecont.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Save Failed, Try Again!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getDate(final EditText et){
        int day, month, year, hour, mins;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

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

}
