package com.example.plangenie;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    ArrayList<ModelEvent> arrayList;
    Button createEventBtn;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private  FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        createEventBtn = view.findViewById(R.id.createEvent_btn);

        // initializes Firebase Authentication and gets the current user's unique ID
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userId = firebaseUser.getUid();

        //get reference to the database
        firebaseDatabase = FirebaseDatabase.getInstance();
        //point the location "Users/events" in the database.
        databaseReference = firebaseDatabase.getReference("Users").child(userId).child("events");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        eventAdapter = new EventAdapter(getContext(), arrayList);
        recyclerView.setAdapter(eventAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                        ModelEvent event = dataSnapshot.getValue(ModelEvent.class);
                        arrayList.add(event);
                }

                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

        createEvent();

        return view;
    }

    //send to another activity where user can create their event
    public void createEvent()
    {
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailEventActivity.class);
                startActivity(i);
            }
        });
    }
}