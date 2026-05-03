package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Schedule {
    @Id
    private Integer eventId;
    private String date;
    private String time;
    private String venue;

    public Schedule() {}
    public Schedule(Integer eventId, String date, String time, String venue) { this.eventId = eventId; this.date = date; this.time = time; this.venue = venue; }
    public Integer getEventId() { return eventId; } public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getDate() { return date; } public void setDate(String date) { this.date = date; }
    public String getTime() { return time; } public void setTime(String time) { this.time = time; }
    public String getVenue() { return venue; } public void setVenue(String venue) { this.venue = venue; }
}
