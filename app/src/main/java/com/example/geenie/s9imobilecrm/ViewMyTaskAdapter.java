package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 20/7/17.
 */

public class ViewMyTaskAdapter extends RecyclerView.Adapter<ViewMyTaskHolder> {

    protected List<Task> list;
    protected Context context;
    protected String key;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ViewMyTaskAdapter(Context context, List<Task> list, String key){
        this.context = context;
        this.list = list;
        this.key = key;
    }

    @Override
    public ViewMyTaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewMyTaskHolder viewMyTaskHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_list, parent, false);
        viewMyTaskHolder = new ViewMyTaskHolder(view, list, context);

        return viewMyTaskHolder;
    }

    @Override
    public void onBindViewHolder(final ViewMyTaskHolder holder, final int position) {

        if(key.equalsIgnoreCase("normal")){
            holder.tvTaskAssignedTo.setVisibility(View.GONE);
        }

        holder.tvTaskTitle.setText((list.get(position).getTitle()));
        holder.tvTaskDesc.setText((list.get(position).getDesc()));
        holder.tvTaskCompany.setText((list.get(position).getCompanyName()));
        holder.tvTaskDueDate.setText("By ".concat(list.get(position).getDueDate()));

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = df.format(c.getTime());

        String receivedDate = list.get(position).getDueDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateAppointment = null;
        Date dateToday = null;

        try {
            dateToday = sdf.parse(todayDate);
            dateAppointment = sdf.parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateToday.after(dateAppointment)) {
            holder.tvTaskDueDate.setTextColor(Color.RED);
        }

        System.out.println("viewmytask: Adapter: " + list.get(position).getTitle() );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMyDetailedTaskActivity.class);
                Bundle extras = new Bundle();
                extras.putString("title", list.get(position).getTitle());
                extras.putString("companyid", list.get(position).getCompanyid());
                extras.putString("companyname", list.get(position).getCompanyName());
                extras.putString("key", key);
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equalsIgnoreCase(list.get(position).getAssigned_uid())){
                    User u = dataSnapshot.getValue(User.class);
                    holder.tvTaskAssignedTo.setText("Assigned to: " + u.getName());
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
    public int getItemCount() {
        return this.list.size();
    }
}
