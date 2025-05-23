package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.model.AssessmentResult;

import java.util.Date;
import java.text.SimpleDateFormat;

public class AssessmentResultDAO {
    private SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AssessmentResultDAO(Context context) {
        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert new assessment result
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

    // Update retake count + score for a module
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

    // Get result for a specific user + module
    public AssessmentResult getResult(int userId, int moduleId) {
        Cursor cursor = db.query("ASSESSMENT_RESULT",
                null,
                "UserID=? AND ModuleID=?",
                new String[]{String.valueOf(userId), String.valueOf(moduleId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            AssessmentResult result = new AssessmentResult();
            result.setResultId(cursor.getInt(cursor.getColumnIndex("ResultID")));
            result.setUserId(cursor.getInt(cursor.getColumnIndex("UserID")));
            result.setModuleId(cursor.getInt(cursor.getColumnIndex("ModuleID")));
            result.setScore(cursor.getInt(cursor.getColumnIndex("Score")));
            result.setTotalQuestions(cursor.getInt(cursor.getColumnIndex("TotalQuestions")));
            result.setRetakeCount(cursor.getInt(cursor.getColumnIndex("RetakeCount")));
            try {
                result.setDateTaken(dateFormat.parse(cursor.getString(cursor.getColumnIndex("DateTaken"))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            cursor.close();
            return result;
        }
        return null;
    }
}
