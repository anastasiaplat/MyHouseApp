package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InteractiveMapFragment extends Fragment implements OnObjectAddedListener {

    private RelativeLayout relativeLayout;
    private TextView textInfo;
    private int buttonCounter = 0; // счётчик для позиционирования кнопок
    private static final int MARGIN_DP = 16; // отступ между кнопками в dp
    private List<Button> buttons = new ArrayList<>(); // список всех созданных кнопок
    private boolean isWaitingForPosition = false; // флаг ожидания выбора позиции
    private ButtonConfig pendingButtonConfig; // конфигурация кнопки, ожидающей позиции
    private View previewOverlay; // предпросмотр кнопки
    private Button applyButton;
    private boolean isInEditMode = false; // флаг режима редактирования
    private Button selectedButtonForEdit; //выбранная для редактирования кнопка


    public void enterEditMode() {
        if (buttons.isEmpty()) {
            Toast.makeText(requireContext(), "Нет объектов для редактирования", Toast.LENGTH_SHORT).show();
            return;
        }

        isInEditMode = true;
        Toast.makeText(requireContext(), "Выберите объект для редактирования", Toast.LENGTH_SHORT).show();
        highlightAllButtons();

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactive_map, container, false);
        relativeLayout = view.findViewById(R.id.map_view);

        textInfo = new TextView(requireContext());
        textInfo.setId(View.generateViewId());
        textInfo.setText("Объекты не добавлены");
        // Задаём параметры ширины и высоты
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        // Устанавливаем атрибут для центрирования по горизонтали и вертикали относительно родителя
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        // Применяем параметры к TextView
        textInfo.setLayoutParams(params);

        // Дополнительно можно настроить внешний вид TextView
        textInfo.setGravity(Gravity.CENTER); // центрирование текста внутри TextView
        textInfo.setTextSize(16); // размер текста
        textInfo.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar)); // цвет текста

        // Добавляем TextView в родительский RelativeLayout
        relativeLayout.addView(textInfo);


