package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class contact_us extends AppCompatActivity {
    EditText etName, etMobile, etAddress, etMessage;
    Button etSubmit;
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etAddress = findViewById(R.id.etAddress);
        etMessage = findViewById(R.id.etMessage);
        etSubmit = findViewById(R.id.etSubmit);

        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);

        mauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("ContactData");

        etSubmit.setOnClickListener(v -> contactData());

        // Moved these from contactData() to here
        homeicon.setOnClickListener(v -> {
            startActivity(new Intent(contact_us.this, Home.class));
            finish();
        });

        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(contact_us.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            startActivity(new Intent(contact_us.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(contact_us.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(contact_us.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void contactData() {
        String name = etName.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            etMobile.setError("Mobile number is required");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Address is required");
            return;
        }
        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Message is required");
            return;
        }

        String id = databaseReference.push().getKey();
        HashMap<String, String> contactMap = new HashMap<>();
        contactMap.put("Name", name);
        contactMap.put("mobile", mobile);
        contactMap.put("address", address);
        contactMap.put("message", message);

        assert id != null;
        databaseReference.child(id).setValue(contactMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etMobile.setText("");
                    etAddress.setText("");
                    etMessage.setText("");
                });
    }
}
