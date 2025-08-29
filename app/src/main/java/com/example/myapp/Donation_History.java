package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Donation_History extends AppCompatActivity {
    private LinearLayout historyContainer;
    private TextView totalAmountText;
    private DatabaseReference donationsref;
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;
    private int totalDonated = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donation_history);
        historyContainer = findViewById(R.id.historyContainer);
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        mauth = FirebaseAuth.getInstance();
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);
        totalAmountText = findViewById(R.id.totalAmountText);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        donationsref = FirebaseDatabase.getInstance().getReference("Donations").child(userId);
        fetchDonationsHistory();

    }

    private void fetchDonationsHistory() {
        donationsref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalDonated = 0;
                historyContainer.removeAllViews();
                if (!snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "No donations history found", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataSnapshot donationSnapshot : snapshot.getChildren()) {
                    Object amountObj = donationSnapshot.child("amount").getValue();
                    String date = donationSnapshot.child("date").getValue(String.class);
                    if(amountObj != null && date != null) {
                        int amount= 0;
                        if(amountObj instanceof Long) {
                            amount = ((Long) amountObj).intValue();

                        }
                        else if(amountObj instanceof Integer) {
                            amount = (Integer) amountObj;
                        } else if (amountObj instanceof Double) {
                            amount = (int) Math.round((Double) amountObj);


                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error : Unkwon datatype", Toast.LENGTH_SHORT).show();


                        }
                        totalDonated += amount;
                        TextView entry= new TextView(Donation_History.this);
                        entry.setText("Date: " + date + "Amount: INR " + amount);
                        entry.setTextSize(16f);
                        historyContainer.addView(entry);


                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error : Missing amount or date", Toast.LENGTH_SHORT).show();

                    }

                }
                totalAmountText.setText("Total donated: INR " + totalDonated);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch donations history", Toast.LENGTH_SHORT).show();
            }

            });
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Donation_History.this, Home.class));
                finish();
            }
        });
        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(Donation_History.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            // Already on profile; maybe show a toast
            startActivity(new Intent(Donation_History.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(Donation_History.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(Donation_History.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        }
}