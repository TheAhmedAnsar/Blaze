package com.example.blaze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class AccountPassword_4 extends AppCompatActivity {
    AppCompatButton bt;
    TextInputEditText username, pass, conpass;

    ProgressBar progressBar;
    LoginManager loginManager;

    ConstraintLayout constraintLayout;

    String number, password, Con_password, user;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_password4);

        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));

        constraintLayout = findViewById(R.id.constraint);
        username = findViewById(R.id.username);
//        pass = findViewById(R.id.password);
//        conpass = findViewById(R.id.conf_password);

        user = username.getText().toString().trim();
//        password = pass.getText().toString().trim();
//        Con_password = conpass.getText().toString().trim();
        progressBar = findViewById(R.id.progressbar1);



//        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
//        value = sharedPreferences.getString("key", "");
//
        value = FirebaseAuth.getInstance().getUid();

        Intent intent = getIntent();
        number = intent.getStringExtra("number");

        bt = findViewById(R.id.Button);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                FirebaseDatabase.getInstance().getReference().child("USERNAME")
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                if (snapshot.getKey().equals(username.getText().toString().toLowerCase())) {
//                                    Toast.makeText(AccountPassword_4.this, "Unavailable", Toast.LENGTH_SHORT).show();
//                                } else {
//
//                                    FirebaseDatabase.getInstance().getReference().child("users_credentials").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("credentials")
//                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    Toast.makeText(AccountPassword_4.this, "Add", Toast.LENGTH_SHORT).show();
//                                                    constraintLayout.setAlpha(1);
//                                                    progressBar.setVisibility(View.GONE);
//                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
////                                                                HashMap<String, Object> obj = new HashMap<>();

                                                    String contact = "+91"+number;

                                                    HashMap<String, Object> Data = new HashMap<>();
                                                    Data.put("name", username.getText().toString().toLowerCase());
                                                    Data.put("phone", contact);
                                                    Data.put("uid", FirebaseAuth.getInstance().getUid());
                                                    FirebaseDatabase.getInstance().getReference().child("users_credentials").child(value)
                                                            .updateChildren(Data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {



                                                                    Intent intent = new Intent(AccountPassword_4.this, Profile_creation.class);
                                                                    intent.putExtra("phone", contact);


                                                                    startActivity(intent);
                                                                    finish();

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(AccountPassword_4.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                }


//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//
//                                                    Toast.makeText(AccountPassword_4.this, e.toString(), Toast.LENGTH_SHORT).show();
//                                                    constraintLayout.setAlpha(1);
//                                                    progressBar.setVisibility(View.GONE);
//                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//
//                                                }
//                                            });


//                                }


//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
////                            }
//                        });




//                if (password.equals(Con_password)) {
//
//
//                    constraintLayout.setAlpha(0.5F);
//                    progressBar.setVisibility(View.VISIBLE);
//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//
//                    HashMap<String, Object> obj = new HashMap<>();
//                    obj.put("username", username.getText().toString().toLowerCase());
//                    obj.put("password", pass.getText().toString());
//                    obj.put("uid", FirebaseAuth.getInstance().getUid());
//
//
//
//
//                } else {
//                    Toast.makeText(AccountPassword_4.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//                }


//            }
        });

    }

//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        loginManager.setLastLoginActivity(3);
//    }
}