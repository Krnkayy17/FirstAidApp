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

import java.util.ArrayList;
import java.util.List;

public class SubTopicActivity extends AppCompatActivity {

    private TextView subtopicTitle;
    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;

    private List<Content> contentList;
    private int moduleID;
    private String moduleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic);

        // Toolbar Setup
       androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sub Topics Module");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        // Bind views
        subtopicTitle = findViewById(R.id.subtopicTitle);
        recyclerView = findViewById(R.id.recyclerViewContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get module ID and title from Intent
        moduleID = getIntent().getIntExtra("MODULE_ID", -1);
        moduleTitle = getIntent().getStringExtra("MODULE_TITLE");

        if (moduleID == -1 || moduleTitle == null || moduleTitle.isEmpty()) {
            Toast.makeText(this, "Error: Module information missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup database and DAO
        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(this);
        ContentDAO contentDAO = new ContentDAO(this);

        // Fetch content for the module
        contentList = contentDAO.getContentByModule(moduleID);
        if (contentList == null) contentList = new ArrayList<>();

        // Insert initial content if not found
        if (contentList.isEmpty()) {
            contentDAO.insertInitialContent(moduleID, moduleTitle);
            contentList = contentDAO.getContentByModule(moduleID);
            if (contentList == null) contentList = new ArrayList<>();
        }

        // Group content by order and pick the first item in each group with a title
        List<Content> groupedList = new ArrayList<>();
        int lastOrder = -1;

        for (Content content : contentList) {
            int currentOrder = content.getContentOrder();
            if (currentOrder != lastOrder &&
                    content.getContentTitle() != null &&
                    !content.getContentTitle().trim().isEmpty()) {

                groupedList.add(content);
                lastOrder = currentOrder;
            }
        }

        if (groupedList.isEmpty()) {
            Toast.makeText(this, "No subtopics available for this module", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup and bind RecyclerView
        contentAdapter = new ContentAdapter(this, groupedList);
        recyclerView.setAdapter(contentAdapter);

        // Set module title
        subtopicTitle.setText(moduleTitle);
    }
}
