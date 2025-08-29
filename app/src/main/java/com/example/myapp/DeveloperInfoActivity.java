package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class DeveloperInfoActivity extends AppCompatActivity {
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);
        mauth = FirebaseAuth.getInstance();
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeveloperInfoActivity.this, Home.class));
                finish();
            }
        });
        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(DeveloperInfoActivity.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            // Already on profile; maybe show a toast
            startActivity(new Intent(DeveloperInfoActivity.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(DeveloperInfoActivity.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(DeveloperInfoActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


    }
}