//        if (buttons.isEmpty()) {
//            textInfo.setVisibility(View.VISIBLE);
//            Toast.makeText(requireContext(), "отображаем", Toast.LENGTH_SHORT).show();
//            } else {
//            textInfo.setVisibility(View.GONE);
//            Toast.makeText(requireContext(), "скрываем в онкриет", Toast.LENGTH_SHORT).show();
//        }


        // Обработчик касаний по контейнеру
        if (this.relativeLayout != null) {
            this.relativeLayout.setOnTouchListener((v, event) -> {
                            if (isInEditMode) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    selectButtonForEdit(event.getX(), event.getY());
                                    return true;
                                }
                                return false;
                            }
                if (isWaitingForPosition)
                    try {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            showPreviewAtPosition(event.getX(), event.getY());
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            updatePreviewPosition(event.getX(), event.getY());
                            return true;
                        default:
                            return false;
                    }
                }catch (Exception e) {
                        Toast.makeText(requireContext(), "Ошибка обработки касания", Toast.LENGTH_SHORT).show();
                    }
                return false;
            });
        }
        return view;
    }
    /**
     * Конфигурация для создания кнопки
     */
    private static class ButtonConfig {
        String text;
        int width;
        int height;
        int color;
        boolean isStandalone;

        ButtonConfig(String text, int width, int height, int color, boolean isStandalone) {
            this.text = text;
            this.width = width;
            this.height = height;
            this.color = color;
            this.isStandalone = isStandalone;
        }
    }
    public void createButton(String text, int width, int height, int color, boolean isStandalone) {
        if (relativeLayout == null || !isAdded()) {
            pendingButtonConfig = new ButtonConfig(text, width, height, color, isStandalone);
            return;
        }

        textInfo.setVisibility(View.GONE);

        // Для первой кнопки всегда standalone
        if (buttons.isEmpty()) {
            isStandalone = true;
        }

        pendingButtonConfig = new ButtonConfig(text, width, height, color, isStandalone);
        isWaitingForPosition = true;

        // Визуальная индикация ожидания выбора позиции (опционально)
        if (relativeLayout != null && isAdded()) {
            relativeLayout.setBackgroundColor(0xFFEDFCEC);
            Toast.makeText(requireContext(), "Выберите место для установки объекта", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Показывает предварительный просмотр кнопки в указанной позиции
     */
    private void showPreviewAtPosition(float x, float y) {
        if (pendingButtonConfig == null || relativeLayout == null) return;





        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (pendingButtonConfig.width * density);
        int heightPx = (int) (pendingButtonConfig.height * density);

        // Создаём предварительный просмотр только если его ещё нет
        if (previewOverlay == null) {
            previewOverlay = new View(requireContext());
            previewOverlay.setBackgroundColor(0x80C0C0C0); // Полупрозрачный серый

            RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(widthPx, heightPx);
            previewParams.leftMargin = (int) x - widthPx / 2;
            previewParams.topMargin = (int) y - heightPx / 2;
            previewOverlay.setLayoutParams(previewParams);

            relativeLayout.addView(previewOverlay);
        } else {
            // Обновляем позицию существующего превью
            updatePreviewPosition(x, y);
        }
        // Создаём кнопку "Применить" только если её ещё нет
        if (applyButton == null) {
            applyButton = new Button(requireContext());
            applyButton.setText("Применить");
            applyButton.setWidth(150);
            applyButton.setHeight(40);
            applyButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
            applyButton.setAllCaps(false);
            applyButton.setTextSize(15);
            applyButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white_green));

            // Используем RelativeLayout.LayoutParams, чтобы позиционировать кнопку внутри RelativeLayout (container)
            RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            // Располагаем кнопку внизу контейнера
            buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            // Центрируем кнопку по горизонтали
            buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            // Добавляем небольшие отступы (16dp), чтобы кнопка не прилипала к краям
            int marginInDp = 16;
            float density_preview_btn = getResources().getDisplayMetrics().density;
            int marginInPx = (int) (marginInDp * density);
            buttonParams.setMargins(0, 0, 0, marginInPx);
            applyButton.setLayoutParams(buttonParams);

            // Добавляем кнопку в контейнер
            relativeLayout.addView(applyButton);

            // Обработчик кнопки "Применить"
            applyButton.setOnClickListener(v -> {
                    if (!createButtonAtPositionFromPreview(
                            (int) (pendingButtonConfig.width * getResources().getDisplayMetrics().density),
                            (int) (pendingButtonConfig.height * getResources().getDisplayMetrics().density)
                    )) {
                        // Если создание не удалось (есть пересечение), остаёмся в режиме предварительного просмотра
                        return;
                    }

                // Создаём реальную кнопку в позиции предварительного просмотра
                createButtonAtPositionFromPreview(widthPx, heightPx);

                // Убираем предварительный просмотр и кнопку "Применить"
                relativeLayout.removeView(previewOverlay);
                relativeLayout.removeView(applyButton);
                previewOverlay = null;
                applyButton = null;

                isWaitingForPosition = false;
                relativeLayout.setBackgroundColor(0x00000000);
            });
        }
    }
    /**
     * Обновляет позицию предварительного просмотра
     */
    private void updatePreviewPosition(float x, float y) {
        if (previewOverlay == null) return;

        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (pendingButtonConfig.width * density);
        int heightPx = (int) (pendingButtonConfig.height * density);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) previewOverlay.getLayoutParams();
        params.leftMargin = (int) x - widthPx / 2;
        params.topMargin = (int) y - heightPx / 2;

        previewOverlay.setLayoutParams(params);
    }
    /**
     * Создаёт реальную кнопку на основе позиции предварительного просмотра
     */
    private boolean createButtonAtPositionFromPreview(int widthPx, int heightPx) {
        // Получаем позицию предварительного просмотра
        RelativeLayout.LayoutParams previewParams = (RelativeLayout.LayoutParams) previewOverlay.getLayoutParams();
        int finalX = previewParams.leftMargin;
        int finalY = previewParams.topMargin;


        // Создаём прямоугольник для новой кнопки
        Rect newButtonRect = new Rect(finalX, finalY, finalX + widthPx, finalY + heightPx);

        // Проверяем наложение с существующими кнопками
        if (hasOverlap(newButtonRect)) {
            Toast.makeText(requireContext(), "Кнопка перекрывает существующую. Выберите другое место", Toast.LENGTH_SHORT).show();
            return false; // Прерываем создание кнопки
        }
        // Проверяем, что кнопка внутри контейнера
        if (!isInsideContainer(newButtonRect)) {
            Toast.makeText(requireContext(), "Кнопка выходит за границы контейнера. Переместите её внутрь", Toast.LENGTH_SHORT).show();
            return false;
        }

        Button newButton = new Button(requireContext());
        newButton.setText(pendingButtonConfig.text);
        newButton.setBackgroundColor(pendingButtonConfig.color);
        newButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
        newButton.setTextSize(18);
        newButton.setAllCaps(false);
        // Убираем стандартные отступы кнопки
        newButton.setMinWidth(0);
        newButton.setMinHeight(0);
        newButton.setPadding(0, 0, 0, 0);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widthPx, heightPx);
        params.leftMargin = finalX;
        params.topMargin = finalY;

        newButton.setLayoutParams(params);
        newButton.setId(View.generateViewId());
        relativeLayout.addView(newButton);
        buttons.add(newButton);
        return true;
    }
        /**
     * Проверяет, есть ли наложение с существующими кнопками
     */
    private boolean hasOverlap(Rect testRect) {
        for (Button existingButton : buttons) {
            Rect existingRect = getButtonBounds(existingButton);
            if (Rect.intersects(testRect, existingRect)) {
                return true;
            }
        }
        return false;
    }

   /**
     * Получает границы кнопки в координатах контейнера
     */
    private Rect getButtonBounds(Button button) {
        int[] location = new int[2];
        button.getLocationOnScreen(location);

        // Получаем координаты контейнера
        int[] containerLocation = new int[2];
        relativeLayout.getLocationOnScreen(containerLocation);

        // Переводим в координаты контейнера
        int buttonLeft = location[0] - containerLocation[0];
        int buttonTop = location[1] - containerLocation[1];

        return new Rect(
                buttonLeft,
                buttonTop,
                buttonLeft + button.getWidth(),
                buttonTop + button.getHeight()
        );
    }

    /**
     * Проверяет, находится ли прямоугольник полностью внутри контейнера
     */
    private boolean isInsideContainer(Rect rect) {
        if (relativeLayout == null) return false;

        int containerWidth = relativeLayout.getWidth();
        int containerHeight = relativeLayout.getHeight();

        return rect.left >= 0 &&
                rect.top >= 0 &&
                rect.right <= containerWidth &&
                rect.bottom <= containerHeight;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Очищаем ссылки при уничтожении фрагмента
        if (previewOverlay != null && relativeLayout != null) {
            relativeLayout.removeView(previewOverlay);
        }
        if (applyButton != null && relativeLayout != null) {
            relativeLayout.removeView(applyButton);
        }
        previewOverlay = null;
        applyButton = null;
    }



    /**
     * Подсвечивает все кнопки рамкой для выбора
     */
    public void highlightAllButtons() {
        isInEditMode = true;
        for (Button button : buttons) {
            // Или простой вариант — изменить цвет фона с прозрачностью
            button.setBackgroundColor(0x80ADD8E6); // Полупрозрачный голубой
        }
    }

    /**
     * Снимает подсветку со всех кнопок
     */
    private void removeButtonHighlights() {
        for (Button button : buttons) {
            // Возвращаем исходный цвет (сохраняйте исходный цвет при создании кнопки)
            button.setBackgroundColor(getOriginalButtonColor(button));
        }
    }
    /**
     * Получает исходный цвет кнопки (реализуйте хранение цветов)
     */
    private int getOriginalButtonColor(Button button) {
        // Здесь должна быть логика получения исходного цвета
        // Например, можно хранить цвета в Map<Button, Integer>
        return 0xFF90EE90; // Возвращаем светло‑зелёный по умолчанию
    }


    /**
     * Обрабатывает выбор кнопки для редактирования
     */
    private void selectButtonForEdit(float x, float y) {
        for (Button button : buttons) {
            Rect buttonRect = getButtonBounds(button);
            if (buttonRect.contains((int) x, (int) y)) {
                // Передаём выбор кнопки в HomeFragment для показа диалога
                ((HomeFragment) requireParentFragment()).showEditDialog(button);
                return;
            }
        }
    }

    /**
     * Применяет изменения к кнопке после редактирования (вызывается из HomeFragment)
     */
    public void applyButtonEdit(Button button, String newName, int newWidth, int newHeight) {
        button.setText(newName);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) button.getLayoutParams();
        params.width = newWidth;
        params.height = newHeight;
        button.setLayoutParams(params);

        isInEditMode = false;
        removeButtonHighlights();
    }







    @Override
    public void onObjectAdded(String objectName, Integer sizeX, Integer sizeY) {
//        addButtonToMap(objectName);
    }
}