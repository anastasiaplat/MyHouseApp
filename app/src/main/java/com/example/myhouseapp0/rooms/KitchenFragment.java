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

public class KitchenFragment extends Fragment {

    public KitchenFragment() {
        // Required empty public constructor
    }

    public static KitchenFragment newInstance(String param1, String param2) {
        return new KitchenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kitchen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_kitchen = view.findViewById(R.id.btn_back_kitchen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            btn_back_from_kitchen.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        }
        TextView tv_temp_and_hum_kitchen = view.findViewById(R.id.tv_temp_kitchen);
        tv_temp_and_hum_kitchen.setText("%%°    %%");

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch_kitchen = (Switch) view.findViewById(R.id.switch_light_kitchen);
        btn_switch_kitchen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Подключите устройство", Toast.LENGTH_SHORT).show();
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch1_kitchen = (Switch) view.findViewById(R.id.switch_heating_kitchen);
        btn_switch1_kitchen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Подключите устройство", Toast.LENGTH_SHORT).show();
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch2_kitchen = (Switch) view.findViewById(R.id.switch_condik_kitchen);
        btn_switch2_kitchen.setOnCheckedChangeListener((buttonView, isChecked) -> {
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