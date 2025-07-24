package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.VideoRecommendation;

import java.util.ArrayList;
import java.util.List;

public class VideoRecommendationDAO {

    private SQLiteDatabase db;
    private final FirstAidDatabaseHelper dbHelper;

    public VideoRecommendationDAO(Context context) {
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

    // Inserts a new video recommendation into the database
    public void insertRecommendation(VideoRecommendation video) {
        open();
        ContentValues values = new ContentValues();
        values.put("module_id", video.getModuleId());
        values.put("title", video.getTitle());
        values.put("url", video.getUrl());
        values.put("thumbnail_url", video.getThumbnailUrl());
        values.put("tag", video.getTag());
        db.insert("video_recommendations", null, values);
        close();
    }

    // Returns all recommendations for a specific module ID
    public List<VideoRecommendation> getRecommendationsByModule(int moduleId) {
        open();
        List<VideoRecommendation> list = new ArrayList<>();
        Cursor cursor = db.query(
                "video_recommendations",
                null,
                "module_id = ?",
                new String[]{String.valueOf(moduleId)},
                null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                VideoRecommendation video = new VideoRecommendation();
                video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                video.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
                video.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                video.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                video.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail_url")));
                video.setTag(cursor.getString(cursor.getColumnIndexOrThrow("tag")));
                list.add(video);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return list;
    }

    // Inserts a predefined list of videos for initial database setup
    public void insertInitialVideos(SQLiteDatabase db) {
        // Check if table already has data
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM video_recommendations", null);
        if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
            cursor.close();
            return; // Skip if already inserted
        }
        cursor.close();

        ContentValues values = new ContentValues();

        // CPR Videos (Module ID 1)
        insertVideo(db, values, 1, "Learn how to do CPR", "hizBdM1Ob68", "cpr_basics");
        insertVideo(db, values, 1, "How to perform Hands-only CPR", "6eRwgM2Pa4o", "cpr_hands_only");
        insertVideo(db, values, 1, "How to Use a Defibrillator (AED)", "UFvL7wTFzl0", "aed");
        insertVideo(db, values, 1, "CPR & AED Video", "bQNO6G-o7d4", "aed");
        insertVideo(db, values, 1, "The Recovery Position", "GmqXqwSV3bo", "recovery_position");
        insertVideo(db, values, 1, "Child CPR - Lay Rescuer", "22hHcU9nO-Y", "child_cpr");
        insertVideo(db, values, 1, "How to perform Infant CPR", "vxbOi9ZSMDo", "infant_cpr");
        insertVideo(db, values, 1, "CPR method for adult patient (BM)", "5cjb0h_kbjY", "adult_cpr");
        insertVideo(db, values, 1, "How to Give Baby CPR", "avYRvVHAvfM", "infant_cpr");
        insertVideo(db, values, 1, "Cara menggunakan AED untuk pertolongan cemas", "fYmlkW29XvA", "aed");
        insertVideo(db, values, 1, "Airway Manoeuvres (BLS)", "7NNe_Qje3yg", "airway");
        insertVideo(db, values, 1, "Open Airway and Give Breaths", "gQzOer68zEI", "airway");
        insertVideo(db, values, 1, "How to Perform Chest Compressions", "3Y5sO2REU2o", "compressions");

        // Bleeding Management Videos (Module ID 2)
        insertVideo(db, values, 2, "How to Stop Bleeding in an Emergency", "8sEijZkfUHI", "bleeding_control");
        insertVideo(db, values, 2, "Children First Aid: Nosebleed", "_UGeHti3zlI", "nosebleed");
        insertVideo(db, values, 2, "How to Treat Shock", "61urGQrmeNM", "shock");
        insertVideo(db, values, 2, "How to apply an improvised tourniquet", "DVBJ-9fNAxM", "tourniquet");
        insertVideo(db, values, 2, "Bleeding Control: Arterial Bleeding", "wA4ps2Bw1e0", "arterial_bleeding");
        insertVideo(db, values, 2, "First Aid: Cuts & Bruises", "AhANvBB9hz0", "cuts_bruises");
        insertVideo(db, values, 2, "How To Deal With Severe Bleeding", "5GVYiD0WzaE", "bleeding_control");
        insertVideo(db, values, 2, "Bleeding - Animated", "V8KiNURVjgk", "bleeding_basics");
        insertVideo(db, values, 2, "Stop-the-Bleed: How to Apply a Tourniquet", "qxH_NzFUwpM", "tourniquet");
        insertVideo(db, values, 2, "Embedded Object in a Cut", "xQXGG66SaFk", "cuts_object");
    }

    private void insertVideo(SQLiteDatabase db, ContentValues values, int moduleId, String title, String youtubeId, String tag) {
        values.clear();
        values.put("module_id", moduleId);
        values.put("title", title);
        values.put("url", "https://youtu.be/" + youtubeId);
        values.put("thumbnail_url", "https://img.youtube.com/vi/" + youtubeId + "/0.jpg");
        values.put("tag", tag);
        db.insert("video_recommendations", null, values);
    }

    public List<VideoRecommendation> getTopRecommendationsForModule(int moduleId, int limit) {
        open();
        List<VideoRecommendation> list = new ArrayList<>();
        Cursor cursor = db.query(
                "video_recommendations",
                null,
                "module_id = ?",
                new String[]{String.valueOf(moduleId)},
                null, null,
                "id ASC",
                String.valueOf(limit)
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                VideoRecommendation video = new VideoRecommendation();
                video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                video.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
                video.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                video.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                video.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail_url")));
                video.setTag(cursor.getString(cursor.getColumnIndexOrThrow("tag")));
                list.add(video);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return list;
    }

    // Gets top 10 global recommendations sorted by click count
    public List<VideoRecommendation> getAllRecommendationsSortedByClickCount() {
        List<VideoRecommendation> videos = new ArrayList<>();
        open();
        Cursor cursor = db.rawQuery(
                "SELECT vr.*, COUNT(vcl.id) AS click_count " +
                        "FROM video_recommendations vr " +
                        "LEFT JOIN video_click_log vcl ON vr.module_id = vcl.module_id AND vr.title = vcl.video_title " +
                        "GROUP BY vr.id " +
                        "ORDER BY click_count DESC LIMIT 10", null);

        if (cursor.moveToFirst()) {
            do {
                VideoRecommendation video = new VideoRecommendation(
                        cursor.getInt(cursor.getColumnIndexOrThrow("module_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("url")),
                        cursor.getString(cursor.getColumnIndexOrThrow("thumbnail_url")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tag"))
                );
                videos.add(video);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return videos;
    }

    public List<VideoRecommendation> getTopGlobalRecommendations(int limit) {
        List<VideoRecommendation> videos = new ArrayList<>();
        open();
        Cursor cursor = db.rawQuery(
                "SELECT vr.* FROM video_recommendations vr " +
                        "JOIN video_click_log vcl ON vr.module_id = vcl.module_id AND vr.title = vcl.video_title " +
                        "GROUP BY vr.module_id, vr.title ORDER BY COUNT(*) DESC LIMIT ?",
                new String[]{String.valueOf(limit)}
        );
        // Convert cursor to list...
        close();
        return videos;
    }

    //Get videos by tag (for AI-like recommendations)
    public List<VideoRecommendation> getRecommendationsByTag(String tag, int limit) {
        open();
        List<VideoRecommendation> list = new ArrayList<>();
        Cursor cursor = db.query(
                "video_recommendations",
                null,
                "tag = ?",
                new String[]{tag},
                null, null,
                "id ASC",
                String.valueOf(limit)
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                VideoRecommendation video = new VideoRecommendation();
                video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                video.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
                video.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                video.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                video.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail_url")));
                video.setTag(cursor.getString(cursor.getColumnIndexOrThrow("tag")));
                list.add(video);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return list;
    }

    public void logVideoClick(int moduleId, String videoTitle, String youtubeVideoId, long timestamp, String tag) {
        open();
        ContentValues values = new ContentValues();
        values.put("module_id", moduleId);
        values.put("video_title", videoTitle);
        values.put("youtube_video_id", youtubeVideoId);
        values.put("timestamp", String.valueOf(timestamp)); // store as String for consistency
        values.put("tag", tag);
        db.insert("video_click_log", null, values);
        close();
    }

    // Returns top N most watched tags for a module
    public List<String> getMostWatchedTags(int moduleId, int topN) {
        open();
        List<String> topTags = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT r.tag, COUNT(*) as frequency " +
                        "FROM video_click_log l " +
                        "JOIN video_recommendations r ON l.youtube_video_id = substr(r.url, -11) " +
                        "WHERE r.tag IS NOT NULL AND r.module_id = ? " +
                        "GROUP BY r.tag " +
                        "ORDER BY frequency DESC " +
                        "LIMIT ?",
                new String[]{String.valueOf(moduleId), String.valueOf(topN)}
        );

        if (cursor.moveToFirst()) {
            do {
                topTags.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return topTags;
    }

    // Gets all recommendations reshuffled based on re-watch frequency
    public List<VideoRecommendation> getRecommendationsByWatchCount() {
        List<VideoRecommendation> videos = new ArrayList<>();
        open();

        Cursor cursor = db.rawQuery(
                "SELECT vr.* FROM video_recommendations vr " +
                        "LEFT JOIN video_click_log vcl ON substr(vr.url, -11) = vcl.youtube_video_id " +
                        "GROUP BY vr.id " +
                        "ORDER BY COUNT(vcl.id) DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                VideoRecommendation video = new VideoRecommendation();
                video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                video.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
                video.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                video.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                video.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail_url")));
                video.setTag(cursor.getString(cursor.getColumnIndexOrThrow("tag")));
                videos.add(video);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return videos;
    }

    public List<VideoRecommendation> getRecommendationsByUserWatchCount(String userId, int limit) {
        List<VideoRecommendation> videos = new ArrayList<>();
        open();

        Cursor cursor = db.rawQuery(
                "SELECT vr.*, COUNT(vcl.id) AS click_count " +
                        "FROM video_recommendations vr " +
                        "LEFT JOIN video_click_log vcl ON substr(vr.url, -11) = vcl.youtube_video_id " +
                        "AND vcl.tag LIKE ? " +
                        "GROUP BY vr.id " +
                        "ORDER BY click_count DESC " +
                        "LIMIT ?",
                new String[]{"%" + userId + ":%", String.valueOf(limit)}
        );

        if (cursor.moveToFirst()) {
            do {
                VideoRecommendation video = new VideoRecommendation();
                video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                video.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
                video.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                video.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                video.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail_url")));
                video.setTag(cursor.getString(cursor.getColumnIndexOrThrow("tag")));
                videos.add(video);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return videos;
    }



    public void clearAllRecommendations() {
        open();
        db.delete("video_recommendations", null, null);
        close();
    }
}
