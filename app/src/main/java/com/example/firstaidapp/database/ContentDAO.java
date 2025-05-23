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

        insertContent(cprModuleID, "Introduction to CPR", "Cardiopulmonary Resuscitation (CPR) is a life-saving procedure used in emergencies when a person has stopped breathing, or their heart has stopped beating. " +
                "The main goal of CPR is to maintain circulation and oxygen supply to vital organs until medical help arrives.","@drawable/cpr", null, 1);

        insertContent(cprModuleID, "When to Perform CPR", "CPR should be performed when a person:\n" +
                "•\tIs unresponsive and not breathing or only gasping.\n" +
                "•\tHas no detectable pulse.\n" +
                "•\tHas suffered from drowning, cardiac arrest, suffocation, or drug overdose.\n", null, null, 2);

        insertContent(cprModuleID, "The Chain of Survival", "1.\tEarly recognition and emergency response – Call emergency services immediately.\n" +
                "2.\tEarly CPR – Start chest compressions to maintain circulation.\n" +
                "3.\tRapid defibrillation – Use an Automated External Defibrillator (AED) if available.\n" +
                "4.\tAdvanced medical care – Paramedics provide further resuscitation efforts.\n" +
                "5.\tPost-cardiac arrest care – Ensures long-term recovery.\n", null, null, 3);

        insertContent(cprModuleID, "DRSABCD Action Plan", "Follow these seven critical steps to assess and assist a collapsed person:\n" +
                "\uD83D\uDED1 D – Danger\n" +
                "•\tEnsure the area is safe for you, the victim, and bystanders.\n" +
                "•\tDo not approach if there is any ongoing danger.\n" +
                "\uD83D\uDC4B R – Response\n" +
                "•\tGently shake the person and ask loudly, “Are you okay?”\n" +
                "•\tIf no response, proceed to the next step.\n" +
                "\uD83D\uDCDE S – Send for Help\n" +
                "•\tCall emergency services (e.g., 999/112) or ask someone else to call.\n" +
                "•\tRequest an Automated External Defibrillator (AED), if available.\n" +
                "\uD83E\uDEC1 A – Airway\n" +
                "•\tTilt the person’s head back and lift the chin to open the airway.\n" +
                "•\tCheck for any visible blockage and remove it only if safe to do so.\n" +
                "\uD83C\uDF2C\uFE0F B – Breathing\n" +
                "•\tLook for chest movement, listen for breath sounds, and feel for air from the nose or mouth.\n" +
                "•\tCheck for no more than 10 seconds.\n" +
                "•\tIf not breathing or only gasping, begin CPR.\n" +
                "❤\uFE0F C – CPR\n" +
                "•\tBegin 30 chest compressions followed by 2 rescue breaths.\n" +
                "•\tContinue cycles of 30:2.\n" +
                "•\tPush hard and fast (at least 5 cm deep, 100–120 compressions per minute).\n" +
                "⚡ D – Defibrillation\n" +
                "•\tTurn on the AED and follow the prompts.\n" +
                "•\tApply the pads to the bare chest.\n" +
                "•\tDo not touch the person during analysis or shock delivery.\n" +
                "•\tResume CPR immediately after any shock.\n", null, null, 4);

        insertContent(cprModuleID, "Steps for Adult CPR", "1.\tPosition your hands at the center of the chest and interlock fingers.\n" +
                "2.\tDeliver compressions: at least 2 inches deep, 100–120 per minute.\n" +
                "3.\tAllow full chest recoil after each compression.\n" +
                "4.\tGive 2 rescue breaths if trained:\n" +
                "o\tPinch the nose, seal the mouth, and blow until the chest rises.\n" +
                "5.\tContinue 30:2 cycles until help arrives or signs of life return.\n", null, null, 5);

        insertContent(cprModuleID, "CPR for Infants & Children", "•\tInfants (<1 year): Use two fingers for compressions (1.5 inches deep).\n" +
                "•\tChildren (1–8 years): Use one or two hands based on size (about 2 inches deep).\n" +
                "•\tUse gentle rescue breaths—just enough to make the chest rise.\n", null, null, 6);

        insertContent(cprModuleID, "Using an AED", "An Automated External Defibrillator (AED) is a portable device that analyzes the heart’s rhythm and delivers an electric shock if necessary to restore a normal heartbeat. Follow these steps to use an AED safely and effectively:\n" +
                "1.\tTurn on the AED by pressing the power button or opening the lid. The device will begin giving clear, step-by-step voice instructions.\n" +
                "2.\tExpose the person’s chest by removing any clothing. If the chest is wet, dry it thoroughly with a towel or cloth.\n" +
                "3.\tAttach the electrode pads to the person’s bare chest. One pad should be placed on the upper right side of the chest, just below the collarbone. The second pad should be placed on the lower left side, below the armpit. Follow the diagram on the pads for correct placement.\n" +
                "4.\tPlug in the pad connector if the AED requires it. Some models automatically detect the pads once they are attached.\n" +
                "5.\tEnsure no one is touching the person. Clearly announce, “Stand clear!” to everyone nearby.\n" +
                "6.\tThe AED will analyze the person’s heart rhythm. Do not touch the person during this process.\n" +
                "7.\tIf the AED determines that a shock is needed, it will instruct you to press the shock button. Press it only when prompted.\n" +
                "8.\tImmediately resume CPR with 30 chest compressions and 2 rescue breaths after the shock is delivered, or if no shock is advised.\n" +
                "9.\tContinue to follow the AED’s voice prompts. It may re-analyze the heart rhythm every two minutes and advise additional shocks or continued CPR.\n", null, null, 7);

        insertContent(cprModuleID, "Safety Tips", "•\tNever use the AED in a wet environment—move the person to a dry area if possible.\n" +
                "•\tDo not place pads over a pacemaker or medication patches—move the pad placement slightly to avoid these areas.\n" +
                "•\tFor infants and young children, use pediatric pads or activate child mode if the AED has it. If only adult pads are available, you can use them but place one pad in the center of the chest and the other on the back.\n", null, null, 8);

        insertContent(cprModuleID, "Special Cases", "•\tDrowning: Give 5 initial rescue breaths before compressions.\n" +
                "•\tTrauma: Handle with care to prevent further injuries.\n" +
                "•\tPregnancy: Perform compressions slightly higher and tilt the person slightly to the left.\n", null, null, 9);

        insertContent(cprModuleID, "CPR Summary", "Quick action using the DRSABCD plan, effective chest compressions, and defibrillation can significantly improve survival chances. " +
                "Consistent training and practice empower you to act confidently in an emergency.", null, null, 10);

        insertContent(bleedingModuleID, "Introduction to Bleeding", "Definition and Importance\n" +
                "Bleeding, also known as hemorrhage, is the escape of blood from the circulatory system. It can result from trauma, injury, or medical conditions. Managing bleeding quickly and effectively is vital to prevent hypovolemic shock, unconsciousness, or death.\n" +
                "When to Manage Bleeding\n" +
                "•\tIf there is visible bleeding from a wound.\n" +
                "•\tWhen blood is seen pooling, soaking through clothing or bandages.\n" +
                "•\tIf internal bleeding is suspected (bruising, vomiting blood, etc.).\n", null, null, 1);

        insertContent(bleedingModuleID, "Assessing the Situation", "Ensuring Safety\n" +
                "•\tWear disposable gloves or use a barrier (plastic bag, cloth) if gloves are unavailable.\n" +
                "•\tCheck the scene for dangers before approaching (glass, weapons, etc.).\n" +
                "Checking the Victim\n" +
                "•\tSpeak to them calmly and check for responsiveness.\n" +
                "•\tIdentify the source and severity of bleeding.\n" +
                "Calling for Help (Emergency Numbers)\n" +
                "•\tMalaysia: Dial 999 or 112 from a mobile phone.\n" +
                "•\tIf alone with a severely bleeding victim, call first before beginning treatment.\n", null, null, 2);

        insertContent(bleedingModuleID, "Types of Bleeding", "1.\tArterial Bleeding – Bright red, spurting with each heartbeat. Life-threatening.\n" +
                "2.\tVenous Bleeding – Dark red, steady flow. Can be serious but easier to control.\n" +
                "3.\tCapillary Bleeding – Oozes from surface wounds. Often minor but still requires care.\n", null, null, 3);

        insertContent(bleedingModuleID, "Steps for Bleeding Control", "1. Apply Direct Pressure\n" +
                "•\tUse a clean dressing or cloth. Apply firm, steady pressure.\n" +
                "•\tDo not remove soaked dressings—place another on top if needed.\n" +
                "2. Elevate the Wound\n" +
                "•\tIf on a limb, raise it above heart level to reduce blood flow.\n" +
                "3. Apply a Pressure Bandage\n" +
                "•\tWrap a bandage tightly over the dressing to maintain pressure.\n" +
                "4. Apply Pressure to Pressure Points (if bleeding continues)\n" +
                "•\tFor arms: Brachial artery (inner arm).\n" +
                "•\tFor legs: Femoral artery (groin area).\n" +
                "5. Monitor for Shock\n" +
                "•\tSymptoms: Pale skin, cold sweat, rapid breathing, weak pulse.\n" +
                "•\tKeep the person lying flat. Elevate legs unless there is an injury preventing it.\n" +
                "6. Seek Medical Assistance\n" +
                "•\tSevere bleeding: call for emergency help immediately.\n" +
                "•\tContinue care until professional help arrives.\n", null, null, 4);

        insertContent(bleedingModuleID, "Special Cases", "a) Nosebleeds (Epistaxis)\n" +
                "•\tHave the person sit upright, tilt head forward.\n" +
                "•\tPinch the soft part of the nose for 10–15 minutes.\n" +
                "•\tAvoid leaning backward or blowing the nose.\n" +
                "b) Internal Bleeding\n" +
                "•\tSigns: Bruising, swelling, pain, vomiting/coughing blood, confusion.\n" +
                "•\tLay the person down. Do not give food or drink.\n" +
                "•\tCall emergency services immediately.\n" +
                "c) Embedded Objects\n" +
                "•\tDo not remove the object.\n" +
                "•\tApply pressure around the object.\n" +
                "•\tSecure object in place with bandages.\n" +
                "•\tSeek emergency medical help.\n", null, null, 5);

        insertContent(bleedingModuleID, "Wound Care", "•\tApply antiseptic solution if available.\n" +
                "•\tCover with sterile dressing.\n" +
                "•\tChange dressings daily or when soaked.\n" +
                "•\tDo not use cotton wool or tissue—fibers can stick in the wound.\n", null, null, 6);

        insertContent(bleedingModuleID, "Tourniquet Use", "Tourniquet Use (Last Resort)\n" +
                "•\tUse only when direct pressure fails for life-threatening limb bleeding.\n" +
                "•\tApply 5 cm above the wound.\n" +
                "•\tTighten until bleeding stops.\n" +
                "•\tRecord the time of application visibly on the person.\n" +
                "•\tNever loosen or remove; let trained responders handle it.\n", null, null, 7);

        insertContent(bleedingModuleID, "Working as a Team", "Single Rescuer\n" +
                "•\tPerform direct pressure and call for help.\n" +
                "•\tStay with the victim until professional care arrives.\n" +
                "Two Rescuers\n" +
                "•\tOne maintains pressure/control of bleeding.\n" +
                "•\tThe other calls emergency services and assists with dressings/bandages.\n" +
                "Switching Roles\n" +
                "•\tCommunicate clearly and switch tasks if one person becomes fatigued.\n", null, null, 8);

        insertContent(bleedingModuleID, "Common Mistakes", "❌ Removing soaked dressings → Add more layers instead.\n" +
                "❌ Not applying enough pressure → Apply firm, steady pressure directly on the wound.\n" +
                "❌ Delaying medical help → Always seek professional aid for serious bleeding.\n" +
                "❌ Ignoring shock symptoms → Always monitor and respond to signs of shock.\n" +
                "❌ Tilting the head back for nosebleeds → Always lean forward to avoid swallowing blood.\n", null, null, 9);

        insertContent(bleedingModuleID, "Bleeding Summary", "✅ Control bleeding using pressure, elevation, and dressing.\n" +
                "✅ Recognize and respond to arterial, venous, and capillary bleeding.\n" +
                "✅ Use tourniquet only for life-threatening situations.\n" +
                "✅ Monitor for and manage signs of shock.\n" +
                "✅ Always call emergency services for severe or persistent bleeding.\n", null, null, 10);

        close();
    }

}
