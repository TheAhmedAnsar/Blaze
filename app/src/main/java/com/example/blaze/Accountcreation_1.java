package com.example.blaze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class Accountcreation_1 extends AppCompatActivity {
    AppCompatButton btregister, btlog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountcreation1);

        btregister = findViewById(R.id.registerButton);
        btlog = findViewById(R.id.loginButton);

        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accountcreation_1.this, Accountregister_2.class);
//                Toast.makeText(Accountcreation_1.this, "1", Toast.LENGTH_SHORT).show();
                startActivity(intent);

finish();
            }
        });

        btlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accountcreation_1.this, phone_enter_login.class);
//                Toast.makeText(Accountcreation_1.this, "2", Toast.LENGTH_SHORT).show();

                startActivity(intent);

finish();
            }
        });

//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.parseColor("#5EC0F6"));
//        getWindow().setNavigationBarColor(Color.parseColor("#5EC0F6"));

    }
}