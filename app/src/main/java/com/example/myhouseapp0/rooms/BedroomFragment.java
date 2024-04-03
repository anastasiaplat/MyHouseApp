package com.example.myhouseapp0.rooms;

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
import android.widget.TextView;

import com.example.myhouseapp0.DB_helper;
import com.example.myhouseapp0.HomeFragment;
import com.example.myhouseapp0.R;

import org.w3c.dom.Text;

public class BedroomFragment extends Fragment {
    DB_helper db_helper;

    public BedroomFragment() {
    }

    public static BedroomFragment newInstance() {
        return new BedroomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bedroom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_bedroom = view.findViewById(R.id.btn_back_bedroom);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            btn_back_from_bedroom.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        }
        TextView tv_temp_and_hum_bedroom = view.findViewById(R.id.tv_temp_bedroom);
        tv_temp_and_hum_bedroom.setText("%%°     %%");
        //tv_temp_and_hum_bedroom.setText(db_helper.getTempData());


    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}