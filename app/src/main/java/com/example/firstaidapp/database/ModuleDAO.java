package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Module;

import java.util.ArrayList;
import java.util.List;

//ModuleDAO for Database Operations
public class ModuleDAO {
    private SQLiteDatabase db;
    private ModuleDatabaseHelper dbHelper;

    public ModuleDAO(Context context) {
        dbHelper = new ModuleDatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addModule(Module module) {
        open();
        ContentValues values = new ContentValues();
        values.put(ModuleDatabaseHelper.COLUMN_MODULE_NAME, module.getModuleName());
        values.put(ModuleDatabaseHelper.COLUMN_MODULE_DESCRIPTION, module.getDescription());
        values.put(ModuleDatabaseHelper.COLUMN_DIFFICULTY_LEVEL, module.getDifficultyLevel());
        values.put(ModuleDatabaseHelper.COLUMN_ESTIMATED_DURATION, module.getEstimatedDuration());
        values.put(ModuleDatabaseHelper.COLUMN_TOTAL_ASSESSMENTS, module.getTotalAssessments());
        values.put(ModuleDatabaseHelper.COLUMN_MODULE_COMPLETION_CRITERIA, module.getCompletionCriteria());
        values.put(ModuleDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE, module.getAccessedDate());

        long result = db.insert(ModuleDatabaseHelper.TABLE_MODULE, null, values);
        close();
        return result;
    }

    public List<Module> getAllModules() {
        open();
        List<Module> moduleList = new ArrayList<>();
        Cursor cursor = db.query(ModuleDatabaseHelper.TABLE_MODULE,
                new String[]{ModuleDatabaseHelper.COLUMN_MODULE_ID, ModuleDatabaseHelper.COLUMN_MODULE_NAME,
                        ModuleDatabaseHelper.COLUMN_MODULE_DESCRIPTION, ModuleDatabaseHelper.COLUMN_DIFFICULTY_LEVEL,
                        ModuleDatabaseHelper.COLUMN_ESTIMATED_DURATION, ModuleDatabaseHelper.COLUMN_TOTAL_ASSESSMENTS,
                        ModuleDatabaseHelper.COLUMN_MODULE_COMPLETION_CRITERIA, ModuleDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE,
                        ModuleDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Module module = new Module(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getString(7), // Last Accessed Date
                        cursor.getString(8)  // Completion Status
                );
                moduleList.add(module);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return moduleList;
    }

    // Update Last Accessed Date
    public void updateLastAccessed(int moduleID, String lastAccessedDate) {
        open();
        ContentValues values = new ContentValues();
        values.put(ModuleDatabaseHelper.COLUMN_MODULE_ACCESSED_DATE, lastAccessedDate);

        db.update(ModuleDatabaseHelper.TABLE_MODULE, values, ModuleDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleID)});
        close();
    }

    //Update Completion Status
    public void updateCompletionStatus(int moduleID, String status) {
        open();
        ContentValues values = new ContentValues();
        values.put(ModuleDatabaseHelper.COLUMN_MODULE_COMPLETION_STATUS, status);

        db.update(ModuleDatabaseHelper.TABLE_MODULE, values, ModuleDatabaseHelper.COLUMN_MODULE_ID + "=?",
                new String[]{String.valueOf(moduleID)});
        close();
    }


}