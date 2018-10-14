package com.example.geenie.s9imobilecrm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;

public class CapturePhotoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private Boolean exist = false;
    private int count, counter;
    private String dbkey = "";

    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private ChildEventListener childEventListenerChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);

        recyclerView = findViewById(R.id.rvPhotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        retrieveStoragePath();
        checkIfExist();


        Button btnCapturePhoto = findViewById(R.id.btnLaunchPhotoCaptureLib);
        btnCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(CapturePhotoActivity.this)                         //  Initialize ImagePicker with activity or fragment context
                        .setToolbarColor("#212121")         //  Toolbar color
                        .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                        .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                        .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                        .setProgressBarColor("#4CAF50")     //  ProgressBar color
                        .setBackgroundColor("#212121")      //  Background color
                        .setCameraOnly(false)               //  Camera mode
                        .setMultipleMode(true)              //  Select multiple images or single image
                        .setFolderMode(true)                //  Folder mode
                        .setShowCamera(true)                //  Show camera button
                        .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                        .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                        .setDoneTitle("Done")               //  Done button title
                        .setLimitMessage("You have reached selection limit")    // Selection limit message
                        .setMaxSize(10)                     //  Max images can be selected
                        .setSavePath("ImagePicker")             //  Selected images
                        .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                        .setKeepScreenOn(true)              //  Keep screen on when selecting images
                        .start();                           //  Start ImagePicker


            }
        });

//        Button btnViewPhoto = findViewById(R.id.btnLaunchPhotoViewLib);
//        btnViewPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ii = new Intent(CapturePhotoActivity.this, PreviewLongImageActivity.class);
//                ii.putExtra("imageName", myImagePath);
//                startActivity(ii);
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {

            System.out.println("exist prompt exist status: " + exist);
            System.out.println("exist prompt");

//            //creation of the childeventlistner
            final ChildEventListener childEventListenerChecker = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    exist = true;
                    if(dbkey.equals("")) {
                        dbkey = dataSnapshot.getKey();
                    }
                    System.out.println("exist childevent: " + exist);
                    System.out.println("exist dbkey: " + dbkey);
                    Photo photo = dataSnapshot.getValue(Photo.class);
                    count = Integer.parseInt(photo.getCount());
                    counter = count;
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            databaseReference.child("Photo").child("-LKkPuTjiV5s7M_4zTkl").addChildEventListener(childEventListenerChecker);
            databaseReference.child("Photo").child("-LKkPuTjiV5s7M_4zTkl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(exist){
                        ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                        for (int i = 0; i < images.size(); i++) {
                            storageReference.child("-LKkPuTjiV5s7M_4zTkl".concat("/pic" + counter + ".jpg")).putFile(Uri.fromFile(new File(images.get(i).getPath())));
                            counter++;
                        }
                        Photo photo2 = new Photo(String.valueOf(images.size() + count));
                        databaseReference.child("Photo").child("-LKkPuTjiV5s7M_4zTkl").child(dbkey).setValue(photo2);
                        System.out.println("exist pushing child: in SingleEvent");
                        System.out.println("exist singleValue: " + exist);

                        final Handler handler = new Handler();
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                }
                                catch (Exception e) { }
                                handler.post(new Runnable() {
                                    public void run() {
                                        retrieveStoragePath();
                                    }
                                });
                            }
                        }).start();
                    }
                    else{
                        databaseReference.child("Photo").child("-LKkPuTjiV5s7M_4zTkl").removeEventListener(childEventListenerChecker);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(!exist){
                System.out.println("exist: " + exist);

                ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                for (int i = 0; i < images.size(); i++) {
                    storageReference.child("-LKkPuTjiV5s7M_4zTkl".concat("/pic" + i + ".jpg")).putFile(Uri.fromFile(new File(images.get(i).getPath())));
                }

                Photo photo2 = new Photo(String.valueOf(images.size()));
                databaseReference.child("Photo").child("-LKkPuTjiV5s7M_4zTkl").push().setValue(photo2);
                System.out.println("exist pushing child: in (!exist checker)");

                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        }
                        catch (Exception e) { }
                        handler.post(new Runnable() {
                            public void run() {
                                retrieveStoragePath();
                            }
                        });
                    }
                }).start();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void retrieveStoragePath(){
        databaseReference.child("Photo").child("-LKkPuTjiV5s7M_4zTkl").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                System.out.println("exist storage count: " + photo.getCount());
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < Integer.parseInt(photo.getCount()); i++) {
                    arrayList.add(String.valueOf(i));
                }
                getPhotos(arrayList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPhotos(ArrayList<String> list){

//        PhotoAdapter photoAdapter = new PhotoAdapter(list,CapturePhotoActivity.this);
//        recyclerView.setAdapter(photoAdapter);
    }

    private void checkIfExist(){

        childEventListenerChecker = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                exist = true;
                if(dbkey.equals("")) {
                    dbkey = dataSnapshot.getKey();
                }
                System.out.println("exist childevent: " + exist);
                System.out.println("exist dbkey: " + dbkey);
                Photo photo = dataSnapshot.getValue(Photo.class);
                count = Integer.parseInt(photo.getCount());
                counter = count;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child("Photo").child("-LKkPuTjiV5s7M_4zTkl").addChildEventListener(childEventListenerChecker);
    }
}
