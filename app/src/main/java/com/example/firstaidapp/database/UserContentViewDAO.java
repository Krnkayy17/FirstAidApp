package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Content;

import java.util.ArrayList;
import java.util.List;

public class UserContentViewDAO {

    private final SQLiteDatabase db;
    private final FirstAidDatabaseHelper dbHelper;

    public static final String TABLE_USER_CONTENT_VIEW = "USER_CONTENT_VIEW";
    public static final String COLUMN_VIEW_ID = "ViewID";
    public static final String COLUMN_USER_ID = "UserID";
    public static final String COLUMN_MODULE_ID = "ModuleID";
    public static final String COLUMN_CONTENT_ID = "ContentID";
    public static final String COLUMN_VIEWED = "Viewed"; // 0 or 1

    public UserContentViewDAO(Context context) {
        dbHelper = new FirstAidDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER_CONTENT_VIEW + " (" +
                COLUMN_VIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_MODULE_ID + " INTEGER, " +
                COLUMN_CONTENT_ID + " INTEGER, " +
                COLUMN_VIEWED + " INTEGER DEFAULT 1)");
    }

    public void markContentAsViewed(int userId, int contentId, int moduleId) {
        if (!isContentViewed(userId, contentId, moduleId)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_MODULE_ID, moduleId);
            values.put(COLUMN_CONTENT_ID, contentId);
            values.put(COLUMN_VIEWED, 1);
            db.insert(TABLE_USER_CONTENT_VIEW, null, values);
        }
    }

    public boolean isContentViewed(int userId, int contentId, int moduleId) {
        Cursor cursor = db.query(TABLE_USER_CONTENT_VIEW,
                null,
                COLUMN_USER_ID + "=? AND " + COLUMN_MODULE_ID + "=? AND " + COLUMN_CONTENT_ID + "=? AND " + COLUMN_VIEWED + "=1",
                new String[]{String.valueOf(userId), String.valueOf(moduleId), String.valueOf(contentId)},
                null, null, null);

        boolean viewed = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        return viewed;
    }

    public int getViewedContentCount(int userId, int moduleId) {
        int count = 0;
        SQLiteDatabase readableDb = dbHelper.getReadableDatabase();

        Cursor cursor = readableDb.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_USER_CONTENT_VIEW +
                        " WHERE " + COLUMN_USER_ID + "=? AND " + COLUMN_MODULE_ID + "=? AND " + COLUMN_VIEWED + "=1",
                new String[]{String.valueOf(userId), String.valueOf(moduleId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        readableDb.close();
        return count;
    }

    public List<Content> getAllContentForModule(int moduleId) {
        List<Content> contents = new ArrayList<>();
        SQLiteDatabase readableDb = dbHelper.getReadableDatabase();

        Cursor cursor = readableDb.rawQuery("SELECT * FROM Content WHERE ModuleID = ? ORDER BY ContentOrder ASC",
                new String[]{String.valueOf(moduleId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Content content = new Content();
                content.setContentId(cursor.getInt(cursor.getColumnIndexOrThrow("ContentID")));
                content.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("ModuleID")));
                content.setContentTitle(cursor.getString(cursor.getColumnIndexOrThrow("ContentTitle")));
                content.setContentOrder(cursor.getInt(cursor.getColumnIndexOrThrow("ContentOrder")));
                content.setContentType(cursor.getString(cursor.getColumnIndexOrThrow("ContentType")));
                content.setContentImage(cursor.getString(cursor.getColumnIndexOrThrow("ContentImage")));
                contents.add(content);
            } while (cursor.moveToNext());
            cursor.close();
        }

        readableDb.close();
        return contents;
    }

    public int getViewedCountForModule(int userId, int moduleId) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USER_CONTENT_VIEW +
                        " WHERE " + COLUMN_USER_ID + "=? AND " + COLUMN_MODULE_ID + "=? AND " + COLUMN_VIEWED + "=1",
                new String[]{String.valueOf(userId), String.valueOf(moduleId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getLastViewedOrder(int userId, int moduleId) {
        Cursor cursor = db.rawQuery(
                "SELECT c.ContentOrder FROM " + TABLE_USER_CONTENT_VIEW + " v " +
                        "JOIN Content c ON v.ContentID = c.ContentID " +
                        "WHERE v.UserID = ? AND v.ModuleID = ? " +
                        "ORDER BY c.ContentOrder DESC LIMIT 1",
                new String[]{String.valueOf(userId), String.valueOf(moduleId)}
        );

        int order = 1; // fallback to beginning
        if (cursor.moveToFirst()) {
            order = cursor.getInt(0);
        }
        cursor.close();
        return order;
    }

    public void clearAllViews() {
        db.delete(TABLE_USER_CONTENT_VIEW, null, null);
    }
}
