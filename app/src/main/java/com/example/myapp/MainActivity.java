package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email,password;

    Button loginbtn,guestbtn,loginwithmobilebtn,guestloginbtn;
    TextView redirectToRegister;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        email= findViewById(R.id.useremail);
        password=findViewById(R.id.userpassword);
        loginbtn=findViewById(R.id.loginbtn);
        guestloginbtn = findViewById(R.id.guestloginbtn);
        guestbtn=findViewById(R.id.guestloginbtn);
        loginwithmobilebtn=findViewById(R.id.loginwithmobile);
        redirectToRegister=findViewById(R.id.signup);
        mauth =FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=email.getText().toString();
                String userpassword=password.getText().toString();
                if (useremail.equals("admin") && userpassword.equals("12345678")) {
                    startActivity(new Intent(MainActivity.this, AdminPanel.class));

                }
                else {
                    mauth.signInWithEmailAndPassword(useremail, userpassword).
                            addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, Home.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        redirectToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,signup.class);
                startActivity(intent);
                finish();

            }
        });
        guestloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Home.class));
            }
        });
        }


}