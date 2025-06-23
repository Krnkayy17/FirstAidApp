package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.VideoClickLog;

public class VideoClickLogDAO {
    private SQLiteDatabase db;
    private FirstAidDatabaseHelper dbHelper;

    public VideoClickLogDAO(Context context) {
        dbHelper = new FirstAidDatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void insertLog(VideoClickLog log) {
        open();
        ContentValues values = new ContentValues();
        values.put("module_id", log.getModuleId());
        values.put("video_title", log.getVideoTitle());
        values.put("youtube_video_id", log.getYoutubeVideoId());
        values.put("timestamp", log.getTimestamp());
        values.put("tag", log.getTag());
        db.insert("video_click_log", null, values);
        close();
    }

}
