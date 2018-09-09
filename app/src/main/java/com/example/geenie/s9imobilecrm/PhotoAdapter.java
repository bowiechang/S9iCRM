package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by admin on 20/7/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private ArrayList<String> list;
    protected Context context;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public PhotoAdapter(ArrayList<String> list, Context context){
        this.context = context;
        this.list = list;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoHolder photoHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.photo_list, parent, false);
        photoHolder = new PhotoHolder(view, context);

        return photoHolder;
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {

        System.out.println("photo:: position:" + position);
        //reading of image

//        gs://s9imobilecrm.appspot.com/-LKkPuTjiV5s7M_4zTkl/pic0.jpg
//        gs://local-abbey-167209.appspot.com/userid31a15PgEqPgVFVshoyX4Hlxxge23/pic.jpg
//
//        String string = ""

        final StorageReference pathref = storageReference.child("-LKkPuTjiV5s7M_4zTkl"+"/pic" + position + ".jpg");
        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                System.out.println("photo:: into rv");
                // Load the image using Glide
                Glide.with(context)
                        .load(uri.toString())
                        .into(holder.imageView);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("failed uri", "failed: " + e.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
