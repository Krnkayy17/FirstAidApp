package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.firstaidapp.models.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FirstAidApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_CONTENT = "CONTENT";

    // Column Names
    public static final String COLUMN_CONTENT_ID = "ContentID";
    public static final String COLUMN_MODULE_ID = "ModuleID";
    public static final String COLUMN_CONTENT_TITLE = "ContentTitle";
    public static final String COLUMN_CONTENT_TEXT = "ContentText";
    public static final String COLUMN_CONTENT_IMAGE = "ContentImage";
    public static final String COLUMN_CONTENT_URL = "ContentURL";
    public static final String COLUMN_CONTENT_ORDER = "ContentOrder";

    // Create Table Query
    private static final String CREATE_TABLE_CONTENT = "CREATE TABLE " + TABLE_CONTENT + " ("
            + COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MODULE_ID + " INTEGER, "
            + COLUMN_CONTENT_TITLE + " TEXT, "
            + COLUMN_CONTENT_TEXT + " TEXT, "
            + COLUMN_CONTENT_IMAGE + " TEXT, "
            + COLUMN_CONTENT_URL + " TEXT, "
            + COLUMN_CONTENT_ORDER + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES MODULE(ModuleID))";

    public ContentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT);
        onCreate(db);
    }

    // Insert Content
    public void insertContent(int moduleID, String title, String text, String image, String url, int order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MODULE_ID, moduleID);
        values.put(COLUMN_CONTENT_TITLE, title);
        values.put(COLUMN_CONTENT_TEXT, text);
        values.put(COLUMN_CONTENT_IMAGE, image);
        values.put(COLUMN_CONTENT_URL, url);
        values.put(COLUMN_CONTENT_ORDER, order);

        db.insert(TABLE_CONTENT, null, values);
        db.close();
    }

    // Get All Content by ModuleID Ordered
    public List<Content> getContentByModule(int moduleID) {
        List<Content> contentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CONTENT + " WHERE " + COLUMN_MODULE_ID + " = ? ORDER BY " + COLUMN_CONTENT_ORDER + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moduleID)});

        if (cursor.moveToFirst()) {
            do {
                Content content = new Content(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MODULE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ORDER))
                );
                contentList.add(content);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return contentList;
    }

    // Get Next Subtopic
    public Content getNextSubtopic(int moduleID, int currentOrder) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CONTENT + " WHERE " + COLUMN_MODULE_ID + " = ? AND " + COLUMN_CONTENT_ORDER + " > ? ORDER BY " + COLUMN_CONTENT_ORDER + " ASC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moduleID), String.valueOf(currentOrder)});

        if (cursor.moveToFirst()) {
            Content content = new Content(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MODULE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ORDER))
            );
            cursor.close();
            db.close();
            return content;
        }

        cursor.close();
        db.close();
        return null;
    }
}
