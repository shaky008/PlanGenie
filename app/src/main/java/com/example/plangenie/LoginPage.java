package com.example.plangenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

//Login class that lets user use the app only if they have signed up and created an account

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText loginEmail, loginPassword;
    private TextView signup, forgotPassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        signup = findViewById(R.id.loginSignUp);
        loginBtn = findViewById(R.id.LoginButton);
        forgotPassword = findViewById(R.id.loginForgotPassword);

        //opens signup activity
        signup.setOnClickListener(this);

        //opens main activity
        loginBtn.setOnClickListener(this);

        //opens forgot password activity
        forgotPassword.setOnClickListener(this);



    }

    //opens other activity when clicked
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //opens signup page when clicked on "new? Register a new account"
            case R.id.loginSignUp:
                startActivity(new Intent(this, SIgnup.class));
                break;
            //opens MainAcivity page when clicked on the Login button
            case R.id.LoginButton:
                validLogin();
                break;

            case R.id.loginForgotPassword:
                startActivity(new Intent(this, ForgetPassword.class));
                break;
        }
    }

    //validate if the user has an account or not
    private void validLogin()
    {
        String emailString = loginEmail.getText().toString().trim();
        String passwordString = loginPassword.getText().toString().trim();

        if (emailString.isEmpty())
        {
            loginEmail.setError("Email is required");
            loginEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches())
        {
            loginEmail.setError("Please enter valid email");
            loginEmail.requestFocus();
            return;
        }

        if (passwordString.isEmpty())
        {
            loginPassword.setError("Please enter valid password");
            loginPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(LoginPage.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(LoginPage.this, "Failed, please enter correct password and email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}