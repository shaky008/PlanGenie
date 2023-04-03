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


    private OnclickListener onClickListener;

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public EventAdapter(Context context, ArrayList<ModelEvent> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void setData(ArrayList<ModelEvent> newData) {
        this.arrayList.clear();
        arrayList.addAll(newData);
        notifyDataSetChanged();
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

                if (onClickListener != null)
                {
                    onClickListener.onClick(holder.getAdapterPosition(), modelEvent);
                }
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

    public interface OnclickListener
    {
        void onClick(int position, ModelEvent modelEvent);
    }
}