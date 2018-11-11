package com.example.geenie.s9imobilecrm;

//public class ViewMyNameCardHolder extends RecyclerView.ViewHolder {
//
//    protected TextView tvCompanyName, tvCompanyAddress, tvCompanyOfficeNum, tvCompanyIndustry,
//            tvCompanyLack, tvCompanyPriorityLevel, tvNumberOfTimesCalled;
//    private Context context;
//
//    public ViewMyNameCardHolder(View itemview, List<Company> list, Context context){
//        super(itemview);
//        this.context = context;
//
//        tvCompanyName = itemview.findViewById(R.id.tvCompanyName);
//        tvCompanyAddress = itemview.findViewById(R.id.tvCompanyAddress);
//        tvCompanyOfficeNum = itemview.findViewById(R.id.tvCompanyOfficeNumber);
//        tvCompanyIndustry = itemview.findViewById(R.id.tvCompanyIndustry);
//        tvCompanyLack = itemview.findViewById(R.id.tvCompanyLack);
//        tvCompanyPriorityLevel = itemview.findViewById(R.id.tvCompanyPriorityLevel);
//        tvNumberOfTimesCalled = itemview.findViewById(R.id.tvNumberOfTimesCalled);
//
//    }
//}


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ViewMyNameCardHolder extends RecyclerView.ViewHolder {

    protected TextView tvCompanyName, tvCompanyAddress, tvCompanyOfficeNum,
            tvCompanyLack, tvCompanyPriorityLevel, tvNumberOfTimesCalled;
    protected ImageView ivNumberOfTimesCalled;
    private Context context;

    public ViewMyNameCardHolder(View itemview, List<Company> list, Context context){
        super(itemview);
        this.context = context;

        tvCompanyName = itemview.findViewById(R.id.tvCompanyName);
        tvCompanyAddress = itemview.findViewById(R.id.tvCompanyAddress);
        tvCompanyOfficeNum = itemview.findViewById(R.id.tvCompanyOfficeNumber);
        tvCompanyLack = itemview.findViewById(R.id.tvCompanyLack);
        tvCompanyPriorityLevel = itemview.findViewById(R.id.tvCompanyPriorityLevel);
        tvNumberOfTimesCalled = itemview.findViewById(R.id.tvNumberOfTimesCalled);
        ivNumberOfTimesCalled = itemview.findViewById(R.id.ivNumberCall);

    }
}