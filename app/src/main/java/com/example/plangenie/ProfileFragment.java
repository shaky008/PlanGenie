package com.example.plangenie;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private TextView nameTextView, emailTextView;
    private Button editBtn, logoutBtn;

    private String userId;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextview);
        editBtn = view.findViewById(R.id.editButton);
        logoutBtn = view.findViewById(R.id.logoutButton);

        //get the uid of the selected user
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists())
               {
                   User user = snapshot.getValue(User.class);
                   if(user != null)
                   {
                       String name = user.getFullName();
                       String email = user.getEmail();

                       //sets the name and the email from the database of the loggedin user
                       nameTextView.setText(name);
                       emailTextView.setText(email);
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    return view;
    }

    //logouts the user from the app
    public void logout()
    {
        mAuth.signOut();
        Toast.makeText(getActivity(), "Signout successful", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), LoginPage.class);
        startActivity(i);
    }
}