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

public class ViewMyCopierAdapter extends RecyclerView.Adapter<ViewMyCopierHolder> {

    protected List<Copier> list;
    protected Context context;
    private String cname;

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ViewMyCopierAdapter(Context context, List<Copier> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewMyCopierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewMyCopierHolder viewMyCopierHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.copier_list, parent, false);
        viewMyCopierHolder = new ViewMyCopierHolder(view, list, context);

        return viewMyCopierHolder;
    }

    @Override
    public void onBindViewHolder(final ViewMyCopierHolder holder, final int position) {

        holder.tvCopierBrandAndModel.setText((list.get(position).getBrand().concat(" ") + list.get(position).getModel()));
        holder.tvCopierAge.setText(holder.tvCopierAge.getText().toString().concat(list.get(position).getAge()));
        holder.tvCopierProblem.setText(holder.tvCopierProblem.getText().toString().concat(list.get(position).getProblemfaced()));
        holder.tvCopierContExpiryDate.setText(holder.tvCopierContExpiryDate.getText().toString().concat(list.get(position).getContractExpiryDate()));

        databaseReference.child("Company").orderByKey().equalTo(list.get(position).getCompany_id()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Company company = dataSnapshot.getValue(Company.class);
                if(company!=null){
                    cname = company.getName();
                    holder.tvCopierCompanyName.setText(holder.tvCopierCompanyName.getText().toString().concat(" " + cname));
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
