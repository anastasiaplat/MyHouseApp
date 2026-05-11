package com.example.myhouseapp0;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

@SuppressLint("ValidFragment")
public class EditObjectDialog extends DialogFragment {

        public interface OnObjectEditedListener {
            void onObjectEdited(String newName, int newWidth, int newHeight);
//            void onPositionEditRequested(); // Новый метод для запроса редактирования позиции
        }

        private String initialName;
        private int initialWidth;
        private int initialHeight;
        private OnObjectEditedListener listener;

    public EditObjectDialog(String initialName, int initialWidth, int initialHeight, OnObjectEditedListener listener) {
        this.initialName = initialName;
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
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
            Button btnSave = dialog.findViewById(R.id.btnConfirm_edit);
            Button btnCancel = dialog.findViewById(R.id.btnCancel_edit);
            Button btnChangePosition = dialog.findViewById(R.id.btn_change_position);

            // Заполняем поля текущими значениями
            etName.setText(initialName);
            etLength.setText(String.valueOf(initialHeight));
            etWidth.setText(String.valueOf(initialWidth));

            btnChangePosition.setOnClickListener(v -> {
                        // Закрываем диалог
                        dialog.dismiss();
                        // Уведомляем о необходимости включить режим редактирования позиции
                        if (listener != null) {
//                            listener.onPositionEditRequested();
                        }
                    });
            btnSave.setOnClickListener(v -> {
                String newName = etName.getText().toString();
                int newWidth = Integer.parseInt(etWidth.getText().toString());
                int newHeight = Integer.parseInt(etLength.getText().toString());

                if (listener != null) {
                    listener.onObjectEdited(newName, newWidth, newHeight);
                }
                dialog.dismiss();
            });

            btnCancel.setOnClickListener(v -> dialog.dismiss());

            return dialog;
        }
}
