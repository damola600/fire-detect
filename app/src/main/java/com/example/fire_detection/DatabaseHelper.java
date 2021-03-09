package com.example.fire_detection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final  int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fire_detection.db";
    private static final String TABLE_NAME = "data";
    private static final String COL_1 = "id";
    private static final String COL_2 = "time";
    private static final String COL_3 = "temperature";
    private static final String COL_4 = "humidity";
    private static final String COL_5 = "fire_prob";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableData = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                COL_1 + " Integer NOT NULL,"+
                COL_2 + " Text NOT NULL,"+
                COL_3 + " Text NOT NULL,"+
                COL_4 + " Text NOT NULL,"+
                COL_5 + " Text NOT NULL)" + ";";
        db.execSQL(createTableData);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table IF EXISTS " + TABLE_NAME + ";");

    }

    public boolean addData(String id, String time, String temperature, String humidity, String fire_prob){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, time);
        contentValues.put(COL_3, temperature);
        contentValues.put(COL_4, humidity);
        contentValues.put(COL_5, fire_prob);

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if(result == 0) return false;
        else return true;
    }

    public Cursor getTemp(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT temperature FROM " + TABLE_NAME + ";", null);
        return data;
    }

    public Cursor getTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT time FROM " + TABLE_NAME + ";", null);
        return data;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
        return data;
    }


    public Cursor getHumidity(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT humidity FROM " + TABLE_NAME + ";", null);
        return data;
    }

    public Cursor getFire_prob(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT fire_prob FROM " + TABLE_NAME + ";", null);
        return data;
    }
}
