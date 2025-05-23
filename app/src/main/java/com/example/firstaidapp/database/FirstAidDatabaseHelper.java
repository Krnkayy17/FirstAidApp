package com.example.firstaidapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FirstAidDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "FirstAidApp.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_USER = "USER";
    public static final String TABLE_MODULE = "MODULE";
    public static final String TABLE_CONTENT = "CONTENT";
    public static final String TABLE_QUESTION = "QUESTION";
    public static final String TABLE_ASSESSMENT_RESULT = "ASSESSMENT_RESULT";

    // USER table
    public static final String COLUMN_USER_ID = "UserID";
    public static final String COLUMN_USER_NAME = "UserName";
    public static final String COLUMN_USER_EMAIL = "UserEmail";
    public static final String COLUMN_USER_PHONE = "UserPhoneNum";
    public static final String COLUMN_USER_PASSWORD = "UserPassword";

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

    // CONTENT Table Columns
    public static final String COLUMN_CONTENT_ID = "ContentID";
    public static final String COLUMN_CONTENT_TITLE = "ContentTitle";
    public static final String COLUMN_CONTENT_TEXT = "ContentText";
    public static final String COLUMN_CONTENT_IMAGE = "ContentImage";
    public static final String COLUMN_CONTENT_URL = "ContentURL";
    public static final String COLUMN_CONTENT_ORDER = "ContentOrder";

    // QUESTION table
    public static final String COLUMN_QUESTION_ID = "QuestionID";
    public static final String COLUMN_QUESTION_TEXT = "QuestionText";
    public static final String COLUMN_QUESTION_TYPE = "QuestionType";
    public static final String COLUMN_OPTION_A = "OptionA";
    public static final String COLUMN_OPTION_B = "OptionB";
    public static final String COLUMN_OPTION_C = "OptionC";
    public static final String COLUMN_OPTION_D = "OptionD";
    public static final String COLUMN_CORRECT_ANSWER = "CorrectAnswer";
    public static final String COLUMN_CORRECT_SEQUENCE = "CorrectSequence";
    public static final String COLUMN_QUESTION_IMAGE = "QuestionImage";

    // ASSESSMENT_RESULT table
    public static final String COLUMN_RESULT_ID = "ResultID";
    public static final String COLUMN_SCORE = "Score";
    public static final String COLUMN_TOTAL_QUESTIONS = "TotalQuestions";
    public static final String COLUMN_RETAKE_COUNT = "RetakeCount";
    public static final String COLUMN_DATE_TAKEN = "DateTaken";


    public FirstAidDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // USER table
        String createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_USER_PHONE + " TEXT, " +
                COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // MODULE table
        String createModuleTable = "CREATE TABLE " + TABLE_MODULE + " (" +
                COLUMN_MODULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MODULE_NAME + " TEXT NOT NULL, " +
                COLUMN_MODULE_DESCRIPTION + " TEXT, " +
                COLUMN_DIFFICULTY_LEVEL + " TEXT, " +
                COLUMN_ESTIMATED_DURATION + " INTEGER, " +
                COLUMN_TOTAL_ASSESSMENTS + " INTEGER, " +
                COLUMN_MODULE_COMPLETION_CRITERIA + " TEXT, " +
                COLUMN_MODULE_ACCESSED_DATE + " TEXT DEFAULT 'Not Accessed Yet', " +
                COLUMN_MODULE_COMPLETION_STATUS + " TEXT DEFAULT 'Not Started')";
        db.execSQL(createModuleTable);

        // CONTENT table
        String createContentTable = "CREATE TABLE " + TABLE_CONTENT + " (" +
                COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MODULE_ID + " INTEGER, " +
                COLUMN_CONTENT_TITLE + " TEXT, " +
                COLUMN_CONTENT_TEXT + " TEXT, " +
                COLUMN_CONTENT_IMAGE + " TEXT, " +
                COLUMN_CONTENT_URL + " TEXT, " +
                COLUMN_CONTENT_ORDER + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + "))";
        db.execSQL(createContentTable);

        // QUESTION table
        String createQuestionTable = "CREATE TABLE " + TABLE_QUESTION + " (" +
                COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MODULE_ID + " INTEGER, " +
                COLUMN_QUESTION_TEXT + " TEXT NOT NULL, " +
                COLUMN_QUESTION_TYPE + " TEXT NOT NULL, " +
                COLUMN_OPTION_A + " TEXT, " +
                COLUMN_OPTION_B + " TEXT, " +
                COLUMN_OPTION_C + " TEXT, " +
                COLUMN_OPTION_D + " TEXT, " +
                COLUMN_CORRECT_ANSWER + " TEXT, " +
                COLUMN_CORRECT_SEQUENCE + " TEXT, " +
                COLUMN_QUESTION_IMAGE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + "))";
        db.execSQL(createQuestionTable);

        // ASSESSMENT_RESULT table
        String createAssessmentResultTable = "CREATE TABLE " + TABLE_ASSESSMENT_RESULT + " (" +
                COLUMN_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_MODULE_ID + " INTEGER, " +
                COLUMN_SCORE + " INTEGER, " +
                COLUMN_TOTAL_QUESTIONS + " INTEGER, " +
                COLUMN_RETAKE_COUNT + " INTEGER, " +
                COLUMN_DATE_TAKEN + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + "))";
        db.execSQL(createAssessmentResultTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_RESULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
