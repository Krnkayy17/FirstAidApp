package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.User;

public class UserDAO {

    private SQLiteDatabase db;
    private FirstAidDatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new FirstAidDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert new user
    public boolean insertUser(String name, String email, String phone, String password, String userType, String userImage) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_USER_NAME, name);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_PHONE, phone);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_PASSWORD, password);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_TYPE, userType);
        values.put(FirstAidDatabaseHelper.COLUMN_USER_IMAGE, userImage);

        long result = db.insert(FirstAidDatabaseHelper.TABLE_USER, null, values);
        return result != -1;
    }

    // Check if user exists by email
    public boolean checkUserExists(String email) {
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_USER,
                null,
                FirstAidDatabaseHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{email},
                null, null, null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Check login credentials
    public boolean checkUserCredentials(String email, String password) {
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
        return isValid;
    }

    // Get user by email & password
    public User getUserByCredentials(String email, String password) {
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
            user = extractUserFromCursor(cursor);
        }

        cursor.close();
        return user;
    }

    // Get user type only
    public String getUserType(int userId) {
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_USER,
                new String[]{FirstAidDatabaseHelper.COLUMN_USER_TYPE},
                FirstAidDatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        String userType = "general";
        if (cursor.moveToFirst()) {
            userType = cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_TYPE));
        }

        cursor.close();
        return userType;
    }

    // Get user by ID
    public User getUserById(int userId) {
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_USER,
                null,
                FirstAidDatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        User user = null;
        if (cursor.moveToFirst()) {
            user = extractUserFromCursor(cursor);
        }

        cursor.close();
        return user;
    }

    // Update user
    public boolean updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_USER_NAME, user.getUserName());
        values.put(FirstAidDatabaseHelper.COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(FirstAidDatabaseHelper.COLUMN_USER_PHONE, user.getUserPhoneNum());
        values.put(FirstAidDatabaseHelper.COLUMN_USER_PASSWORD, user.getUserPassword());
        values.put(FirstAidDatabaseHelper.COLUMN_USER_TYPE, user.getUserType());
        values.put(FirstAidDatabaseHelper.COLUMN_USER_IMAGE, user.getUserImage());

        int rows = db.update(
                FirstAidDatabaseHelper.TABLE_USER,
                values,
                FirstAidDatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())}
        );

        return rows > 0;
    }

    // Get user ID by email
    public int getUserIdByEmail(String email) {
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_USER,
                new String[]{FirstAidDatabaseHelper.COLUMN_USER_ID},
                FirstAidDatabaseHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{email},
                null, null, null
        );

        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_ID));
        }

        cursor.close();
        return id;
    }

    // Helper to create User from Cursor
    private User extractUserFromCursor(Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_USER_IMAGE))
        );
    }
}
