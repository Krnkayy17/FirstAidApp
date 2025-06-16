package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.database.FirstAidDatabaseHelper;
import com.example.firstaidapp.database.QuestionDAO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.deleteDatabase("FirstAidApp.db");
        setContentView(R.layout.activity_main);

        // Initialize the database
        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(this);
        dbHelper.getWritableDatabase(); // This ensures DB gets created

        // Insert sample questions only if not already inserted
        QuestionDAO questionDAO = new QuestionDAO(this);
        if (questionDAO.getQuestionsByModuleId(1).isEmpty()) {
            questionDAO.insertSampleQuestions(this);
        }

        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, LogInActivity.class)
            )
        );
        signupButton.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, SignUpActivity.class)
            )
        );
    }
}