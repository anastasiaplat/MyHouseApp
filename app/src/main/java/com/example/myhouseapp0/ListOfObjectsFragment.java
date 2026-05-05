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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhouseapp0.databinding.FragmentListOfObjectsBinding;
import com.example.myhouseapp0.rooms.BathroomFragment;
import com.example.myhouseapp0.rooms.BedroomFragment;
import com.example.myhouseapp0.rooms.CarRoomFragment;
import com.example.myhouseapp0.rooms.GreenhouseFragment;
import com.example.myhouseapp0.rooms.HallRoomFragment;
import com.example.myhouseapp0.rooms.KitchenFragment;
import com.example.myhouseapp0.rooms.StoreroomFragment;
import com.example.myhouseapp0.rooms.YardFragment;


public class ListOfObjectsFragment extends Fragment implements OnObjectAddedListener {


    private ConstraintLayout constraintLayout;
    private int buttonCounter = 0;
    private TextView textInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list_of_objects, container, false);
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

        return view;

    }
    public void onObjectAdded(String objectName, Integer sizeX, Integer sizeY) {
        addButtonToList(objectName);

    }
    @SuppressLint("ResourceAsColor")
    private void addButtonToList(String name) {
        textInfo.setVisibility(View.GONE);
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
        constraintSet.clone(constraintLayout); // копируем текущие ограничения
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        int newButtonId = newButton.getId();
        // Горизонтальное позиционирование: левая граница кнопки к левой границе родителя
        constraintSet.connect(
                newButtonId, ConstraintSet.START,
                parentId, ConstraintSet.START, 32
        );
        // Вертикальное позиционирование: верхняя граница кнопки относительно верха родителя
        // Смещение зависит от счётчика — каждая следующая кнопка ниже
        constraintSet.connect(
                newButtonId, ConstraintSet.TOP,
                parentId, ConstraintSet.TOP, 100 + buttonCounter * 300
        );
        // Применяем ограничения
        constraintSet.applyTo(constraintLayout);
        buttonCounter++;
        newButton.setOnClickListener(v -> replaceFragment(new CustomFragment()));


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Button btn_to_hallroom = view.findViewById(R.id.btn_hallroom);
//        btn_to_hallroom.setOnClickListener(v -> replaceFragment(new HallRoomFragment()));
//
//        Button btn_to_bedroom = view.findViewById(R.id.btn_bedroom);
//        btn_to_bedroom.setOnClickListener(v -> replaceFragment(new BedroomFragment()));
//
//        Button btn_to_bathroom = view.findViewById(R.id.btn_bathroom);
//        btn_to_bathroom.setOnClickListener(v -> replaceFragment(new BathroomFragment()));
//
//        Button btn_to_kitchen = view.findViewById(R.id.btn_kitchen);
//        btn_to_kitchen.setOnClickListener(v -> replaceFragment(new KitchenFragment()));
//
//        Button btn_to_carroom = view.findViewById(R.id.btn_carroom);
//        btn_to_carroom.setOnClickListener(v -> replaceFragment(new CarRoomFragment()));
//
//        Button btn_to_greenhouse = view.findViewById(R.id.btn_greenhouse);
//        btn_to_greenhouse.setOnClickListener(v -> replaceFragment(new GreenhouseFragment()));
//
//        Button btn_to_storeroom = view.findViewById(R.id.btn_storeroom);
//        btn_to_storeroom.setOnClickListener(v -> replaceFragment(new StoreroomFragment()));



    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



}