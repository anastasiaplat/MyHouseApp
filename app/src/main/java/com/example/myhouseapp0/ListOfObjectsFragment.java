package com.example.myhouseapp0;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myhouseapp0.rooms.BathroomFragment;
import com.example.myhouseapp0.rooms.BedroomFragment;
import com.example.myhouseapp0.rooms.CarRoomFragment;
import com.example.myhouseapp0.rooms.GreenhouseFragment;
import com.example.myhouseapp0.rooms.HallRoomFragment;
import com.example.myhouseapp0.rooms.KitchenFragment;
import com.example.myhouseapp0.rooms.StoreroomFragment;
import com.example.myhouseapp0.rooms.YardFragment;


public class ListOfObjectsFragment extends Fragment {


    private int pageNumber;
    private ConstraintLayout constraintLayout;
    private int buttonCounter = 1;
    private Button createButton;
    private int lastButtonId = -1; // ID последней созданной кнопки
    public static ListOfObjectsFragment newInstance(int pageNumber) {
        ListOfObjectsFragment fragment = new ListOfObjectsFragment();
        Bundle args = new Bundle();
        args.putInt("page_number", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNumber = getArguments().getInt("page_number");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        int tabNumber = getArguments().getInt("tab_number");
        int layoutResId = tabNumber == 1 ? R.layout.fragment_list_of_objects : R.layout.fragment_interactive_map;

        View view = inflater.inflate(layoutResId, container, false);
        constraintLayout = view.findViewById(R.id.constraint_layout);

        // Сохраняем ID корневого элемента как отправную точку
        lastButtonId = constraintLayout.getId();

        return view;


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

    // Метод для создания новой кнопки в ConstraintLayout
    public void createNewButton() {
        Button newButton = new Button(getActivity());
        int currentTab = getArguments().getInt("tab_number");
        newButton.setText("Кнопка на Tab" + currentTab + " #" + buttonCounter);

        int newButtonId = View.generateViewId();
        newButton.setId(newButtonId);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16);
        newButton.setLayoutParams(params);

        constraintLayout.addView(newButton);

        // Позиционируем новую кнопку
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        if (buttonCounter == 1) {
            // Первая кнопка — позиционируем от верха контейнера
            constraintSet.connect(
                    newButtonId, ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                    16
            );
        } else {
            // Последующие кнопки — под предыдущей
            constraintSet.connect(
                    newButtonId, ConstraintSet.TOP,
                    lastButtonId, ConstraintSet.BOTTOM,
                    16
            );
        }

        // Центрируем по горизонтали
        constraintSet.connect(
                newButtonId, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START,
                16
        );
        constraintSet.connect(
                newButtonId, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END,
                16
        );

        constraintSet.applyTo(constraintLayout);

        lastButtonId = newButtonId;
        buttonCounter++;
    }

}