package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Point {
    @Id
    private String dept;
    private Integer score;

    public Point() {}
    public Point(String dept, Integer score) { this.dept = dept; this.score = score; }
    public String getDept() { return dept; } public void setDept(String dept) { this.dept = dept; }
    public Integer getScore() { return score; } public void setScore(Integer score) { this.score = score; }
}
