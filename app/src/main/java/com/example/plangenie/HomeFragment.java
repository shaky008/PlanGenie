package com.example.plangenie;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment
{

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<ModelEvent> arrayList;
    private Button createEventBtn;

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

        //opens dialog box when clicked on an element in cardview in recycleView
        eventAdapter.setOnClickListener(new EventAdapter.OnclickListener() {
            @Override
            public void onClick(int position, ModelEvent modelEvent)
            {
                dialogMenu(position);
            }
        });


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

    public void dialogMenu(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("The Event");
        builder.setMessage("What would you like to do with it?");
        builder.setCancelable(true);

        mAuth = FirebaseAuth.getInstance();
        //gets the id of the current user
        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        //creates a new node called events for the user and get a reference to it
        DatabaseReference eventRef = databaseReference.child("completed").push();

        //create a map to store the event data
        Map<String, Object> eventData = new HashMap<>();


//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot eventSnapshot : snapshot.getChildren())
//                {
//                    eventId = eventSnapshot.getKey();
//                    System.out.println("----------------------------" + eventId + "----------------------------");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        //sends the selected event inside events node to a new node called completed and deletes from the events node in firebase database
        builder.setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventData.put("eventTopic", arrayList.get(position).getEventTopic());
                if ( arrayList.get(position).getEventDate() != null)
                {
                    eventData.put("eventDate", arrayList.get(position).getEventDate());
                }
                if (arrayList.get(position).getEventTime() != null)
                {
                    eventData.put("eventTime", arrayList.get(position).getEventTime());
                }
                eventRef.setValue(eventData);
            }
        });

        //deletes the selected event from the events node in firebase database
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("Users").child(userId).child("events").orderByChild("eventTopic").equalTo(arrayList.get(position).getEventTopic());

//                eventAdapter.notifyItemRemoved(position);
//                eventAdapter.notifyItemRangeChanged(position, arrayList.size());

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        arrayList.remove(position);
                        eventAdapter.notifyItemRemoved(position);
                        eventAdapter.notifyDataSetChanged();

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });


            }
        });


        builder.create().show();

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