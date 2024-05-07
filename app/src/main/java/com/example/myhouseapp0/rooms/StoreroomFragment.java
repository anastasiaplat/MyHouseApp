package com.example.myhouseapp0.rooms;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhouseapp0.HomeFragment;
import com.example.myhouseapp0.R;

public class StoreroomFragment extends Fragment {
    public StoreroomFragment() {
        // Required empty public constructor
    }

    public static StoreroomFragment newInstance() {
        return new StoreroomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storeroom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_storeroom = view.findViewById(R.id.btn_back_storeroom);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            btn_back_from_storeroom.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        }
        TextView tv_temp_and_hum_storeroom = view.findViewById(R.id.tv_temp_storeroom);
        tv_temp_and_hum_storeroom.setText("%%°    %%");

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch_storeroom = (Switch) view.findViewById(R.id.switch_light_storeroom);
        btn_switch_storeroom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Подключите устройство", Toast.LENGTH_SHORT).show();
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch1_storeroom = (Switch) view.findViewById(R.id.switch_heating_storeroom);
        btn_switch1_storeroom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Подключите устройство", Toast.LENGTH_SHORT).show();
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch2_storeroom = (Switch) view.findViewById(R.id.switch_condik_storeroom);
        btn_switch2_storeroom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Подключите устройство", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}