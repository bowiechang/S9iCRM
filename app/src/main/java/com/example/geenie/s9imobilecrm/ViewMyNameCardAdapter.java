package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 20/7/17.
 */

public class ViewMyNameCardAdapter extends RecyclerView.Adapter<ViewMyNameCardHolder>{

    protected List<Company> list;
    protected Context context;

    public ViewMyNameCardAdapter(Context context, List<Company> list, List<Company> list2){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewMyNameCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewMyNameCardHolder viewMyNameCardHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.allshows_list2, parent, false);
        View view = layoutInflater.inflate(R.layout.redesign_namecard_list, parent, false);
        viewMyNameCardHolder = new ViewMyNameCardHolder(view, list, context);

        return viewMyNameCardHolder;
    }

    @Override
    public void onBindViewHolder(final ViewMyNameCardHolder holder, final int position) {

        String[] split = list.get(position).getPriorityLevel().split("\\.");
        String PL = split[1];

        holder.tvCompanyName.setText((list.get(position).getName()));
        holder.tvCompanyAddress.setText(holder.tvCompanyAddress.getText().toString().concat(getLatLng(list.get(position).getPostalCode())));
        holder.tvCompanyLack.setText(restringLacking(list.get(position).getCompanyLackOf()));
        holder.tvCompanyOfficeNum.setText((list.get(position).getOfficeTel()));
        holder.tvCompanyPriorityLevel.setText(holder.tvCompanyPriorityLevel.getText().toString().concat(PL));
        holder.tvNumberOfTimesCalled.setText((list.get(position).getNumberOfTimesCalled() + ""));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMyNameCardDetailedActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", list.get(position).getName());
                extras.putString("postalcode", list.get(position).getPostalCode());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

    }

    public String restringLacking(String lacking){

        List<String> listLacking = Arrays.asList(lacking.split(" "));
        String newLacking = "Lacking of ";
        for (int i = 0; i < listLacking.size(); i++) {
            System.out.println("i value: " + i);
            if(i == 0){
                newLacking = newLacking.concat(listLacking.get(i).toString());
            }
            else if(listLacking.size() - 1 == i){
                System.out.println("i value: " + i);
                // Last iteration
                newLacking = newLacking.concat(" and " + listLacking.get(i));
                System.out.println("newlacking value: " + newLacking);
            }
            else{
                newLacking = newLacking.concat(", " + listLacking.get(i));
                System.out.println("newlacking value: " + newLacking);
            }
        }
        System.out.println("newlacking final value: " + newLacking);
        return newLacking;
    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public String getLatLng(String zip){

        String finaladdress = "";

        final Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                getAddress(address.getLatitude(), address.getLongitude());

                List<Address> address2 = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1);
                finaladdress = address2.get(0).getAddressLine(0);

            }
        } catch (IOException e) {
            // handle exception
        }
        return finaladdress;
    }

    public void getAddress(double lat, double lng){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
