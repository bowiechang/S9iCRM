package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class ViewMyFollowUpHolder extends RecyclerView.ViewHolder {

    protected TextView tvFollowUpDate, tvFollowUpName, tvFollowUpType, tvFollowUpStatus;
    private Context context;

    public ViewMyFollowUpHolder(View itemview, List<FollowUp> list, Context context){
        super(itemview);
        this.context = context;

        tvFollowUpDate = itemview.findViewById(R.id.tvFollowupDate);
        tvFollowUpName = itemview.findViewById(R.id.tvFollowupName);
        tvFollowUpType = itemview.findViewById(R.id.tvFollowupType);
        tvFollowUpStatus = itemview.findViewById(R.id.tvFollowupStatus);

    }
}
