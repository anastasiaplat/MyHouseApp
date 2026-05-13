package com.example.myhouseapp0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    EditText etUser, etPwd, etRepwd;
    Button btnRegister, btnGoToLogin, btn_back;
    DB_helper db_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
        });
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, EntranceActivity.class);
            startActivity(intent);
        });
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
                    if(registeredSuccess) {
                        Toast.makeText(RegistrationActivity.this, "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegistrationActivity.this, EntranceActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this, "Регистрация не прошла, повторите попытку", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                }
            }
        });




    }



}