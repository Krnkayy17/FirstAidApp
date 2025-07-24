package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentDAO {

    private final FirstAidDatabaseHelper dbHelper;

    public ContentDAO(Context context) {
        this.dbHelper = new FirstAidDatabaseHelper(context);
    }

    // Insert individual content row
    public long insertContent(Content content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FirstAidDatabaseHelper.COLUMN_MODULE_ID, content.getModuleId());
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_TITLE, content.getContentTitle());
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_TEXT, content.getContentText());
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_IMAGE, content.getContentImage());
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_URL, content.getContentURL());
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER, content.getContentOrder());
        values.put(FirstAidDatabaseHelper.COLUMN_CONTENT_TYPE, content.getContentType());
        return db.insert(FirstAidDatabaseHelper.TABLE_CONTENT, null, values);
    }

    // Get all content for a module
    public List<Content> getContentByModule(int moduleId) {
        List<Content> contentList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_CONTENT,
                null,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{String.valueOf(moduleId)},
                null, null,
                FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER + " ASC, " +
                        FirstAidDatabaseHelper.COLUMN_CONTENT_ID + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                contentList.add(Content.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contentList;
    }

    // Get content by module + order
    public List<Content> getContentsByModuleAndOrder(int moduleId, int order) {
        List<Content> contentList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_CONTENT,
                null,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + " = ? AND " +
                        FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER + " = ?",
                new String[]{String.valueOf(moduleId), String.valueOf(order)},
                null, null,
                FirstAidDatabaseHelper.COLUMN_CONTENT_ID + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                contentList.add(Content.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contentList;
    }

    // Get all distinct content orders for a module
    public List<Integer> getAllOrdersForModule(int moduleId) {
        List<Integer> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT " + FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER +
                        " FROM " + FirstAidDatabaseHelper.TABLE_CONTENT +
                        " WHERE " + FirstAidDatabaseHelper.COLUMN_MODULE_ID + " = ?" +
                        " ORDER BY " + FirstAidDatabaseHelper.COLUMN_CONTENT_ORDER + " ASC",
                new String[]{String.valueOf(moduleId)}
        );

        if (cursor.moveToFirst()) {
            do {
                orders.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    // ✅ Used in SubTopicDetailActivity to count all contents in a module
    public List<Content> getAllContentsByModule(int moduleId) {
        return getContentByModule(moduleId); // Simply calls your existing method
    }


    // ✅ Insert initial content based on module title
    public void insertInitialContent(int moduleId, String moduleTitle) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + FirstAidDatabaseHelper.TABLE_CONTENT +
                        " WHERE " + FirstAidDatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{String.valueOf(moduleId)}
        );
        if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
            cursor.close();
            return; // content already inserted
        }
        if (cursor != null) cursor.close();

        int order = 1;

        if (moduleTitle.equalsIgnoreCase("CPR")) {
            // CPR module based on CPR.docx (only summary shown here for brevity)
            insertContent(new Content(moduleId, "Introduction to CPR", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "cpr_intro", null, order, "image"));
            insertContent(new Content(moduleId, null, "Cardiopulmonary Resuscitation (CPR) is a life-saving procedure used in emergencies when a person has stopped breathing, or their heart has stopped beating. The main goal of CPR is to maintain circulation and oxygen supply to vital organs until medical help arrives.", null, null, order, "text"));
            order++;

            insertContent(new Content(moduleId, "When to Perform CPR", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "when_to_perform_cpr", null, order, "image"));
            insertContent(new Content(moduleId, null, "CPR should be performed when a person:\n" +
                    "•\tIs unresponsive and not breathing or only gasping.\n" +
                    "•\tHas no detectable pulse.\n" +
                    "•\tHas suffered from drowning, cardiac arrest, suffocation, or drug overdose.\n", null, null, order, "text"));
            order++;

            insertContent(new Content(moduleId, "The Chain of Survival", null, null, null, order, "text"));
            insertContent(new Content (moduleId, null, null, "chain_of_survival", null, order, "image"));
            insertContent(new Content(moduleId, null, "1.\tEarly recognition and emergency response – Call emergency services immediately.\n" +
                    "2.\tEarly CPR – Start chest compressions to maintain circulation.\n" +
                    "3.\tRapid defibrillation – Use an Automated External Defibrillator (AED) if available.\n" +
                    "4.\tAdvanced medical care – Paramedics provide further resuscitation efforts.\n" +
                    "5.\tPost-cardiac arrest care – Ensures long-term recovery.\n", null, null, order, "text"));
            order++;

            insertContent(new Content(moduleId, "DRSABCD Action Plan", "Follow these seven critical steps to assess and assist a collapsed person:", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "\uD83D\uDED1 D – Danger\n" +
                    "•\tEnsure the area is safe for you, the victim, and bystanders.\n" +
                    "•\tDo not approach if there is any ongoing danger.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "check_danger", null, order, "image"));
            insertContent(new Content(moduleId, null, "\uD83D\uDC4B R – Response\n" +
                    "•\tGently shake the person and ask loudly, “Are you okay?”\n" +
                    "•\tIf no response, proceed to the next step.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "check_response", null, order, "image"));
            insertContent(new Content(moduleId, null, "\uD83D\uDCDE S – Send for Help\n" +
                    "•\tCall emergency services (e.g., 999/112) or ask someone else to call.\n" +
                    "•\tRequest an Automated External Defibrillator (AED), if available.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "call_emergency", null, order, "image"));
            insertContent(new Content(moduleId, null, "\uD83E\uDEC1 A – Airway\n" +
                    "•\tTilt the person’s head back and lift the chin to open the airway.\n" +
                    "•\tCheck for any visible blockage and remove it only if safe to do so.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "check_airway", null, order, "image"));
            insertContent(new Content(moduleId, null, "\uD83C\uDF2C\uFE0F B – Breathing\n" +
                    "•\tLook for chest movement, listen for breath sounds, and feel for air from the nose or mouth.\n" +
                    "•\tCheck for no more than 10 seconds.\n" +
                    "•\tIf not breathing or only gasping, begin CPR.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "check_for_breathing", null, order, "image"));
            insertContent(new Content(moduleId, null, "❤\uFE0F C – CPR\n" +
                    "•\tBegin 30 chest compressions followed by 2 rescue breaths.\n" +
                    "•\tContinue cycles of 30:2.\n" +
                    "•\tPush hard and fast (at least 5 cm deep, 100–120 compressions per minute)\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "cpr_compress_rescue_breath", null, order, "image"));
            insertContent(new Content(moduleId, null, null, "chest_compression", null, order, "image"));
            insertContent(new Content(moduleId, null, null, "rescue_breathing", null, order, "image"));
            insertContent(new Content(moduleId, null, "⚡ D – Defibrillation\n" +
                    "•\tTurn on the AED and follow the prompts.\n" +
                    "•\tApply the pads to the bare chest.\n" +
                    "•\tDo not touch the person during analysis or shock delivery.\n" +
                    "•\tResume CPR immediately after any shock.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "using_aed", null, order, "image"));
            insertContent(new Content(moduleId, null, "Watch Video: DRSABCD First Aid", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/FsRura3FIo0?si=B554sTXf7gdn4djK", order, "video"));
            order++;

            insertContent(new Content(moduleId, "Steps for Adult CPR", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "1.\tPosition your hands at the center of the chest and interlock fingers.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "adult_compression", null, order, "image"));
            insertContent(new Content(moduleId, null, "2.\tDeliver compressions: at least 2 inches deep, 100–120 per minute.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "cpr_rescue_breath", null, order, "image"));
            insertContent(new Content(moduleId, null, "3.\tAllow full chest recoil after each compression.\n" +
                    "4.\tGive 2 rescue breaths if trained:\n" +
                    "o\tPinch the nose, seal the mouth, and blow until the chest rises.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "deliver_compress", null, order, "image"));
            insertContent(new Content(moduleId, null, "5.\tContinue 30:2 cycles until help arrives or signs of life return.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "help_arrived", null, order, "image"));
            insertContent(new Content(moduleId, null, "Watch Video: How to do CPR on Adult", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/BQNNOh8c8ks?si=Df8t2_nE2VMQNhTC", order, "video"));
            order++;

            insertContent(new Content(moduleId, "CPR for Infants & Children", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tInfants (<1 year): Use two fingers for compressions (1.5 inches deep).", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "baby_chest_compression", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tChildren (1–8 years): Use one or two hands based on size (about 2 inches deep).", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "child_chest_compression", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tUse gentle rescue breaths—just enough to make the chest rise.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "baby_rescue_breath", null, order, "image"));
            insertContent(new Content(moduleId, null, "Watch Video: How to do CPR on a Child", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/c7Q1s7ppSwc?si=clz1NM9lDiXlChPW", order, "video"));
            insertContent(new Content(moduleId, null, "Watch Video: How to do CPR on an Infant", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/XJQF-qUWOLA?si=9PYQR9Up6pg-OYti", order, "video"));
            order++;

            insertContent(new Content(moduleId, "Using an AED", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "aed_logo", null, order, "image"));
            insertContent(new Content(moduleId, null, "An Automated External Defibrillator (AED) is a portable device that analyzes the heart’s rhythm and delivers an electric shock if necessary to restore a normal heartbeat. Follow these steps to use an AED safely and effectively:", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "1.\tTurn on the AED by pressing the power button or opening the lid. The device will begin giving clear, step-by-step voice instructions.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "open_aed", null, order, "image"));
            insertContent(new Content(moduleId, null, "2.\tExpose the person’s chest by removing any clothing. If the chest is wet, dry it thoroughly with a towel or cloth.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "3.\tAttach the electrode pads to the person’s bare chest. One pad should be placed on the upper right side of the chest, just below the collarbone. The second pad should be placed on the lower left side, below the armpit. Follow the diagram on the pads for correct placement", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "apply_aed_pads", null, order, "image"));
            insertContent(new Content(moduleId, null, null, "aed_placement", null, order, "image"));
            insertContent(new Content(moduleId, null, "4.\tPlug in the pad connector if the AED requires it. Some models automatically detect the pads once they are attached.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "5.	Ensure no one is touching the person. Clearly announce, “Stand clear!” to everyone nearby.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "follow_aed_instruction", null, order, "image"));
            insertContent(new Content(moduleId, null, "6.\tThe AED will analyze the person’s heart rhythm. Do not touch the person during this process.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "7.\tIf the AED determines that a shock is needed, it will instruct you to press the shock button. Press it only when prompted.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "turn_on_aed", null, order, "image"));
            insertContent(new Content(moduleId, null, "8.\tImmediately resume CPR with 30 chest compressions and 2 rescue breaths after the shock is delivered, or if no shock is advised.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "continue_compression", null, order, "image"));
            insertContent(new Content(moduleId, null, "9.\tContinue to follow the AED’s voice prompts. It may re-analyze the heart rhythm every two minutes and advise additional shocks or continued CPR.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Watch Video: How to use an AED", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/2PJR0JyLPZY?si=MXVC9ab3WI7uXoNf  ", order, "video"));
            order++;

            insertContent(new Content(moduleId, "Safety Tips", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "safety_tips", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tNever use the AED in a wet environment—move the person to a dry area if possible.\n" +
                    "•\tDo not place pads over a pacemaker or medication patches—move the pad placement slightly to avoid these areas.\n" +
                    "•\tFor infants and young children, use pediatric pads or activate child mode if the AED has it. If only adult pads are available, you can use them but place one pad in the center of the chest and the other on the back.\n", null, null, order, "text"));
            order++;

            insertContent(new Content(moduleId, "Special Cases", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tDrowning: Give 5 initial rescue breaths before compressions.\n" +
                    "•\tTrauma: Handle with care to prevent further injuries.\n" +
                    "•\tPregnancy: Perform compressions slightly higher and tilt the person slightly to the left.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Watch Video: How to Perform CPR on a Drowning Casualty", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/74QiUtXcV5o?si=Hn9s4UhLh6RrIOcQ", order, "video"));
            insertContent(new Content(moduleId, null, "Watch Vidoe: Do you do CPR on a Trauma Patient", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/JYyuRYFvR34?si=GhUyrBKBnri7ufwN", order, "video"));
            insertContent(new Content(moduleId, null, "Watch Video: How to do CPR on a Pregnant Woman", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/doVSpYgjkvk?si=2a6ywCf1mhVqk5JP", order, "video"));
            order++;

            insertContent(new Content(moduleId, "CPR Summary", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "drsabcd", null, order, "image"));
            insertContent(new Content(moduleId, null, "Quick action using the DRSABCD plan, effective chest compressions, and defibrillation can significantly improve survival chances. Consistent training and practice empower you to act confidently in an emergency.", null, null, order, "text"));

        } else if (moduleTitle.equalsIgnoreCase("Bleeding Management")) {
            // Bleeding Management Module Content
            insertContent(new Content(moduleId, "Introduction to Bleeding Management", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "stop_bleed", null, order, "image"));
            insertContent(new Content(moduleId, null, "Bleeding, also known as hemorrhage, is the escape of blood from the circulatory system. It can result from trauma, injury, or medical conditions. Managing bleeding quickly and effectively is vital to prevent hypovolemic shock, unconsciousness, or death.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "When to Manage Bleeding", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tIf there is visible bleeding from a wound.\n" +
                    "•\tWhen blood is seen pooling, soaking through clothing or bandages.\n" +
                    "•\tIf internal bleeding is suspected (bruising, vomiting blood, etc.).\n", null, null, order, "text"));
            order++;

            insertContent(new Content(moduleId, "Assessing the Situation", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Ensuring Safety", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "ensure_safety", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tWear disposable gloves or use a barrier (plastic bag, cloth) if gloves are unavailable.\n" +
                    "•\tCheck the scene for dangers before approaching (glass, weapons, etc.).\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Checking the Victim", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "check_victim", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tSpeak to them calmly and check for responsiveness.\n" +
                    "•\tIdentify the source and severity of bleeding.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Calling for Help (Emergency Numbers)", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "call_help", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tMalaysia: Dial 999 or 112 from a mobile phone.\n" +
                    "•\tIf alone with a severely bleeding victim, call first before beginning treatment.\n", null, null, order, "text"));
            order++;

            insertContent(new Content(moduleId, "Types of Bleeding", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "1.\tArterial Bleeding – Bright red, spurting with each heartbeat. Life-threatening.\n" +
                    "2.\tVenous Bleeding – Dark red, steady flow. Can be serious but easier to control.\n" +
                    "3.\tCapillary Bleeding – Oozes from surface wounds. Often minor but still requires care.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "type_of_bleeding", null, order, "image"));
            insertContent(new Content(moduleId, null, "Watch Video: Types of bleed on The First Aid", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/dbKuHjKq9Ys?si=FvmGl2FWZ3_S9CZ2", order, "video"));
            order++;

            insertContent(new Content(moduleId, "Basic Steps for Bleeding Control", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "external_bleeding", null, order, "image"));
            insertContent(new Content(moduleId, null, "1. Apply Direct Pressure", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "apply_direct_pressure", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tUse a clean dressing or cloth. Apply firm, steady pressure.\n" +
                    "•\tDo not remove soaked dressings—place another on top if needed.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "2. Elevate the Wound", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "elevate_wound", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tIf on a limb, raise it above heart level to reduce blood flow.e", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "3. Apply a Pressure Bandage", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "apply_pressure_bandage", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tWrap a bandage tightly over the dressing to maintain pressure.", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "4. Apply Pressure to Pressure Points (if bleeding continues)", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "brachial", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tFor arms: Brachial artery (inner arm).", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "femoral", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tFor legs: Femoral artery (groin area).e", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "pressure_point", null, order, "text"));
            insertContent(new Content(moduleId, null, "5. Monitor for Shock", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "monitor_for_shock", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tSymptoms: Pale skin, cold sweat, rapid breathing, weak pulse.\n" +
                    "•\tKeep the person lying flat. Elevate legs unless there is an injury preventing it.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "monitor_shock", null, order, "image"));
            insertContent(new Content(moduleId, null, "6. Seek Medical Help", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "seek_medical_assistant", null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tSevere bleeding: call for emergency help immediately.\n" +
                    "•\tContinue care until professional help arrives.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Watch Video: How to Treat Severe Bleeding", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/NxO5LvgqZe0?si=_R11fYDFMObJtArR", order, "video"));
            order++;

            insertContent(new Content(moduleId, "Special Cases of Bleeding", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "a) Nosebleeds (Epistaxis)", null, null, order, "image"));
            insertContent(new Content(moduleId, null, "•\tHave the person sit upright, tilt head forward.\n" +
                    "•\tPinch the soft part of the nose for 10–15 minutes.\n" +
                    "•\tAvoid leaning backward or blowing the nose.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Watch Video: How to Treat Nose Bleeds", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/PmmhxW0vVXA?si=kdcKZJIFjcA7LwmJ", order, "video"));
            insertContent(new Content(moduleId, null, null, "nose_bleeding", null, order, "image"));
            insertContent(new Content(moduleId, null, "b) Internal Bleeding", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tSigns: Bruising, swelling, pain, vomiting/coughing blood, confusion.\n" +
                    "•\tLay the person down. Do not give food or drink.\n" +
                    "•\tCall emergency services immediately.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "internal_bleeding", null, order, "image"));
            insertContent(new Content(moduleId, null, "c) Embedded Objects", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tDo not remove the object.\n" +
                    "•\tApply pressure around the object.\n" +
                    "•\tSecure object in place with bandages.\n" +
                    "•\tSeek emergency medical help.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "embedded_obj", null, order, "image"));
            insertContent(new Content(moduleId, null, "Watch Video: Treating an Embedded object on the First Aid", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/Q6nSg9Iqc0s?si=mn6uDqwR5krcQCVs", order, "video"));
            order++;

            insertContent(new Content(moduleId, "Wound Care and Infection Prevention", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tClean minor wounds with running water or saline.\n" +
                    "•\tApply antiseptic solution if available.\n" +
                    "•\tCover with sterile dressing.\n" +
                    "•\tChange dressings daily or when soaked.\n" +
                    "•\tDo not use cotton wool or tissue—fibers can stick in the wound.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "prevent_wound_infection", null, order, "image"));
            order++;

            insertContent(new Content(moduleId, "When to Use a Tourniquet", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Tourniquet Use (Last Resort)", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tUse only when direct pressure fails for life-threatening limb bleeding.\n" +
                    "•\tApply 5 cm above the wound.\n" +
                    "•\tTighten until bleeding stops.\n" +
                    "•\tRecord the time of application visibly on the person.\n" +
                    "•\tNever loosen or remove; let trained responders handle it.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, "tourniqet", null, order, "image"));
            insertContent(new Content(moduleId, null, null, "tourniqet_placement", null, order, "image"));
            insertContent(new Content(moduleId, null, "Watch Video: THow to use a tourniquet", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/jCKCjH_Ugs0?si=7DBsUVPRDV69JWe5", order, "video"));
            insertContent(new Content(moduleId, null, "Watch Video: Stop the Bleed - Tourniquet", null, null, order, "text"));
            insertContent(new Content(moduleId, null, null, null, "https://youtu.be/u3nrDb5XjSI?si=MrYla98ihaxk4iMt", order, "video"));
            order++;

            insertContent(new Content(moduleId, "Bleeding Management with a Team", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Single Rescuer", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tPerform direct pressure and call for help.\n" +
                    "•\tStay with the victim until professional care arrives.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Two Rescuers", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tOne maintains pressure/control of bleeding.\n" +
                    "•\tThe other calls emergency services and assists with dressings/bandages.\n", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "Switching Roles", null, null, order, "text"));
            insertContent(new Content(moduleId, null, "•\tCommunicate clearly and switch tasks if one person becomes fatigued.", null, null, order, "text"));

            insertContent(new Content(moduleId, "Common Mistakes and How to Avoid Them", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "❌ Removing soaked dressings → Add more layers instead.\n" +
                    "❌ Not applying enough pressure → Apply firm, steady pressure directly on the wound.\n" +
                    "❌ Delaying medical help → Always seek professional aid for serious bleeding.\n" +
                    "❌ Ignoring shock symptoms → Always monitor and respond to signs of shock.\n" +
                    "❌ Tilting the head back for nosebleeds → Always lean forward to avoid swallowing blood.\n", null, null, order, "text"));
            order++;

            insertContent(new Content(moduleId, "Bleeding Management Summary", null, null, null, order, "text"));
            insertContent(new Content(moduleId, null, "✅ Control bleeding using pressure, elevation, and dressing.\n" +
                    "✅ Recognize and respond to arterial, venous, and capillary bleeding.\n" +
                    "✅ Use tourniquet only for life-threatening situations.\n" +
                    "✅ Monitor for and manage signs of shock.\n" +
                    "✅ Always call emergency services for severe or persistent bleeding.\n" +
                    "Learn and practice bleeding control regularly to build confidence in responding to emergencies.\n", null, null, order, "text"));
        }
    }
}
