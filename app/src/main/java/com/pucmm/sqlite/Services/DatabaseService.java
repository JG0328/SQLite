package com.pucmm.sqlite.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pucmm.sqlite.Helpers.DatabaseHelper;

public class DatabaseService {
    DatabaseHelper databaseHelper;
    Context context;
    SQLiteDatabase database;

    public DatabaseService(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);

        openConnection();
    }

    public void openConnection() {
        if (databaseHelper == null) DatabaseHelper.getInstance(context);
        database = databaseHelper.getWritableDatabase();
    }

    public void closeConnection() {
        databaseHelper.close();
    }

    public void createProduct(String name, float price, String categoryName) {
        openConnection();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("categoryName", categoryName);
        contentValues.put("price", price);

        database.insert("products", null, contentValues);
    }

    public void createCategory(String name) {
        openConnection();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        database.insert("categories", null, contentValues);
    }

    public Cursor getProducts() {
        openConnection();
        Cursor cursor = database.query("products", new String[]{"_id", "name", "categoryName", "price"}, null, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }
}
