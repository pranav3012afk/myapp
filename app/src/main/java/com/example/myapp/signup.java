package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {
    EditText name,email,password,mobile;
    Button signupbtn;
    TextView signupno;
    private FirebaseAuth mauth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name=findViewById(R.id.signupname);
        email=findViewById(R.id.signupemail);
        password=findViewById(R.id.signuppassword);
        mobile=findViewById(R.id.signupmobile);
        signupbtn=findViewById(R.id.signbtn);
        signupno=findViewById(R.id.signupno);
        mauth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
        signupno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void signUpUser()
    {
        String sName = name.getText().toString().trim();
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();
        String sMobile = mobile.getText().toString().trim();

        if(TextUtils.isEmpty(sName)){
                name.setError("name is required");
                return;
        }
        if(TextUtils.isEmpty(sEmail)){
            email.setError("email is required");
            return;
        }
        if(TextUtils.isEmpty(sPassword)){
            password.setError("password is required");
            return;
        }
        if(TextUtils.isEmpty(sMobile)){
            mobile.setError("mobile number is required ");
            return;
        }
        if(password.length()<6){
            password.setError("password lenght should be equal to 6 or more");
        }

        mauth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user=mauth.getCurrentUser();
                        if(user!= null){
                            String userid = user.getUid();
                            HashMap<String,String> userdata = new HashMap<>();
                            userdata.put("name",sName);
                            userdata.put("email",sEmail);
                            userdata.put("mobile",sMobile);

                            databaseReference.child(userid).setValue(userdata).
                                    addOnCompleteListener(task2->{
                                        if(task2.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"registered successfully",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(signup.this,Home.class));
                                            finish();

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"registrastion failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}