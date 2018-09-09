package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class ViewMySharedCoWorkingHolder extends RecyclerView.ViewHolder {

    protected TextView tvSharedCoWorkingCompanyName, tvSharedCoWorkingAppointment, tvSharedCoWorkingFollowUp;
    private Context context;

    public ViewMySharedCoWorkingHolder(View itemview, List<SharedCoWorkingCompany> list, Context context){
        super(itemview);
        this.context = context;

        tvSharedCoWorkingCompanyName = itemview.findViewById(R.id.tvSharedCoWorkingCompanyName);
        tvSharedCoWorkingAppointment = itemview.findViewById(R.id.tvSharedCoWorkingAppointment);
        tvSharedCoWorkingFollowUp = itemview.findViewById(R.id.tvSharedCoWorkingFollowUp);

    }
}
