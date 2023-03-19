package com.example.plangenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {

    EditText fpName,fpEmail;
    Button fpConfirm;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        fpName = findViewById(R.id.forgotPpFullName);
        fpEmail = findViewById(R.id.fPpEmail);
        fpConfirm = findViewById(R.id.fPpConfirm);

        auth = FirebaseAuth.getInstance();

        fpConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailS = fpEmail.getText().toString().trim();
                String fullNameS = fpName.getText().toString().trim();

                if (fullNameS.isEmpty())
                {
                    fpName.setError("Name cannot be blank");
                }
                if (emailS.isEmpty())
                {
                    fpEmail.setError("Email cannot be blank");
                }
                else
                {
                    resetPassword(fullNameS, emailS);
                }
            }
        });
    }

    private void resetPassword(String fullNameS, String emailS) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //represents a particular location in your Firebase Database and can be used for reading or writing data to that location.
        DatabaseReference userRef = database.getReference("Users");
        //query functionality for specifying which documents you want to retrieve from a collection or collection group
        Query query = userRef.orderByChild("email").equalTo(emailS);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    //checks if the account detail exists or not
                    //check if fullname matches
                    for (DataSnapshot userSnapShot : snapshot.getChildren())
                    {
                        String name = userSnapShot.child("fullName").getValue(String.class);
//                         || Patterns.EMAIL_ADDRESS.matcher(emailS).matches()w
                        if (name != null && name.equals(fullNameS))
                        {
                            auth.sendPasswordResetEmail(emailS).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ForgetPassword.this, "password Reset link sent to email", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(ForgetPassword.this, "Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            //Name does not match
                            //display error in EditText
                            fpName.setError("Enter correct name");
                        }
                    }
                }
                else
                {
                    fpEmail.setError("enter correct email");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}