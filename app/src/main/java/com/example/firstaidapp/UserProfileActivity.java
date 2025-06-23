package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.database.UserDAO;
import com.example.firstaidapp.models.User;
import com.example.firstaidapp.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvEmail, tvPhone, tvUserType;
    private ImageView imgProfile;
    private Button btnLogout, btnEditProfile;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile"); // or module name

        toolbar.setNavigationOnClickListener(v -> onBackPressed());*/

        // Initialize Views
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        //tvPhone = findViewById(R.id.tvPhone);
        tvUserType = findViewById(R.id.tvUserType);
        imgProfile = findViewById(R.id.imgProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Get current user ID from session
        SessionManager sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        if (userId == -1) {
            // Invalid session fallback
            startActivity(new Intent(this, LogInActivity.class));
            finish();
            return;
        }

        loadUserProfile();

        // Edit Profile
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.nav_modules) startActivity(new Intent(this, ModuleActivity.class));
            else if (id == R.id.nav_assessments) startActivity(new Intent(this, AssessmentActivity.class));
            else if (id == R.id.nav_progress) startActivity(new Intent(this, ProgressSummaryActivity.class));
            else if (id == R.id.nav_profile) startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(); // Refresh user info when coming back from EditProfile
    }

    private void loadUserProfile() {
        UserDAO userDAO = new UserDAO(this);
        User currentUser = userDAO.getUserById(userId);

        if (currentUser != null) {
            tvUserName.setText(currentUser.getUserName());
            tvEmail.setText(currentUser.getUserEmail());
            tvUserType.setText("User Type: " + currentUser.getUserType());

            // Load profile image with Glide
            if (currentUser.getUserImage() != null && !currentUser.getUserImage().isEmpty()) {
                Glide.with(this)
                        .load(currentUser.getUserImage())
                        .placeholder(R.drawable.ic_profile)
                        .into(imgProfile);
            } else {
                imgProfile.setImageResource(R.drawable.ic_profile);
            }
        }
    }
}
