package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class AdminViewAllAppointmentHolder extends RecyclerView.ViewHolder {

    protected TextView tvApptCompanyName, tvApptDateTime, tvApptLocationAddress, tvCreatedBy;
    private Context context;

    public AdminViewAllAppointmentHolder(View itemview, List<Appointment> list, Context context){
        super(itemview);
        this.context = context;

        tvApptCompanyName = itemview.findViewById(R.id.tvApptCompanyName);
        tvApptDateTime = itemview.findViewById(R.id.tvApptDateTime);
        tvApptLocationAddress = itemview.findViewById(R.id.tvApptLocationAddress);
        tvCreatedBy = itemview.findViewById(R.id.tvCreatedBy);

    }
}
