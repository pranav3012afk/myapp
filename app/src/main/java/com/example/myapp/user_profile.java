package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_profile extends AppCompatActivity {
    TextView profilename, profileemail, profilephone;
    EditText newpassword;
    LinearLayout changepass, profilelogout, admindonation;
    FirebaseAuth mauth;
    FirebaseUser currentuser;
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    Button gotologinpage;
    DatabaseReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profilename = findViewById(R.id.profilename);
        profileemail = findViewById(R.id.profileemail);
        gotologinpage = findViewById(R.id.gotologinpage);
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);
        profilephone = findViewById(R.id.profilephone);
        newpassword = findViewById(R.id.newpassword);
        changepass = findViewById(R.id.changepass);
        profilelogout = findViewById(R.id.profilelogout);
        admindonation = findViewById(R.id.admindonation);

        mauth = FirebaseAuth.getInstance();
        currentuser = mauth.getCurrentUser();

        if (currentuser != null) {
            profileemail.setText("Email :" + currentuser.getEmail());
            userref = FirebaseDatabase.getInstance().getReference("Users").child(currentuser.getUid());

            userref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    profilename.setText("name :" + name);
                    profilephone.setText("phone:" + phone);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
        }

        gotologinpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(user_profile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass = newpassword.getText().toString().trim();
                if (TextUtils.isEmpty(newpass) || newpass.length() < 8) {
                    newpassword.setError("password must be greater than 8");
                    newpassword.setText("");
                    return;
                }

                if (currentuser != null) {
                    currentuser.updatePassword(newpass).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profilelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                Intent intent = new Intent(user_profile.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        admindonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_profile.this, Donation_History.class));
            }
        });

        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_profile.this, Home.class));
                finish();
            }
        });
        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(user_profile.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            // Already on profile; maybe show a toast
            Toast.makeText(this, "Already on Profile", Toast.LENGTH_SHORT).show();
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(user_profile.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(user_profile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }
}
