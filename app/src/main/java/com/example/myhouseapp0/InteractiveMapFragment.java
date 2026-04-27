package com.example.myhouseapp0;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class InteractiveMapFragment extends Fragment implements OnObjectAddedListener{

    private RelativeLayout relativeLayout;
    private int buttonCounter = 0; // счётчик для позиционирования кнопок
    private static final int MARGIN_DP = 16; // отступ между кнопками в dp

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interactive_map, container, false);
    }

    @Override
    public void onObjectAdded(String objectName, Integer sizeX, Integer sizeY) {

        addButtonToMap(objectName);
    }
    private void addButtonToMap(String name) {
        // Создаём новую кнопку
        Button newButton;
        newButton = new Button(requireContext());
        newButton.setText(name);

        // Задаём фиксированный размер (квадратная кнопка)
        int buttonSize = dpToPx(80); // размер 80dp
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                buttonSize,
                buttonSize
        );

        // Позиционируем кнопку с отступами
        params.leftMargin = dpToPx(MARGIN_DP) + (buttonCounter % 3) * (buttonSize + dpToPx(MARGIN_DP));
        params.topMargin = dpToPx(MARGIN_DP * 2) + (buttonCounter / 3) * (buttonSize + dpToPx(MARGIN_DP));

        // Добавляем обработчик нажатия
        newButton.setOnClickListener(v -> {
            // Здесь можно добавить логику для работы с картой
            // Например, при нажатии на кнопку — центрировать карту на этом объекте
            // или показывать дополнительную информацию
        });

        newButton.setLayoutParams(params);
        relativeLayout.addView(newButton);

        buttonCounter++;
    }

    /**
     * Конвертирует dp в пиксели
     */
    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}