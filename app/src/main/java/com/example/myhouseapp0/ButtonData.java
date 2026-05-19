package com.example.myhouseapp0;

// Класс для хранения данных кнопки
public class ButtonData {
    private String name;
    private Object tag;
    private int width;
    private int height;
    private int color;
    private int positionX; // для карты — координата X
    private int positionY; // для карты — координата Y

    public ButtonData(String name, Object tag, int width, int height, int color) {
        this.name = name;
        this.tag = tag;
        this.width = width;
        this.height = height;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    // Геттеры
    public String getName() {
        return name;
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
