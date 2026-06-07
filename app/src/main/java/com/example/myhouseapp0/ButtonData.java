package com.example.myhouseapp0;

import java.util.ArrayList;
import java.util.List;

// Класс для хранения данных кнопки
public class ButtonData {
    private String name;
    private Object tag;
    private int width;
    private int height;
    private List<String> existingDevices;
    private int color;
    private int positionX; // для карты — координата X
    private int positionY; // для карты — координата Y

    public ButtonData(String name, Object tag, int width, int height, int color) {
        this.name = name;
        this.tag = tag;
        this.width = Math.max(width, 1); // Минимальная ширина 1 dp
        this.height = Math.max(height, 1); // Минимальная высота 1 dp
        this.color = color;
        this.existingDevices = new ArrayList<>(); // Инициализируем пустой список
        this.positionX = 0; // Инициализируем нулями
        this.positionY = 0;
    }

    // Геттеры
    public String getName() {
        return name;
    }
    public List<String> getExistingDevices() {
        return existingDevices;
    }

    public Object getTag() {
        return tag;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getColor() {
        return color;
    }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    // Сеттеры (если нужно изменять данные)
    public void setName(String name) { this.name = name; }
    public void setTag(String tag) { this.tag = tag; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setColor(int color) { this.color = color; }
    public void setPositionX(int positionX) { this.positionX = positionX; }
    public void setPositionY(int positionY) { this.positionY = positionY; }
}
