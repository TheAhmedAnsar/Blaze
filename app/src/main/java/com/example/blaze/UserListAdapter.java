package com.example.blaze;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserObject> userList;

    Context context;
    String value;


    private static final String PREFS_NAME = "timing";
    private static final String KEY_LAST_MESSAGE_TIME = "lastMessageTime";

    public UserListAdapter(ArrayList<UserObject> userList) {
        this.userList = userList;
    }


    @NotNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, null, false);
        RecyclerView.LayoutParams ip = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(ip);
        UserListViewHolder rcv = new UserListViewHolder(layout);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {


        context = holder.itemView.getContext();
        holder.mName.setText(userList.get(position).getName());

        holder.mPhone.setText(userList.get(position).getPhone());
//
//        SharedPreferences sharedPreferences = context.getSharedPreferences("myKey", MODE_PRIVATE);
//        value = sharedPreferences.getString("key", "");

        value = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(userList.get(position).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String time = snapshot.child("lastseen").getValue(String.class);


                        if (time.equals("Online")) {
                            holder.lastSeen.setText("Active now");
                            holder.statusimage.setBackgroundResource(R.drawable.online_bg);


                        } else {
                            long longValue = Long.parseLong(time);

                            long currentTime = System.currentTimeMillis();

// Calculate the time difference in milliseconds
                            long timeDifference = currentTime - longValue;
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
                                formattedTime = " few seconds ago";
                            } else if (minutes < 60) {
                                formattedTime = "few minutes ago";
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

//        if (time.equals("Online"))
//        {
//
//        }
//        else {
                            holder.lastSeen.setText("Active " + formattedTime);
                            holder.statusimage.setBackgroundResource(R.drawable.offline_bg);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        String time = userList.get(position).getLastseen();


        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.user_user);


        Glide.with(holder.itemView.getContext())
                .applyDefaultRequestOptions(requestOptions)
                .load(userList.get(position).getProfileurl())
                .into(holder.profile_of_users);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context, Blaze_send.class);


                // Add data as extras to the intent
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                long lastMessageTime = prefs.getLong(KEY_LAST_MESSAGE_TIME, 0);

                // Get the current timestamp
                long currentTime = System.currentTimeMillis();

                // Calculate the time difference in seconds
                long timeDifferenceInSeconds = (currentTime - lastMessageTime) / 1000;


                if (timeDifferenceInSeconds < 30) {
                    // Show a message or disable the button to inform the user to wait
                    Toast.makeText(context, "Please wait " + (30 - timeDifferenceInSeconds) + " seconds before sending a new message.", Toast.LENGTH_SHORT).show();
                    // Optionally, you can disable any button or action that starts this activity again.

                } else {
                    // Continue with your MessageActivity initialization and logic
                    FirebaseDatabase.getInstance().getReference().child("users_credentials").child(value)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String mynumber = snapshot.child("phone").getValue(String.class);
                                    Log.d("mynumber", "onCreate: " + mynumber);


                                    if (userList.get(position).getPhone().equals(mynumber)) {

                                        Toast.makeText(context, "You can't Blaze Youself", Toast.LENGTH_SHORT).show();

                                    } else {
                                        intent.putExtra("name", userList.get(position).getName());
                                        intent.putExtra("phone", userList.get(position).getPhone());
                                        intent.putExtra("myphone", mynumber);
                                        intent.putExtra("profile", userList.get(position).getProfileurl());
                                        intent.putExtra("uid", userList.get(position).getUid());
                                        context.startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    // ... rest of the code ...
                }


//        intent.putExtra("name", userList.get(position).getName());
//        intent.putExtra("phone", userList.get(position).getPhone());
//        intent.putExtra("profile", userList.get(position).getProfileurl());


//        FirebaseDatabase.getInstance().getReference().child("users_credentials");


                // Start the activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mPhone;
        de.hdodenhof.circleimageview.CircleImageView profile_of_users;
        LinearLayoutCompat statusimage;
        TextView lastSeen;

        public UserListViewHolder(View view) {
            super(view);

            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            profile_of_users = itemView.findViewById(R.id.profile_image);

            statusimage = view.findViewById(R.id.status_indicator);
            lastSeen = view.findViewById(R.id.lastseen);


        }
    }
}
