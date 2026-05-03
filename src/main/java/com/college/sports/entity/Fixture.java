package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Fixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer eventId;
    private String matchDetails;

    public Fixture() {}
    public Fixture(Integer id, Integer eventId, String matchDetails) { this.id = id; this.eventId = eventId; this.matchDetails = matchDetails; }
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public Integer getEventId() { return eventId; } public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getMatchDetails() { return matchDetails; } public void setMatchDetails(String matchDetails) { this.matchDetails = matchDetails; }
}
