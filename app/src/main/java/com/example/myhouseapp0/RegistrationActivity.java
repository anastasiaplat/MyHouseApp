package com.example.myhouseapp0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    EditText etUser, etPwd, etRepwd;
    Button btnRegister, btnGoToLogin;
    DB_helper db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        etUser = findViewById(R.id.ET_login);
        etPwd=findViewById(R.id.ET_password0);
        etRepwd = findViewById(R.id.ET_password1);
        btnRegister = findViewById(R.id.btn_to_register);
        db_helper = new DB_helper(this);
        btnRegister.setOnClickListener(view -> {
            String user, pwd, repwd;
            user = etUser.getText().toString();
            pwd = etPwd.getText().toString();
            repwd = etRepwd.getText().toString();
//                нужно поменять нулл на пустую строку чтоб пустые проблеы нельзя было ставить!
            if (user.equals("") || pwd.equals("") || repwd.equals("")) {
                Toast.makeText(RegistrationActivity.this, "Пожалуйста, заполните все поля",Toast.LENGTH_LONG).show();
            } else {
                if(pwd.equals(repwd)) {
                    if(db_helper.checkUsername(user)) {
                        Toast.makeText(RegistrationActivity.this, "Пользователь с таким логином уже существует", Toast.LENGTH_LONG).show();
                        return;
                    }
                    boolean registeredSuccess = db_helper.insertData(user, pwd);
                    if(registeredSuccess)
                        Toast.makeText(RegistrationActivity.this, "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(RegistrationActivity.this, "Регистрация не прошла", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                }
            }
        });




    }



}