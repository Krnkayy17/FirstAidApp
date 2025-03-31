package com.example.firstaidapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.database.ContentDatabaseHelper;
import com.example.firstaidapp.models.Content;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubTopicActivity extends AppCompatActivity {

    private TextView subtopicTitle, subtopicContent;
    private ImageView subtopicImage;
    private Button nextButton;

    private ContentDatabaseHelper dbHelper;
    private List<Content> contentList;
    private int currentIndex = 0;
    private int moduleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic);

        // Initialize UI components
        subtopicTitle = findViewById(R.id.subtopicTitle);
        subtopicContent = findViewById(R.id.subtopicContent);
        subtopicImage = findViewById(R.id.subtopicImage);
        nextButton = findViewById(R.id.nextButton);

        // Get module ID from intent
        moduleID = getIntent().getIntExtra("MODULE_ID", -1);
        if (moduleID == -1) {
            Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize database and get content list
        dbHelper = new ContentDatabaseHelper(this);
        contentList = dbHelper.getContentByModule(moduleID);

        if (contentList.isEmpty()) {
            Toast.makeText(this, "No content available for this module", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display first subtopic
        displayContent(currentIndex);

        // Handle Next button click
        nextButton.setOnClickListener(view -> {
            if (currentIndex < contentList.size() - 1) {
                currentIndex++;
                displayContent(currentIndex);
            } else {
                // If last subtopic, show "Done" instead of "Next"
                nextButton.setText("Done");
                nextButton.setOnClickListener(doneView -> {
                    Toast.makeText(SubTopicActivity.this, "Module Completed!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubTopicActivity.this, QuizActivity.class);
                    intent.putExtra("MODULE_ID", moduleID);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }

    private void displayContent(int index) {
        Content content = contentList.get(index);
        subtopicTitle.setText(content.getContentTitle());
        subtopicContent.setText(content.getContentText());

        // Load image using Picasso if available
        if (content.getContentImage() != null && !content.getContentImage().isEmpty()) {
            Picasso.get().load(content.getContentImage()).into(subtopicImage);
            subtopicImage.setVisibility(View.VISIBLE);
        } else {
            subtopicImage.setVisibility(View.GONE);
        }

        // If this is the last subtopic, change button text to "Done"
        if (index == contentList.size() - 1) {
            nextButton.setText("Done");
        } else {
            nextButton.setText("Next");
        }
    }
}
