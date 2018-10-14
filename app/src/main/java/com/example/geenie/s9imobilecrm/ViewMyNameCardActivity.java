package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewMyNameCardActivity extends AppCompatActivity implements View.OnClickListener {

    private Company company;
    private RecyclerView recyclerView;
    private TextView tvAddNameCard;
    private SearchView sv;
    private ArrayList<Company> list;
    private ArrayList<Company> listUntounched;
//    private ArrayList<Company> listClone;
    ArrayList <Company> arraylistcurrentfilter;

    private ViewMyNameCardAdapter viewMyNameCardsAdapter;

    private Spinner spinnerSort1, spinnerSort2, spinnerFilter;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Company");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_name_card);

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

        sv = findViewById(R.id.mSearch);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                spinnerFilter.setSelection(0);
                spinnerSort1.setSelection(0);
                spinnerSort2.setSelection(0);
                Toast.makeText(getApplicationContext(), "Querying: " + query, Toast.LENGTH_SHORT).show();

                ArrayList<Company>listClone = new ArrayList<>();
                ArrayList<Company>listSubmit = new ArrayList<>();
                listSubmit.clear();
                listClone.addAll(list);

                if(!query.equals("")){
                    for (int i = 0; i < listClone.size(); i++) {
                        Company company = listClone.get(i);
                        if (company.getName().toLowerCase().contains(query.toLowerCase())) {
                            listSubmit.add(company);
                        }
                    }
                    getMyNameCardList(listSubmit);
                }else{
                    getMyNameCardList(list);
                }
                return false;
            }
        });

        tvAddNameCard = findViewById(R.id.addNameCard);
        tvAddNameCard.setOnClickListener(this);

        recyclerView = findViewById(R.id.rvUpcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        listUntounched = new ArrayList<>();
//        listClone = new ArrayList<>();
        arraylistcurrentfilter = new ArrayList<>();
        read();

        spinnerSort1 = findViewById(R.id.spinnerSort1);
        spinnerSort2 = findViewById(R.id.spinnerSort2);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        spinnerSort1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(arraylistcurrentfilter.isEmpty()){
                    //USE LIST
                    switch(i) {
                        case 0:
                            getMyNameCardList(listUntounched);
                            break;
                        //calls highest to lowest
                        case 1:
                            sortby("callhighlow", list);
                            break;
                        //calls lowest to highest
                        case 2:
                            sortby("calllowhigh", list);
                            break;
                        //PL urgent to normal
                        case 3:
                            sortby("plhighlow", list);
                            break;
                        //PL normal to urgent
                        case 4:
                            sortby("pllowhigh", list);
                            break;
                    }
                }
                else{
                    switch(i) {
                        case 0:
                            getMyNameCardList(listUntounched);
                            break;
                        //calls highest to lowest
                        case 1:
                            sortby("callhighlow", arraylistcurrentfilter);
                            break;
                        //calls lowest to highest
                        case 2:
                            sortby("calllowhigh", arraylistcurrentfilter);
                            break;
                        //PL urgent to normal
                        case 3:
                            sortby("plhighlow", arraylistcurrentfilter);
                            break;
                        //PL normal to urgent
                        case 4:
                            sortby("pllowhigh", arraylistcurrentfilter);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSort2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(arraylistcurrentfilter.isEmpty()){
                    //USE LIST
                    switch(i) {
                        case 0:
                            getMyNameCardList(listUntounched);
                            break;
                        //calls highest to lowest
                        case 1:
                            sortby("callhighlow", list);
                            break;
                        //calls lowest to highest
                        case 2:
                            sortby("calllowhigh", list);
                            break;
                    }
                }
                else{
                    switch(i) {
                        case 0:
                            getMyNameCardList(listUntounched);
                            break;
                        //calls highest to lowest
                        case 1:
                            sortby("callhighlow", arraylistcurrentfilter);
                            break;
                        //calls lowest to highest
                        case 2:
                            sortby("calllowhigh", arraylistcurrentfilter);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i) {
                    case 0:
                        getMyNameCardList(listUntounched);
                        spinnerSort1.setVisibility(View.VISIBLE);
                        spinnerSort2.setVisibility(View.GONE);
                        break;
                    case 1:
                        filterWithClone("Copier");
                        spinnerSort1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        filterWithClone("Scanner");
                        spinnerSort1.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        filterWithClone("Shredder");
                        spinnerSort1.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        filterWithClone("a.Urgent");
                        spinnerSort1.setVisibility(View.GONE);
                        spinnerSort2.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        filterWithClone("b.Follow Up");
                        spinnerSort1.setVisibility(View.GONE);
                        spinnerSort2.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        filterWithClone("c.Normal");
                        spinnerSort1.setVisibility(View.GONE);
                        spinnerSort2.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    public void read(){

        list.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                company = dataSnapshot.getValue(Company.class);
                if(company!=null){
                    if(company.getCreateBy().equals(uid)){
                        list.add(company);
                        getMyNameCardList(list);
                        System.out.println("reading...");
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUntounched.addAll(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMyNameCardList(ArrayList list){

        System.out.println("getmyshowslist list size: " + list.size());

        viewMyNameCardsAdapter = new ViewMyNameCardAdapter(ViewMyNameCardActivity.this, list, list);
        recyclerView.setAdapter(viewMyNameCardsAdapter);

//        if(listClone.isEmpty()) {
//            cloneList();
//        }
    }

    public void filterWithClone(String filterkey){

//        cloneList();
        System.out.println("list size: " + list.size());
        arraylistcurrentfilter.clear();

        //filter for lacking
        if(filterkey.equals("Copier") || filterkey.equals("Scanner") || filterkey.equals("Shredder")) {
            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                if (company.getCompanyLackOf().contains(filterkey)){
                    arraylistcurrentfilter.add(company);
                }
            }
            getMyNameCardList(arraylistcurrentfilter);
        }

        //filter for priority levels
        else if(filterkey.equals("a.Urgent")) {
            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                if (company.getPriorityLevel().equals(filterkey)){
                    arraylistcurrentfilter.add(company);
                }
            }
            getMyNameCardList(arraylistcurrentfilter);
        }
        else if(filterkey.equals("b.Follow Up")) {
            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                if (company.getPriorityLevel().equals(filterkey)){
                    arraylistcurrentfilter.add(company);
                }
            }
            getMyNameCardList(arraylistcurrentfilter);
        }
        else if(filterkey.equals("c.Normal")) {
            for (int i = 0; i < list.size(); i++) {
                Company company = list.get(i);
                if (company.getPriorityLevel().equals(filterkey)){
                    arraylistcurrentfilter.add(company);
                }
            }
            getMyNameCardList(arraylistcurrentfilter);
        }
    }

    public void sortby(String sorterkey, ArrayList<Company> list){

        if(sorterkey.equals("callhighlow")) {
            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company companyob1, Company companyob2) {
                    return Integer.valueOf(companyob2.getNumberOfTimesCalled()).compareTo(companyob1.getNumberOfTimesCalled()); // To compare integer values
                }
            });
            getMyNameCardList(list);
        }
        else if(sorterkey.equals("calllowhigh")) {
            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company companyob1, Company companyob2) {
                return Integer.valueOf(companyob1.getNumberOfTimesCalled()).compareTo(companyob2.getNumberOfTimesCalled()); // To compare integer values
                }
            });
            getMyNameCardList(list);
        }
        else if(sorterkey.equals("pllowhigh")) {
            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company companyob1, Company companyob2) {
                     return companyob2.getPriorityLevel().compareToIgnoreCase(companyob1.getPriorityLevel()); // To compare string values
                }
            });
            getMyNameCardList(list);
        }
        else if(sorterkey.equals("plhighlow")) {
            Collections.sort(list, new Comparator<Company>() {
                @Override
                public int compare(Company companyob1, Company companyob2) {
                    return companyob1.getPriorityLevel().compareToIgnoreCase(companyob2.getPriorityLevel()); // To compare string values
                }
            });
            getMyNameCardList(list);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void cloneList() {
//        if(!listClone.isEmpty()) {
//            listClone.clear();
//        }
//        listClone = (ArrayList<Company>)list.clone();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(tvAddNameCard)){
            Intent i = new Intent(ViewMyNameCardActivity.this, AddNameCardActivity.class);
            ViewMyNameCardActivity.this.startActivity(i);
        }
    }
}
