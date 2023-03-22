package com.example.plangenie;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ModelEvent> arrayList = new ArrayList<>();
    Button createEventBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        createEventBtn = view.findViewById(R.id.createEvent_btn);

        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));

        EventAdapter adapter = new EventAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        createEvent();

        return view;
    }

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