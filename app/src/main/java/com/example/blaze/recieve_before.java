package com.example.blaze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class recieve_before extends AppCompatActivity {

//    de.hdodenhof.circleimageview.CircleImageView imageview;
MaterialCardView materialCardView;
de.hdodenhof.circleimageview.CircleImageView imageView;
LottieAnimationView lottieAnimationView;
TextView textView, textView2;

    String profile_url, name, msg, my_number, my_uid, UID, sender_number;

    private Ringtone defaultRingtone;
    private Handler handler = new Handler();
ImageView backimage;

    private float startY;
    private static final int SWIPE_THRESHOLD = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_before);

        materialCardView = findViewById(R.id.iv_image);
        lottieAnimationView = findViewById(R.id.animationView);
textView = findViewById(R.id.swipetext);

backimage = findViewById(R.id.back);

        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));


        lottieAnimationView.setSpeed(2.0f);
        ShakeAnimation.create().with(materialCardView)
                .setDuration(3600)
                .setRepeatCount(ShakeAnimation.INFINITE)
                .start();

        imageView = findViewById(R.id.profile_image);
textView2 = findViewById(R.id.name_user);


        my_uid = FirebaseAuth.getInstance().getUid();

        name = getIntent().getStringExtra("name");
        msg = getIntent().getStringExtra("message");
        profile_url = getIntent().getStringExtra("profile");
        my_number = getIntent().getStringExtra("mynumber");
        sender_number = getIntent().getStringExtra("number");
        UID = getIntent().getStringExtra("uid");

        Glide.with(this).load(profile_url).into(imageView);
//        textView2.setText(name);

        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        // Create the Ringtone object for the default ringtone
        defaultRingtone = RingtoneManager.getRingtone(this, defaultRingtoneUri);

        // Play the default ringtone
        defaultRingtone.play();

        // Stop the ringtone after the desired duration
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRingtone();
            }
        }, 10000);


        String contactName = getContactNameFromPhoneNumber("+91"+ sender_number);
        if (contactName != null) {
            // Contact name is available, display it
            textView2.setText(contactName);

            // Use the contact name as desired
        } else {

//            String number_details = name + " " +"+91 "+ my_number;
            String number_details = sender_number;
            // Contact name is not available, display the phone number instead
            textView2.setText(number_details);
            // Use the phone number as desired
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


    private void stopRingtone() {
        if (defaultRingtone != null && defaultRingtone.isPlaying()) {
            defaultRingtone.stop();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure to stop the ringtone if the activity is destroyed before the duration completes
        stopRingtone();
//        Intent serviceIntent = new Intent(this, OverlayService.class);
//        startService(serviceIntent);

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                float endY = event.getY();
                float deltaY = endY - startY;
                if (Math.abs(deltaY) > SWIPE_THRESHOLD && deltaY < 0) {
                    // Swipe-up gesture detected, open HomeActivity
                    stopRingtone();
//                    showHomeActivity();
slideUpMainActivity();
//                    startActivity(new Intent(recieve_before.this, reciceve_msg.class));
//                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
//                    overridePendingTransition(R.anim.slide_up, 0);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        stopRingtone();
       finish();
    }



    private void slideUpMainActivity() {
        View mainLayout = findViewById(R.id.mainlayout); // Replace with the ID of your main layout
        mainLayout.animate()
                .translationY(-mainLayout.getHeight()) // Adjust this factor as needed
                .setDuration(500) // Adjust the duration as needed
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // MainActivity is now slid up, start HomeActivity without any animation
//                        startActivity(new Intent(recieve_before.this, reciceve_msg.class));

                        Intent intent = new Intent(recieve_before.this, reciceve_msg.class);


                        intent.putExtra("closeReceiveActivity", true);
                        intent.putExtra("name",name);
                        intent.putExtra("profile",profile_url);
                        intent.putExtra("message",msg);
                        intent.putExtra("mynumber",my_number);
                        intent.putExtra("number",sender_number);
                        intent.putExtra("uid",UID);


                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Use ActivityOptions to specify no custom animation for HomeActivity
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(recieve_before.this, 0, 0);
                        startActivity(intent, options.toBundle());
                        finish();
                    }
                })
                .start();
    }




}




