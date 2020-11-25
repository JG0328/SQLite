package com.pucmm.sqlite.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    static DatabaseHelper instance;

    public DatabaseHelper(@Nullable Context context) {
        super(context, "android_sqlite.db", null, 1);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) instance = new DatabaseHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table products (_id integer primary key autoincrement, name varchar(128) not null, categoryName varchar(128) not null, price float not null)");
        sqLiteDatabase.execSQL("create table categories (_id integer primary key autoincrement, name varchar(128) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists products");
        sqLiteDatabase.execSQL("drop table if exists categories");
        onCreate(sqLiteDatabase);
    }
}
