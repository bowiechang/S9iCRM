package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class ViewMyCopierHolder extends RecyclerView.ViewHolder {

    protected TextView tvCopierBrandAndModel, tvCopierCompanyName, tvCopierAge, tvCopierProblem, tvCopierContExpiryDate;
    private Context context;

    public ViewMyCopierHolder(View itemview, List<Copier> list, Context context){
        super(itemview);
        this.context = context;

        tvCopierBrandAndModel = itemview.findViewById(R.id.tvCopierBrandAndModel);
        tvCopierCompanyName = itemview.findViewById(R.id.tvCopierCompanyName);
        tvCopierAge = itemview.findViewById(R.id.tvCopierAge);
        tvCopierProblem = itemview.findViewById(R.id.tvCopierProblem);
        tvCopierContExpiryDate = itemview.findViewById(R.id.tvCopierContExpiryDate);
    }
}
