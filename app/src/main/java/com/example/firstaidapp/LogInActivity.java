package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.database.FirstAidDatabaseHelper;
import com.example.firstaidapp.database.UserDAO;
import com.example.firstaidapp.models.User;
import com.example.firstaidapp.utils.SessionManager;

public class LogInActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView signUpText;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize views
        editTextEmail = findViewById(R.id.text_email);
        editTextPassword = findViewById(R.id.text_pwd);
        buttonLogin = findViewById(R.id.btn_login);
        signUpText = findViewById(R.id.go_tp_signup);

        // Initialize database and DAO
        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(this);
        userDAO = new UserDAO(this);

        // Login button click
        buttonLogin.setOnClickListener(view -> loginUser());

        // Sign up link click
        signUpText.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, SignUpActivity.class)));
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userDAO.getUserByCredentials(email, password);
        if (user != null) {
            Toast.makeText(this, "Login successful! Welcome, " + user.getUserName(), Toast.LENGTH_SHORT).show();

            SessionManager sessionManager = new SessionManager(this);
            sessionManager.saveUserSession(
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserEmail(),
                    user.getUserType(),
                    user.getUserImage()
            );

            // Redirect to home
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
        }
    }
}
