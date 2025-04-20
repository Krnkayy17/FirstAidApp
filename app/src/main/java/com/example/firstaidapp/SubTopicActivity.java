package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.database.ContentDAO;
import com.example.firstaidapp.database.FirstAidDatabaseHelper;
import com.example.firstaidapp.models.Content;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubTopicActivity extends AppCompatActivity {

    private TextView subtopicTitle, subtopicContent;
    private ImageView subtopicImage;
    private Button nextButton;

    private List<Content> contentList;
    private int currentIndex = 0;
    private int moduleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic);

        subtopicTitle = findViewById(R.id.subtopicTitle);
        subtopicContent = findViewById(R.id.subtopicContent);
        subtopicImage = findViewById(R.id.subtopicImage);
        nextButton = findViewById(R.id.nextButton);

        moduleID = getIntent().getIntExtra("MODULE_ID", -1);
        if (moduleID == -1) {
            Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(this);
        ContentDAO contentDAO = new ContentDAO(dbHelper);

        // Check if content exists for this module
        contentList = contentDAO.getContentByModule(moduleID);

        // If not, insert default content
        if (contentList.isEmpty()) {
            contentDAO.insertInitialContent();
            contentList = contentDAO.getContentByModule(moduleID); // Reload updated list
        }

        if (contentList.isEmpty()) {
            Toast.makeText(this, "No content available for this module", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        displayContent(currentIndex);

        nextButton.setOnClickListener(view -> {
            if (currentIndex < contentList.size() - 1) {
                currentIndex++;
                displayContent(currentIndex);
            } else {
                Toast.makeText(SubTopicActivity.this, "Module Completed!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SubTopicActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayContent(int index) {
        Content content = contentList.get(index);
        subtopicTitle.setText(content.getContentTitle());
        subtopicContent.setText(content.getContentText());

        if (content.getContentImage() != null && !content.getContentImage().isEmpty()) {
            Picasso.get().load(content.getContentImage()).into(subtopicImage);
            subtopicImage.setVisibility(View.VISIBLE);
        } else {
            subtopicImage.setVisibility(View.GONE);
        }

        nextButton.setText(index == contentList.size() - 1 ? "Done" : "Next");
    }
}
