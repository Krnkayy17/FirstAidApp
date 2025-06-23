package com.example.firstaidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // your splash layout

        // Optional fade-in animation
        ImageView icon = findViewById(R.id.splash_icon);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        icon.startAnimation(fadeIn);

        // Delay then start MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class); // or HomeActivity.class
            startActivity(intent);
            finish(); // close splash so user can't go back to it
        }, 2000); // 2 seconds
    }
}
