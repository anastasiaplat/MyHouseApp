package com.example.myhouseapp0;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<ButtonData>> buttonsData = new MutableLiveData<>();

    public LiveData<List<ButtonData>> getButtonsData() { return buttonsData; }
    public void setButtonsData(List<ButtonData> data) { buttonsData.setValue(data); }

    // Методы для добавления и удаления кнопок
    public void addButton(ButtonData button) {
        List<ButtonData> current = buttonsData.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(button);
        buttonsData.setValue(current);
    }

    public void removeButton(Object tag) {
        List<ButtonData> current = buttonsData.getValue();
        if (current != null) {
            current.removeIf(button -> button.getTag().equals(tag));
            buttonsData.setValue(current);
        }
    }
    public void updateButtonPosition(String tag, int x, int y) {
        List<ButtonData> current = buttonsData.getValue();
        if (current != null) {
            for (ButtonData button : current) {
                if (button.getTag().equals(tag)) {
                    button.setPositionX(x);
                    button.setPositionY(y);
                    break;
                }
            }
            buttonsData.setValue(current);
        }
    }
}

