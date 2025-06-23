package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.firstaidapp.database.FirstAidDatabaseHelper;
import com.example.firstaidapp.database.UserDAO;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextPassword, editTextConfirmPassword;
    private Button buttonCreateAccount;
    private TextView goToLogin;
    private UserDAO userDAO;
    private RadioGroup radioUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirstAidDatabaseHelper dbHelper = new FirstAidDatabaseHelper(this);
        userDAO = new UserDAO(this);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        //editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        radioUserType = findViewById(R.id.radioUserType);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        goToLogin = findViewById(R.id.go_to_login);

        buttonCreateAccount.setOnClickListener(view -> registerUser());
        goToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        //String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        int selectedTypeId = radioUserType.getCheckedRadioButtonId();
        String userType = (selectedTypeId == R.id.radioVolunteer) ? "volunteer" : "general";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userDAO.checkUserExists(email)) {
            Toast.makeText(this, "Email already registered!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userDAO.insertUser(name, email, password, userType, null)) {
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Sign Up failed. Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}