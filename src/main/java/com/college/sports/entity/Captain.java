package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Captain {
    @Id
    private String dept;
    private String name;
    private Integer year;

    public Captain() {}
    public Captain(String dept, String name, Integer year) { this.dept = dept; this.name = name; this.year = year; }
    public String getDept() { return dept; } public void setDept(String dept) { this.dept = dept; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public Integer getYear() { return year; } public void setYear(Integer year) { this.year = year; }
}
