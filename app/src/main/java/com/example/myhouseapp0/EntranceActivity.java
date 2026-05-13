package com.example.myhouseapp0;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EntranceActivity extends AppCompatActivity {

    DB_helper db_helper;
    Button btn_login, btn_to_register0, btn_back1;
    EditText etUsername, etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_helper = new DB_helper(this);

        btn_to_register0.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);
        });
        btn_back1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        btn_login.setOnClickListener(v -> {
            boolean isLoggedId = db_helper.checkUserForEntrance(etUsername.getText().toString(), etPwd.getText().toString());
            if (isLoggedId){
                Intent intent = new Intent(EntranceActivity.this, MajorActivity.class);
                startActivity(intent);
            } else
                Toast.makeText(EntranceActivity.this, "Пароль неверный", Toast.LENGTH_LONG).show();
        });



    }
}
