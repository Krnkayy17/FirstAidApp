package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.deleteDatabase("FirstAidApp.db");
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, LogInActivity.class)
            )
        );
        signupButton.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, SignUpActivity.class)
            )
        );
    }
}