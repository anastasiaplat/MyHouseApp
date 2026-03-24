package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DB_helper extends SQLiteOpenHelper{
    public static final String DBName="myhouse.db";
    public static final String TABLE_DEVICES = "devices";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public DB_helper(@Nullable Context context) {
        super(context, DBName, null, 1);
    }
    private static final String CREATE_TABLE_DEVICES =
            "CREATE TABLE " + TABLE_DEVICES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL);";
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("create table Users(username TEXT primary key, password TEXT)");
        sqLiteDatabase.execSQL("create table TempAndHumidity(date DATE primary key, data TEXT)");
        sqLiteDatabase.execSQL(CREATE_TABLE_DEVICES);
        // Добавляем тестовые устройства
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_NAME + ") VALUES ('Датчик температуры'), ('Датчик влажности'), ('Отопление')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Users");
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_DEVICES);
        onCreate(sqLiteDatabase);
    }


    public List<String> getDevices() {
        List<String> devices = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_DEVICES, new String[]{COLUMN_NAME},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                devices.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
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
