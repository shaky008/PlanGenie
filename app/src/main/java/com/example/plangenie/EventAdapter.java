package com.example.plangenie;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>
{
    Context context;
    ArrayList<ModelEvent> arrayList;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userId;

    public EventAdapter(Context context, ArrayList<ModelEvent> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design,parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, int position) {
        ModelEvent modelEvent = arrayList.get(position);
        holder.title.setText(modelEvent.getEventTopic());
        holder.date.setText(modelEvent.getEventDate());
        holder.time.setText(modelEvent.getEventTime());

                //opens a dialog box when clicked on each recycleVIew cardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    //holds references to the views that represent the item layout in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.eventTitleTextView);
            date= itemView.findViewById(R.id.dateTextView);
            time = itemView.findViewById(R.id.timeTextView);
        }
    }

    public void dialogMenu(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("The Event");
        builder.setMessage("What would you like to do with it?");
        builder.setCancelable(true);

        auth = FirebaseAuth.getInstance();
        //gets the id of the current user
        userId = auth.getCurrentUser().getUid();
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

                ArrayList arrayList1 = new ArrayList<>(arrayList);
                arrayList.clear();
                arrayList.addAll(arrayList1);
                notifyDataSetChanged();

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
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
}