package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class aboutus extends AppCompatActivity {
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);

        homeicon.setOnClickListener(v -> {
            startActivity(new Intent(aboutus.this, Home.class));
            finish();
        });

        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(aboutus.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            startActivity(new Intent(aboutus.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(aboutus.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(aboutus.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        mauth = FirebaseAuth.getInstance();
    }
}