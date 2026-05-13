package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CustomFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        // Получаем данные из аргументов
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> devices = args.getStringArrayList("devices");
            // Отображаем устройства в RecyclerView/ListView
            displayDevices(devices, view);
        }


        return view;
    }
    private void displayDevices(ArrayList<String> devices, View rootView) {
        TextView tvDevices = rootView.findViewById(R.id.title_text);
        if (devices != null && !devices.isEmpty()) {
            tvDevices.setText("Выбранные устройства: " + String.join(", ", devices));
        } else {
            tvDevices.setText("Устройства не выбраны");
        }
    }
}
