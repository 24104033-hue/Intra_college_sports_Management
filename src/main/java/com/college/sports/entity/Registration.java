package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer eventId;
    private String dept;

    public Registration() {}
    public Registration(Integer id, Integer eventId, String dept) { this.id = id; this.eventId = eventId; this.dept = dept; }
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public Integer getEventId() { return eventId; } public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getDept() { return dept; } public void setDept(String dept) { this.dept = dept; }
}
