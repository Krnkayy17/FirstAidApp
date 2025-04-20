package archive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ModuleDatabaseHelper extends SQLiteOpenHelper {

    // Database Name & Version
    private static final String DATABASE_NAME = "FirstAidApp.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    public static final String TABLE_MODULE = "MODULE";
    public static final String TABLE_MODULE_CONTENT = "MODULE_CONTENT"; // RENAMED to avoid conflict with ContentDatabaseHelper

    // MODULE Table Columns
    public static final String COLUMN_MODULE_ID = "ModuleID";
    public static final String COLUMN_MODULE_NAME = "ModuleName";
    public static final String COLUMN_MODULE_DESCRIPTION = "ModuleDescription";
    public static final String COLUMN_DIFFICULTY_LEVEL = "DifficultyLevel";
    public static final String COLUMN_ESTIMATED_DURATION = "EstimatedDuration";
    public static final String COLUMN_TOTAL_ASSESSMENTS = "TotalAssessments";
    public static final String COLUMN_MODULE_COMPLETION_CRITERIA = "ModuleCompletionCriteria";
    public static final String COLUMN_MODULE_ACCESSED_DATE = "ModuleAccessedDate";
    public static final String COLUMN_MODULE_COMPLETION_STATUS = "ModuleCompletionStatus";

    // MODULE_CONTENT Table Columns
    public static final String COLUMN_CONTENT_ID = "ContentID";
    public static final String COLUMN_MODULE_ID_FK = "ModuleID";  // Foreign Key
    public static final String COLUMN_CONTENT_TYPE = "ContentType";
    public static final String COLUMN_CONTENT_TITLE = "ContentTitle";
    public static final String COLUMN_CONTENT_URL = "ContentUrl";
    public static final String COLUMN_CONTENT_DESCRIPTION = "ContentDescription";
    public static final String COLUMN_CONTENT_SEQUENCE_ORDER = "ContentSequenceOrder";

    // SQL to Create MODULE Table
    private static final String CREATE_TABLE_MODULE = "CREATE TABLE " + TABLE_MODULE + " (" +
            COLUMN_MODULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MODULE_NAME + " TEXT NOT NULL, " +
            COLUMN_MODULE_DESCRIPTION + " TEXT, " +
            COLUMN_DIFFICULTY_LEVEL + " TEXT, " +
            COLUMN_ESTIMATED_DURATION + " INTEGER, " +
            COLUMN_TOTAL_ASSESSMENTS + " INTEGER, " +
            COLUMN_MODULE_COMPLETION_CRITERIA + " TEXT, " +
            COLUMN_MODULE_ACCESSED_DATE + " TEXT DEFAULT 'Not Accessed Yet', " +
            COLUMN_MODULE_COMPLETION_STATUS + " TEXT DEFAULT 'Not Started'" +
            ");";

    // SQL to Create MODULE_CONTENT Table
    private static final String CREATE_TABLE_MODULE_CONTENT = "CREATE TABLE " + TABLE_MODULE_CONTENT + " (" +
            COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MODULE_ID_FK + " INTEGER, " +
            COLUMN_CONTENT_TYPE + " TEXT, " +
            COLUMN_CONTENT_TITLE + " TEXT, " +
            COLUMN_CONTENT_URL + " TEXT, " +
            COLUMN_CONTENT_DESCRIPTION + " TEXT, " +
            COLUMN_CONTENT_SEQUENCE_ORDER + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_MODULE_ID_FK + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + ") ON DELETE CASCADE" +
            ");";

    public ModuleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*@Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MODULE);
        db.execSQL(CREATE_TABLE_MODULE_CONTENT);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULE_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULE);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MODULE);
        db.execSQL(CREATE_TABLE_MODULE_CONTENT);
        Log.d("ModuleDatabaseHelper", "MODULE and MODULE_CONTENT tables created");
    }



}
