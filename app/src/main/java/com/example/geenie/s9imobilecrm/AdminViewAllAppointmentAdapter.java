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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminViewAllAppointmentAdapter extends RecyclerView.Adapter<AdminViewAllAppointmentHolder> {

    protected List<Appointment> list;
    protected Context context;


    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

    private HashMap<String, User> userList = new HashMap<>();
    private User user;
    private String name;

    public AdminViewAllAppointmentAdapter(Context context, List<Appointment> list){
        this.context = context;
        this.list = list;
//        getName();
    }

    @NonNull
    @Override
    public AdminViewAllAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdminViewAllAppointmentHolder viewMyAppointmentHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_appointment_list, parent, false);
        viewMyAppointmentHolder = new AdminViewAllAppointmentHolder(view, list, context);

        return viewMyAppointmentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminViewAllAppointmentHolder holder, final int position) {

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User u = dataSnapshot.getValue(User.class);
                userList.put(dataSnapshot.getKey(), u);
                System.out.println("userlist.size() at childevent :" + userList.size());
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (int i = 0; i < userList.size(); i++) {
//                    System.out.println("adminviewallappt: key: " + userList.keySet().toArray()[i].toString());
//                    if(userList.keySet().toArray()[i].equals(list.get(position).getCreateby())){
//
//                        Object[] keys = userList.keySet().toArray();
//                        String name = keys[i].toString();
//
//                        holder.tvCreatedBy.setText(name);
//                    }
//                }

                for ( Map.Entry<String, User> entry : userList.entrySet()) {
                    String key = entry.getKey();
                    User user = entry.getValue();

                    if(key.equalsIgnoreCase(list.get(position).getCreateby())){
                        holder.tvCreatedBy.setText("Created By: " + user.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        for (int i = 0; i < userList.size(); i++) {
////            System.out.println("adminviewallappt: key: " + userList.keySet().toArray()[i].toString());
////            if(userList.keySet().toArray()[i].equals(list.get(position).getCreateby())){
////                holder.tvCreatedBy.setText(user.getName());
////            }
////        }

        holder.tvApptCompanyName.setText((list.get(position).getName()));
        holder.tvApptDateTime.setText(list.get(position).getDate() .concat(" at " + list.get(position).getTime()));
        holder.tvApptLocationAddress.setText((list.get(position).getLocationAddress()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMyAppointmentDetailedActivity.class);
                Bundle extras = new Bundle();
                extras.putString("companyname", list.get(position).getName());
                extras.putString("time", list.get(position).getTime());
                extras.putString("date", list.get(position).getDate());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void getName(){

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User u = dataSnapshot.getValue(User.class);
                userList.put(dataSnapshot.getKey(), u);
                System.out.println("userlist.size() at childevent :" + userList.size());
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
}
