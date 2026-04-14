package com.example.myhouseapp0;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MajorActivity.class);
            startActivity(intent);
        });
    }
}