package com.example.myhouseapp0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn_sign_in, btn_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_registration = findViewById(R.id.btn_registration);

        btn_registration.setOnClickListener(v -> {
            Intent intent = new Intent(".RegistrationActivity");
            startActivity(intent);
        });


        
    }

}