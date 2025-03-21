package com.example.blaze;


import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class phone_enter_login extends AppCompatActivity {

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
    boolean isPhoneNumberRegistered = false;


    private static final int SMS_PERMISSION_REQUEST_CODE = 123;
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

        bt.setText("SIGN IN");

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
                phonenumber = editText.getText().toString();
                constraintLayout.setAlpha(0.5F);
                progressBar.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                FirebaseDatabase.getInstance().getReference().child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phone = "+91" + phonenumber;

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            key = snapshot1.getKey();


                            if (!key.equals(phone)) {
                                Log.d("phonenumber", "onDataChange: " + key);
                                isPhoneNumberRegistered = true;
                                break;
                            }
                        }



                        if (!isPhoneNumberRegistered) {

                            Toast.makeText(phone_enter_login.this, "This number is not registered, Kindly please Register", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(phone_enter_login.this, Accountcreation_1.class);
                            finish();
//                            startActivity(inte

                        }
                        else
                        {

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + phonenumber,
                                    60,
                                    TimeUnit.SECONDS,
                                    phone_enter_login.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                            Toast.makeText(phone_enter_login.this, "Code Sent", Toast.LENGTH_SHORT).show();
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
//                                            Toast.makeText(phone_enter_login.this, phonenumber, Toast.LENGTH_SHORT).show();
//
//                                            Toast.makeText(phone_enter_login.this, codesent, Toast.LENGTH_SHORT).show();
////
                                            Intent intent = new Intent(getApplicationContext(), otp_enter_login.class);
                                            intent.putExtra("mobile", phonenumber);
                                            intent.putExtra("otp", codesent);
                                            intent.putExtra("token", resendToken);


                                            progressBar.setVisibility(GONE);
                                            constraintLayout.setAlpha(1);
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