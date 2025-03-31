package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Updated layout name to match HomeActivity

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    return true; // Already in HomeActivity, no need to restart
                } else if (itemId == R.id.nav_modules) {
                    startActivity(new Intent(HomeActivity.this, ModuleActivity.class));
                    return true;
                } else if (itemId == R.id.nav_assessments) {
                    startActivity(new Intent(HomeActivity.this, AssessmentActivity.class));
                    return true;
                } else if (itemId == R.id.nav_progress) {
                    startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
