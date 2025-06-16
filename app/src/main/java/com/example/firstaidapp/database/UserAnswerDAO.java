package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Question;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserAnswerDAO {
    private final SQLiteDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public UserAnswerDAO(Context context) {
        db = new FirstAidDatabaseHelper(context).getWritableDatabase();
    }

    public void insertUserAnswer(int userId, int moduleId, Question question, boolean isCorrect) {
        ContentValues values = new ContentValues();
        values.put("UserID", userId);
        values.put("ModuleID", moduleId);
        values.put("QuestionID", question.getQuestionId());
        values.put("UserAnswer", question.getUserAnswer());
        values.put("UserSequence", question.getUserSequence());
        values.put("IsCorrect", isCorrect ? 1 : 0);
        values.put("DateAnswered", dateFormat.format(new Date()));

        db.insert("USER_ANSWER", null, values);
    }
}
