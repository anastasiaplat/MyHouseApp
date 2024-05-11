package com.example.myhouseapp0;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;

import com.example.myhouseapp0.databinding.ActivityMainBinding;
import com.example.myhouseapp0.databinding.ActivityMajorBinding;

public class MajorActivity extends AppCompatActivity {
    ActivityMajorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMajorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    replaceFragment(new HomeFragment());
                }
            } else if (itemId == R.id.history) {
                replaceFragment(new HistoryFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.bottomNavigationView.setOutlineAmbientShadowColor(getColor(R.color.white_green));
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}