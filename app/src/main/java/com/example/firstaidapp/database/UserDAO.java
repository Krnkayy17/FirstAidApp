package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.User;

public class UserDAO {

    private SQLiteDatabase db;
    private FirstAidDatabaseHelper dbHelper;

    public UserDAO(FirstAidDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert new user
    public boolean insertUser(String name, String email, String phone, String password) {
        open();
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_USER_NAME, name);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_PHONE, phone);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_PASSWORD, password);

        long result = db.insert(FirstAidDatabaseHelper.TABLE_USER, null, values);
        close();

        return result != -1;
    }

    // Check if user exists by email
    public boolean checkUserExists(String email) {
        open();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_USER,
                null,
                FirstAidDatabaseHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{email},
                null, null, null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        close();
        return exists;
    }

    // Check login credentials
    public boolean checkUserCredentials(String email, String password) {
        open();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_USER,
                null,
                FirstAidDatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " +
                        FirstAidDatabaseHelper.COLUMN_USER_PASSWORD + " = ?",
                new String[]{email, password},
                null, null, null
        );

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        close();
        return isValid;
    }

    // Get user by credentials
    public User getUserByCredentials(String email, String password) {
        open();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_USER,
                null,
                FirstAidDatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " +
                        FirstAidDatabaseHelper.COLUMN_USER_PASSWORD + " = ?",
                new String[]{email, password},
                null, null, null
        );

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_PASSWORD))
            );
        }

        cursor.close();
        close();
        return user;
    }
}
