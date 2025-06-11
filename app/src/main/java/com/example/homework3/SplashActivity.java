package com.example.homework3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar for full screen experience
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeAnimations();
        startMainActivityWithDelay();
    }

    private void initializeAnimations() {
        // Get views
        ImageView splashMainImage = findViewById(R.id.splash_main_image);
        TextView gameTitleText = findViewById(R.id.game_title_splash);
        ProgressBar loadingProgress = findViewById(R.id.loading_progress);
        TextView loadingText = findViewById(R.id.loading_text);

        // Create fade in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);

        // Create slide in from top animation
        Animation slideInFromTop = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideInFromTop.setDuration(800);

        // Create scale animation for main image
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        scaleAnimation.setDuration(1200);

        // Apply animations with delays
        splashMainImage.startAnimation(scaleAnimation);

        // Delay the title animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            gameTitleText.startAnimation(slideInFromTop);
        }, 600);

        // Delay the loading elements
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loadingProgress.startAnimation(fadeIn);
            loadingText.startAnimation(fadeIn);
        }, 1000);
    }

    private void startMainActivityWithDelay() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // Add transition animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            // Finish splash activity so user can't go back to it
            finish();
        }, SPLASH_DURATION);
    }

    @Override
    public void onBackPressed() {
        // Disable back button on splash screen
        // Do nothing to prevent user from going back
        super.onBackPressed();
    }
}