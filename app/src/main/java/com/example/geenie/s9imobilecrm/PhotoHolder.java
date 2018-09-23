package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.com.felix.imagezoom.ImageZoom;

public class PhotoHolder extends RecyclerView.ViewHolder {

//    protected ImageView imageView;
    protected ImageZoom imageViewZoom;
    private Context context;



    public PhotoHolder(View itemview, Context context){
        super(itemview);
        this.context = context;

//        imageView = itemView.findViewById(R.id.ivPhoto);
        imageViewZoom = itemView.findViewById(R.id.ivPhoto);

    }

}
