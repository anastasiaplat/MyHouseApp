package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class EditObjectDialog extends DialogFragment {

        public interface OnObjectEditedListener {
            void onObjectEdited(String newName, int newWidth, int newLength,  List<String> selectedDevices);
//            void onPositionEditRequested(); // Новый метод для запроса редактирования позиции
void onObjectDeleted(); // Новый метод для обработки удаления
        }

        private String initialName;
        private int initialWidth;
        private int initialHeight;
    private boolean[] checkedDevices;
    private List<String> allDevices;
    private List<String> selectedDevices; // Список устройств для редактирования
        private OnObjectEditedListener listener;

    public EditObjectDialog(String initialName, int initialWidth, int initialHeight,
                            List<String> allDevices, List<String> selectedDevices,
                            OnObjectEditedListener listener) {
        this.initialName = initialName;
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        this.allDevices = allDevices != null ? new ArrayList<>(allDevices) : new ArrayList<>();
        this.selectedDevices = selectedDevices != null ? new ArrayList<>(selectedDevices) : new ArrayList<>();
        this.listener = listener;
    }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_object);
            dialog.setTitle("Редактирование объекта");

            EditText etName = dialog.findViewById(R.id.etObjectName_edit);
            EditText etLength = dialog.findViewById(R.id.etLength_edit);
            EditText etWidth = dialog.findViewById(R.id.etWidth_edit);
            ListView devices = dialog.findViewById(R.id.spinnerDevices_edit);
            Button btnSave = dialog.findViewById(R.id.btnConfirm_edit);
            Button btnCancel = dialog.findViewById(R.id.btnCancel_edit);
//            Button btnChangePosition = dialog.findViewById(R.id.btn_change_position);
            Button btnDelete = dialog.findViewById(R.id.btnDelete_edit);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_list_item_multiple_choice,
                    allDevices
            );
            devices.setAdapter(arrayAdapter);
            devices.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

// Отмечаем устройства, которые были выбраны ранее
            for (int i = 0; i < allDevices.size(); i++) {
                String device = allDevices.get(i);
                if (selectedDevices.contains(device)) {
                    devices.setItemChecked(i, true);
                }
            }

            // Заполняем поля текущими значениями
            etName.setText(initialName);
            etLength.setText(String.valueOf(initialHeight));
            etWidth.setText(String.valueOf(initialWidth));

//            btnChangePosition.setOnClickListener(v -> {
//                        // Закрываем диалог
//                        dialog.dismiss();
//                        // Уведомляем о необходимости включить режим редактирования позиции
//                        if (listener != null) {
////                            listener.onPositionEditRequested();
//                        }
//                    });
            btnSave.setOnClickListener(v -> {
                String newName = etName.getText().toString();
                String newWidth = etWidth.getText().toString();
                String newLength = etLength.getText().toString();
                boolean isValid = true;
                int newOriginalSizeX, newOriginalSizeY; // исходные значения для проверок
                int newFinalSizeX = 0, newFinalSizeY = 0; // итоговые размеры для кнопки

                // Проверка на заполнение имени
                if (newName.isEmpty()) {
                    etName.setError("Поле обязательно для заполнения");
                    isValid = false;
                } else if (!isNameUnique(newName)) {
                    etName.setError("Название должно быть уникальным");
                    isValid = false;
                } else {
                    etName.setError(null);
                }

                int newSizeX, newSizeY;
                // Проверка sizeX
                if (newWidth.isEmpty()) {
                    etWidth.setError("Поле обязательно для заполнения");
                    isValid = false;
                } else {
                    try {
                        newOriginalSizeX = Integer.parseInt(newWidth);
                        if (newOriginalSizeX < 100 || newOriginalSizeX > 1000) {
                            etWidth.setError("Значение допустимо от 100 до 1000");
                            isValid = false;
                        } else {
                            etWidth.setError(null);
                            newFinalSizeX = newOriginalSizeX / 5; // конвертация в масштабе 5:1
                        }
                    } catch (NumberFormatException e) {
                        etWidth.setError("Введите корректное число");
                        isValid = false;
                    }
                }

                // Проверка sizeY
                if (newLength.isEmpty()) {
                    etLength.setError("Поле обязательно для заполнения");
                    isValid = false;
                } else {
                    try {
                        newOriginalSizeY = Integer.parseInt(newLength);
                        if (newOriginalSizeY < 100 || newOriginalSizeY > 1000) {
                            etLength.setError("Значение допустимо от 100 до 1000");
                            isValid = false;
                        } else {
                            etLength.setError(null);
                            newFinalSizeY = newOriginalSizeY / 5; // конвертация в масштабе 5:1
                        }
                    } catch (NumberFormatException e) {
                        etLength.setError("Введите корректное число");
                        isValid = false;
                    }
                }
                // Получаем выбранные устройства
//                SparseBooleanArray checkedItems = devices.getCheckedItemPositions();
//                List<String> selectedDevices = new ArrayList<>();
//                for (int i = 0; i < arrayAdapter.getCount(); i++) {
//                    if (checkedItems.get(i)) {
//                        selectedDevices.add(arrayAdapter.getItem(i));
//                    }
//                }

// Получаем выбранные устройства
                List<String> finalSelectedDevices = new ArrayList<>();
                for (int i = 0; i < allDevices.size(); i++) {
                    if (devices.isItemChecked(i)) {
                        finalSelectedDevices.add(allDevices.get(i));
                    }
                }


                if (finalSelectedDevices.isEmpty()) {
                    Toast.makeText(requireContext(), "Выберите хотя бы одно устройство", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }


                if (isValid) {
                    // Все проверки пройдены — выполняем основное действие
                    if (listener != null) {
                        listener.onObjectEdited(newName, newFinalSizeX, newFinalSizeY, finalSelectedDevices);

                    }
                    dialog.dismiss();

                }

            });

            btnCancel.setOnClickListener(v -> dialog.dismiss());

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onObjectDeleted();
                }
                dialog.dismiss();
            });


            return dialog;
        }
    private boolean isNameUnique(String name) {
        // Здесь может быть проверка в базе данных, SharedPreferences или списке
        // Пример с простым списком:
        List<String> existingNames = getExistingNames(); // ваш источник данных
        return !existingNames.contains(name);
    }

    private List<String> getExistingNames() {
        // Реализация получения существующих имён
        // Может быть запрос к БД, чтение из файла и т. д.
        return new ArrayList<>(); // замените на реальную реализацию
    }
    // Метод для получения списка доступных устройств
    // В реальном приложении данные могут приходить из БД или API
    private List<String> getAvailableDevices() {
        List<String> devices = new ArrayList<>();
        devices.add("Устройство 1");
        devices.add("Устройство 2");
        devices.add("Устройство 3");
        devices.add("Устройство 4");
        devices.add("Устройство 5");
        return devices;
    }
}
