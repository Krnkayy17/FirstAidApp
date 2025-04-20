package archive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.firstaidapp.models.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FirstAidApp.db";
    private static final int DATABASE_VERSION = 4;

    // Table Name
    public static final String TABLE_CONTENT = "CONTENT";

    // Column Names
    public static final String COLUMN_CONTENT_ID = "ContentID";
    public static final String COLUMN_MODULE_ID = "ModuleID";
    public static final String COLUMN_CONTENT_TITLE = "ContentTitle";
    public static final String COLUMN_CONTENT_TEXT = "ContentText";
    public static final String COLUMN_CONTENT_IMAGE = "ContentImage";
    public static final String COLUMN_CONTENT_URL = "ContentURL";
    public static final String COLUMN_CONTENT_ORDER = "ContentOrder";

    // Create Table Query
    private static final String CREATE_TABLE_CONTENT = "CREATE TABLE " + TABLE_CONTENT + " ("
            + COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MODULE_ID + " INTEGER, "
            + COLUMN_CONTENT_TITLE + " TEXT, "
            + COLUMN_CONTENT_TEXT + " TEXT, "
            + COLUMN_CONTENT_IMAGE + " TEXT, "
            + COLUMN_CONTENT_URL + " TEXT, "
            + COLUMN_CONTENT_ORDER + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES MODULE(ModuleID))";

    public ContentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTENT);
        insertInitialContent(db); // ✅ pass db instance
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT);
        onCreate(db);
    }

    // ✅ Insert content using shared db instance
    public void insertContent(SQLiteDatabase db, int moduleID, String title, String text, String image, String url, int order) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MODULE_ID, moduleID);
        values.put(COLUMN_CONTENT_TITLE, title);
        values.put(COLUMN_CONTENT_TEXT, text);
        values.put(COLUMN_CONTENT_IMAGE, image);
        values.put(COLUMN_CONTENT_URL, url);
        values.put(COLUMN_CONTENT_ORDER, order);
        db.insert(TABLE_CONTENT, null, values);
    }

    public List<Content> getContentByModule(int moduleID) {
        List<Content> contentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTENT + " WHERE " + COLUMN_MODULE_ID + " = ? ORDER BY " + COLUMN_CONTENT_ORDER + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moduleID)});

        if (cursor.moveToFirst()) {
            do {
                Content content = new Content(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MODULE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ORDER))
                );
                contentList.add(content);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return contentList;
    }

   /* public Content getNextSubtopic(int moduleID, int currentOrder) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTENT + " WHERE " + COLUMN_MODULE_ID + " = ? AND " + COLUMN_CONTENT_ORDER + " > ? ORDER BY " + COLUMN_CONTENT_ORDER + " ASC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moduleID), String.valueOf(currentOrder)});

        if (cursor.moveToFirst()) {
            Content content = new Content(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MODULE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_TEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ORDER))
            );
            cursor.close();
            db.close();
            return content;
        }

        cursor.close();
        db.close();
        return null;
    }*/

    // ✅ Inserts all CPR and Bleeding content during onCreate
    public void insertInitialContent(SQLiteDatabase db) {
        int cprModuleID = 1;
        int bleedingModuleID = 2;

        insertContent(db, cprModuleID, "Introduction to CPR", "CPR is a life-saving procedure...", null, null, 1);
        insertContent(db, cprModuleID, "When to Perform CPR", "Perform CPR when unresponsive...", null, null, 2);
        insertContent(db, cprModuleID, "The Chain of Survival", "1. Early response\n2. Early CPR...", null, null, 3);
        insertContent(db, cprModuleID, "DRSABCD Action Plan", "🛑 D – Danger\n👋 R – Response\n...", null, null, 4);
        insertContent(db, cprModuleID, "Steps for Adult CPR", "Push hard and fast: 100-120/min...", null, null, 5);
        insertContent(db, cprModuleID, "CPR for Infants & Children", "Gentler compressions, two fingers...", null, null, 6);
        insertContent(db, cprModuleID, "Using an AED", "Turn on, attach pads, press shock...", null, null, 7);
        insertContent(db, cprModuleID, "Safety Tips", "Avoid water, avoid metal...", null, null, 8);
        insertContent(db, cprModuleID, "Special Cases", "For drowning or pregnant victims...", null, null, 9);
        insertContent(db, cprModuleID, "CPR Summary", "Fast action saves lives!", null, null, 10);

        insertContent(db, bleedingModuleID, "Introduction to Bleeding", "Bleeding is loss of blood from vessels...", null, null, 1);
        insertContent(db, bleedingModuleID, "Assessing the Situation", "Wear gloves. Check the scene...", null, null, 2);
        insertContent(db, bleedingModuleID, "Types of Bleeding", "Arterial, Venous, Capillary", null, null, 3);
        insertContent(db, bleedingModuleID, "Steps for Bleeding Control", "Apply pressure, elevate, bandage...", null, null, 4);
        insertContent(db, bleedingModuleID, "Special Cases", "Nosebleeds, embedded objects...", null, null, 5);
        insertContent(db, bleedingModuleID, "Wound Care", "Rinse, antiseptic, cover properly...", null, null, 6);
        insertContent(db, bleedingModuleID, "Tourniquet Use", "Use only in severe bleeding cases...", null, null, 7);
        insertContent(db, bleedingModuleID, "Working as a Team", "One treats, one calls for help...", null, null, 8);
        insertContent(db, bleedingModuleID, "Common Mistakes", "Do not remove soaked dressings...", null, null, 9);
        insertContent(db, bleedingModuleID, "Bleeding Summary", "Apply pressure, elevate, call 999.", null, null, 10);
    }
}
