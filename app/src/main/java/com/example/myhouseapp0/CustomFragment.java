package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class CustomFragment extends Fragment {
    private TextView titleObjectNameTextView;
    private TextView deviceListTextView;
    private ConstraintLayout constraintLayout;
    private List<Integer> deviceTextViewIds = new ArrayList<>(); // Храним ID созданных TextView
    private List<Integer> deviceRowIds = new ArrayList<>(); // Храним ID строк (каждая строка — 3 элемента)
    private DB_helper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        constraintLayout = view.findViewById(R.id.custom_container);
        titleObjectNameTextView = view.findViewById(R.id.title_object_name);




        // Получаем тег кнопки из аргументов
        Object buttonTag = getArguments().getString("button_tag");



        // Получаем устройства из БД
        dbHelper = new DB_helper(requireContext());
//        List<String> devices = dbHelper.getDevicesForButton(buttonTag);
//// Создаём TextView для каждого устройства
//        for (String device : devices) {
//            TextView deviceTextView = new TextView(requireContext());
//            deviceTextView.setText(device);
//            deviceTextView.setTextSize(16);
//            deviceTextView.setPadding(16, 8, 16, 8);
//            devicesContainer.addView(deviceTextView);
//        }

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
// Удаляем все ранее созданные TextView устройств
        for (int id : deviceTextViewIds) {
            constraintLayout.removeView(constraintLayout.findViewById(id));
        }
        deviceTextViewIds.clear();

        List<String> devices = dbHelper.getDevicesForButton(buttonTag);

        if (devices.isEmpty()) {
            // Если устройств нет, добавляем один TextView с сообщением
            addDeviceTextView("Устройства не выбраны", constraintLayout.getId());
        } else {
            // Создаём TextView для каждого устройства
            int previousId = R.id.view_devices_bedroom; // Начинаем от view_device_bedroom
            for (String device : devices) {
                previousId = addDeviceTextView(device, previousId);
            }
        }
    }
    public void setObjectName (String name) {
//        objectName.setText(name);
    }
    private int addDeviceTextView(String deviceName, int anchorId) {
        TextView textView = new TextView(requireContext());
        int newId = View.generateViewId();
        textView.setId(newId);

// 1. Создаём фоновый View (прямоугольник)
        View backgroundView = new View(requireContext());
        int bgId = View.generateViewId();
        backgroundView.setId(bgId);
        backgroundView.setBackgroundResource(R.drawable.view_rectangle_object);

        // Устанавливаем фиксированные размеры: 330 пикселей на 60 пикселей
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(330, 60);
        backgroundView.setLayoutParams(layoutParams);
        // 3. Создаём Switch (переключатель)
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchView = new Switch(requireContext());
        int switchId = View.generateViewId();
        switchView.setId(switchId);


        // Настройки TextView
        textView.setText(deviceName);
        textView.setTextSize(16);
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
        textView.setPadding(16, 12, 16, 12); // внутренние отступы


        // Добавляем в контейнер
        constraintLayout.addView(textView);
        constraintLayout.addView(backgroundView);
        constraintLayout.addView(switchView);
        deviceTextViewIds.add(newId);

        // Настраиваем ограничения через ConstraintSet
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Горизонтальное позиционирование: по центру родителя
        constraintSet.connect(
                newId, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START, 32
        );
        constraintSet.connect(
                newId, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END, 32
        );
// Позиционирование фонового View
        constraintSet.connect(bgId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 32);
        constraintSet.connect(bgId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 32);
        int verticalMargin = (anchorId == R.id.view_device_bedroom) ? 24 : 16;
        constraintSet.connect(bgId, ConstraintSet.TOP, anchorId, ConstraintSet.BOTTOM, verticalMargin);
        constraintSet.constrainPercentHeight(bgId, 0.1f); // Высота фона — 10 % от родителя

        // Позиционирование Switch
            constraintSet.connect(switchId, ConstraintSet.END, bgId, ConstraintSet.END, -16);
            constraintSet.centerVertically(switchId, bgId);
            // Отступ между TextView и Switch
            constraintSet.connect(newId, ConstraintSet.END, switchId, ConstraintSet.START, 16);


        // Вертикальное позиционирование
        if (anchorId == constraintLayout.getId()) {
            // Для первого элемента — позиционируем относительно view_device_bedroom
            constraintSet.connect(
                    newId, ConstraintSet.TOP,
                    R.id.view_device_bedroom, ConstraintSet.BOTTOM, 24
            );
        } else {
            // Для последующих — относительно предыдущего TextView
            constraintSet.connect(
                    newId, ConstraintSet.TOP,
                    anchorId, ConstraintSet.BOTTOM, 16
            );
        }

        constraintSet.applyTo(constraintLayout);
        return newId;
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
