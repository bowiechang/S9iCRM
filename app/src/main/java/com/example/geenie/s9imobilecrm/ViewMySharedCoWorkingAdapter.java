package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 20/7/17.
 */

public class ViewMySharedCoWorkingAdapter extends RecyclerView.Adapter<ViewMySharedCoWorkingHolder>{

    protected List<SharedCoWorkingCompany> list;
    protected Context context;

    ArrayList<Appointment> appointmentList = new ArrayList<>();
    ArrayList<FollowUp> followUpList = new ArrayList<>();

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ViewMySharedCoWorkingAdapter(Context context, List<SharedCoWorkingCompany> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewMySharedCoWorkingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewMySharedCoWorkingHolder viewMySharedCoWorkingHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sharedcoworking_list, parent, false);
        viewMySharedCoWorkingHolder = new ViewMySharedCoWorkingHolder(view, list, context);

        return viewMySharedCoWorkingHolder;
    }

    @Override
    public void onBindViewHolder(final ViewMySharedCoWorkingHolder holder, final int position) {

        //company
        databaseReference.child("Company").orderByKey().equalTo(list.get(position).getCompanyid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                holder.tvSharedCoWorkingCompanyName.setText(company.getName());
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

        //appt
        databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                if(appointment.getCompany_id().equals(list.get(position).getCompanyid())){
                    appointmentList.add(appointment);
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

                //setting of latestapptdate
                String latestApptDate;
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                if(appointmentList.size() == 0){
                    holder.tvSharedCoWorkingAppointment.setText(holder.tvSharedCoWorkingAppointment.getText().toString().concat(" No appointments found"));
                }
                else{
                    for (int i = 0; i < appointmentList.size(); i++) {

                        Appointment appointment = appointmentList.get(i);

                        String receivedDate = appointment.getDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateAppointment1 = null;
                        Date dateAppointment2 = null;

                        try {
                            dateAppointment1 = sdf.parse(appointment.getDate());

                            if (appointmentList.size() > i+1) {

                                Appointment appointment1 = appointmentList.get(i + 1);
                                dateAppointment2 = sdf.parse(appointment1.getDate());
                            } else {
                                latestApptDate = appointment.getDate();
                                holder.tvSharedCoWorkingAppointment.setText(holder.tvSharedCoWorkingAppointment.getText().toString().concat(" " + latestApptDate));
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (dateAppointment1.after(dateAppointment2)) {
                            System.out.println("date1: " + dateAppointment1);
                            System.out.println("date2: " + dateAppointment2);
                            latestApptDate = appointment.getDate();
                            holder.tvSharedCoWorkingAppointment.setText(holder.tvSharedCoWorkingAppointment.getText().toString().concat(" " + latestApptDate));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //followup
        //appt
        databaseReference.child("FollowUp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FollowUp followUp = dataSnapshot.getValue(FollowUp.class);
                if(followUp.getCompanyid().equals(list.get(position).getCompanyid())){
                    followUpList.add(followUp);
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

                //setting of latestFUdate
                String latestFollowUpDateDate;
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                if(followUpList.size() == 0){
                    holder.tvSharedCoWorkingFollowUp.setText(holder.tvSharedCoWorkingFollowUp.getText().toString().concat(" No follow ups found"));
                }
                else{

                    for (int i = 0; i < followUpList.size(); i++) {

                        FollowUp followUp = followUpList.get(i);

                        String receivedDate = followUp.getFollowupDueDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateFollowUp1 = null;
                        Date dateFollowUp2 = null;

                        try {
                            dateFollowUp1 = sdf.parse(followUp.getFollowupDueDate());

                            if (followUpList.size() > i+1) {

                                FollowUp followUp1 = followUpList.get(i + 1);
                                dateFollowUp2 = sdf.parse(followUp1.getFollowupDueDate());
                            } else {
                                latestFollowUpDateDate = followUp.getFollowupDueDate();
                                holder.tvSharedCoWorkingFollowUp.setText(holder.tvSharedCoWorkingFollowUp.getText().toString().concat(" " + latestFollowUpDateDate));
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (dateFollowUp1.after(dateFollowUp2)) {
                            latestFollowUpDateDate = followUp.getFollowupDueDate();
                            holder.tvSharedCoWorkingFollowUp.setText(holder.tvSharedCoWorkingFollowUp.getText().toString().concat(" " + latestFollowUpDateDate));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewDetailedSharedCoWorkingCompanyActivity.class);
                Bundle extras = new Bundle();
                extras.putString("companyid", list.get(position).getCompanyid());
                intent.putExtras(extras);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
