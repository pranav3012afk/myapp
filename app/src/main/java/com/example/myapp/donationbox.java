package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class donationbox extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "DonationBoxActivity";

    SeekBar amountseekbar;
    TextView selectedamount, welcome;
    EditText Dmessage;
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    Button donatebtn;
    int selectedvalue = 0;
    FirebaseAuth mauth;
    DatabaseReference databaseRef;

    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donationbox);


        amountseekbar = findViewById(R.id.amountseekbar);
        selectedamount = findViewById(R.id.selectedamount);
        welcome = findViewById(R.id.welcome);
        Dmessage = findViewById(R.id.Dmessage);
        donatebtn = findViewById(R.id.donatebtn);
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);

        mauth = FirebaseAuth.getInstance(); // moved here before logoutclick
        FirebaseUser user = mauth.getCurrentUser();

        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(donationbox.this, Home.class));
                finish();
            }
        });
        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(donationbox.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            startActivity(new Intent(donationbox.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(donationbox.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(donationbox.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        if(user != null){
            String userid = user.getUid();
            databaseRef = FirebaseDatabase.getInstance().getReference("users");
            databaseRef.child(userid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userName = snapshot.getValue(String.class);
                    if(userName != null) {
                        welcome.setText("Welcome , " + userName + "!");
                    } else {
                        welcome.setText("Welcome!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    welcome.setText("Welcome!");
                }
            });
        }

        amountseekbar.setMax(100);

        amountseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedvalue = progress;
                selectedamount.setText("INR " + selectedvalue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedvalue==0) {
                    Toast.makeText(getApplicationContext(),"Please select an amount",Toast.LENGTH_LONG).show();
                    return;
                }
                String message = Dmessage.getText().toString().trim();
                startPayment(userName,selectedvalue,message);
            }
        });
    }

    private void startPayment(String name, int amount, String message) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_Xgkf8GeyQcs3WV");
        checkout.setImage(R.drawable.img_1);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "NGO Donation Box");
            options.put("description", "Donation Payment");
            options.put("image", "https://your_logo_url.png");
            options.put("theme.color", "#00796B");
            options.put("currency", "INR");
            options.put("amount", amount * 100);
            options.put("prefill.email", "sastepranav75@gmail.com");
            options.put("prefill.contact", "+91 8489965599");

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        String message = Dmessage.getText().toString().trim();
        int amount = selectedvalue;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Donations").child(userId);
        String donationId = dbref.push().getKey();
        if(donationId != null) {
            HashMap<String,Object> donationData = new HashMap<>();
            donationData.put("name",userName);
            donationData.put("amount",amount);
            donationData.put("message",message);
            donationData.put("date",currentDate);
            dbref.child(donationId).setValue(donationData).addOnSuccessListener(unused -> {
                Toast.makeText(getApplicationContext(),"Payment Successful",Toast.LENGTH_LONG).show();
            });
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(getApplicationContext(),"Payment Failed",Toast.LENGTH_LONG).show();
    }
}
