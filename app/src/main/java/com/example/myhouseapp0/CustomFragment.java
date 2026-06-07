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
    private List<Integer> deviceTextViewIds = new ArrayList<>();
    private List<Integer> deviceRowIds = new ArrayList<>();
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
            int previousId = R.id.view_devices_bedroom; // Начинаем от view_device_bedroom
            for (String device : devices) {
                previousId = addDeviceTextView(device, previousId);
        }
    }
    public void setObjectName (String name) {
    }
    private int addDeviceTextView(String deviceName, int anchorId) {


        TextView textView = new TextView(requireContext());
        int newId = View.generateViewId();
        textView.setId(newId);
        // Настройки TextView
        textView.setText(deviceName);
        textView.setTextSize(16);
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
        textView.setPadding(16, 16, 16, 12); // внутренние отступы
// Размеры wrap_content
        ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(textParams);

// 1. Создаём фоновый View (прямоугольник)
        View backgroundView = new View(requireContext());
        int bgId = View.generateViewId();
        int widthDp = 330;
        int heightDp = 60;
        int marginBottomDp = 256;
        backgroundView.setId(bgId);
        backgroundView.setBackgroundResource(R.drawable.view_rectangle_object);

        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (widthDp * density);
        int heightPx = (int) (heightDp * density);
        int marginBottomPx = (int) (marginBottomDp * density);

        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(widthPx, heightPx);
        backgroundView.setLayoutParams(viewParams);
        constraintLayout.addView(backgroundView);
        // Добавляем в контейнер
        constraintLayout.addView(textView);

        // 3. Создаём Switch (переключатель)
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchView = new Switch(requireContext());
        int switchId = View.generateViewId();
        switchView.setId(switchId);
// Конвертируем размеры для Switch
        int switchWidthDp = 82;
        int switchHeightDp = 53;
        int switchWidthPx = (int) (switchWidthDp * density);
        int switchHeightPx = (int) (switchHeightDp * density);

        ViewGroup.LayoutParams switchParams = new ViewGroup.LayoutParams(switchWidthPx, switchHeightPx);
        switchView.setLayoutParams(switchParams);

// Очищаем текст (по умолчанию может быть что‑то установлено)
        switchView.setText("");

// Добавляем Switch в контейнер
        constraintLayout.addView(switchView);

// --- Настраиваем ограничения для всех элементов ---
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

// Настройки для backgroundView
//        if (anchorId != -1) {
            // Если есть предыдущий элемент — привязываемся к его низу
            constraintSet.connect(
                    bgId,
                    ConstraintSet.TOP,
                    anchorId,
                    ConstraintSet.BOTTOM
            );

        constraintSet.connect(
                bgId,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
        );
        constraintSet.connect(
                bgId,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
        );
        constraintSet.setVerticalBias(bgId, 0.03f);
        constraintSet.setHorizontalBias(bgId, 0.5f); // Центрирование
        int horizontalMarginPxView = (int) (1 * density);
        constraintSet.setMargin(bgId, ConstraintSet.START, horizontalMarginPxView);

        // Вертикальные привязки для backgroundView (чтобы элемент не «исчез»)
        constraintSet.connect(
                bgId,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );

        // Настройки для TextView
        constraintSet.connect(
                newId,
                ConstraintSet.START,
                bgId,
                ConstraintSet.START
        );
        constraintSet.connect(
                newId,
                ConstraintSet.END,
                switchId,
                ConstraintSet.START
        );
        constraintSet.connect(
                newId,
                ConstraintSet.TOP,
                bgId,
                ConstraintSet.TOP
        );
        constraintSet.connect(
                newId,
                ConstraintSet.BOTTOM,
                bgId,
                ConstraintSet.BOTTOM
        );
        constraintSet.setVerticalBias(newId, 0.5f);
        constraintSet.setHorizontalBias(newId, 0.0f);
        int horizontalMarginPx = (int) (8 * density);
        constraintSet.setMargin(newId, ConstraintSet.START, horizontalMarginPx);
        constraintSet.setMargin(newId, ConstraintSet.END, horizontalMarginPx);
//        constraintSet.setMargin(newId, ConstraintSet.TOP, horizontalMarginPx);

        // Настройки для Switch
        constraintSet.connect(
                switchId,
                ConstraintSet.TOP,
                bgId,
                ConstraintSet.TOP
        );
        constraintSet.connect(
                switchId,
                ConstraintSet.BOTTOM,
                bgId,
                ConstraintSet.BOTTOM
        );
        constraintSet.connect(
                switchId,
                ConstraintSet.END,
                bgId,
                ConstraintSet.END
        );
        constraintSet.setHorizontalBias(switchId, 1.0f); // Прижатие к правому краю

// Применяем все настройки ограничений к контейнеру
        constraintSet.applyTo(constraintLayout);

        deviceTextViewIds.add(bgId);
        return bgId;
    }
    // Метод для возврата к HomeFragment
    private void goBackToHome() {
        // Получаем активность и вызываем метод замены фрагмента
        if (getActivity() instanceof MajorActivity) {
            ((MajorActivity) getActivity()).replaceFragment(new HomeFragment());
        }
    }
}
