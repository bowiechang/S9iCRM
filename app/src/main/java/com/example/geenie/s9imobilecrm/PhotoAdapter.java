package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 20/7/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private ArrayList<String> list;
    protected Context context;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Bitmap bitmap;
    private String dbkey;

    public PhotoAdapter(ArrayList<String> list, Context context, String dbkey){
        this.context = context;
        this.list = list;
        this.dbkey = dbkey;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoHolder photoHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.photo_list, parent, false);
        photoHolder = new PhotoHolder(view, context);

        System.out.println("bitmap insert: onCREATEviewholder" );


        return photoHolder;
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {

        System.out.println("bitmap insert: onBINDviewholder" );


        final StorageReference pathrefIVZoom = storageReference.child(dbkey+"/pic" + position + ".jpg");
        pathrefIVZoom.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    final File localFile = File.createTempFile("Images", "jpg");
                    pathrefIVZoom.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.imageViewZoom.setImageBitmap(getResizedBitmap(bitmap, 800, 1100));
                            System.out.println("bitmap insert: true" );
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        final StorageReference pathref = storageReference.child("-LKkPuTjiV5s7M_4zTkl"+"/pic" + position + ".jpg");
//        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//                System.out.println("photo:: into rv");
//                // Load the image using Glide
//                Glide.with(context)
//                        .asBitmap()
//                        .load(uri.toString())
////                        .apply(new RequestOptions().override(4000,500))
//                        .into(holder.imageViewZoom);
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("failed uri", "failed: " + e.toString());
//            }
//        });
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
