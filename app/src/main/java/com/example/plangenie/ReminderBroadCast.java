package com.example.plangenie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReminderBroadCast extends BroadcastReceiver
{
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String userId;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("events");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get data from snapshot
                String title = snapshot.child("eventTopic").getValue(String.class);

                getNotification(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            private void getNotification(String title)
            {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMe")
                        .setSmallIcon(R.drawable.plange)
                        .setContentTitle(title)
                        .setContentText("Hey don't forget about...")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                notificationManager.notify(200, builder.build());
            }
        });

    }
}
