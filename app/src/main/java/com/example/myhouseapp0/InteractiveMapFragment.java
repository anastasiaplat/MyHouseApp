package com.example.myhouseapp0;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InteractiveMapFragment extends Fragment implements OnObjectAddedListener {

    private RelativeLayout relativeLayout;
    private int buttonCounter = 0; // счётчик для позиционирования кнопок
    private static final int MARGIN_DP = 16; // отступ между кнопками в dp
    private List<Button> buttons = new ArrayList<>(); // Список всех созданных кнопок
    private boolean isWaitingForPosition = false; // Флаг ожидания выбора позиции
    private ButtonConfig pendingButtonConfig; // Конфигурация кнопки, ожидающей позиции
    private View previewOverlay; // Предпросмотр кнопки
    private Button applyButton; // Кнопка "Применить"

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactive_map, container, false);
        relativeLayout = view.findViewById(R.id.map_view);

        // Обработчик касаний по контейнеру
        if (this.relativeLayout != null) {
            this.relativeLayout.setOnTouchListener((v, event) -> {
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
    /**
     * Запускает процесс создания кнопки — ожидает выбора позиции пользователем
     * @param text Текст на кнопке
     * @param width Ширина кнопки в dp
     * @param height Высота кнопки в dp
     * @param color Цвет фона кнопки
     * @param isStandalone Если true — кнопка будет отдельно стоящей
     */
    public void createButton(String text, int width, int height, int color, boolean isStandalone) {
        if (relativeLayout == null || !isAdded()) {
            pendingButtonConfig = new ButtonConfig(text, width, height, color, isStandalone);
            return;
        }

        // Для первой кнопки всегда standalone
        if (buttons.isEmpty()) {
            isStandalone = true;
        }

        pendingButtonConfig = new ButtonConfig(text, width, height, color, isStandalone);
        isWaitingForPosition = true;

        // Визуальная индикация ожидания выбора позиции (опционально)
        if (relativeLayout != null && isAdded()) {
            relativeLayout.setBackgroundColor(0x20FF0000);
        }
    }
    /**
     * Создаёт кнопку в указанной позиции
     * @param x Координата X в пикселях
     * @param y Координата Y в пикселях
     */

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
            applyButton.setBackgroundColor(0xFF4CAF50);
            applyButton.setTextColor(0xFFFFFFFF);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            buttonParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            applyButton.setLayoutParams(buttonParams);

            // Добавляем кнопку в контейнер
            relativeLayout.addView(applyButton);

            // Обработчик кнопки "Применить"
            applyButton.setOnClickListener(v -> {
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
//
//
//        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        buttonParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        applyButton.setLayoutParams(buttonParams);
//
//        // Добавляем кнопку в контейнер
//        relativeLayout.addView(applyButton);
//
//        // Обработчик перемещения предварительного просмотра
//        previewOverlay.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                float newX = event.getRawX();
//                float newY = event.getRawY();
//
//                // Переводим координаты в систему контейнера
//                int[] containerLocation = new int[2];
//                relativeLayout.getLocationOnScreen(containerLocation);
//                newX -= containerLocation[0];
//                newY -= containerLocation[1];
//
//                updatePreviewPosition((int) newX, (int) newY, widthPx, heightPx);
//                return true;
//            }
//            return false;
//        });

        // Обработчик кнопки "Применить"
//        applyButton.setOnClickListener(v -> {
//            // Создаём реальную кнопку в позиции предварительного просмотра
//            createButtonAtPositionFromPreview(widthPx, heightPx);
//
//            // Убираем предварительный просмотр и кнопку "Применить"
//            relativeLayout.removeView(previewOverlay);
//            relativeLayout.removeView(applyButton);
//            previewOverlay = null;
//            applyButton = null;
//
//            isWaitingForPosition = false;
//            relativeLayout.setBackgroundColor(0x00000000);
//        });
//    }
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
    private void createButtonAtPositionFromPreview(int widthPx, int heightPx) {
        // Получаем позицию предварительного просмотра
        RelativeLayout.LayoutParams previewParams = (RelativeLayout.LayoutParams) previewOverlay.getLayoutParams();
        int finalX = previewParams.leftMargin;
        int finalY = previewParams.topMargin;

        Point finalPosition = findNearestFreeSpace(finalX, finalY, widthPx, heightPx);

        if (finalPosition == null) {
            Toast.makeText(requireContext(), "Не удалось найти свободное место для кнопки", Toast.LENGTH_SHORT).show();
            return;
        }

        Button newButton = new Button(requireContext());
        newButton.setText(pendingButtonConfig.text);
        newButton.setBackgroundColor(pendingButtonConfig.color);
        // Убираем стандартные отступы кнопки
        newButton.setMinWidth(0);
        newButton.setMinHeight(0);
        newButton.setPadding(0, 0, 0, 0);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widthPx, heightPx);
        params.leftMargin = finalPosition.x;
        params.topMargin = finalPosition.y;

        newButton.setLayoutParams(params);
        newButton.setId(View.generateViewId());
        relativeLayout.addView(newButton);
        buttons.add(newButton);
    }
    /**
     * Рассчитывает финальную позицию кнопки с проверкой наложения
     */
    private Point calculateFinalPosition(float touchX, float touchY, int newWidth, int newHeight) {
        int startX = (int) touchX - newWidth / 2;
        int startY = (int) touchY - newHeight / 2;

        Point position = findNearestFreeSpace(startX, startY, newWidth, newHeight);
        return limitToContainerBounds(position, newWidth, newHeight);
    }
    /**
     * Находит ближайшее свободное место для кнопки
     */
    private Point findNearestFreeSpace(int x, int y, int width, int height) {
        Rect newButtonRect = new Rect(x, y, x + width, y + height);

        if (!hasOverlap(newButtonRect)) {
            return new Point(x, y);
        }

        List<Point> candidatePositions = new ArrayList<>();

        int[] offsetsX = {-width, 0, width, 0};
        int[] offsetsY = {0, -height, 0, height};

        for (int i = 0; i < offsetsX.length; i++) {
            int testX = x + offsetsX[i];
            int testY = y + offsetsY[i];
            Rect testRect = new Rect(testX, testY, testX + width, testY + height);
            if (!hasOverlap(testRect) && isInsideContainer(testRect)) {
                candidatePositions.add(new Point(testX, testY));
            }
        }

    // Если нашли свободные позиции, выбираем ближайшую к исходной точке
    if (!candidatePositions.isEmpty()) {
        return findClosestPoint(candidatePositions, x, y);
    }

    // Если не нашли свободных позиций в основных направлениях, ищем более далеко
    return searchWiderArea(x, y, width, height);
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





    private void createButtonAtPosition(float x, float y) {
        if (pendingButtonConfig == null) return;

        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (pendingButtonConfig.width * density);
        int heightPx = (int) (pendingButtonConfig.height * density);

        Point finalPosition = calculateFinalPosition(x, y, widthPx, heightPx);

        Button newButton = new Button(requireContext());
        newButton.setText(pendingButtonConfig.text);
        newButton.setBackgroundColor(pendingButtonConfig.color);
        // Убираем стандартные отступы кнопки
        newButton.setMinWidth(0);
        newButton.setMinHeight(0);
        newButton.setPadding(0, 0, 0, 0);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widthPx, heightPx);

//        if (pendingButtonConfig.isStandalone || buttons.isEmpty()) {
//            // Отдельно стоящая кнопка или первая кнопка
////            params.leftMargin = (int) x - widthPx / 2;
////            params.topMargin = (int) y - heightPx / 2;
//            params.leftMargin = Math.max(0, (int) x - widthPx / 2);
//            params.topMargin = Math.max(0, (int) y - heightPx / 2);
//        } else {
//            // Примыкающая к ближайшей кнопке
//
//            Button nearestButton = findNearestButton(x, y);
////            Point attachmentPoint = calculateAttachmentPoint(nearestButton, x, y);
////
////            params.leftMargin = attachmentPoint.x;
////            params.topMargin = attachmentPoint.y;
//            if (nearestButton != null) {
//                Point attachmentPoint = calculateAttachmentPoint(nearestButton, x, y, widthPx, heightPx);
//                params.leftMargin = attachmentPoint.x;
//                params.topMargin = attachmentPoint.y;
//            } else {
//                // Если ближайшая кнопка не найдена, размещаем как отдельно стоящую
//                params.leftMargin = Math.max(0, (int) x - widthPx / 2);
//                params.topMargin = Math.max(0, (int) y - heightPx / 2);
//            }
//        }

        params.leftMargin = finalPosition.x;
        params.topMargin = finalPosition.y;


        newButton.setLayoutParams(params);
        newButton.setId(View.generateViewId());
        relativeLayout.addView(newButton);
        buttons.add(newButton);

        // Сброс состояния
        pendingButtonConfig = null;
        if (relativeLayout != null && isAdded()) {
            relativeLayout.setBackgroundColor(0x00000000);
        }
    }
    /**
     * Рассчитывает финальную позицию кнопки с проверкой наложения
     */
//    private Point calculateFinalPosition(float touchX, float touchY, int newWidth, int newHeight) {
//        // Начальная позиция — точно по центру касания
//        int startX = (int) touchX - newWidth / 2;
//        int startY = (int) touchY - newHeight / 2;
//
//        // Проверяем наложение с существующими кнопками
//        Point position = findNearestFreeSpace(startX, startY, newWidth, newHeight);
//
//        // Ограничиваем позицию границами контейнера
//        return limitToContainerBounds(position, newWidth, newHeight);
//    }
//    private Point findNearestFreeSpace(int x, int y, int width, int height) {
//        Rect newButtonRect = new Rect(x, y, x + width, y + height);
//
//        // Если нет наложения, возвращаем исходную позицию
//        if (!hasOverlap(newButtonRect)) {
//            return new Point(x, y);
//        }
//
//        // Ищем ближайшее свободное место в разных направлениях
//        List<Point> candidatePositions = new ArrayList<>();
//
//        // Пробуем разные направления сдвига
//        int[] offsetsX = {-width, 0, width, 0};
//        int[] offsetsY = {0, -height, 0, height};
//
//        for (int i = 0; i < offsetsX.length; i++) {
//            int testX = x + offsetsX[i];
//            int testY = y + offsetsY[i];
//            Rect testRect = new Rect(testX, testY, testX + width, testY + height);
//
//            // Проверяем, что позиция свободна и внутри контейнера
//            if (!hasOverlap(testRect) && isInsideContainer(testRect)) {
//                candidatePositions.add(new Point(testX, testY));
//            }
//        }
//        // Если нашли свободные позиции, выбираем ближайшую к исходной точке
//        if (!candidatePositions.isEmpty()) {
//            return findClosestPoint(candidatePositions, x, y);
//        }
//
//        // Если не нашли свободных позиций в основных направлениях, ищем более далеко
//        return searchWiderArea(x, y, width, height);
//    }
    /**
     * Проверяет, есть ли наложение с существующими кнопками
     */
//    private boolean hasOverlap(Rect testRect) {
//        for (Button existingButton : buttons) {
//            Rect existingRect = getButtonBounds(existingButton);
//            if (Rect.intersects(testRect, existingRect)) {
//                return true;
//            }
//        }
//        return false;
//    }
    /**
     * Ищет свободное место в более широкой области
     */
    private Point searchWiderArea(int x, int y, int width, int height) {
        // Радиус поиска в пикселях
        int searchRadius = 200;

        // Шаг поиска
        int step = Math.max(width, height) / 2;

        List<Point> freePositions = new ArrayList<>();

        // Спиральный поиск вокруг исходной точки
        for (int r = step; r <= searchRadius; r += step) {
            // Проверяем позиции по кругу
            for (int angle = 0; angle < 360; angle += 45) {
                double rad = Math.toRadians(angle);
                int testX = x + (int) (r * Math.cos(rad));
                int testY = y + (int) (r * Math.sin(rad));

                Rect testRect = new Rect(testX, testY, testX + width, testY + height);

                if (!hasOverlap(testRect) && isInsideContainer(testRect)) {
                    freePositions.add(new Point(testX, testY));
                }
            }

            // Если нашли хотя бы одну свободную позицию, останавливаем поиск
            if (!freePositions.isEmpty()) break;
        }

        if (!freePositions.isEmpty()) {
            return findClosestPoint(freePositions, x, y);
        }

        // Крайний случай — возвращаем исходную позицию (наложение неизбежно)
        return new Point(x, y);
    }

    /**
     * Находит точку, ближайшую к указанной позиции
     */
    private Point findClosestPoint(List<Point> points, int originX, int originY) {
        Point closest = points.get(0);
        float minDistance = Float.MAX_VALUE;

        for (Point point : points) {
            float distance = (float) Math.sqrt(Math.pow(point.x - originX, 2) + Math.pow(point.y - originY, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closest = point;
            }
        }
        return closest;
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
    /**
     * Ограничивает позицию границами контейнера
     */
    private Point limitToContainerBounds(Point position, int width, int height) {
        int maxX = relativeLayout.getWidth() - width;
        int maxY = relativeLayout.getHeight() - height;

        position.x = Math.max(0, Math.min(position.x, maxX));
        position.y = Math.max(0, Math.min(position.y, maxY));

        return position;
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
     * Проверяет наложение с существующими кнопками и сдвигает позицию при необходимости
     */
    private Point checkAndResolveOverlap(int x, int y, int width, int height) {
        Rect newButtonRect = new Rect(x, y, x + width, y + height);

        for (Button existingButton : buttons) {
            Rect existingRect = getButtonBounds(existingButton);
            if (Rect.intersects(newButtonRect, existingRect)) {
                // Есть наложение — сдвигаем вправо
                x = existingRect.right;
                // Проверяем новое положение на наложение
                newButtonRect.set(x, y, x + width, y + height);
                if (Rect.intersects(newButtonRect, existingRect)) {
                    // Если всё ещё накладывается, сдвигаем вниз
                    y = existingRect.bottom;
                    x = existingRect.left; // Возвращаем к левому краю целевой кнопки
                }
            }
        }
        return new Point(x, y);
    }
    /**
     * Получает границы кнопки в координатах контейнера
     */
//    private Rect getButtonBounds(Button button) {
//        int[] location = new int[2];
//        button.getLocationOnScreen(location);
//
//        int containerLeft = 0;
//        int containerTop = 0;
//
//        // Получаем координаты контейнера
//        int[] containerLocation = new int[2];
//        relativeLayout.getLocationOnScreen(containerLocation);
//
//        // Переводим в координаты контейнера
//        int buttonLeft = location[0] - containerLocation[0];
//        int buttonTop = location[1] - containerLocation[1];
//
//        return new Rect(
//                buttonLeft,
//                buttonTop,
//                buttonLeft + button.getWidth(),
//                buttonTop + button.getHeight()
//        );
//    }
    /**
     * Ограничивает позицию границами контейнера
     */
//    private Point limitToContainerBounds(Point position, int width, int height) {
//        int maxX = relativeLayout.getWidth() - width;
//        int maxY = relativeLayout.getHeight() - height;
//
//        position.x = Math.max(0, Math.min(position.x, maxX));
//        position.y = Math.max(0, Math.min(position.y, maxY));
//
//        return position;
//    }


    /**
     * Находит ближайшую кнопку к указанной точке
     */
    private Button findNearestButton(float x, float y) {
        Button nearest = null;
        float minDistance = Float.MAX_VALUE;

        for (Button button : buttons) {
            int[] location = new int[2];
            button.getLocationInWindow(location);
            float buttonX = location[0] + button.getWidth() / 2f;
            float buttonY = location[1] + button.getHeight() / 2f;

            float distance = (float) Math.sqrt(Math.pow(buttonX - x, 2) + Math.pow(buttonY - y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearest = button;
            }
        }
        return nearest;
    }
    /**
     * Рассчитывает точку присоединения новой кнопки к существующей
     */
//    private Point calculateAttachmentPoint(Button targetButton, float x, float y) {
//        Toast.makeText(requireContext(), "расчет точки", Toast.LENGTH_SHORT).show();
//        int[] location = new int[2];
//        targetButton.getLocationInWindow(location);
//
//        int targetCenterX = location[0] + targetButton.getWidth() / 2;
//        int targetCenterY = location[1] + targetButton.getHeight() / 2;
//
//        float dx = x - targetCenterX;
//        float dy = y - targetCenterY;
//
//        // Определяем направление присоединения (верх, низ, лево, право)
//        if (Math.abs(dx) > Math.abs(dy)) {
//            // Горизонтальное присоединение
//            if (dx > 0) {
//                // Справа от целевой кнопки
//                return new Point(location[0] + targetButton.getWidth(), location[1]);
//            } else {
//                // Слева от целевой кнопки
//                return new Point(location[0] - pendingButtonConfig.width, location[1]);
//            }
//        } else {
//            // Вертикальное присоединение
//            if (dy > 0) {
//                // Ниже целевой кнопки
//                return new Point(location[0], location[1] + targetButton.getHeight());
//            } else {
//                // Выше целевой кнопки
//                return new Point(location[0], location[1] - pendingButtonConfig.height);
//            }
//        }
//    }
    private Point calculateAttachmentPoint(Button targetButton, float touchX, float touchY, int newWidth, int newHeight) {
        int[] location = new int[2];
        targetButton.getLocationOnScreen(location);

        int targetWidth = targetButton.getWidth();
        int targetHeight = targetButton.getHeight();

        // Получаем координаты центра целевой кнопки
        int targetCenterX = location[0] + targetWidth / 2;
        int targetCenterY = location[1] + targetHeight / 2;

        // Определяем направление присоединения на основе координат касания
        float dx = touchX - targetCenterX;
        float dy = touchY - targetCenterY;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        android.util.Log.i("BUTTON_DISTANCE",
                String.format("Distance to center: %.2f px (dx=%.2f, dy=%.2f)",
                        distance, dx, dy));

        String direction;
        Point result;
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "RIGHT" : "LEFT";
            result = (dx > 0)
                    ? new Point(location[0] + targetWidth, location[1])
                    : new Point(location[0] - newWidth, location[1]);
        } else {
            direction = (dy > 0) ? "BOTTOM" : "TOP";
            result = (dy > 0)
                    ? new Point(location[0], location[1] + targetHeight)
                    : new Point(location[0], location[1] - newHeight);
        }

        android.util.Log.i("BUTTON_DIRECTION",
                "Attachment direction: " + direction +
                        " (final: X=" + result.x + ", Y=" + result.y + ")");
        return result;

    }
    private void addButtonToMap(String name) {
        // Создаём новую кнопку
        Button newButton;
        newButton = new Button(requireContext());
        newButton.setId(View.generateViewId());
        newButton.setText(name);
        newButton.setAllCaps(false);
        newButton.setBackgroundResource(R.drawable.btn_rectangle);
        newButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
        newButton.setTextSize(18);

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

    @Override
    public void onObjectAdded(String objectName, Integer sizeX, Integer sizeY) {
        addButtonToMap(objectName);
    }
}