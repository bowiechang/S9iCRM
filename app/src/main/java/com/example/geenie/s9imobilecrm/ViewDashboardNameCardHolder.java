package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class ViewDashboardNameCardHolder extends RecyclerView.ViewHolder {

    protected TextView tvCompanyName, tvCompanyAddress, tvCompanyOfficeNum, tvCompanyPL;
    private Context context;

    public ViewDashboardNameCardHolder(View itemview, List<Company> list, Context context){
        super(itemview);
        this.context = context;

        tvCompanyName = itemview.findViewById(R.id.tvCompanyName);
        tvCompanyAddress = itemview.findViewById(R.id.tvCompanyAddress);
        tvCompanyOfficeNum = itemview.findViewById(R.id.tvCompanyOfficeNumber);
        tvCompanyPL = itemview.findViewById(R.id.tvCompanyPriorityLevel);

    }
}
