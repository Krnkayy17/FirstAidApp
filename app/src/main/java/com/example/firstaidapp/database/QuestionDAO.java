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
                "mcq", "Airway", "Response", "Danger", "Breathing", "C", null,
                "cpr_drsabcd.png"));

        questionDAO.insertQuestion(new Question(1,
                "What is the correct ratio of chest compressions to rescue breaths for an adult?",
                "mcq", "15:2", "30:2", "20:4", "10:1", "B", null,
                "cpr_ratio.png"));

        questionDAO.insertQuestion(new Question(1,
                "When should you stop giving CPR?",
                "mcq", "After 1 minute", "When help arrives or the person starts breathing",
                "After 50 compressions", "If the casualty doesn’t respond immediately", "B", null,
                "cpr_stop.png"));

        questionDAO.insertQuestion(new Question(1,
                "How deep should chest compressions be for an adult?",
                "mcq", "1 inch", "2 inches (5 cm)", "3 inches", "Just enough to make the chest move", "B", null,
                "cpr_depth.png"));

        questionDAO.insertQuestion(new Question(1,
                "What should you do if the person starts breathing during CPR?",
                "mcq", "Continue compressions", "Stop CPR and put them in the recovery position",
                "Check their pulse", "Ask them questions", "B", null,
                "cpr_breathing.png"));

        questionDAO.insertQuestion(new Question(1,
                "How do you open the airway?",
                "mcq", "Jaw thrust", "Tilt head back and lift the chin", "Shake the shoulders", "Roll the person over", "B", null,
                "cpr_airway.png"));

        questionDAO.insertQuestion(new Question(1,
                "What should you check before starting CPR?",
                "mcq", "Their wallet", "Their name", "Danger to yourself and others", "Time of day", "C", null,
                "cpr_danger.png"));

        // CPR Scenarios
        questionDAO.insertQuestion(new Question(1,
                "Arrange the correct steps for CPR.",
                "scenario", null, null, null, null, null,
                "Check for danger,Check for response,Send for help,Open airway,Check breathing,Start CPR",
                "cpr_steps.png"));

        questionDAO.insertQuestion(new Question(1,
                "Arrange your actions for CPR on a child who collapsed.",
                "scenario", null, null, null, null, null,
                "Ensure safety,Check for response,Call for help,Perform 5 initial breaths,Start compressions",
                "cpr_child.png"));

        questionDAO.insertQuestion(new Question(1,
                "Arrange how to respond when a bystander offers an AED during CPR.",
                "scenario", null, null, null, null, null,
                "Stop CPR briefly,Turn on AED,Attach pads,Follow voice prompts",
                "aed_use.png"));

        // BLEEDING MCQs
        questionDAO.insertQuestion(new Question(2,
                "What should you do first when someone is bleeding heavily?",
                "mcq", "Panic", "Call their parents", "Check for danger and wear gloves", "Give them painkillers", "C", null,
                "bleeding_danger.png"));

        questionDAO.insertQuestion(new Question(2,
                "What is the best method to control bleeding?",
                "mcq", "Elevation", "Direct pressure", "Let it bleed out", "Wash with soap", "B", null,
                "bleeding_pressure.png"));

        questionDAO.insertQuestion(new Question(2,
                "What should you do if blood soaks through a dressing?",
                "mcq", "Remove it", "Add another dressing on top", "Wash the wound", "Apply ice", "B", null,
                "bleeding_dressing.png"));

        questionDAO.insertQuestion(new Question(2,
                "How do you recognize internal bleeding?",
                "mcq", "Loud snoring", "Blue skin", "Swelling and bruising", "Eye redness", "C", null,
                "internal_bleeding.png"));

        questionDAO.insertQuestion(new Question(2,
                "When should you elevate a bleeding limb?",
                "mcq", "After applying pressure", "While walking", "During CPR", "Never", "A", null,
                "bleeding_elevate.png"));

        questionDAO.insertQuestion(new Question(2,
                "What should be avoided during bleeding management?",
                "mcq", "Gloves", "Applying pressure", "Removing embedded objects", "Calling for help", "C", null,
                "bleeding_avoid.png"));

        questionDAO.insertQuestion(new Question(2,
                "Which of the following is a sign of shock due to blood loss?",
                "mcq", "Calmness", "Pale, cold, and clammy skin", "Redness in face", "Fast speech", "B", null,
                "shock_signs.png"));

        // BLEEDING Scenarios
        questionDAO.insertQuestion(new Question(2,
                "Arrange your response when someone cuts their hand deeply.",
                "scenario", null, null, null, null, null,
                "Wear gloves,Apply direct pressure,Elevate hand,Monitor for signs of shock",
                "bleeding_cut_hand.png"));

        questionDAO.insertQuestion(new Question(2,
                "Arrange the steps to control heavy knee bleeding.",
                "scenario", null, null, null, null, null,
                "Ensure safety,Apply pressure with clean dressing,Elevate leg,Add dressing if needed",
                "bleeding_knee.png"));

        questionDAO.insertQuestion(new Question(2,
                "You suspect internal bleeding. Arrange your actions.",
                "scenario", null, null, null, null, null,
                "Reassure casualty,Keep them still,Call emergency help,Monitor vital signs",
                "internal_bleeding_steps.png"));
    }
}
