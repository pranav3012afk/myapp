package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome extends AppCompatActivity {
    ImageView welcomelogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(welcome.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
        welcomelogo = findViewById(R.id.welcomeanim);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_anim);
        welcomelogo.startAnimation(animation);
    }
}