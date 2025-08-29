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

public class volenteer extends AppCompatActivity {
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    Button btSubmit;
    EditText entername, enternumber, enteraddress, entermessage;
    FirebaseAuth mauth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volenteer); // Moved to top

        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);

        btSubmit = findViewById(R.id.btSubmit);
        entername = findViewById(R.id.entername);
        enternumber = findViewById(R.id.enternumber);
        enteraddress = findViewById(R.id.enteraddress);
        entermessage = findViewById(R.id.entermessage);

        mauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("volenteerdata");

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volenteerdata();
            }
        });

        homeicon.setOnClickListener(v -> {
            startActivity(new Intent(volenteer.this, Home.class));
            finish();
        });

        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(volenteer.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            startActivity(new Intent(volenteer.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(volenteer.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(volenteer.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void volenteerdata() {
        String name = entername.getText().toString().trim();
        String number = enternumber.getText().toString().trim();
        String address = enteraddress.getText().toString().trim();
        String message = entermessage.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            entername.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(number)) {
            enternumber.setError("Mobile number is required");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            enteraddress.setError("Address is required");
            return;
        }
        if (TextUtils.isEmpty(message)) {
            entermessage.setError("Message is required");
            return;
        }

        String id = databaseReference.push().getKey();
        HashMap<String, String> contactMap = new HashMap<>();
        contactMap.put("Name", name);
        contactMap.put("mobile", number);
        contactMap.put("address", address);
        contactMap.put("message", message);

        assert id != null;
        databaseReference.child(id).setValue(contactMap).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
            entername.setText("");
            enternumber.setText("");
            enteraddress.setText("");
            entermessage.setText("");
        });
    }
}
