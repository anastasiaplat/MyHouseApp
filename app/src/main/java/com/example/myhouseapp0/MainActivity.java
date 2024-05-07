package com.example.myhouseapp0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_sign_in = (Button) findViewById(R.id.btn_sign_in);

        Button btn_to_register = (Button) findViewById(R.id.btn_registration);
        btn_to_register.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        btn_sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EntranceActivity.class);
            startActivity(intent);
        });


        
    }

}