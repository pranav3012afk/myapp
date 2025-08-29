package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class
AdminPanel extends AppCompatActivity {
    LinearLayout allusers, admindonation, adminmessage;
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        allusers = findViewById(R.id.allusers);
        admindonation = findViewById(R.id.admindonation);
        adminmessage = findViewById(R.id.adminmessage);

        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);

        mauth = FirebaseAuth.getInstance();

        allusers.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanel.this, user_list.class));
        });

        adminmessage.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanel.this, adminmessages.class));
        });

        homeicon.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanel.this, Home.class));
            finish();
        });

        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanel.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanel.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(AdminPanel.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(AdminPanel.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
