package com.example.blaze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class Splashscreen extends AppCompatActivity {
    boolean created;
    LoginManager loginManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);


        loginManager = new LoginManager(this);
        int lastLoginActivity = loginManager.getLastLoginActivity();
        SharedPreferences prefs = getSharedPreferences("Creation", Context.MODE_PRIVATE);
        created = prefs.getBoolean("profile", false);


        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                finally{
                    if (isInternetConnected()) {

                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                            if (created)
                            {
                                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {

                                Intent intent = new Intent(Splashscreen.this, Accountcreation_1.class);
                                startActivity(intent);
                                finish();
                            }
//                            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//                                Intent intent = new Intent(Splashscreen.this, Accountcreation_1.class);
//                                startActivity(intent);
//
////                        Toast.makeText(Splashscreen.this, "No user", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//
//                            else {
//                                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }

                        }

                        else if (FirebaseAuth.getInstance().getCurrentUser() == null)
                        {

                            Intent intent = new Intent(Splashscreen.this, Accountcreation_1.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                    else
                    {

                        Toast.makeText(Splashscreen.this, "Please connect to the Internet", Toast.LENGTH_SHORT).show();
                    }



                }

            }




        };   thread.start();

    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

}