package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DB_helper extends SQLiteOpenHelper{
    public static final String DBName="myhouse.db";

    public static final String TABLE_OBJECTS = "Objects";
    public static final String TABLE_DEVICES = "devices";
    private static final String COLUMN_BUTTON_TAG = "button_tag";

    // Поля таблицы Objects
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "object_name";
    public static final String COLUMN_SIZEX = "sizeX";
    public static final String COLUMN_SIZEY = "sizeY";

    // Поля таблицы Devices
    public static final String COLUMN_DEVICE_ID = "id";
    public static final String COLUMN_DEVICE_NAME = "device_name";


    public DB_helper(@Nullable Context context) {
        super(context, DBName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        String createObjectsTable = "CREATE TABLE " + TABLE_OBJECTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_SIZEX + " REAL, " +
                COLUMN_SIZEY + " REAL" +
                ")";

        // Создание таблицы Devices

        String createDevicesTable = "CREATE TABLE " + TABLE_DEVICES + " (" +
                COLUMN_BUTTON_TAG + " TEXT, " +
                COLUMN_DEVICE_NAME + " TEXT " +
                ")";

        sqLiteDatabase.execSQL(createObjectsTable);
        sqLiteDatabase.execSQL(createDevicesTable);

        // Добавляем тестовые устройства
        insertTestDevices(sqLiteDatabase);
    }
    private void insertTestDevices(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEVICE_NAME, "Устройство 1");
        db.insert(TABLE_DEVICES, null, values);

        values.clear();
        values.put(COLUMN_DEVICE_NAME, "Устройство 2");
        db.insert(TABLE_DEVICES, null, values);

        values.clear();
        values.put(COLUMN_DEVICE_NAME, "Устройство 3");
        db.insert(TABLE_DEVICES, null, values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJECTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        onCreate(sqLiteDatabase);
    }


    public List<String> getDevices() {
        List<String> devices = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_DEVICES, new String[]{COLUMN_DEVICE_NAME},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                devices.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return devices;
    }

    // Сохранение устройств для кнопки
    public void saveDevicesForButton(Object buttonTag, List<String> deviceNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Удаляем старые записи для этого тега
            db.delete(TABLE_DEVICES, COLUMN_BUTTON_TAG + " = ?",
                    new String[]{String.valueOf(buttonTag)});

            // Вставляем новые записи
            ContentValues values = new ContentValues();
            for (String device : deviceNames) {
                values.put(COLUMN_BUTTON_TAG, String.valueOf(buttonTag));
                values.put(COLUMN_DEVICE_NAME, device);
                db.insert(TABLE_DEVICES, null, values);
            }
        } catch (SQLiteException e) {
            Log.e("DB_helper", "Ошибка при сохранении устройств: " + e.getMessage());
        } finally {
            db.close();
        }
    }
    // Получение устройств для кнопки
    public List<String> getDevicesForButton(Object buttonTag) {
        List<String> devices = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVICES,
                new String[]{COLUMN_DEVICE_NAME},
                COLUMN_BUTTON_TAG + " = ?",
                new String[]{String.valueOf(buttonTag)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                devices.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return devices;
    }










    public boolean insertData(String username, String password){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
       // contentValues.put("name", name);
        contentValues.put("password", password);
        long result = myDB.insert("users", null, contentValues);
        return result != -1;
    }
    public boolean insertTempData(String date, String temp_and_humidity){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("temp_and_humidity", temp_and_humidity);
        long result = myDB.insert("TempAndHumidity", null, contentValues);
        return result != -1;
    }
    public String getTempData(){
        SQLiteDatabase myDB = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = myDB.rawQuery("select * from TempAndHumidity ORDER BY id DESC LIMIT 1;", null);
        return cursor.getString(1);
    }
    public boolean checkUsername(String username) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = myDB.rawQuery("select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public boolean checkUserForEntrance(String username, String pwd){
        SQLiteDatabase DB = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = DB.rawQuery("select * from users where username = ? and password=?", new String[]{username, pwd});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }
//    public void updateName(String login,String name)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("username",login);
//        cv.put("name",name);
//        db.update("users",cv,"Username = ?",new String[] { name });
//        db.close();
//    }
}
