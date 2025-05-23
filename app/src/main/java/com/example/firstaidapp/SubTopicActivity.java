package com.example.firstaidapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.ContentAdapter;
import com.example.firstaidapp.database.ContentDAO;
import com.example.firstaidapp.database.FirstAidDatabaseHelper;
import com.example.firstaidapp.models.Content;

import java.util.List;

public class SubTopicActivity extends AppCompatActivity {

    private TextView subtopicTitle;
    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;

    private List<Content> contentList;
    private int moduleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic);

        subtopicTitle = findViewById(R.id.subtopicTitle);
        recyclerView = findViewById(R.id.recyclerViewContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        moduleID = getIntent().getIntExtra("MODULE_ID", -1);
        if (moduleID == -1) {
            Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(this);
        ContentDAO contentDAO = new ContentDAO(dbHelper);
        contentList = contentDAO.getContentByModule(moduleID);

        if (contentList.isEmpty()) {
            contentDAO.insertInitialContent();
            contentList = contentDAO.getContentByModule(moduleID);
        }

        if (contentList.isEmpty()) {
            Toast.makeText(this, "No content available for this module", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        contentAdapter = new ContentAdapter(this, contentList);
        recyclerView.setAdapter(contentAdapter);
    }
}
