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

import java.util.List;

/**
 * Created by admin on 20/7/17.
 */

public class ViewMyFollowUpAdapter extends RecyclerView.Adapter<ViewMyFollowUpHolder> {

    protected List<FollowUp> list;
    protected Context context;

    private String cname = "";

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ViewMyFollowUpAdapter(Context context, List<FollowUp> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewMyFollowUpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewMyFollowUpHolder viewMyFollowUpHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.followup_list, parent, false);
        viewMyFollowUpHolder = new ViewMyFollowUpHolder(view, list, context);

        return viewMyFollowUpHolder;
    }

    @Override
    public void onBindViewHolder(final ViewMyFollowUpHolder holder, final int position) {


        holder.tvFollowUpDate.setText("By ".concat(list.get(position).getFollowupDueDate()));
        holder.tvFollowUpName.setText((cname));

        //capitalise first letter
        String status = list.get(position).getFollowUpStatus();
        String type = list.get(position).getTypeOfFollowup();
        String newstatus = status.substring(0, 1).toUpperCase()+status.substring(1);
        String newtype = type.substring(0, 1).toUpperCase()+type.substring(1);

        holder.tvFollowUpStatus.setText((newstatus));
        holder.tvFollowUpType.setText((newtype));

        databaseReference.child("Company").orderByKey().equalTo(list.get(position).getCompanyid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                if(company!=null){
                    cname = company.getName();
                    holder.tvFollowUpName.setText((holder.tvFollowUpName.getText().toString().concat(cname)));
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMyDetailedFollowUpActivity.class);
                Bundle extras = new Bundle();
                extras.putString("companyid", list.get(position).getCompanyid());
                extras.putString("companyname", cname);
                extras.putString("date", list.get(position).getFollowupDueDate());
                extras.putString("type", list.get(position).getTypeOfFollowup());
                extras.putString("status", list.get(position).getFollowUpStatus());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

//    public void getCompanyName(final String companyid){
//        databaseReference.child("Company").orderByKey().equalTo(companyid).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Company company = dataSnapshot.getValue(Company.class);
//                if(company!=null){
//                    cname = company.getName();
//                    System.out.println("cname: " + cname);
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
