package com.example.myhouseapp0;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

public class DialogAddObject extends DialogFragment {
    private OnObjectAddedListener listener;
    private DB_helper dbHelper;
    private Spinner devicesSpinner;
    private EditText nameEditText;
    private EditText widthEditText, heightEditText;

    public interface OnObjectAddedListener {
        void onObjectAdded(MapObject object);
    }

    public void setOnObjectAddedListener(OnObjectAddedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_object, null);

        dbHelper = new DB_helper(requireContext());
        devicesSpinner = dialogView.findViewById(R.id.spinnerDevices);
        nameEditText = dialogView.findViewById(R.id.etObjectName);
        widthEditText = dialogView.findViewById(R.id.etWidth);
        heightEditText = dialogView.findViewById(R.id.etHeight);

        // Заполняем выпадающий список устройствами из БД
        List<String> deviceList = dbHelper.getDevices();
//        devicesSpinner.setItems(deviceList);

        builder.setView(dialogView)
                .setTitle("Добавить объект")
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    saveObject();
                })
                .setNegativeButton("Отмена", null);
        return builder.create();
    }

    private void saveObject() {
        String name = nameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Введите название", Toast.LENGTH_SHORT).show();
            return;
        }
//
//        List<String> selectedDevices = devicesSpinner.getSelectedStrings();
//        if (selectedDevices.isEmpty()) {
//            Toast.makeText(requireContext(), "Выберите хотя бы одно устройство", Toast.LENGTH_SHORT).show();
//            return;
//        }

        float width = Float.parseFloat(widthEditText.getText().toString());
        float height = Float.parseFloat(heightEditText.getText().toString());

        MapObject object = new MapObject();
        object.setName(name);
//        object.setDevices(selectedDevices);
        object.setWidth(width);
        object.setHeight(height);

        if (listener != null) {
            listener.onObjectAdded(object);
        }
    }
}

