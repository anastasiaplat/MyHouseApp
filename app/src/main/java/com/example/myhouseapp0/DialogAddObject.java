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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class DialogAddObject extends DialogFragment {
    private OnObjectAddedListener listener;
    private DB_helper dbHelper;
    private Spinner devicesSpinner;
    private EditText editTextName, editTextSizeX, editTextSizeY;
    private Button btn_Confirm, btn_Cancel;

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

        initViews(dialogView);
        loadDevicesToSpinner();

        builder.setView(dialogView)
                .setTitle("Добавить объект");

        return builder.create();
    }

    private void initViews(View view) {
        editTextName = view.findViewById(R.id.etObjectName);
        editTextSizeX = view.findViewById(R.id.etWidth);
        editTextSizeY = view.findViewById(R.id.etLength);
        devicesSpinner = view.findViewById(R.id.spinnerDevices);
        btn_Confirm = view.findViewById(R.id.btnConfirm);
        btn_Cancel = view.findViewById(R.id.btnCancel);

        btn_Confirm.setOnClickListener(v -> saveObject());
        btn_Cancel.setOnClickListener(v -> dismiss());
    }
    private void loadDevicesToSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(dbHelper.TABLE_DEVICES,
                new String[]{dbHelper.COLUMN_DEVICE_ID, dbHelper.COLUMN_DEVICE_NAME},
                null, null, null, null, null);

        List<String> deviceNames = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                deviceNames.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                deviceNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devicesSpinner.setAdapter(adapter);
    }
    private void saveObject() {
        String name = editTextName.getText().toString().trim();
        String sizeXStr = editTextSizeX.getText().toString();
        String sizeYStr = editTextSizeY.getText().toString();}
}

