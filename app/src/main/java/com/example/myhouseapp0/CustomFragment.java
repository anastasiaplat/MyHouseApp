package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CustomFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        LinearLayout devicesContainer = view.findViewById(R.id.devices_container);

// Получаем тег кнопки из аргументов
        Object buttonTag = getArguments().getString("button_tag");

        // Получаем устройства из БД
        DB_helper dbHelper = new DB_helper(requireContext());
        List<String> devices = dbHelper.getDevicesForButton(buttonTag);
// Создаём TextView для каждого устройства
        for (String device : devices) {
            TextView deviceTextView = new TextView(requireContext());
            deviceTextView.setText(device);
            deviceTextView.setTextSize(16);
            deviceTextView.setPadding(16, 8, 16, 8);
            devicesContainer.addView(deviceTextView);
        }

        // Получаем данные из аргументов
//        Bundle args = getArguments();
//        if (args != null) {
//            ArrayList<String> devices = args.getStringArrayList("devices");
//            // Отображаем устройства в RecyclerView/ListView
//            displayDevices(devices, view);
//        }
        Button btnBack = view.findViewById(R.id.btn_back_custom);
        btnBack.setOnClickListener(v -> goBackToHome());

        return view;
    }
    // Метод для возврата к HomeFragment
    private void goBackToHome() {
        // Получаем активность и вызываем метод замены фрагмента
        if (getActivity() instanceof MajorActivity) {
            ((MajorActivity) getActivity()).replaceFragment(new HomeFragment());
        }
    }
    private void displayDevices(ArrayList<String> devices, View rootView) {
        TextView tvDevices = rootView.findViewById(R.id.title_text);
            tvDevices.setText("Выбранные устройства: " + String.join(", ", devices));
    }
}
