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

public class ViewDashboardCopierAdapter extends RecyclerView.Adapter<ViewDashboardCopierHolder> {

    protected List<Copier> list;
    protected Context context;
    private String cname;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ViewDashboardCopierAdapter(Context context, List<Copier> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewDashboardCopierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDashboardCopierHolder viewDashboardCopierHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dashboard_copier_list, parent, false);
        viewDashboardCopierHolder = new ViewDashboardCopierHolder(view, list, context);

        return viewDashboardCopierHolder;
    }

    @Override
    public void onBindViewHolder(final ViewDashboardCopierHolder holder, final int position) {

        holder.tvCopierBrandAndModel.setText((list.get(position).getBrand().concat(" ") + list.get(position).getModel()));
        holder.tvCopierContExpiryDate.setText("Contract Expiry on ".concat(list.get(position).getContractExpiryDate()));

        databaseReference.child("Company").orderByKey().equalTo(list.get(position).getCompany_id()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                if(company!=null){
                    cname = company.getName();
                    holder.tvCopierCompanyName.setText(cname);
                    holder.tvCopierOfficeNumber.setText(company.getOfficeTel());
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
                Intent intent = new Intent(context, ViewDetailedCopierActivity.class);
                Bundle extras = new Bundle();
                extras.putString("model", list.get(position).getModel());
                extras.putString("brand", list.get(position).getBrand());
                extras.putString("companyid", list.get(position).getCompany_id());
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
