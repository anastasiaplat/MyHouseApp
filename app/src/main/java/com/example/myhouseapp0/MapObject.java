package com.example.myhouseapp0;

import java.util.List;

public class MapObject {
    private String name;
    private List<String> devices;
    private float width; // в условных единицах
    private float height; // в условных единицах
    private float x; // координата X на карте
    private float y; // координата Y на карте

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getDevices() { return devices; }
    public void setDevices(List<String> devices) { this.devices = devices; }

    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}

