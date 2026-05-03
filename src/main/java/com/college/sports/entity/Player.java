package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer eventId;
    private String dept;
    private String name;
    private String rollNo;
    private String year;

    public Player() {}
    public Player(Integer id, Integer eventId, String dept, String name, String rollNo, String year) { this.id = id; this.eventId = eventId; this.dept = dept; this.name = name; this.rollNo = rollNo; this.year = year; }
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public Integer getEventId() { return eventId; } public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getDept() { return dept; } public void setDept(String dept) { this.dept = dept; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getRollNo() { return rollNo; } public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getYear() { return year; } public void setYear(String year) { this.year = year; }
}
