package com.example.plangenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogBox extends AppCompatActivity {

    private Button completeBtn;
    private Button deleteBtn;
    private Button cancelBtn;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_box);

        completeBtn = findViewById(R.id.completeDialogBtn);
        cancelBtn = findViewById(R.id.cancelDialogBtn);
        deleteBtn = findViewById(R.id.deleteDialogBtn);

        //get reference to Users node in the database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());

        onComplete();
        onCancel();
    }

    //sends the data from home recycleView to completed recycleView
    //deletes node from the event node and creates a new node called completed
    public void onComplete()
    {
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DialogExtra", "Complete button clicked");
                Toast.makeText(DialogBox.this, "working", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCancel()
    {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DialogBox.this, HomeFragment.class));
            }
        });
    }
}