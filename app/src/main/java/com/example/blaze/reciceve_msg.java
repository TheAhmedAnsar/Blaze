package com.example.blaze;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class reciceve_msg extends AppCompatActivity {
    //TextView name, msg;
    String profile_url, name, msg, my_number, my_uid, UID, sender_number;
    private int duration = 30; // Duration in seconds

    private ValueAnimator valueAnimator;

    ImageView CloseButton;
    NestedScrollView MainScroll;
    NestedScrollView scrollView;

    LinearLayoutCompat Heyy, Smile, Reply, linearhide, keyboardreply;

    private LinearProgressIndicator progressBar;
    TextView textview, textView_waiting_text, textview_waiting;
    LottieAnimationView animationView;

String pushkey;
    private Handler handler123;
    private Runnable toggleVisibilityRunnable;

    AppCompatEditText editText;
    TextView username, reply;
    ImageView imageview;

    de.hdodenhof.circleimageview.CircleImageView profileimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciceve_msg);

        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));


        textview = findViewById(R.id.text);
        textview_waiting = findViewById(R.id.toname);

        textView_waiting_text = findViewById(R.id.waiting_text);

        animationView = findViewById(R.id.animationView);

        MainScroll = findViewById(R.id.mainscroll);
        scrollView = findViewById(R.id.scrollview);

        CloseButton = findViewById(R.id.close_button);
        Heyy = findViewById(R.id.hey);
        Smile = findViewById(R.id.smile);
        Reply = findViewById(R.id.replies);

        linearhide = findViewById(R.id.setvisible);
        keyboardreply = findViewById(R.id.linearLayoutCompat13);
        editText = findViewById(R.id.message);

        progressBar = findViewById(R.id.progressBar);

        imageview = findViewById(R.id.send_message);

        my_uid = FirebaseAuth.getInstance().getUid();

        name = getIntent().getStringExtra("name");
        msg = getIntent().getStringExtra("message");
        profile_url = getIntent().getStringExtra("profile");
        my_number = getIntent().getStringExtra("mynumber");
        sender_number = getIntent().getStringExtra("number");
        UID = getIntent().getStringExtra("uid");


        progressBar.setScaleX(-1);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


                    CloseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                         finish();

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


            }
        });


        if (editText.hasFocus())
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText,  InputMethodManager.SHOW_IMPLICIT);


        }
        MainScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainScroll.fullScroll(View.FOCUS_DOWN);

            }
        },500);

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


        String contactName = getContactNameFromPhoneNumber("+91"+ sender_number);



//        Toast.makeText(this, contactName, Toast.LENGTH_SHORT).show();
        Log.d("contactName", "onCreate: " + contactName);
        username = findViewById(R.id.user_name);
//        reply = findViewById(R.id.reply);
        profileimage = findViewById(R.id.profile_image);


        if (contactName != null) {
            // Contact name is available, display it
            username.setText(contactName);

            // Use the contact name as desired
        } else {

            String number_details = name + " " +"+91 "+ sender_number;
            // Contact name is not available, display the phone number instead
            username.setText(number_details);
            // Use the phone number as desired
        }

        Log.d("sender_number", "onCreate: " + my_number + " " + sender_number );
        String decryptedMessage = enryptdecrypt.decrypt(msg);
        textview.setText(decryptedMessage);
        Glide.with(this).load(profile_url).into(profileimage);



   View.OnClickListener onClickListener = new View.OnClickListener() {
       @Override
       public void onClick(View view) {

           switch (view.getId()) {
               case R.id.hey:

                   String encryptedMessage = enryptdecrypt.encrypt("Heyy");
                   sendmessage(encryptedMessage);
                   togglevisiblitiy();
                   // Handle button1 click
                   break;

               case R.id.smile:
                   // Handle button2 click
                   String encryptedMessageagain = enryptdecrypt.encrypt("😊");
                   sendmessage(encryptedMessageagain);
                   togglevisiblitiy();
//                   handler123.postDelayed(toggleVisibilityRunnable, 30000);
//                   startProgressAnimation();


                   break;
               case R.id.replies:

                   togglevisiblitiy_sendreply();
//                   handler123.postDelayed(toggleVisibilityRunnable, 30000);
//                   startProgressAnimation();



                   // Handle button3 click
                   break;
               // Add cases for more buttons if needed
           }

       }
   };

   Heyy.setOnClickListener(onClickListener);
   Smile.setOnClickListener(onClickListener);
   Reply.setOnClickListener(onClickListener);


        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                toggleVisibilityagain();
