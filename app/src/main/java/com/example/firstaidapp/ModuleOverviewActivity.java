package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.models.Module;

public class ModuleOverviewActivity extends AppCompatActivity {

    private ImageView moduleImage;
    private TextView moduleTitle, moduleDescription;
    private Button startLearningButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_overview);

        // UI init
        moduleImage = findViewById(R.id.moduleImage);
        moduleTitle = findViewById(R.id.moduleTitle);
        moduleDescription = findViewById(R.id.moduleDescription);
        startLearningButton = findViewById(R.id.startLearningButton);

        // Get module ID
        Intent intent = getIntent();
        int moduleId = intent.getIntExtra("MODULE_ID", -1);

        // Fetch module details
        ModuleDAO moduleDAO = new ModuleDAO(this);
        Module module = moduleDAO.getModuleById(moduleId);

        if (module != null) {
            moduleTitle.setText(module.getModuleName());
            moduleDescription.setText(module.getDescription());

            int moduleImageResId = intent.getIntExtra("MODULE_IMAGE", -1);
            if (moduleImageResId != -1) {
                moduleImage.setImageResource(moduleImageResId);
            }
        }


        startLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subtopicIntent = new Intent(ModuleOverviewActivity.this, SubTopicActivity.class);
                subtopicIntent.putExtra("MODULE_ID", moduleId);
                subtopicIntent.putExtra("MODULE_TITLE", module.getModuleName()); // ✅ Add this line
                startActivity(subtopicIntent);
            }
        });

    }

}
