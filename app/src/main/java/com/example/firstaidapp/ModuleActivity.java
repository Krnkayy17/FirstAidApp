package com.example.firstaidapp;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.ModuleAdapter;
import com.example.firstaidapp.database.FirstAidDatabaseHelper;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.models.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ModuleAdapter moduleAdapter;
    private List<Module> moduleList;
    private ModuleDAO moduleDAO;
    private ImageView imgModuleBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        // Initialize UI elements
        imgModuleBanner = findViewById(R.id.imgModuleBanner);
        recyclerView = findViewById(R.id.recyclerViewModules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imgModuleBanner.setImageResource(R.drawable.ic_modules);

        // Initialize database and DAO
        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(this);
        moduleDAO = new ModuleDAO(this);
        // Load modules from database
        moduleList = moduleDAO.getAllModules();

        if (moduleList.isEmpty()) {
            moduleList = new ArrayList<>();
            moduleList.add(new Module(1, "CPR", "Learn CPR techniques", "Intermediate", 15, 5, "Complete all assessments", "Not Accessed Yet", "Not Started"));
            moduleList.add(new Module(2, "Bleeding Management", "Control bleeding in emergencies", "Beginner", 10, 4, "Pass all assessments", "Not Accessed Yet", "Not Started"));

            for (Module module : moduleList) {
                moduleDAO.addModule(module);  // <-- Add this line
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload module list to reflect any changes made
        loadModules();
    }

    private void loadModules() {
        ModuleDAO moduleDAO = new ModuleDAO(this);
        List<Module> moduleList = moduleDAO.getAllModules();
        moduleAdapter = new ModuleAdapter(this, moduleList);
        recyclerView.setAdapter(moduleAdapter);
    }

}
