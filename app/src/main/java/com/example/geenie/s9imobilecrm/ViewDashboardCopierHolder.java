package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class ViewDashboardCopierHolder extends RecyclerView.ViewHolder {

    protected TextView tvCopierBrandAndModel, tvCopierCompanyName, tvCopierOfficeNumber, tvCopierContExpiryDate;
    private Context context;

    public ViewDashboardCopierHolder(View itemview, List<Copier> list, Context context){
        super(itemview);
        this.context = context;

        tvCopierBrandAndModel = itemview.findViewById(R.id.tvCopierBrandAndModel);
        tvCopierCompanyName = itemview.findViewById(R.id.tvCopierCompanyName);
        tvCopierOfficeNumber = itemview.findViewById(R.id.tvCopierCompanyOfficeNumber);
        tvCopierContExpiryDate = itemview.findViewById(R.id.tvCopierContExpiryDate);
    }
}
