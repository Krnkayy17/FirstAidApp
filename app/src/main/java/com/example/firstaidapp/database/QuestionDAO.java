package com.example.firstaidapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.firstaidapp.models.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private SQLiteDatabase db;

    public QuestionDAO(Context context) {
        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert new question
    public long insertQuestion(Question question) {
        ContentValues values = new ContentValues();
        values.put("ModuleID", question.getModuleId());
        values.put("QuestionText", question.getQuestionText());
        values.put("QuestionType", question.getQuestionType());
        values.put("OptionA", question.getOptionA());
        values.put("OptionB", question.getOptionB());
        values.put("OptionC", question.getOptionC());
        values.put("OptionD", question.getOptionD());
        values.put("CorrectAnswer", question.getCorrectAnswer());
        values.put("CorrectSequence", question.getCorrectSequence());
        values.put("QuestionImage", question.getQuestionImage());
        values.put("Explanation", question.getExplanation());


        return db.insert("QUESTION", null, values);
    }

    // Get all questions by module ID
    public List<Question> getQuestionsByModuleId(int moduleId) {
        List<Question> questions = new ArrayList<>();
        Cursor cursor = db.query(
                FirstAidDatabaseHelper.TABLE_QUESTION,
                null,
                FirstAidDatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{String.valueOf(moduleId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Question q = new Question();
                q.setQuestionId(cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_QUESTION_ID)));
                q.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_MODULE_ID)));
                q.setQuestionText(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_QUESTION_TEXT)));
                q.setQuestionType(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_QUESTION_TYPE)));
                q.setOptionA(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_OPTION_A)));
                q.setOptionB(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_OPTION_B)));
                q.setOptionC(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_OPTION_C)));
                q.setOptionD(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_OPTION_D)));
                q.setCorrectAnswer(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CORRECT_ANSWER)));
                q.setCorrectSequence(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_CORRECT_SEQUENCE)));
                q.setQuestionImage(cursor.getString(cursor.getColumnIndexOrThrow(FirstAidDatabaseHelper.COLUMN_QUESTION_IMAGE)));
                q.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow("Explanation")));


                questions.add(q);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return questions;
    }


    public void insertSampleQuestions(Context context) {
        QuestionDAO questionDAO = new QuestionDAO(context);

        // CPR MCQs
        questionDAO.insertQuestion(new Question(1,
                "What is the first step in the DRSABCD action plan?",
                "mcq", "Airway", "Response", "Danger", "Breathing", "C",
                null,
                "cpr_drsabcd.png",
                "Danger is always the first step. Ensure the area is safe for you, the victim, and bystanders before proceeding.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "What is the correct ratio of chest compressions to rescue breaths for an adult?",
                "mcq", "15:2", "30:2", "20:4", "10:1", "B",
                null,
                "cpr_ratio.png",
                "The correct CPR ratio for adults is 30 compressions followed by 2 rescue breaths, as recommended by resuscitation guidelines.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "When should you stop giving CPR?",
                "mcq", "After 1 minute", "When help arrives or the person starts breathing",
                "After 50 compressions", "If the casualty doesn’t respond immediately", "B",
                null,
                "cpr_stop.png",
                "You should continue CPR until professional help arrives or the person shows signs of life such as normal breathing.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "How deep should chest compressions be for an adult?",
                "mcq", "1 inch", "2 inches (5 cm)", "3 inches", "Just enough to make the chest move", "B",
                null,
                "cpr_depth.png",
                "Chest compressions for adults should be about 2 inches or 5 cm deep to be effective in circulating blood.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "What should you do if the person starts breathing during CPR?",
                "mcq", "Continue compressions", "Stop CPR and put them in the recovery position",
                "Check their pulse", "Ask them questions", "B",
                null,
                "cpr_breathing.png",
                "If the person starts breathing, stop CPR and place them in the recovery position to maintain an open airway.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "How do you open the airway?",
                "mcq", "Jaw thrust", "Tilt head back and lift the chin", "Shake the shoulders", "Roll the person over", "B",
                null,
                "cpr_airway.png",
                "The head tilt-chin lift method is used to open the airway by preventing the tongue from blocking it.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "What should you check before starting CPR?",
                "mcq", "Their wallet", "Their name", "Danger to yourself and others", "Time of day", "C",
                null,
                "cpr_danger.png",
                "Always check for danger first to ensure the scene is safe before assisting a casualty.",
                null,
                null
        ));

        // CPR Scenarios
        questionDAO.insertQuestion(new Question(1,
                "Arrange the correct steps for CPR.",
                "scenario", null, null, null, null, null,
                "Check for danger,Check for response,Send for help,Open airway,Check breathing,Start CPR",
                "cpr_steps.png",
                "This follows the DRSABCD action plan: ensure safety, assess response, call for help, open the airway, check breathing, and start CPR if needed.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "Arrange your actions for CPR on a child who collapsed.",
                "scenario", null, null, null, null, null,
                "Ensure safety,Check for response,Call for help,Perform 5 initial breaths,Start compressions",
                "cpr_child.png",
                "For children, after ensuring safety and calling for help, start with 5 initial rescue breaths before compressions as they often suffer from breathing-related issues.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(1,
                "Arrange how to respond when a bystander offers an AED during CPR.",
                "scenario", null, null, null, null, null,
                "Stop CPR briefly,Turn on AED,Attach pads,Follow voice prompts",
                "aed_use.png",
                "Pause CPR only briefly to use the AED. Turn it on, attach the pads as instructed, and follow its voice prompts to deliver shocks if needed.",
                null,
                null
        ));


        // BLEEDING MCQs
        questionDAO.insertQuestion(new Question(2,
                "What should you do first when someone is bleeding heavily?",
                "mcq", "Panic", "Call their parents", "Check for danger and wear gloves", "Give them painkillers", "C",
                null,
                "bleeding_danger.png",
                "The first priority is your safety. Ensure the scene is safe and wear gloves to prevent infection before giving aid.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "What is the best method to control bleeding?",
                "mcq", "Elevation", "Direct pressure", "Let it bleed out", "Wash with soap", "B",
                null,
                "bleeding_pressure.png",
                "Direct pressure is the most effective and immediate method to control external bleeding by helping the blood to clot.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "What should you do if blood soaks through a dressing?",
                "mcq", "Remove it", "Add another dressing on top", "Wash the wound", "Apply ice", "B",
                null,
                "bleeding_dressing.png",
                "Do not remove the soaked dressing—this could reopen the wound. Add another layer on top to maintain pressure and absorb blood.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "How do you recognize internal bleeding?",
                "mcq", "Loud snoring", "Blue skin", "Swelling and bruising", "Eye redness", "C",
                null,
                "internal_bleeding.png",
                "Internal bleeding may show signs such as swelling, bruising, or vomiting blood. It can be life-threatening and needs urgent care.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "When should you elevate a bleeding limb?",
                "mcq", "After applying pressure", "While walking", "During CPR", "Never", "A",
                null,
                "bleeding_elevate.png",
                "Elevating the limb after applying pressure reduces blood flow to the area, aiding in bleeding control.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "What should be avoided during bleeding management?",
                "mcq", "Gloves", "Applying pressure", "Removing embedded objects", "Calling for help", "C",
                null,
                "bleeding_avoid.png",
                "Do not remove embedded objects from a wound. Instead, apply pressure around the object and secure it in place until help arrives.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "Which of the following is a sign of shock due to blood loss?",
                "mcq", "Calmness", "Pale, cold, and clammy skin", "Redness in face", "Fast speech", "B",
                null,
                "shock_signs.png",
                "Signs of shock from blood loss include pale, cool, clammy skin, rapid breathing, and a weak pulse. These indicate poor circulation.",
                null,
                null
        ));

        //BLEEDING Scenarios
        questionDAO.insertQuestion(new Question(2,
                "Arrange your response when someone cuts their hand deeply.",
                "scenario", null, null, null, null, null,
                "Wear gloves,Apply direct pressure,Elevate hand,Monitor for signs of shock",
                "bleeding_cut_hand.png",
                "Wear gloves for safety, apply direct pressure to stop bleeding, elevate the hand to reduce blood flow, and monitor for shock symptoms like pale skin or rapid breathing.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "Arrange the steps to control heavy knee bleeding.",
                "scenario", null, null, null, null, null,
                "Ensure safety,Apply pressure with clean dressing,Elevate leg,Add dressing if needed",
                "bleeding_knee.png",
                "Always ensure the scene is safe first. Apply firm pressure with a clean dressing, elevate the limb to slow bleeding, and reinforce with more dressings if needed.",
                null,
                null
        ));

        questionDAO.insertQuestion(new Question(2,
                "You suspect internal bleeding. Arrange your actions.",
                "scenario", null, null, null, null, null,
                "Reassure casualty,Keep them still,Call emergency help,Monitor vital signs",
                "internal_bleeding_bruise.png",
                "Internal bleeding can be life-threatening. Keep the casualty calm and still, call emergency services immediately, and monitor their breathing and pulse until help arrives.",
                null,
                null
        ));

    }
}
