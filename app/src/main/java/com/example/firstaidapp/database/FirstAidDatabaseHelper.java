package com.example.firstaidapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FirstAidDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FirstAidApp.db";
    private static final int DATABASE_VERSION = 8;

    // Table names
    public static final String TABLE_USER = "USER";
    public static final String TABLE_MODULE = "MODULE";
    public static final String TABLE_CONTENT = "CONTENT";
    public static final String TABLE_QUESTION = "QUESTION";
    public static final String TABLE_ASSESSMENT_RESULT = "ASSESSMENT_RESULT";
    public static final String TABLE_USER_ANSWER = "USER_ANSWER";
    public static final String TABLE_USER_CONTENT_VIEW = "USER_CONTENT_VIEW";

    // Common Columns
    public static final String COLUMN_USER_ID = "UserID";
    public static final String COLUMN_MODULE_ID = "ModuleID";

    // USER Table
    public static final String COLUMN_USER_NAME = "UserName";
    public static final String COLUMN_USER_EMAIL = "UserEmail";
    public static final String COLUMN_USER_PHONE = "UserPhoneNum";
    public static final String COLUMN_USER_PASSWORD = "UserPassword";
    public static final String COLUMN_USER_TYPE = "UserType";
    public static final String COLUMN_USER_IMAGE = "UserImage";

    // MODULE Table
    public static final String COLUMN_MODULE_NAME = "ModuleName";
    public static final String COLUMN_MODULE_DESCRIPTION = "ModuleDescription";
    public static final String COLUMN_DIFFICULTY_LEVEL = "DifficultyLevel";
    public static final String COLUMN_ESTIMATED_DURATION = "EstimatedDuration";
    public static final String COLUMN_TOTAL_ASSESSMENTS = "TotalAssessments";
    public static final String COLUMN_MODULE_COMPLETION_CRITERIA = "ModuleCompletionCriteria";
    public static final String COLUMN_MODULE_ACCESSED_DATE = "ModuleAccessedDate";
    public static final String COLUMN_MODULE_COMPLETION_STATUS = "ModuleCompletionStatus";
    public static final String COLUMN_PROGRESS_PERCENTAGE = "ProgressPercentage";
    public static final String COLUMN_IS_LOCKED = "is_locked";

    // CONTENT Table
    public static final String COLUMN_CONTENT_ID = "ContentID";
    public static final String COLUMN_CONTENT_TITLE = "ContentTitle";
    public static final String COLUMN_CONTENT_TEXT = "ContentText";
    public static final String COLUMN_CONTENT_IMAGE = "ContentImage";
    public static final String COLUMN_CONTENT_URL = "ContentURL";
    public static final String COLUMN_CONTENT_ORDER = "ContentOrder";
    public static final String COLUMN_CONTENT_TYPE = "ContentType";

    // QUESTION Table
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
    public static final String COLUMN_EXPLANATION = "Explanation";

    // ASSESSMENT_RESULT Table
    public static final String COLUMN_RESULT_ID = "ResultID";
    public static final String COLUMN_SCORE = "Score";
    public static final String COLUMN_TOTAL_QUESTIONS = "TotalQuestions";
    public static final String COLUMN_RETAKE_COUNT = "RetakeCount";
    public static final String COLUMN_DATE_TAKEN = "DateTaken";

    // USER_ANSWER Table
    public static final String COLUMN_ANSWER_ID = "AnswerID";
    public static final String COLUMN_USER_ANSWER = "UserAnswer";
    public static final String COLUMN_USER_SEQUENCE = "UserSequence";
    public static final String COLUMN_IS_CORRECT = "IsCorrect";
    public static final String COLUMN_DATE_ANSWERED = "DateAnswered";

    private final Context context;

    public FirstAidDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_USER + " (" +
                        COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USER_NAME + " TEXT, " +
                        COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                        COLUMN_USER_PHONE + " TEXT, " +
                        COLUMN_USER_PASSWORD + " TEXT, " +
                        COLUMN_USER_TYPE + " TEXT DEFAULT 'general', " +
                        COLUMN_USER_IMAGE + " TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_MODULE + " (" +
                        COLUMN_MODULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MODULE_NAME + " TEXT NOT NULL, " +
                        COLUMN_MODULE_DESCRIPTION + " TEXT, " +
                        COLUMN_DIFFICULTY_LEVEL + " TEXT, " +
                        COLUMN_ESTIMATED_DURATION + " INTEGER, " +
                        COLUMN_TOTAL_ASSESSMENTS + " INTEGER, " +
                        COLUMN_MODULE_COMPLETION_CRITERIA + " TEXT, " +
                        COLUMN_MODULE_ACCESSED_DATE + " TEXT DEFAULT 'Not Accessed Yet', " +
                        COLUMN_MODULE_COMPLETION_STATUS + " TEXT DEFAULT 'Not Started', " +
                        COLUMN_PROGRESS_PERCENTAGE + " INTEGER DEFAULT 0, " +
                        COLUMN_IS_LOCKED + " INTEGER DEFAULT 0)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_CONTENT + " (" +
                        COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MODULE_ID + " INTEGER, " +
                        COLUMN_CONTENT_TITLE + " TEXT, " +
                        COLUMN_CONTENT_TEXT + " TEXT, " +
                        COLUMN_CONTENT_IMAGE + " TEXT, " +
                        COLUMN_CONTENT_URL + " TEXT, " +
                        COLUMN_CONTENT_ORDER + " INTEGER, " +
                        COLUMN_CONTENT_TYPE + " TEXT, " +
                        "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + "))"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_USER_CONTENT_VIEW + " (" +
                        COLUMN_USER_ID + " INTEGER, " +
                        COLUMN_CONTENT_ID + " INTEGER, " +
                        COLUMN_MODULE_ID + " INTEGER, " +
                        "Viewed INTEGER DEFAULT 1, " +
                        "PRIMARY KEY(" + COLUMN_USER_ID + ", " + COLUMN_CONTENT_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_CONTENT_ID + ") REFERENCES " + TABLE_CONTENT + "(" + COLUMN_CONTENT_ID + "))"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_QUESTION + " (" +
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
                        COLUMN_EXPLANATION + " TEXT, " +
                        "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + "))"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_ASSESSMENT_RESULT + " (" +
                        COLUMN_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USER_ID + " INTEGER, " +
                        COLUMN_MODULE_ID + " INTEGER, " +
                        COLUMN_SCORE + " INTEGER, " +
                        COLUMN_TOTAL_QUESTIONS + " INTEGER, " +
                        COLUMN_RETAKE_COUNT + " INTEGER, " +
                        COLUMN_DATE_TAKEN + " TEXT, " +
                        "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + "))"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_USER_ANSWER + " (" +
                        COLUMN_ANSWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USER_ID + " INTEGER, " +
                        COLUMN_MODULE_ID + " INTEGER, " +
                        COLUMN_QUESTION_ID + " INTEGER, " +
                        COLUMN_USER_ANSWER + " TEXT, " +
                        COLUMN_USER_SEQUENCE + " TEXT, " +
                        COLUMN_IS_CORRECT + " INTEGER, " +
                        COLUMN_DATE_ANSWERED + " TEXT, " +
                        "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_QUESTION_ID + ") REFERENCES " + TABLE_QUESTION + "(" + COLUMN_QUESTION_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_MODULE_ID + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE " + TABLE_MODULE + " ADD COLUMN " + COLUMN_IS_LOCKED + " INTEGER DEFAULT 0");
        }

        // Future upgrades can be handled with additional conditions
        // if (oldVersion < 9) { ... }
    }
}
