package com.example.plangenie;

public class ModelEvent
{
    private String title;
    private String date;
    private String time;

    //default constructor
    public ModelEvent()
    {

    }

    //parametized constructor
    public ModelEvent(String newTitle, String newDate, String newTime)
    {
        this.title = newTitle;
        this.date = newDate;
        this.time = newTime;
    }

    //getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
