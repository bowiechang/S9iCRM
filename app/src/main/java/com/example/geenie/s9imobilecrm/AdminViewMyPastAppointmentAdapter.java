package com.example.geenie.s9imobilecrm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 20/7/17.
 */

public class AdminViewMyPastAppointmentAdapter extends RecyclerView.Adapter<AdminViewMyPastAppointmentHolder> {

    protected List<Appointment> list;
    protected Context context;

    private ArrayList<String> arrayListContactkey;
    private ArrayList<String> arrayListContactName;
    private HashMap<String, User> userList = new HashMap<>();


    //firebase init
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
    private ChildEventListener childEventListenerContacts;

    public AdminViewMyPastAppointmentAdapter(Context context, List<Appointment> list){
        this.context = context;
        this.list = list;

        arrayListContactkey = new ArrayList<>();
        arrayListContactName = new ArrayList<>();
    }

    @Override
    public AdminViewMyPastAppointmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdminViewMyPastAppointmentHolder adminViewMyPastAppointmentHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_past_appointment_list, parent, false);
        adminViewMyPastAppointmentHolder = new AdminViewMyPastAppointmentHolder(view, list, context);

        return adminViewMyPastAppointmentHolder;
    }

    @Override
    public void onBindViewHolder(final AdminViewMyPastAppointmentHolder holder, final int position) {

        databaseReferenceUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User u = dataSnapshot.getValue(User.class);
                userList.put(dataSnapshot.getKey(), u);
                System.out.println("userlist.size() at childevent :" + userList.size());
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

        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( Map.Entry<String, User> entry : userList.entrySet()) {
                    String key = entry.getKey();
                    User user = entry.getValue();

                    if(key.equalsIgnoreCase(list.get(position).getCreateby())){
                        holder.tvCreatedBy.setText("Created By: " + user.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.tvApptCompanyName.setText((list.get(position).getName()));
        holder.tvApptDateTime.setText(list.get(position).getDate() .concat(" at " + list.get(position).getTime()));
        holder.tvApptLocationAddress.setText((list.get(position).getLocationAddress()));

        if(list.get(position).getComments().equals("")){
            holder.tvApptComment.setText("No comments");
        }
        else {
            holder.tvApptComment.setText(list.get(position).getComments());
        }

        if(list.get(position).getContact_id().equalsIgnoreCase("empty")){
            holder.tvApptContact.setText("No records");
        }
        else{
            retrieveContact(list.get(position).getCompany_id(), position, holder);
        }

    }
    public void retrieveContact(final String companyDbkey, final int position, final AdminViewMyPastAppointmentHolder holder){

        childEventListenerContacts = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Contact contact = dataSnapshot.getValue(Contact.class);
                if(contact!=null){
                    arrayListContactkey.add(dataSnapshot.getKey());
                    String contactValue = contact.getName().concat(", " + contact.getTitle());
                    if(contact.getIc()){
                        contactValue = contact.getName().concat("(IC)");
                    }
                    arrayListContactName.add(contactValue);
                }
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

        databaseReference.child("Contact").orderByChild("company_id").equalTo(companyDbkey).addChildEventListener(childEventListenerContacts);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("Contact").removeEventListener(childEventListenerContacts);
                System.out.println("child event removed");

                if(arrayListContactkey.size() != 0){
                    for (int i = 0; i < arrayListContactkey.size(); i++) {
                        if(arrayListContactkey.get(i).equals(list.get(position).getContact_id())){
                            holder.tvApptContact.setText(arrayListContactName.get(i).toString());
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
