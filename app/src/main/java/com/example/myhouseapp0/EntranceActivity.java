package com.example.myhouseapp0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EntranceActivity extends AppCompatActivity {

    DB_helper db_helper;
    Button btn_login;
    EditText etUsername, etPwd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        db_helper = new DB_helper(this);
        etUsername = findViewById(R.id.ET_login);
        etPwd = findViewById(R.id.ET_password);
        btn_login = findViewById(R.id.btn_to_login);
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isLoggedId = db_helper.checkUserForEntrance(etUsername.getText().toString(), etPwd.getText().toString());
//                if (isLoggedId){
//                    Intent intent = new Intent(EntranceActivity.this, )
//                }
//            }
//        });
        //https://www.youtube.com/watch?v=WAejZCkLJAI&t=145s



    }
}
