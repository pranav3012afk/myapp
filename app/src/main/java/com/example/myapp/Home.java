package com.example.myapp;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    TextView Welcome;
    FirebaseAuth mauth;
    DatabaseReference databaseReference;
    Button donhistory;

    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    LinearLayout contactUsLayout, aboutusbtn, eventbtn, volenterbtn, gallarybtn, donationbtn;
    ViewFlipper viewFlipper;

    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        donhistory  = findViewById(R.id.donhistory);
        Welcome = findViewById(R.id.Welcome);
        aboutusbtn = findViewById(R.id.aboutusbtn);
        eventbtn = findViewById(R.id.eventbtn);
        volenterbtn = findViewById(R.id.volenterbtn);
        gallarybtn = findViewById(R.id.gallarybtn);
        donationbtn = findViewById(R.id.donationbtn);
        contactUsLayout = findViewById(R.id.contactUsLayout);

        viewFlipper = findViewById(R.id.homeSlider);
        viewFlipper.startFlipping();

        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);

        mauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        fetchUserName();

        homeicon.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Home.class));
            finish();
        });

        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, donationbox.class));
        });

        profileicon.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        contactUsLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, contact_us.class);
            startActivity(intent);
            finish();
        });
        aboutusbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, aboutus.class);
            startActivity(intent);
        });

        eventbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, eventactivity.class);
            startActivity(intent);
        });

        volenterbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, volenteer.class);
            startActivity(intent);
        });

        donationbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, donationbox.class);
            startActivity(intent);
        });

        gallarybtn.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, gallary.class);
            startActivity(intent);
        });

        contactUsLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, contact_us.class);
            startActivity(intent);
        });

        int[] gridIds = {
                R.id.aboutusbtn,
                R.id.eventbtn,
                R.id.volenterbtn,
                R.id.donationbtn,
                R.id.gallarybtn,
                R.id.contactUsLayout
        };

        for (int i = 0; i < gridIds.length; i++) {
            View btn = findViewById(gridIds[i]);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.grid_items);
            anim.setStartOffset(i * 150); // staggered animation
            btn.startAnimation(anim);
        }
    }

    private void fetchUserName() {
        FirebaseUser currentUser = mauth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userName = snapshot.getValue(String.class);
                    if (userName != null) {
                        Welcome.setText("Welcome, " + userName + "!");
                    } else {
                        Welcome.setText("Welcome, Guest!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
