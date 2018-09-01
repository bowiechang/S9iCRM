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

public class ViewMyTaskAdapter extends RecyclerView.Adapter<ViewMyTaskHolder> {

    protected List<Task> list;
    protected Context context;

    public ViewMyTaskAdapter(Context context, List<Task> list){
        this.context = context;
        this.list = list;
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

        holder.tvTaskTitle.setText(holder.tvTaskTitle.getText().toString().concat(list.get(position).getTitle()));
        holder.tvTaskDesc.setText(holder.tvTaskDesc.getText().toString().concat(list.get(position).getDesc()));
        holder.tvTaskCompany.setText(holder.tvTaskCompany.getText().toString().concat(list.get(position).getCompanyName()));
        holder.tvTaskDueDate.setText(holder.tvTaskDueDate.getText().toString().concat(list.get(position).getDueDate()));

        System.out.println("viewmytask: Adapter: " + list.get(position).getTitle() );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMyDetailedTaskActivity.class);
                Bundle extras = new Bundle();
                extras.putString("title", list.get(position).getTitle());
                extras.putString("companyid", list.get(position).getCompanyid());
                extras.putString("companyname", list.get(position).getCompanyName());
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
