package com.example.blaze;

import static android.view.View.GONE;
import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import xcode.api.phoneverification.OnVerifyCallback;
import xcode.api.phoneverification.PhoneVerification;
import xcode.api.phoneverification.VerificationException;

public class AccountOtp_3 extends AppCompatActivity {
    AppCompatButton bt;
    TextInputEditText editText;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    String number;
    LoginManager loginManager;
    PhoneAuthCredential phoneAuthCredential;
    TextView timer, resend;
    PhoneAuthProvider.ForceResendingToken resendToken;
    String enteredcode;
    TextView Phone_number;
    String codesent;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_otp3);

        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));

        bt = findViewById(R.id.Button);
        backbutton = findViewById(R.id.back);
        Intent intent = getIntent();
        number = intent.getStringExtra("mobile");
        String token = intent.getStringExtra("token");

        Intent intent21 = getIntent();
        if (intent != null) {
            resendToken = intent21.getParcelableExtra("token");
        }

        Phone_number = findViewById(R.id.phonenumber);

        editText = findViewById(R.id.otp_read);
        progressBar = findViewById(R.id.progressbar1);
        constraintLayout = findViewById(R.id.constraint);

        String new_number = "+91-" + number;
        Phone_number.setText(new_number);
         enteredcode = getIntent().getStringExtra("otp");

//        intent.putExtra("token", resendToken);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        editText.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.isFocusedByDefault();
        }

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
                        .setPhoneNumber("+91"+number) // Replace with the actual phone number
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(AccountOtp_3.this)
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

//                Toast.makeText(AccountOtp_3.this, enteredcode, Toast.LENGTH_SHORT).show();

                String optnumber = editText.getText().toString();

                if (!optnumber.isEmpty()) {

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(enteredcode, optnumber);

                    signInWithPhoneAuthCredential(phoneAuthCredential);

                } else {
                    Toast.makeText(AccountOtp_3.this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
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
                    AccountOtp_3.this.enteredcode = verificationId;
                    AccountOtp_3.this.resendToken = forceResendingToken;
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

                    Intent intent = new Intent(AccountOtp_3.this, AccountPassword_4.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("number", number);

                    startActivity(intent);
                    finish();
//                    FirebaseDatabase.getInstance().getReference().child("USERS")
//                            .updateChildren(users).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//
//
//                                    Intent intent = new Intent(AccountOtp_3.this, AccountPassword_4.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.putExtra("number", number);
//
//                                    startActivity(intent);
//                                    finish();
//
//                                }
//                            });


//                    FirebaseDatabase.getInstance().getReference().child("users_credentials").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });


                } else {
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AccountOtp_3.this, e.toString(), Toast.LENGTH_SHORT).show();

                            constraintLayout.setAlpha(1);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                        }
                    });
                }

            }
        });


    }


}