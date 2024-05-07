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

public class YardFragment extends Fragment {

    public YardFragment() {
        // Required empty public constructor
    }
    public static YardFragment newInstance() {
        return new YardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_yard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_yard = view.findViewById(R.id.btn_back_yard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            btn_back_from_yard.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        }
        TextView tv_temp_and_hum_yard = view.findViewById(R.id.tv_temp_yard);
        tv_temp_and_hum_yard.setText("%%°    %%");

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch_yard = (Switch) view.findViewById(R.id.switch_light_yard);
        btn_switch_yard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Подключите устройство", Toast.LENGTH_SHORT).show();
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch1_yard = (Switch) view.findViewById(R.id.switch_door_yard);
        btn_switch1_yard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Подключите устройство", Toast.LENGTH_SHORT).show();
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch2_yard = (Switch) view.findViewById(R.id.switch_video_yard);
        btn_switch2_yard.setOnCheckedChangeListener((buttonView, isChecked) -> {
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