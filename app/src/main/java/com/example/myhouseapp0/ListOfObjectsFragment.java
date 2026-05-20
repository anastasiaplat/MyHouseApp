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
import androidx.lifecycle.ViewModelProvider;

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
    private boolean isInEditMode = false; // Флаг режима редактирования
    private SharedViewModel viewModel;

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

        // Создаём данные кнопки
//        Object tag = "btn_" + System.currentTimeMillis(); // уникальный тег
//        ButtonData buttonData = new ButtonData(name, tag, 330, 65, ContextCompat.getColor(requireContext(), R.color.navbar));
//        buttonData.setPositionX(0); // По умолчанию
//        buttonData.setPositionY(0);
        // Сохраняем в ViewModel
//        viewModel.addButton(buttonData);

        // Создаём кнопку
//        createButtonFromData(buttonData, listButtons.size());

        // Инициализируем textInfo, если ещё не создан
//        if (textInfo == null) {
//            initializeTextInfo();
//        }
//
//        Button newButton = new Button(requireContext());
//        newButton.setId(View.generateViewId());
//        newButton.setText(name);
//        newButton.setWidth(330);
//        newButton.setHeight(65);
//        newButton.setBackgroundResource(R.drawable.btn_rectangle);
//        newButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));
//        newButton.setTextSize(18);
//        newButton.setAllCaps(false);
//
//        constraintLayout.addView(newButton);
//
//        // Позиционируем через ConstraintSet
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(constraintLayout);
//        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
//        int newButtonId = newButton.getId();
//
//        // Горизонтальное позиционирование
//        constraintSet.connect(
//                newButtonId, ConstraintSet.START,
//                parentId, ConstraintSet.START, 32
//        );
//        // Вертикальное позиционирование
//        constraintSet.connect(
//                newButtonId, ConstraintSet.TOP,
//                parentId, ConstraintSet.TOP, 100 + buttonCounter * 300
//        );
//
//        buttonCounter++;
//        newButton.setTag(buttonCounter);
//        listButtons.add(newButton); // Добавляем в список
//
//        // Применяем ограничения
//        constraintSet.applyTo(constraintLayout);
//        newButton.setOnClickListener(v -> replaceWithNewFragment(name));
//
//        // Обновляем видимость textInfo
//        updateTextInfoVisibility();
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
        if (listButtons.isEmpty()) {
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
// Получаем ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
// Восстанавливаем кнопки из ViewModel
        viewModel.getButtonsData().observe(getViewLifecycleOwner(), this::restoreButtons);

        constraintLayout = view.findViewById(R.id.constraint_layout);
// Инициализируем textInfo
        initializeTextInfo();
//        textInfo = new TextView(requireContext());
//        textInfo.setId(View.generateViewId());
//        textInfo.setText("Объекты не добавлены");
//        // Задаём параметры ширины и высоты
//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
//                ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                ConstraintLayout.LayoutParams.WRAP_CONTENT
//        );
//        textInfo.setLayoutParams(params);
//
//        // Добавляем TextView в родительский ConstraintLayout
//        constraintLayout.addView(textInfo);
//
//        // Получаем ConstraintSet для настройки ограничений
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(constraintLayout);
//
//        // Устанавливаем ограничения для центрирования
//        constraintSet.connect(
//                textInfo.getId(),
//                ConstraintSet.START,
//                ConstraintSet.PARENT_ID,
//                ConstraintSet.START
//        );
//        constraintSet.connect(
//                textInfo.getId(),
//                ConstraintSet.END,
//                ConstraintSet.PARENT_ID,
//                ConstraintSet.END
//        );
//        constraintSet.connect(
//                textInfo.getId(),
//                ConstraintSet.TOP,
//                ConstraintSet.PARENT_ID,
//                ConstraintSet.TOP
//        );
//        constraintSet.connect(
//                textInfo.getId(),
//                ConstraintSet.BOTTOM,
//                ConstraintSet.PARENT_ID,
//                ConstraintSet.BOTTOM
//        );
//
//        // Применяем ограничения к ConstraintLayout
//        constraintSet.applyTo(constraintLayout);
//
//        // Дополнительно настраиваем внешний вид
//        textInfo.setGravity(Gravity.CENTER);
//        textInfo.setTextSize(16);
//        textInfo.setTextColor(ContextCompat.getColor(requireContext(), R.color.navbar));


    }
    private void restoreButtons(List<ButtonData> buttonsData) {
        if (buttonsData == null) return;

        // Очищаем текущий список кнопок
        for (Button button : listButtons) {
            constraintLayout.removeView(button);
        }
        listButtons.clear();

        // Создаём кнопки заново
        for (int i = 0; i < buttonsData.size(); i++) {
            createButtonFromData(buttonsData.get(i), i);
        }

        updateTextInfoVisibility();
    }
    private void createButtonFromData(ButtonData buttonData, int position) {
        Button newButton = new Button(requireContext());
        newButton.setId(View.generateViewId());
        newButton.setText(buttonData.getName());
        newButton.setTag(buttonData.getTag());
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

        constraintSet.connect(newButtonId, ConstraintSet.START, parentId, ConstraintSet.START, 32);
        constraintSet.connect(newButtonId, ConstraintSet.TOP, parentId, ConstraintSet.TOP, 100 + position * 300);

        constraintSet.applyTo(constraintLayout);

        setupButtonClickListener(newButton, buttonData);

        listButtons.add(newButton);
    }
    private void setupButtonClickListener(Button button, ButtonData buttonData) {
        if (isInEditMode) {
            // В режиме редактирования — открываем диалог редактирования
            button.setOnClickListener(v -> showEditDialog(buttonData));
        } else {
            // В обычном режиме — переходим на CustomFragment
            button.setOnClickListener(v ->
                    replaceWithNewFragment(buttonData.getName()));
        }
    }
    private void showEditDialog(ButtonData buttonData) {
        EditObjectDialog editDialog = new EditObjectDialog(
                buttonData.getName(),
                buttonData.getWidth(),
                buttonData.getHeight(),
                new EditObjectDialog.OnObjectEditedListener() {
                    @Override
                    public void onObjectEdited(String newName, int newWidth, int newLength, List<String> selectedDevices) {
                        // Обновляем данные в ViewModel
                        List<ButtonData> currentData = new ArrayList<>(viewModel.getButtonsData().getValue());
                        for (ButtonData data : currentData) {
                            if (data.getTag().equals(buttonData.getTag())) {
                                data.setName(newName);
                                data.setWidth(newWidth);
                                data.setHeight(newLength);
                                break;
                            }
                        }
                        viewModel.buttonsData.setValue(currentData);
                    }

                    @Override
                    public void onObjectDeleted() {
                        viewModel.removeButton(buttonData.getTag());
                    }
                }
        );

        editDialog.show(getParentFragmentManager(), "EDIT_OBJECT_DIALOG");
    }

    public void enterEditMode() {
        isInEditMode = true;
        Toast.makeText(requireContext(), "Выберите объект для редактирования", Toast.LENGTH_SHORT).show();
        // Обновляем обработчики всех кнопок
        for (int i = 0; i < listButtons.size(); i++) {
            Button button = listButtons.get(i);
            ButtonData buttonData = viewModel.getButtonsData().getValue().get(i);
            setupButtonClickListener(button, buttonData);
        }
    }

    public void exitEditMode() {
        isInEditMode = false;
        Toast.makeText(requireContext(), "Режим редактирования завершён", Toast.LENGTH_SHORT).show();
        // Возвращаем обычные обработчики
        for (int i = 0; i < listButtons.size(); i++) {
            Button button = listButtons.get(i);
            ButtonData buttonData = viewModel.getButtonsData().getValue().get(i);
            setupButtonClickListener(button, buttonData);
        }
    }
    public void removeButtonFromList(Object buttonTag) {
        viewModel.removeButton(buttonTag);
        // Удаляем кнопку из интерфейса
//        Button buttonToRemove = null;
//        for (Button button : listButtons) {
//            if (button.getTag() != null && button.getTag().equals(buttonTag)) {
//                buttonToRemove = button;
//                break;
//            }
//        }
//
//        if (buttonToRemove != null) {
//            constraintLayout.removeView(buttonToRemove);
//            listButtons.remove(buttonToRemove);
//            updateTextInfoVisibility();
//            repositionButtons();
//        }


//        boolean buttonFound = false;
//        Button buttonToRemove = null;
//
//        // Ищем кнопку по тегу
//        for (Button listButton : listButtons) {
//            if (listButton.getTag() != null && listButton.getTag().equals(buttonTag)) {
//                buttonToRemove = listButton;
//                buttonFound = true;
//                break;
//            }
//        }
//
//        if (buttonFound && buttonToRemove != null) {
//            // Удаляем кнопку из ConstraintLayout
//            constraintLayout.removeView(buttonToRemove);
//            // Удаляем из списка кнопок
//            listButtons.remove(buttonToRemove);
//
//            // Обновляем видимость textInfo
//            updateTextInfoVisibility();
//
//            // Перепозиционируем оставшиеся кнопки
//            repositionButtons();
//        } else {
//            Toast.makeText(requireContext(), "Кнопка для удаления не найдена в списке", Toast.LENGTH_SHORT).show();
//        }
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
// Обновляем данные в ViewModel
        List<ButtonData> currentData = new ArrayList<>(viewModel.getButtonsData().getValue());
        for (ButtonData data : currentData) {
            if (data.getTag().equals(buttonTag)) {
                data.setName(newName);
                break;
            }
        }
        viewModel.buttonsData.setValue(currentData);
        // Кнопки автоматически обновятся через restoreButtons()
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

        fragmentTransaction.addToBackStack(null);
        // Создаём новый фрагмент и передаём ему данные
        CustomFragment newFragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putString("button_name", buttonName);
        newFragment.setArguments(args);

        ((MajorActivity) requireActivity()).replaceFragment(newFragment);
    }
}