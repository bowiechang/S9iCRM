package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by admin on 20/7/17.
 */

public class ViewMyAppointmentAdapter extends RecyclerView.Adapter<ViewMyAppointmentHolder> {

    protected List<Appointment> list;
    protected Context context;

    public ViewMyAppointmentAdapter(Context context, List<Appointment> list){
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewMyAppointmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewMyAppointmentHolder viewMyAppointmentHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.appointment_list, parent, false);
        viewMyAppointmentHolder = new ViewMyAppointmentHolder(view, list, context);

        return viewMyAppointmentHolder;
    }

    @Override
    public void onBindViewHolder(final ViewMyAppointmentHolder holder, final int position) {

        holder.tvApptCompanyName.setText(holder.tvApptCompanyName.getText().toString().concat(list.get(position).getName()));
        holder.tvApptDate.setText(holder.tvApptDate.getText().toString().concat((list.get(position).getDate())));
        holder.tvApptTime.setText(holder.tvApptTime.getText().toString().concat(list.get(position).getTime()));


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
}
