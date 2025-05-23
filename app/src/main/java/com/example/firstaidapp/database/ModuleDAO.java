package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleDAO {
    private SQLiteDatabase db;
    private FirstAidDatabaseHelper dbHelper;

    public ModuleDAO(Context context) {
        dbHelper = new FirstAidDatabaseHelper(context);
        db = dbHelper.getWritableDatabase(); // Auto-open DB
    }

    public long addModule(Module module) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_NAME, module.getModuleName());
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_DESCRIPTION, module.getDescription());
        values.put(FirstAidDatabaseHelper.COLUMN_DIFFICULTY_LEVEL, module.getDifficultyLevel());
        values.put(FirstAidDatabaseHelper.COLUMN_ESTIMATED_DURATION, module.getEstimatedDuration());
        values.put(FirstAidDatabaseHelper.COLUMN_TOTAL_ASSESSMENTS, module.getTotalAssessments());
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_CRITERIA, module.getCompletionCriteria());
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE, module.getAccessedDate());
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS, module.getCompletionStatus());

        return db.insert(FirstAidDatabaseHelper.TABLE_MODULE, null, values);
    }

    public List<Module> getAllModules() {
        List<Module> moduleList = new ArrayList<>();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_MODULE,
                null, null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                Module module = new Module(
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_DIFFICULTY_LEVEL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_ESTIMATED_DURATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_TOTAL_ASSESSMENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_CRITERIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS))
                );
                moduleList.add(module);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return moduleList;
    }

    public void updateLastAccessed(int moduleID, String lastAccessedDate) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE, lastAccessedDate);

        db.update(
                FirstAidDatabaseHelper.TABLE_MODULE,
                values,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleID)}
        );
    }

    public void updateCompletionStatus(int moduleID, String status) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS, status);

        db.update(
                FirstAidDatabaseHelper.TABLE_MODULE,
                values,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleID)}
        );
    }

    public Module getModuleById(int id) {
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_MODULE,
                null,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Module module = new Module(
                    cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_DIFFICULTY_LEVEL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_ESTIMATED_DURATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_TOTAL_ASSESSMENTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_CRITERIA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS))
            );
            cursor.close();
            return module;
        }
        return null;
    }


    public int getModuleProgress(int moduleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT progress FROM MODULE WHERE id = ?", new String[]{String.valueOf(moduleId)});
        int progress = 0;
        if (cursor.moveToFirst()) {
            progress = cursor.getInt(0);
        }
        cursor.close();
        return progress;
    }

}
