package com.example.myhouseapp0;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    public final MutableLiveData<List<ButtonData>> buttonsData = new MutableLiveData<>(new ArrayList<>());
    private boolean isInitializing = true; // Флаг инициализации

    public LiveData<List<ButtonData>> getButtonsData() {
        return buttonsData;
    }

    public void addButton(ButtonData button) {
        List<ButtonData> current = new ArrayList<>(buttonsData.getValue());
        current.add(button);
        buttonsData.setValue(current);
        isInitializing = false; // Сбрасываем флаг после первого добавления
    }

    public void removeButton(Object tag) {
        List<ButtonData> current = buttonsData.getValue();
        if (current != null) {
            current.removeIf(button -> button.getTag().equals(tag));
            buttonsData.setValue(new ArrayList<>(current));
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
            buttonsData.setValue(new ArrayList<>(current));
        }
    }

    public boolean isInitializing() {
        return isInitializing;
    }
}

