package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ModuleOverviewActivity extends AppCompatActivity {

    private ImageView moduleImage;
    private TextView moduleTitle, moduleDescription;
    private Button startLearningButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_overview);

        // Initialize UI elements
        moduleImage = findViewById(R.id.moduleImage);
        moduleTitle = findViewById(R.id.moduleTitle);
        moduleDescription = findViewById(R.id.moduleDescription);
        startLearningButton = findViewById(R.id.startLearningButton);

        Intent intent = getIntent();
        int moduleId = intent.getIntExtra("MODULE_ID", -1); // Needed for SubTopicActivity

        startLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subtopicIntent = new Intent(ModuleOverviewActivity.this, SubTopicActivity.class);
                subtopicIntent.putExtra("MODULE_ID", moduleId); // Send correct module ID
                startActivity(subtopicIntent);
            }
        });

    }
}
