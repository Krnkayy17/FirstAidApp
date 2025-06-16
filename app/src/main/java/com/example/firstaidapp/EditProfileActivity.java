package com.example.firstaidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.database.UserDAO;
import com.example.firstaidapp.models.User;
import com.example.firstaidapp.utils.SessionManager;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView imgProfilePreview;
    private EditText etEditName, etEditEmail, etEditPassword, etConfirmPassword;
    private RadioGroup radioUserTypeEdit;
    private Button btnSaveProfile;
    private Uri selectedImageUri = null;
    private User currentUser;
    private UserDAO userDAO;

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this).load(uri).into(imgProfilePreview);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Profile");
        }


        // Get user ID from session
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();

        // Initialize DAO
        userDAO = new UserDAO(this);
        currentUser = userDAO.getUserById(userId);

        // Bind Views
        imgProfilePreview = findViewById(R.id.imgProfilePreview);
        etEditName = findViewById(R.id.etEditName);
        etEditEmail = findViewById(R.id.etEditEmail);
        etEditPassword = findViewById(R.id.etEditPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        radioUserTypeEdit = findViewById(R.id.radioUserTypeEdit);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        Button btnUploadPhoto = findViewById(R.id.btnUploadPhoto);

        if (currentUser != null) {
            etEditName.setText(currentUser.getUserName());
            etEditEmail.setText(currentUser.getUserEmail());
            etEditPassword.setText(currentUser.getUserPassword());
            etConfirmPassword.setText(currentUser.getUserPassword());

            if ("volunteer".equalsIgnoreCase(currentUser.getUserType())) {
                radioUserTypeEdit.check(R.id.radioVolunteerEdit);
            } else {
                radioUserTypeEdit.check(R.id.radioGeneralEdit);
            }

            if (currentUser.getUserImage() != null && !currentUser.getUserImage().isEmpty()) {
                Glide.with(this).load(currentUser.getUserImage()).into(imgProfilePreview);
            } else {
                imgProfilePreview.setImageResource(R.drawable.ic_profile);
            }

        }

        btnUploadPhoto.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnSaveProfile.setOnClickListener(v -> {
            String name = etEditName.getText().toString().trim();
            String email = etEditEmail.getText().toString().trim();
            String password = etEditPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String userType = radioUserTypeEdit.getCheckedRadioButtonId() == R.id.radioVolunteerEdit
                    ? "volunteer" : "general";

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.setUserName(name);
            currentUser.setUserEmail(email);
            currentUser.setUserPassword(password);
            currentUser.setUserType(userType);

            if (selectedImageUri != null) {
                currentUser.setUserImage(selectedImageUri.toString());
            }

            boolean updated = userDAO.updateUser(currentUser);
            Toast.makeText(this, updated ? "Profile updated!" : "Update failed!", Toast.LENGTH_SHORT).show();

            if (updated) {
                startActivity(new Intent(this, UserProfileActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
