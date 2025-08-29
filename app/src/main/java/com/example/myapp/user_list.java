package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class user_list extends AppCompatActivity {
    ListView userlistview;
    List<Map<String, String>> userlist = new ArrayList<>();
    DatabaseReference userRef;
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    SimpleAdapter adapter;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);
        setContentView(R.layout.activity_user_list);
        userlistview = findViewById(R.id.userListView);

        mauth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        adapter = new SimpleAdapter(this, userlist, R.layout.itemlist,
                new String[]{"name", "phone", "email"},
                new int[]{R.id.nametext, R.id.phonetext, R.id.emailtext});
        userlistview.setAdapter(adapter);
        fetchUsers();
    }

    private void fetchUsers() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String phone = userSnapshot.child("phone").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (name != null && phone != null && email != null) {
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("name", "Name" + name);
                        userMap.put("phone", "Phone" + phone);
                        userMap.put("email", "Email" + email);
                        userlist.add(userMap);


                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_list.this, Home.class));
                finish();
            }
        });
        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(user_list.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            // Already on profile; maybe show a toast
         startActivity(new Intent(user_list.this,user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(user_list.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(user_list.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }
}
