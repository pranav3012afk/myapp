package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adminmessages extends AppCompatActivity {
    ListView messageListView;
    List<Map<String, String>> messageList = new ArrayList<>();
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;
    DatabaseReference userRef;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmessages);
        messageListView = findViewById(R.id.messageListView);
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);
        mauth = FirebaseAuth.getInstance();
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminmessages.this, Home.class));
                finish();
            }
        });
        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(adminmessages.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            // Already on profile; maybe show a toast
            startActivity(new Intent(adminmessages.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(adminmessages.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(adminmessages.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("contactData");
        adapter = new SimpleAdapter(this, messageList, R.layout.activity_adminmessages,
                new String[]{"name", "message"},
                new int[]{R.id.nametext, R.id.phonetext, R.id.emailtext});
        messageListView.setAdapter(adapter);
        fetchUsers();
    }

    private void fetchUsers() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String message= userSnapshot.child("message").getValue(String.class);

                    if (name != null && message != null ) {
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("name", "Name" + name);
                        userMap.put("message", "Message" + message);

                        messageList.add(userMap);


                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}
