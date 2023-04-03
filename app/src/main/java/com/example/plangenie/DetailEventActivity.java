package com.example.plangenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailEventActivity extends AppCompatActivity {

    private EditText eventTopicEditText;
    private Switch showCalenderSwitch, setReminderFSwitch;
    private CalendarView calendarView;
    private TimePicker timePickerView;
    private Button cancelBtn, createBtn;

    final static int req1=1;
    public String a = "0";
    Date selectedDate1;
    private String selectedDate;
    private String selectedTime;
    Calendar calendar = Calendar.getInstance();


    private DatabaseReference eventsDatabaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        eventTopicEditText = findViewById(R.id.EventEditText);
        showCalenderSwitch = findViewById(R.id.switch_button);
        calendarView = findViewById(R.id.calendar_view);
        timePickerView = findViewById(R.id.timePickerView);
        timePickerView.setIs24HourView(true);
        cancelBtn = findViewById(R.id.cancel_button);
        createBtn = findViewById(R.id.create_button);
        calendarView.setVisibility(View.GONE);
        setReminderFSwitch = findViewById(R.id.reminderSwitch);



        //get reference to Users node in the database
        firebaseAuth = FirebaseAuth.getInstance();
        eventsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth
                .getCurrentUser().getUid());

        //when switched turned on, shows calender view and timepicker view
        calender();

        //goes back to home page
        cancel();

        create();

    }

    //shows/hide calenderView and timePicker view when switch is pressed
    public void calender()
    {
        showCalenderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    calendarView.setVisibility(View.VISIBLE);
                    timePickerView.setVisibility(View.VISIBLE);
                }
                else
                {
                    calendarView.setVisibility(View.GONE);
                    timePickerView.setVisibility(View.GONE);
                }
            }
        });
    }

    //when create button is pressed, goes back to home page and displays the created event
    public void create()
    {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTopic = eventTopicEditText.getText().toString().trim();     //stores string retrieved from editText
                if (!eventTopic.isEmpty())
                {
                    //if calender is switched on, show both time and date
                    if (showCalenderSwitch.isChecked())
                    {
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                                calendar.set(Calendar.YEAR, year);
//                                calendar.set(Calendar.MONTH, month);
//                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//                                selectedDate1 = calendar.getTime();
//                                selectedDate = String.valueOf(selectedDate1);
                                selectedDate = dayOfMonth + "/" + month + "/" + year;

                                timePickerView.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                    @Override
                                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                                        selectedTime = hourOfDay + ":" + minute;
                                    }
                                });
                            }
                        });

                        //save event data to the database if both time and date is selected
                        if (selectedDate != null && selectedTime != null)
                        {
                            //save event data to the database
                            saveEventToDatabase(selectedDate, selectedTime);
                            startActivity(new Intent(DetailEventActivity.this, MainActivity.class));
                            Toast.makeText(DetailEventActivity.this, "Sucessfully added", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(DetailEventActivity.this, "Select date and time please", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        //saves ot database if calender not selected
                        saveEventToDatabase(null, null);
                        startActivity(new Intent(DetailEventActivity.this, MainActivity.class));

                    }
                }
                else
                {
                    eventTopicEditText.setError("Please enter your event title");
                }
            }
        });
    }

    //creates new node in the user and save the eventTopic, Date and EventTime if selected to the database
    private void saveEventToDatabase(String selectedDate, String selectedTime)
    {
        //creates a new node called events for the user and get a reference to it
        DatabaseReference eventRef = eventsDatabaseReference.child("events").push();

        //create a map to store the event data
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventTopic", eventTopicEditText.getText().toString());
        if (selectedDate != null)
        {
            eventData.put("eventDate", selectedDate);
        }
        if (selectedTime != null)
        {
            eventData.put("eventTime", selectedTime);
        }
        eventRef.setValue(eventData);
    }

    //sets reminder for the selected date
//    private void setReminder(Calendar target)
//    {
//        if (setReminderFSwitch.isChecked())
//        {
//
//            Toast.makeText(this, "Reminder is set on", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(DetailEventActivity.this, ReminderBroadCast.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(DetailEventActivity.this, req1, i, 0);
//
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, target.getTimeInMillis(), pendingIntent);
//            a = "1";
//
////            calendar.set(selectedDate1);
//        }
//    }

    //Takes you back to homepage
    public void cancel()
    {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailEventActivity.this, MainActivity.class));
            }
        });
    }
}