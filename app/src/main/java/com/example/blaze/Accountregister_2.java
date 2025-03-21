package com.example.blaze;


import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import xcode.api.phoneverification.OnCodeSentCallback;
import xcode.api.phoneverification.PhoneVerification;
import xcode.api.phoneverification.VerificationException;

public class Accountregister_2 extends AppCompatActivity {

    AppCompatButton bt;
    Context context;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    TextInputEditText editText;
    String phonenumber;
    String codesent;
    ImageView backbutton;
    LoginManager loginManager;
    String key;
    private static final int SMS_PERMISSION_REQUEST_CODE = 123;
    boolean isPhoneNumberRegistered = false;
    int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountregister2);

        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));

        bt = findViewById(R.id.Button);
        progressBar = findViewById(R.id.progressbar1);
        constraintLayout = findViewById(R.id.constraint);
        editText = findViewById(R.id.number);

        backbutton = findViewById(R.id.back);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           finish();
            }
        });


        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }
            return false;
        });


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenumber = editText.getText().toString().trim();
                constraintLayout.setAlpha(0.5F);
                progressBar.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                FirebaseDatabase.getInstance().getReference().child("USERS")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String phone = "+91" + phonenumber;

                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            key = snapshot1.getKey();


                                            if (key.equals(phone)) {
                                                Log.d("phonenumber", "onDataChange: " + key);
                                                isPhoneNumberRegistered = true;
                                                break;
                                            }
                                        }


                                            if (isPhoneNumberRegistered)
                                            {
                                                Log.d("phonenumber", "onDataChange: " + key);

                                            Toast.makeText(Accountregister_2.this, "This number is already registered, Kindly please Login", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Accountregister_2.this, Accountcreation_1.class);
//                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            startActivity(intent);

                                                finish();
                                            }
                                        else
                                        {


                                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                    "+91" + phonenumber,
                                                    60,
                                                    TimeUnit.SECONDS,
                                                    Accountregister_2.this,
                                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                        @Override
                                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                                            Toast.makeText(Accountregister_2.this, "Code Sent", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onVerificationFailed(@NonNull FirebaseException e) {


                                                            Toast.makeText(getApplicationContext(), "Something went wrong, please try after sometime", Toast.LENGTH_SHORT).show();

//                                                Toast.makeText(getApplicationContext(), "Faild to fetch OTP", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(GONE);
                                                            constraintLayout.setAlpha(1);
                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                                                        }

                                                        @Override
                                                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                            super.onCodeSent(s, forceResendingToken);


                                                            PhoneAuthProvider.ForceResendingToken resendToken  = forceResendingToken;
                                                            codesent = s;
//                                                            Toast.makeText(Accountregister_2.this, phonenumber, Toast.LENGTH_SHORT).show();
//
//                                                            Toast.makeText(Accountregister_2.this, codesent, Toast.LENGTH_SHORT).show();
////
                                                            Intent intent = new Intent(getApplicationContext(), AccountOtp_3.class);
                                                            intent.putExtra("mobile", phonenumber);
                                                            intent.putExtra("otp", codesent);
                                                            intent.putExtra("token", resendToken);
                                                            progressBar.setVisibility(GONE);
                                                            constraintLayout.setAlpha(1);
                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                            startActivity(intent);
                                                            finish();

                                                        }
                                                    }
                                            );


                                        }

                                        }





                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


            }
        });


////
//                }
//

    }


}