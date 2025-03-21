package com.example.blaze;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class otp_enter_login extends AppCompatActivity {
    AppCompatButton bt;
    TextInputEditText editText;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    String number;
    LoginManager loginManager;
    PhoneAuthCredential phoneAuthCredential;

    TextView timer, resend;
    PhoneAuthProvider.ForceResendingToken resendToken;

    TextView Phone_number;
    String codesent;
    String enteredcode;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 12;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_otp3);

        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));


//        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED);
//        {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//
//        }

//        if (checkSelfPermission(otp_enter_login.this, android.Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//
////            requestContactsPermission();
//            // Request the permission
////                        ActivityCompat.requestPermissions(
////                                otp_enter_login.this,
////                                new String[]{Manifest.permission.READ_CONTACTS},
////                                100
////                        );
//        }
//        else
//        {
//
//        }


        bt = findViewById(R.id.Button);
        Intent intent = getIntent();
        number = intent.getStringExtra("mobile");

        editText = findViewById(R.id.otp_read);
        progressBar = findViewById(R.id.progressbar1);
        constraintLayout = findViewById(R.id.constraint);

        editText.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.isFocusedByDefault();
        }


        Intent intent21 = getIntent();
        if (intent != null) {
            resendToken = intent21.getParcelableExtra("token");
        }

        Phone_number = findViewById(R.id.phonenumber);

        String new_number = "+91-" + number;
        Phone_number.setText(new_number);

        resend = findViewById(R.id.resend_otp_button);

        int count = 60; // number of seconds for the timer
        timer = findViewById(R.id.resend_code);

        new CountDownTimer(count * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                timer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {

                resend.setEnabled(true);

            }
        }.start();
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resend OTP
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91" + number) // Replace with the actual phone number
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(otp_enter_login.this)
                        .setCallbacks(otpCallbacks)
                        .setForceResendingToken(resendToken)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                constraintLayout.setAlpha(0.5F);
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                enteredcode = getIntent().getStringExtra("otp");

//                Toast.makeText(otp_enter_login.this, enteredcode, Toast.LENGTH_SHORT).show();

                String optnumber = editText.getText().toString();

                if (!optnumber.isEmpty()) {

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(enteredcode, optnumber);

                    signInWithPhoneAuthCredential(phoneAuthCredential);

                } else {
                    Toast.makeText(otp_enter_login.this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks otpCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // Handle verification failure
                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    // Update the verificationId and forceResendingToken
                    otp_enter_login.this.enteredcode = verificationId;
                    otp_enter_login.this.resendToken = forceResendingToken;
                }
            };






    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

//                    HashMap<String, Object> obj = new HashMap<>();
//                    obj.put(FirebaseAuth.getInstance().getUid(), true);
//

                    constraintLayout.setAlpha(1);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    String uid = FirebaseAuth.getInstance().getUid();

                    HashMap<String, Object> users = new HashMap<>();
                    users.put(number, true);
                    SharedPreferences prefs = getSharedPreferences("Creation", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("profile", true).apply();


                    Intent intent = new Intent(otp_enter_login.this, MainActivity.class);
                    intent.putExtra("number", number);
                    startActivity(intent);
                    finish();

//                        private void requestContactsPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//            //            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, 21);
//        }
//    }

//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                } else {
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(otp_enter_login.this, e.toString(), Toast.LENGTH_SHORT).show();

                            constraintLayout.setAlpha(1);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                        }
                    });
                }

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getContactList() {

//        String ISOPrefix = getCountryISO();
        Cursor phones = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, null);

        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

//            if (!String.valueOf(phone.charAt(0)).equals("+"))
//                phone = ISOPrefix + phone;
//
//            UserObject mcontact = new UserObject("", name, phone, "");
//            contactList.add(mcontact);
//            frameLayout.stopShimmer();
//            frameLayout.setVisibility(View.GONE);
//
//            getuserdetails(mcontact);

        }

    }



    private void requestContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

//        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, you can now access contacts
//                Toast.makeText(this, "ranted", Toast.LENGTH_SHORT).show();
//
//
//            } else {
//                // Permission denied, handle accordingly
//                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }
}