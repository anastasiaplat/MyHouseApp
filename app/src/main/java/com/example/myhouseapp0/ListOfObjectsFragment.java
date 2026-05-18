package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhouseapp0.rooms.BedroomFragment;
import com.example.myhouseapp0.rooms.HallRoomFragment;

import java.util.ArrayList;
import java.util.List;


public class ListOfObjectsFragment extends Fragment{


    private ConstraintLayout constraintLayout;
    private int buttonCounter = 0;
    private TextView textInfo;
    private List<Button> listButtons = new ArrayList<>();

    private int getListButtonCount() {
        return listButtons.size();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list_of_objects, container, false);

        return view;

    }
//    public void onObjectAdded(String objectName) {
//        addButtonToList(objectName);
//
//    }
    @SuppressLint("ResourceAsColor")
    public void addButtonToList(String name) {
        // Инициализируем textInfo, если ещё не создан
        if (textInfo == null) {
            initializeTextInfo();
        }

        Button newButton = new Button(requireContext());
        newButton.setId(View.generateViewId());
        newButton.setText(name);
        newButton.setWidth(330);
        newButton.setHeight(65);
        newButton.setBackgroundResource(R.drawable.btn_rectangle);
        newButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
        newButton.setTextSize(18);
        newButton.setAllCaps(false);

        constraintLayout.addView(newButton);

        // Позиционируем через ConstraintSet
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        int newButtonId = newButton.getId();

        // Горизонтальное позиционирование
        constraintSet.connect(
                newButtonId, ConstraintSet.START,
                parentId, ConstraintSet.START, 32
        );
        // Вертикальное позиционирование
        constraintSet.connect(
                newButtonId, ConstraintSet.TOP,
                parentId, ConstraintSet.TOP, 100 + buttonCounter * 300
        );

        buttonCounter++;
        newButton.setTag(buttonCounter);
        listButtons.add(newButton); // Добавляем в список

        // Применяем ограничения
        constraintSet.applyTo(constraintLayout);
        newButton.setOnClickListener(v -> replaceWithNewFragment(name));

        // Обновляем видимость textInfo
        updateTextInfoVisibility();
    }
    private void initializeTextInfo() {
        textInfo = new TextView(requireContext());
        textInfo.setId(View.generateViewId());
        textInfo.setText("Объекты не добавлены");
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        textInfo.setLayoutParams(params);
        constraintLayout.addView(textInfo);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        // Центрирование textInfo
        constraintSet.connect(textInfo.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(textInfo.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(textInfo.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(textInfo.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);

        textInfo.setGravity(Gravity.CENTER);
        textInfo.setTextSize(16);
        textInfo.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
    }
    private void updateTextInfoVisibility() {
        if (getListButtonCount() == 0) {
            textInfo.setVisibility(View.VISIBLE);
        } else {
            textInfo.setVisibility(View.GONE);
        }
    }
    public void editButtonOnList(){
        HomeFragment homeFragment = (HomeFragment) requireParentFragment();
        InteractiveMapFragment mapFragment = homeFragment.getMapFragment();

        if (mapFragment == null || mapFragment.selectedButtonId == null) {
            Toast.makeText(requireContext(), "Не удалось получить данные для редактирования", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean buttonFound = false;
        for (Button listButton : listButtons) {
            if (listButton.getTag() != null && listButton.getTag().equals(mapFragment.selectedButtonId)) {
                listButton.setText(mapFragment.newButtonName);
                buttonFound = true;
                break;
            }
        }

        if (!buttonFound) {
            Toast.makeText(requireContext(), "Кнопка для редактирования не найдена", Toast.LENGTH_SHORT).show();
        }

        constraintLayout.requestLayout();
        constraintLayout.invalidate();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        constraintLayout = view.findViewById(R.id.constraint_layout);

        textInfo = new TextView(requireContext());
        textInfo.setId(View.generateViewId());
        textInfo.setText("Объекты не добавлены");
        // Задаём параметры ширины и высоты
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        textInfo.setLayoutParams(params);

        // Добавляем TextView в родительский ConstraintLayout
        constraintLayout.addView(textInfo);

        // Получаем ConstraintSet для настройки ограничений
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Устанавливаем ограничения для центрирования
        constraintSet.connect(
                textInfo.getId(),
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
        );
        constraintSet.connect(
                textInfo.getId(),
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
        );
        constraintSet.connect(
                textInfo.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );
        constraintSet.connect(
                textInfo.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );

        // Применяем ограничения к ConstraintLayout
        constraintSet.applyTo(constraintLayout);

        // Дополнительно настраиваем внешний вид
        textInfo.setGravity(Gravity.CENTER);
        textInfo.setTextSize(16);
        textInfo.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));


    }
    public void removeButtonFromList(Object buttonTag) {
        boolean buttonFound = false;
        Button buttonToRemove = null;

        // Ищем кнопку по тегу
        for (Button listButton : listButtons) {
            if (listButton.getTag() != null && listButton.getTag().equals(buttonTag)) {
                buttonToRemove = listButton;
                buttonFound = true;
                break;
            }
        }

        if (buttonFound && buttonToRemove != null) {
            // Удаляем кнопку из ConstraintLayout
            constraintLayout.removeView(buttonToRemove);
            // Удаляем из списка кнопок
            listButtons.remove(buttonToRemove);

            // Обновляем видимость textInfo
            updateTextInfoVisibility();

            // Перепозиционируем оставшиеся кнопки
            repositionButtons();
        } else {
            Toast.makeText(requireContext(), "Кнопка для удаления не найдена в списке", Toast.LENGTH_SHORT).show();
        }
    }
    private void repositionButtons() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;

        for (int i = 0; i < listButtons.size(); i++) {
            Button currentButton = listButtons.get(i);
            int buttonId = currentButton.getId();

            // Горизонтальное позиционирование
            constraintSet.connect(
                    buttonId, ConstraintSet.START,
                    parentId, ConstraintSet.START, 32
            );
            // Вертикальное позиционирование с отступом 300 px между кнопками
            constraintSet.connect(
                    buttonId, ConstraintSet.TOP,
                    parentId, ConstraintSet.TOP, 100 + i * 300
            );
        }

        constraintSet.applyTo(constraintLayout);
    }
    public void updateButtonInList(Object buttonTag, String newName) {
        boolean buttonFound = false;
        for (Button listButton : listButtons) {
            if (listButton.getTag() != null && listButton.getTag().equals(buttonTag)) {
                listButton.setText(newName);
                buttonFound = true;
                Toast.makeText(requireContext(), "Название обновлено: " + newName, Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!buttonFound) {
            Toast.makeText(requireContext(), "Кнопка для редактирования не найдена", Toast.LENGTH_SHORT).show();
        }

        constraintLayout.requestLayout();
        constraintLayout.invalidate();
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void replaceWithNewFragment(String buttonName) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Создаём новый фрагмент и передаём ему данные
        CustomFragment newFragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putString("button_name", buttonName);
        newFragment.setArguments(args);

        ((MajorActivity) requireActivity()).replaceFragment(newFragment);
    }
}