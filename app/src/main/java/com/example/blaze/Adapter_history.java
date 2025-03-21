package com.example.blaze;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_history extends   RecyclerView.Adapter<Adapter_history.UserListViewHolder> {


    ArrayList<history> userList;

    private ContentResolver contentResolver;

    AlertDialog.Builder builder;;

    public Adapter_history(ArrayList<history> userList) {
        this.userList = userList;

    }

    public Adapter_history(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }


    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users , null , false);
        RecyclerView.LayoutParams ip = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(ip);
        Adapter_history.UserListViewHolder rcv = new UserListViewHolder(layout);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {

        String decryptedMessage = enryptdecrypt.decrypt(userList.get(position).getMessage());

        holder.mPhone.setText(decryptedMessage);

        String time = userList.get(position).getTimestamp();
        long longValue = Long.parseLong(time);
        long currentTime = System.currentTimeMillis();

// Calculate the time difference in milliseconds
        long timeDifference = currentTime -longValue;
// Convert time difference to seconds, minutes, hours, days, weeks, months, and years
        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30; // Assumes a month has 30 days
        long years = days / 365; // Assumes a year has 365 days

// Create a formatted string based on the calculated time difference
        String formattedTime;
        if (seconds < 60) {
            formattedTime = seconds + " seconds ago";
        } else if (minutes < 60) {
            formattedTime = minutes + " minutes ago";
        } else if (hours < 24) {
            formattedTime = hours + " hours ago";
        } else if (days < 7) {
            formattedTime = days + " days ago";
        } else if (weeks < 4) {
            formattedTime = weeks + " weeks ago";
        } else if (months < 12) {
            formattedTime = months + " months ago";
        } else {
            formattedTime = years + " years ago";
        }

        builder = new AlertDialog.Builder(holder.itemView.getContext());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Do you want to delete this message?")
                        .setTitle("Delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                             FirebaseDatabase.getInstance().getReference().child("users_credentials")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child("history")
                                        .child(userList.get(position).getPushvalue())
                                        .removeValue();

                            }
                        });

                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete message");
                alert.show();

            }
        });

        holder.mlastmsg.setText(formattedTime);

        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(userList.get(position).getSenderuid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String value = snapshot.child("image").getValue(String.class);
                                String name = snapshot.child("name").getValue(String.class);
                                userList.get(position).setImage(value);

    userList.get(position).setImage(value);
    RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.user_user);

                                Glide.with(holder.itemView.getContext())
                                        .applyDefaultRequestOptions(requestOptions)
                                        .load(userList.get(position).getImage())
                                        .into(holder.profile_of_users);

                                if (ContextCompat.checkSelfPermission(holder.itemView.getContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                                    // Permission not granted, return null or handle it as you prefer
//                                    return null;
                                    Toast.makeText(holder.itemView.getContext(), "Permission is not granted", Toast.LENGTH_SHORT).show();
                                    holder.mName.setText(userList.get(position).getName());

                                }
                                else {
                                    String numbersave = getContactNameFromPhoneNumber(userList.get(position).getPhone(), holder.itemView.getContext());


                                    holder.mName.setText(numbersave);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


    }



    @Override
    public int getItemCount() {
        return userList.size();
    }



    public static class  UserListViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        TextView mName, mPhone, mlastmsg;
        de.hdodenhof.circleimageview.CircleImageView profile_of_users;
        ImageView checkbox;

        public UserListViewHolder(View view) {
            super(view);

            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            profile_of_users = itemView.findViewById(R.id.profile_image);
            mlastmsg = itemView.findViewById(R.id.lastseen);
            checkbox = itemView.findViewById(R.id.checked);
            constraintLayout = itemView.findViewById(R.id.selectconstraint);



        }

    }


    private String getContactNameFromPhoneNumber(String phoneNumber, Context context) {

//        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                cursor.close();
                return contactName;
            }
            cursor.close();
        }
        return null;
    }





}



