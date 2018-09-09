package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class PhotoHolder extends RecyclerView.ViewHolder {

    protected ImageView imageView;
    private Context context;

    public PhotoHolder(View itemview, Context context){
        super(itemview);
        this.context = context;

        imageView = itemView.findViewById(R.id.ivPhoto);
    }

}
