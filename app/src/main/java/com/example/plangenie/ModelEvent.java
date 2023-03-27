package com.example.plangenie;

public class ModelEvent
{
    private String eventTopic;
    private String eventDate;
    private String eventTime;

    //default constructor
    public ModelEvent()
    {

    }

    //parametized constructor
    public ModelEvent(String newDate, String newTime, String newTitle)
    {
        this.eventDate = newDate;
        this.eventTime = newTime;
        this.eventTopic = newTitle;

    }

    //getters and setters
    public String getEventTopic() {
        return eventTopic;
    }

    public void setEventTopic(String eventTopic) {
        this.eventTopic = eventTopic;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
