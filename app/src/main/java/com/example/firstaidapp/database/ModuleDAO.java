package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleDAO {

    private final SQLiteDatabase db;
    private final FirstAidDatabaseHelper dbHelper;

    public ModuleDAO(Context context) {
        dbHelper = new FirstAidDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
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
        values.put(FirstAidDatabaseHelper.COLUMN_PROGRESS_PERCENTAGE, module.getProgressPercentage());
        values.put(FirstAidDatabaseHelper.COLUMN_IS_LOCKED, module.isLocked() ? 1 : 0);
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
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_PROGRESS_PERCENTAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_IS_LOCKED)) == 1
                );
                moduleList.add(module);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return moduleList;
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
                    cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_PROGRESS_PERCENTAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_IS_LOCKED)) == 1
            );
            cursor.close();
            return module;
        }
        return null;
    }

    public void updateLastAccessed(int moduleId, String lastAccessedDate) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE, lastAccessedDate);

        db.update(
                FirstAidDatabaseHelper.TABLE_MODULE,
                values,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleId)}
        );
    }

    public void updateCompletionStatus(int moduleId, String status) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS, status);

        db.update(
                FirstAidDatabaseHelper.TABLE_MODULE,
                values,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleId)}
        );
    }

    public void updateProgressPercentage(int moduleId, int percentage) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_PROGRESS_PERCENTAGE, percentage);

        db.update(
                FirstAidDatabaseHelper.TABLE_MODULE,
                values,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleId)}
        );
    }

    public void updateLockStatus(int moduleId, boolean locked) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_IS_LOCKED, locked ? 1 : 0);

        db.update(
                FirstAidDatabaseHelper.TABLE_MODULE,
                values,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleId)}
        );
    }

    public int getModuleIdByName(String moduleName) {
        int moduleId = -1;

        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_MODULE,
                new String[]{FirstAidDatabaseHelper.COLUMN_MODULE_ID},
                FirstAidDatabaseHelper.COLUMN_MODULE_NAME + " = ?",
                new String[]{moduleName},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            moduleId = cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_ID));
            cursor.close();
        }

        return moduleId;
    }

    public List<String> getAllModuleNames() {
        List<String> moduleNames = new ArrayList<>();
        Cursor cursor = db.query(
                "MODULE",  // MUST match your onCreate table name
                new String[]{"ModuleName"},
                null, null, null, null,
                "ModuleName ASC"
        );
        if (cursor.moveToFirst()) {
            do {
                moduleNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return moduleNames;
    }


    public int getModuleProgress(int moduleId) {
        Cursor cursor = db.rawQuery(
                "SELECT " + FirstAidDatabaseHelper.COLUMN_PROGRESS_PERCENTAGE +
                        " FROM " + FirstAidDatabaseHelper.TABLE_MODULE +
                        " WHERE " + FirstAidDatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{String.valueOf(moduleId)}
        );

        int progress = 0;
        if (cursor.moveToFirst()) {
            progress = cursor.getInt(0);
        }
        cursor.close();
        return progress;
    }
}
