package com.example.plangenie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;

RecyclerView recyclerView;
ArrayList<ModelEvent> arrayList = new ArrayList<>();
Button createEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab1);
        viewPager2 = findViewById(R.id.viewpager2);

        TabLayout.Tab homeTab = tabLayout.newTab();
        homeTab.setText("Home");
        tabLayout.addTab(homeTab);

        TabLayout.Tab completedTab = tabLayout.newTab();
        completedTab.setText("Completed");
        tabLayout.addTab(completedTab);

        TabLayout.Tab profileTab = tabLayout.newTab();
        profileTab.setText("Profile");
        tabLayout.addTab(profileTab);

        fragmentAdapter =new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Add a callback that will be invoked whenever the page changes
        // or is incrementally scrolled.
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


//        recyclerView = findViewById(R.id.recyclerView);
//        createEventBtn = findViewById(R.id.createEvent_btn);
//
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//        arrayList.add(new ModelEvent("Doctors appointment", "25th march", "2:00PM" ));
//
//
//        EventAdapter adapter = new EventAdapter(this, arrayList);
//        recyclerView.setAdapter(adapter);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        createEvent();



    }

    //jumps to DetailEventActivity where user can create an Event
//    public void createEvent()
//    {
//        createEventBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, DetailEventActivity.class);
//                startActivity(i);
//            }
//        });
//    }
}