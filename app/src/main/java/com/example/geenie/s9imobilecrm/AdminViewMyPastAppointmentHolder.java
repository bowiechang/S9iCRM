package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class AdminViewMyPastAppointmentHolder extends RecyclerView.ViewHolder {

    protected TextView tvApptCompanyName, tvApptDateTime, tvApptLocationAddress, tvApptContact, tvApptComment, tvCreatedBy;
    private Context context;

    public AdminViewMyPastAppointmentHolder(View itemview, List<Appointment> list, Context context){
        super(itemview);
        this.context = context;

        tvApptCompanyName = itemview.findViewById(R.id.tvApptCompanyName);
        tvApptDateTime = itemview.findViewById(R.id.tvApptDateTime);
        tvApptLocationAddress = itemview.findViewById(R.id.tvApptLocationAddress);
        tvApptContact = itemview.findViewById(R.id.tvApptContact);
        tvApptComment = itemview.findViewById(R.id.tvApptComment);
        tvCreatedBy = itemview.findViewById(R.id.tvCreatedBy);

    }
}
