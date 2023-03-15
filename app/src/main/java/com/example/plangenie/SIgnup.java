package com.example.plangenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

//Signup class tht let user signup and create an account in the app
public class SIgnup extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText fullName,email, password;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.signupFullName);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        signupBtn = findViewById(R.id.signupButton);

        //condition to signup an account when pressing the signup button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validSignup();

            }
        });
    }

    //condition for validSignup
    public void validSignup()
    {
        //read txt from EditText
        String nameFull,emailS, passwordS;
        nameFull = fullName.getText().toString().trim();
        emailS = email.getText().toString().trim();
        passwordS = password.getText().toString().trim();

        //if fullname editText is empty, give error
        if (TextUtils.isEmpty(nameFull))
        {
            fullName.setError("Please enter your Name");
            fullName.requestFocus();
            return;
        }

        //if email editText is empty, give error
        if (TextUtils.isEmpty(emailS))
        {
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }

        //if email pattern is wrong, give error
        if (!Patterns.EMAIL_ADDRESS.matcher(emailS).matches())
        {
            email.setError("Enter valid email!");
            email.requestFocus();
            return;
        }

        //if password editText is empty, give error
        if (TextUtils.isEmpty(passwordS))
        {
            password.setError("Please enter password");
            password.requestFocus();
            return;
        }

        //if password's lengt is less than 6, give error
        if(passwordS.length() < 6)
        {
            password.setError("password should be more than 6 characters!");
        }

        //Create a new account by passing the new user's email address and password to createUserWithEmailAndPassword
        mAuth.createUserWithEmailAndPassword(emailS, passwordS)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(nameFull, emailS);
                            //store Users info, get ID for register user n set it to the object
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(SIgnup.this, "User sucessfully created an account", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(SIgnup.this, "Failed to signup. Please try again",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(SIgnup.this, "Failed to signup. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        startActivity(new Intent(this, LoginPage.class));

    }
}