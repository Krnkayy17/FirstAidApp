package com.example.firstaidapp;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.ModuleAdapter;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.models.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ModuleAdapter moduleAdapter;
    private ModuleDAO moduleDAO;
    private List<Module> moduleList;
    private ImageView imgModuleBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        // Initialize UI elements
        imgModuleBanner = findViewById(R.id.imgModuleBanner);
        recyclerView = findViewById(R.id.recyclerViewModules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set Banner Image
        imgModuleBanner.setImageResource(R.drawable.ic_modules);

        // Initialize ModuleDAO
        moduleDAO = new ModuleDAO(this);
        moduleDAO.open();

        // Load modules from database
        moduleList = moduleDAO.getAllModules();
        moduleDAO.close();

        // If no data in database, insert sample modules
        if (moduleList.isEmpty()) {
            moduleList = new ArrayList<>();
            moduleList.add(new Module(1, "CPR", "Learn CPR techniques", "Intermediate", 15, 5, "Complete all assessments", "Not Accessed Yet", "Not Started"));
            moduleList.add(new Module(2, "Bleeding Management", "Control bleeding in emergencies", "Beginner", 10, 4, "Pass all assessments", "Not Accessed Yet", "Not Started"));
        }

        // Set Adapter
        moduleAdapter = new ModuleAdapter(this, moduleList);
        recyclerView.setAdapter(moduleAdapter);
    }
}
