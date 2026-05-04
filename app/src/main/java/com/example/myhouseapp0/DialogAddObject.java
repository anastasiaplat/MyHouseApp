package com.example.myhouseapp0;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogAddObject extends DialogFragment {
    private OnObjectAddedListener listener;
    private DB_helper dbHelper;
//    private Spinner devicesSpinner;
    private EditText etName, etSizeX, etSizeY;
    private Button btn_Confirm, btn_Cancel;
    private DeviceMultiSelectAdapter adapter;
    private List<String> deviceList;
    private OnDevicesSelectedListener listener_devices;
    public interface OnDevicesSelectedListener {
        void onDevicesSelected(List<String> selectedDevices);
    }

    public interface OnObjectAddedListener {
        void onObjectAdded(String name, int sizeX, int sizeY);
    }

    public void setOnObjectAddedListener(OnObjectAddedListener listener) {
        this.listener = listener;
    }
    public static DialogFragment newInstance(OnDevicesSelectedListener listener) {
        DialogFragment fragment = new DialogAddObject();
        ((DialogAddObject) fragment).listener_devices = listener;
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_object, null);

        builder.setView(dialogView);
        dbHelper = new DB_helper(requireContext());


//        initViews(dialogView);
//        loadDevicesToSpinner();

        // Инициализация списка устройств (замените на свои данные)
        deviceList = Arrays.asList(
                "Устройство 1",
                "Устройство 2",
                "Устройство 3",
                "Устройство 4",
                "Устройство 5"
        );

        ListView listView = dialogView.findViewById(R.id.spinnerDevices);
        adapter = new DeviceMultiSelectAdapter(requireContext(), deviceList);
        listView.setAdapter(adapter);

        // Обработчик выбора элементов
        listView.setOnItemClickListener((parent, view, position, id) -> {
            boolean isChecked = !adapter.checkedItems.get(position, false);
            adapter.setItemChecked(position, isChecked);
        });

        etName = dialogView.findViewById(R.id.etObjectName);
//        Spinner spinnerDevices = dialogView.findViewById(R.id.spinnerDevices);
        etSizeX = dialogView.findViewById(R.id.etWidth);
        etSizeX = dialogView.findViewById(R.id.etLength);

        // Заполняем Spinner устройствами (замените на свои данные)
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                requireContext(),
//                android.R.layout.simple_spinner_item,
//                Arrays.asList("Устройство 1", "Устройство 2", "Устройство 3")
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDevices.setAdapter(adapter);

        btn_Confirm = dialogView.findViewById(R.id.btnConfirm);
        btn_Cancel = dialogView.findViewById(R.id.btnCancel);

        btn_Cancel.setOnClickListener(v -> dismiss());

        btn_Confirm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String sizeXStr = etSizeX.getText().toString();
            String sizeYStr = etSizeY.getText().toString();
//            List<String> selectedDevices = new ArrayList<>();
//            selectedDevices.add(spinnerDevices.getSelectedItem().toString());

            if (name.isEmpty() || sizeXStr.isEmpty() || sizeYStr.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int sizeX = Integer.parseInt(sizeXStr);
                int sizeY = Integer.parseInt(sizeYStr);

                if (listener != null) {
                    listener.onObjectAdded(name, sizeX, sizeY);
                }
                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Введите корректные числа для размеров", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }

//    private void initViews(View view) {
//        editTextName = view.findViewById(R.id.etObjectName);
//        editTextSizeX = view.findViewById(R.id.etWidth);
//        editTextSizeY = view.findViewById(R.id.etLength);
//        devicesSpinner = view.findViewById(R.id.spinnerDevices);
//        btn_Confirm = view.findViewById(R.id.btnConfirm);
//        btn_Cancel = view.findViewById(R.id.btnCancel);
//
//        btn_Confirm.setOnClickListener(v -> saveObject());
//        btn_Cancel.setOnClickListener(v -> dismiss());
//    }
//    private void loadDevicesToSpinner() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query(dbHelper.TABLE_DEVICES,
//                new String[]{dbHelper.COLUMN_DEVICE_ID, dbHelper.COLUMN_DEVICE_NAME},
//                null, null, null, null, null);
//
//        List<String> deviceNames = new ArrayList<>();
//        if (cursor.moveToFirst()) {
//            do {
//                deviceNames.add(cursor.getString(1));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                requireContext(),
//                android.R.layout.simple_spinner_item,
//                deviceNames
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        devicesSpinner.setAdapter(adapter);
//    }
//    private void saveObject() {
//        String name = editTextName.getText().toString().trim();
//        String sizeXStr = editTextSizeX.getText().toString();
//        String sizeYStr = editTextSizeY.getText().toString();}
}

