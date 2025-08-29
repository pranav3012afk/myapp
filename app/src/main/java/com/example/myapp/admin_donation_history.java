package com.example.myapp;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class admin_donation_history extends AppCompatActivity {
    ListView donationListView;
    TextView totaldonationamount;
    List<Map<String, String>> donationList = new ArrayList<>();
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;
     DatabaseReference donationRef;
    SimpleAdapter adapter;
    int totalamount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_donation_history);
        donationListView = findViewById(R.id.donationListView);
        totaldonationamount = findViewById(R.id.totaldonationamount);
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);
        mauth = FirebaseAuth.getInstance();
        donationRef = FirebaseDatabase.getInstance().getReference("donations");
        adapter = new SimpleAdapter(
                this,
                donationList,
                R.layout.activity_donation_list,
                new String[]{"name", "amount"},
                new int[]{R.id.nametext, R.id.amounttext
                });
        donationListView.setAdapter(adapter);
        fetchDonations();
    }
    private void fetchDonations() {
        donationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationList.clear();
                totalamount = 0;
                for(DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot donationSnap : userSnap.getChildren()) {
                        String name = donationSnap.child("name").getValue(String.class);
                        String amountstr = String.valueOf(donationSnap.child("amount").getValue());
                        if (name != null && amountstr != null) {

                            int amount = Integer.parseInt(amountstr);
                            totalamount += amount;
                            Map<String, String> map = new HashMap<>();
                            map.put("name", name);
                            map.put("amount", amountstr);

                            donationList.add(map);

                        }
                    }
                    totaldonationamount.setText("Total Donations: " + totalamount);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "failed to load data", Toast.LENGTH_SHORT).show();

            }
        });
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_donation_history.this, Home.class));
                finish();
            }
        });
        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(admin_donation_history.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            // Already on profile; maybe show a toast
            startActivity(new Intent(admin_donation_history.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(admin_donation_history.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(admin_donation_history.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }
}