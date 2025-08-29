package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.*;

import java.util.concurrent.TimeUnit;

public class phonelogin extends AppCompatActivity {

    EditText phoneInput;
    Button sendOtpBtn;
    FirebaseAuth mauth;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonelogin);

        mauth = FirebaseAuth.getInstance();
        phoneInput = findViewById(R.id.phoneInput);
        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneInput.getText().toString().trim();
                if (phone.isEmpty() || phone.length() < 10) {
                    phoneInput.setError("Enter a valid phone number");
                    return;
                }
                sendVerificationCode("+91" + phone);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mauth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    // Auto verification (optional: directly sign in the user here)
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(getApplicationContext(), "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String vId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(vId, token); // not required but good practice
                    verificationId = vId;
                    Intent intent = new Intent(phonelogin.this, otpverification.class);
                    intent.putExtra("verificationId", verificationId);
                    intent.putExtra("phone", phoneInput.getText().toString().trim());
                    startActivity(intent);
                }
            };
}
