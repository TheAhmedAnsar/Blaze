package com.example.blaze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

public class Blaze_send extends AppCompatActivity {

    private LinearLayoutCompat linearLayout, linearLayoutCompat;
    private AppCompatEditText editText;
    TextView textview, textview_name, textview_waiting, textView_waiting_text;
    de.hdodenhof.circleimageview.CircleImageView profile_pic;

    LottieAnimationView animationView;

    private LinearProgressIndicator progressBar;
String pushkey;
    NestedScrollView scrollView;
    NestedScrollView MainScroll;
ImageView imageview, backimage;
    String encryptedMessage;
    String name, phone, profile,my_uid, ContactUid;

    private Handler handler123;
    private Runnable toggleVisibilityRunnable;

    private CountDownTimer countDownTimer;
     FirebaseDatabase database;

    private static final String PREFS_NAME = "timing";
    private static final String KEY_LAST_MESSAGE_TIME = "lastMessageTime";
     String mynumber;

    private ValueAnimator valueAnimator;
    private int duration = 30; // Duration in seconds

    private int progressValue = duration;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blaze_send);

        linearLayout = findViewById(R.id.linearLayoutCompat12);
        linearLayoutCompat = findViewById(R.id.linearLayoutCompat13);
        editText = findViewById(R.id.message);
        textview = findViewById(R.id.text);
        textview_name = findViewById(R.id.user_name);
        profile_pic = findViewById(R.id.profile_image);
        textview_waiting = findViewById(R.id.name);
        animationView = findViewById(R.id.animationView);
textView_waiting_text = findViewById(R.id.waiting_text);
        progressBar = findViewById(R.id.progressBar);
backimage = findViewById(R.id.back);

imageview = findViewById(R.id.send_message);

        scrollView = findViewById(R.id.scrollview);
        MainScroll = findViewById(R.id.mainscroll);


        progressBar.setScaleX(-1);


         name = getIntent().getStringExtra("name");
         phone = getIntent().getStringExtra("phone");
         profile = getIntent().getStringExtra("profile");
        ContactUid = getIntent().getStringExtra("uid");
        mynumber = getIntent().getStringExtra("myphone");

        Log.d("mynumber", "onCreate: " + mynumber);

//        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
//        my_uid = sharedPreferences.getString("key", "");
        my_uid = FirebaseAuth.getInstance().getUid();

         textview_name.setText(name);
        Glide.with(this).load(profile)
                        .into(profile_pic);

        database = FirebaseDatabase.getInstance();


        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));

//        Log.d("uid", "onCreate: " + Uid);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        handler123 = new Handler();
        toggleVisibilityRunnable = new Runnable() {
            @Override
            public void run() {
                toggleVisibility();
            }
        };

        if (editText.hasFocus())
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText,  InputMethodManager.SHOW_IMPLICIT);


        }

            backimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();

                }
            });

            MainScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainScroll.fullScroll(View.FOCUS_DOWN);

            }
        },1000);




        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.hasFocus())
                {

                    MainScroll.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainScroll.fullScroll(View.FOCUS_DOWN);

                        }
                    },500);

                }


            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toggleVisibility();
                handler123.postDelayed(toggleVisibilityRunnable, 30000);
                if (editText.getText().toString().isEmpty())
                {

                    Toast.makeText(Blaze_send.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else {
                    sendmessage();
                    startProgressAnimation();
                    long currentTime = System.currentTimeMillis();

                    // Save the current timestamp in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    prefs.edit().putLong(KEY_LAST_MESSAGE_TIME, currentTime).apply();

                    String text = editText.getText().toString();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
                }

            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String message = editable.toString();
                updateMessage(message);

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                
                
            }
        });


    }





    private void sendmessage() {

        String encryptedMessage = enryptdecrypt.encrypt(editText.getText().toString());


        long time = System.currentTimeMillis();
        HashMap<String, Object> chats = new HashMap<>();
        chats.put("message",encryptedMessage);
        chats.put("timestamp", String.valueOf(time));
        chats.put("phone", mynumber);
        chats.put("senderuid", my_uid);



FirebaseDatabase.getInstance().getReference().child("users_credentials").child(ContactUid).child("messages").child(phone)
        .updateChildren(chats);


        DatabaseReference databaseReference1 =  FirebaseDatabase.getInstance().getReference()
                .child("users_credentials").child(ContactUid).child("history");

        pushkey = databaseReference1.push().getKey();

        HashMap<String, Object> my_chats = new HashMap<>();
        my_chats.put("message",encryptedMessage);
        my_chats.put("timestamp", String.valueOf(time));
        my_chats.put("phone", mynumber);
        my_chats.put("senderuid", my_uid);
        my_chats.put("pushvalue", pushkey);

        databaseReference1.child(pushkey)
        .updateChildren(my_chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference()
                        .child("users_credentials").child(my_uid).child("history");

              String  pushkey2 = databaseReference2.push().getKey();

                HashMap<String, Object> myhistory = new HashMap<>();
                myhistory.put("message",encryptedMessage);
                myhistory.put("timestamp", String.valueOf(time));
                myhistory.put("phone", phone);
                myhistory.put("senderuid", ContactUid);
                myhistory.put("pushvalue", pushkey2);

                databaseReference2.child(pushkey2).updateChildren(myhistory).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                            }
                        });

            }
        });


    }


    private void updateMessage(String message) {


        textview.setText(message);

        if (message.length()== 0)

        {

            textview.setText("Type your message");

        }
    }



    private void toggleVisibility() {
        int textViewVisibility = textview.getVisibility();
        int editTextVisibility = editText.getVisibility();
        int otherLayoutVisibility = textview_name.getVisibility();

        if (textViewVisibility == View.VISIBLE) {
            textview.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
            textView_waiting_text.setVisibility(View.VISIBLE);
            linearLayoutCompat.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            textview_waiting.setText("Waiting for reply from");
        } else {
            textview.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.GONE);
            linearLayoutCompat.setVisibility(View.VISIBLE);
            textView_waiting_text.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            textview_waiting.setText("Blaze to");

        }
    }


    private void startProgressAnimation() {
        valueAnimator = ValueAnimator.ofInt(duration, 0);
        valueAnimator.setDuration(duration * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                progressBar.setProgress(animatedValue);

            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler123.removeCallbacks(toggleVisibilityRunnable);
//        countDownTimer.cancel();
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }
    }


}