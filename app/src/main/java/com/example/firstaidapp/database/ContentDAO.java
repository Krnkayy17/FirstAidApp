package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentDAO {
    private SQLiteDatabase db;
    private FirstAidDatabaseHelper dbHelper;

    public ContentDAO(FirstAidDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertContent(int moduleID, String title, String text, String image, String url, int order) {
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_ID, moduleID);
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_TITLE, title);
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_TEXT, text);
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_IMAGE, image);
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_URL, url);
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER, order);

        db.insert(FirstAidDatabaseHelper.TABLE_CONTENT, null, values);
    }

    public List<Content> getContentByModule(int moduleID) {
        List<Content> contentList = new ArrayList<>();
        open();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_CONTENT,
                null,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{String.valueOf(moduleID)},
                null, null,
                FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Content content = new Content(
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CONTENT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CONTENT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CONTENT_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CONTENT_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CONTENT_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER))
                );
                contentList.add(content);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return contentList;
    }

    public void insertInitialContent() {
        open();
        int cprModuleID = 1;
        int bleedingModuleID = 2;

        insertContent(cprModuleID, "Introduction to CPR", "CPR is a life-saving procedure...", null, null, 1);
        insertContent(cprModuleID, "When to Perform CPR", "Perform CPR when unresponsive...", null, null, 2);
        insertContent(cprModuleID, "The Chain of Survival", "1. Early response\n2. Early CPR...", null, null, 3);
        insertContent(cprModuleID, "DRSABCD Action Plan", "🛑 D – Danger\n👋 R – Response\n...", null, null, 4);
        insertContent(cprModuleID, "Steps for Adult CPR", "Push hard and fast: 100-120/min...", null, null, 5);
        insertContent(cprModuleID, "CPR for Infants & Children", "Gentler compressions, two fingers...", null, null, 6);
        insertContent(cprModuleID, "Using an AED", "Turn on, attach pads, press shock...", null, null, 7);
        insertContent(cprModuleID, "Safety Tips", "Avoid water, avoid metal...", null, null, 8);
        insertContent(cprModuleID, "Special Cases", "For drowning or pregnant victims...", null, null, 9);
        insertContent(cprModuleID, "CPR Summary", "Fast action saves lives!", null, null, 10);

        insertContent(bleedingModuleID, "Introduction to Bleeding", "Bleeding is loss of blood from vessels...", null, null, 1);
        insertContent(bleedingModuleID, "Assessing the Situation", "Wear gloves. Check the scene...", null, null, 2);
        insertContent(bleedingModuleID, "Types of Bleeding", "Arterial, Venous, Capillary", null, null, 3);
        insertContent(bleedingModuleID, "Steps for Bleeding Control", "Apply pressure, elevate, bandage...", null, null, 4);
        insertContent(bleedingModuleID, "Special Cases", "Nosebleeds, embedded objects...", null, null, 5);
        insertContent(bleedingModuleID, "Wound Care", "Rinse, antiseptic, cover properly...", null, null, 6);
        insertContent(bleedingModuleID, "Tourniquet Use", "Use only in severe bleeding cases...", null, null, 7);
        insertContent(bleedingModuleID, "Working as a Team", "One treats, one calls for help...", null, null, 8);
        insertContent(bleedingModuleID, "Common Mistakes", "Do not remove soaked dressings...", null, null, 9);
        insertContent(bleedingModuleID, "Bleeding Summary", "Apply pressure, elevate, call 999.", null, null, 10);

        close();
    }
}
