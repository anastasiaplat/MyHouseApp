package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class CustomFragment extends Fragment {
    private TextView titleObjectNameTextView;
    private TextView deviceListTextView;
    private DB_helper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        LinearLayout devicesContainer = view.findViewById(R.id.devices_container);

        titleObjectNameTextView = view.findViewById(R.id.title_object_name);
        deviceListTextView = view.findViewById(R.id.title_device);

// Получаем тег кнопки из аргументов
        Object buttonTag = getArguments().getString("button_tag");



        // Получаем устройства из БД
        dbHelper = new DB_helper(requireContext());
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String buttonName = args.getString("button_name");
            // Устанавливаем название объекта в заголовок
            if (buttonName != null && !buttonName.isEmpty()) {
                titleObjectNameTextView.setText(buttonName);
            } else {
                titleObjectNameTextView.setText("Название объекта не указано");
            }

            // Получаем тег кнопки по имени
            Object buttonTag = getButtonTagByName(buttonName);
            if (buttonTag != null) {
                updateDeviceListDisplay(buttonTag);
            }
        }
    }
    private Object getButtonTagByName(String buttonName) {
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<ButtonData> buttonsData = viewModel.getButtonsData().getValue();
        if (buttonsData != null) {
            for (ButtonData buttonData : buttonsData) {
                if (buttonData.getName().equals(buttonName)) {
                    return buttonData.getTag();
                }
            }
        }
        return null;
    }
    private void updateDeviceListDisplay(Object buttonTag) {
        List<String> devices = dbHelper.getDevicesForButton(buttonTag);
        StringBuilder deviceListText = new StringBuilder("");
        if (devices.isEmpty()) {
            deviceListText.append("Устройства не выбраны");
        } else {
            for (String device : devices) {
                deviceListText.append(device);
            }
        }
        deviceListTextView.setText(deviceListText.toString());
    }
    public void setObjectName (String name) {
//        objectName.setText(name);
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
            tvDevices.setText(devices.toString());
    }
}