//                handler123.postDelayed(toggleVisibilityRunnable, 30000);
                String gettext = editText.getText().toString();
                if(gettext.isEmpty())
                {

                    Toast.makeText(reciceve_msg.this, "Message cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    String encryptedMessageagain = enryptdecrypt.encrypt(gettext);

                    sendmessage(encryptedMessageagain);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);

                    togglevisiblitiyaftersent();
                    startProgressAnimation();

                }

            }
        });


        handler123 = new Handler();
        toggleVisibilityRunnable = new Runnable() {
            @Override
            public void run() {
                togglevisiblitiy();
            }
        };





    }

    private void togglevisiblitiyaftersent() {

        int textViewVisibility = keyboardreply.getVisibility();

        if (textViewVisibility == View.VISIBLE)
        {

            linearhide.setVisibility(GONE);
            keyboardreply.setVisibility(GONE);
//            textview.setVisibility(GONE);
//            linearhide.setVisibility(GONE);
            textview_waiting.setText("Blaze To");
            textView_waiting_text.setVisibility(View.VISIBLE);
//
            animationView.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.VISIBLE);

        }

        else
        {

            linearhide.setVisibility(View.VISIBLE);
            keyboardreply.setVisibility(GONE);
//            textview.setVisibility(View.VISIBLE);
//            linearhide.setVisibility(View.VISIBLE);
            textview_waiting.setText("Blaze From");
//
            textView_waiting_text.setVisibility(GONE);
//
//
            animationView.setVisibility(View.GONE);

            progressBar.setVisibility(View.GONE);

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

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
            togglevisiblitiy();
            }
        });
    }



    private void togglevisiblitiy() {

        int textViewVisibility = textview.getVisibility();

        if (textViewVisibility == View.VISIBLE)
        {

            textview.setVisibility(GONE);
            linearhide.setVisibility(GONE);
            textview_waiting.setText("Blaze To");
            textView_waiting_text.setVisibility(View.VISIBLE);
//
            animationView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            startProgressAnimation();
//keyboardreply.setVisibility(View.VISIBLE);



        }

        else
        {
            textview.setVisibility(View.VISIBLE);
            linearhide.setVisibility(View.VISIBLE);
            textview_waiting.setText("Blaze From");

            textView_waiting_text.setVisibility(GONE);
            animationView.setVisibility(View.GONE);
//            keyboardreply.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }



    private void togglevisiblitiy_sendreply() {

        int textViewVisibility = linearhide.getVisibility();

        if (textViewVisibility == View.VISIBLE)
        {

            linearhide.setVisibility(GONE);
            keyboardreply.setVisibility(View.VISIBLE);
//            textview.setVisibility(GONE);
//            linearhide.setVisibility(GONE);
            textview_waiting.setText("Blaze To");
//            textView_waiting_text.setVisibility(View.VISIBLE);
//
//            animationView.setVisibility(View.VISIBLE);
//
//            progressBar.setVisibility(View.VISIBLE);

        }

        else
        {

            linearhide.setVisibility(View.VISIBLE);
            keyboardreply.setVisibility(GONE);
//            textview.setVisibility(View.VISIBLE);
//            linearhide.setVisibility(View.VISIBLE);
            textview_waiting.setText("Blaze From");
//
//            textView_waiting_text.setVisibility(GONE);
//
//
//            animationView.setVisibility(View.GONE);
//
//            progressBar.setVisibility(View.GONE);

        }
    }




    private String getContactNameFromPhoneNumber(String phoneNumber) {
        ContentResolver contentResolver = getContentResolver();
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


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
        finish();
    }



    private void sendmessage(String message) {

//        String contact = "+91"+ number;
        long time = System.currentTimeMillis();
        HashMap<String, Object> chats = new HashMap<>();
        chats.put("message",message);
        chats.put("timestamp", String.valueOf(time));
        chats.put("phone", my_number);
        chats.put("senderuid", my_uid);


//        Toast.makeText(this, "Sender uid" + my_number, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(UID).child("messages").child(sender_number)
                .updateChildren(chats);

        DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference()
                .child("users_credentials").child(UID).child("history");

        pushkey = databaseReference1.push().getKey();


        HashMap<String, Object> newchats = new HashMap<>();
        newchats.put("message",message);
        newchats.put("timestamp", String.valueOf(time));
        newchats.put("phone", my_number);
        newchats.put("senderuid", my_uid);
        newchats.put("pushvalue", pushkey);


//                        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(UID).child("history").push()
                                databaseReference1.child(pushkey)
                                .updateChildren(newchats)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                        DatabaseReference databaseReference2 =FirebaseDatabase.getInstance().getReference()
                                                .child("users_credentials").child(my_uid).child("history");


                             String pushkey2 = databaseReference2.push().getKey();

                                        HashMap<String, Object> history = new HashMap<>();
                                        history.put("message",message);
                                        history.put("timestamp", String.valueOf(time));
                                        history.put("phone", sender_number);
                                        history.put("senderuid", UID);
                                        history.put("pushvalue", pushkey2);

//                                        FirebaseDatabase.getInstance().getReference().child("users_credentials").child(my_uid).child("history").push()
                                        databaseReference2.child(pushkey2)
                                                .updateChildren(history).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                    @Override
                                                    public void onSuccess(Void unused) {
//                                                        Toast.makeText(reciceve_msg.this, "Reply sent and add", Toast.LENGTH_SHORT).show();
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


}