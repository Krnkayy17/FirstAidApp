package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.AssessmentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentResultDAO {

    private SQLiteDatabase db;
    private FirstAidDatabaseHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public AssessmentResultDAO(Context context) {
        dbHelper = new FirstAidDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert new result
    public long insertResult(AssessmentResult result) {
        ContentValues values = new ContentValues();
        values.put("UserID", result.getUserId());
        values.put("ModuleID", result.getModuleId());
        values.put("Score", result.getScore());
        values.put("TotalQuestions", result.getTotalQuestions());
        values.put("RetakeCount", result.getRetakeCount());
        values.put("DateTaken", dateFormat.format(result.getDateTaken()));
        return db.insert("ASSESSMENT_RESULT", null, values);
    }

    // Update result by user+module
    public int updateResult(AssessmentResult result) {
        ContentValues values = new ContentValues();
        values.put("Score", result.getScore());
        values.put("TotalQuestions", result.getTotalQuestions());
        values.put("RetakeCount", result.getRetakeCount());
        values.put("DateTaken", dateFormat.format(result.getDateTaken()));

        return db.update("ASSESSMENT_RESULT", values,
                "UserID=? AND ModuleID=?",
                new String[]{String.valueOf(result.getUserId()), String.valueOf(result.getModuleId())});
    }

    // Get latest result for a user + module
    public AssessmentResult getLatestResult(int userId, int moduleId) {
        Cursor cursor = db.query("ASSESSMENT_RESULT",
                null,
                "UserID=? AND ModuleID=?",
                new String[]{String.valueOf(userId), String.valueOf(moduleId)},
                null, null, "DateTaken DESC", "1");

        if (cursor != null && cursor.moveToFirst()) {
            AssessmentResult result = buildResultFromCursor(cursor);
            cursor.close();
            return result;
        }

        if (cursor != null) cursor.close();
        return null;
    }

    // Get latest score only
    public int getLatestScoreForModule(int userId, int moduleId) {
        Cursor cursor = db.query("ASSESSMENT_RESULT",
                new String[]{"Score"},
                "UserID=? AND ModuleID=?",
                new String[]{String.valueOf(userId), String.valueOf(moduleId)},
                null, null,
                "DateTaken DESC",
                "1");

        if (cursor != null && cursor.moveToFirst()) {
            int score = cursor.getInt(cursor.getColumnIndexOrThrow("Score"));
            cursor.close();
            return score;
        }

        if (cursor != null) cursor.close();
        return 0;
    }

    // Get all past attempts for specific user and module
    public List<AssessmentResult> getAllAttemptsForUserAndModule(int userId, int moduleId) {
        List<AssessmentResult> list = new ArrayList<>();

        Cursor cursor = db.query("ASSESSMENT_RESULT",
                null,
                "UserID=? AND ModuleID=?",
                new String[]{String.valueOf(userId), String.valueOf(moduleId)},
                null, null,
                "DateTaken DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(buildResultFromCursor(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

    // Helper method to map cursor to object
    private AssessmentResult buildResultFromCursor(Cursor cursor) {
        AssessmentResult result = new AssessmentResult();
        result.setResultId(cursor.getInt(cursor.getColumnIndexOrThrow("ResultID")));
        result.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("UserID")));
        result.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("ModuleID")));
        result.setScore(cursor.getInt(cursor.getColumnIndexOrThrow("Score")));
        result.setTotalQuestions(cursor.getInt(cursor.getColumnIndexOrThrow("TotalQuestions")));
        result.setRetakeCount(cursor.getInt(cursor.getColumnIndexOrThrow("RetakeCount")));
        try {
            result.setDateTaken(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .parse(cursor.getString(cursor.getColumnIndexOrThrow("DateTaken"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
