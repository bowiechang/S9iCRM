package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class ViewMyTaskHolder extends RecyclerView.ViewHolder {

    protected TextView tvTaskTitle, tvTaskDesc, tvTaskCompany, tvTaskDueDate;
    private Context context;

    public ViewMyTaskHolder(View itemview, List<Task> list, Context context){
        super(itemview);
        this.context = context;

        tvTaskTitle = itemview.findViewById(R.id.tvTaskTitle);
        tvTaskDesc = itemview.findViewById(R.id.tvTaskDesc);
        tvTaskCompany = itemview.findViewById(R.id.tvTaskCompany);
        tvTaskDueDate = itemview.findViewById(R.id.tvTaskDueDate);
    }
}
