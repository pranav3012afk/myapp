package com.example.myapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapp.Home;
import com.example.myapp.R;
import com.google.firebase.auth.*;


public class otpverification extends AppCompatActivity {


    EditText otpInput;
    Button verifyOtp;


    FirebaseAuth mauth;
    String verificationId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.myapp.R.layout.activity_otpverification);


        otpInput = findViewById(R.id.otpInput);
        verifyOtp = findViewById(R.id.verifyBtn);
        mauth = FirebaseAuth.getInstance();
        verificationId = getIntent().getStringExtra("verificationId");
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpInput.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otpInput.setError("Enter a valid otp");
                    return;
                }
                verifyCode(code);
            }
        });
    }
    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(task->{
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(otpverification.this, Home.class));
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
