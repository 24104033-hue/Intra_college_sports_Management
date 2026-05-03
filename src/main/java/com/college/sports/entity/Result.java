package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Result {
    @Id
    private Integer eventId;
    private String winnerDept;
    private String runnerDept;

    public Result() {}
    public Result(Integer eventId, String winnerDept, String runnerDept) { this.eventId = eventId; this.winnerDept = winnerDept; this.runnerDept = runnerDept; }
    public Integer getEventId() { return eventId; } public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getWinnerDept() { return winnerDept; } public void setWinnerDept(String winnerDept) { this.winnerDept = winnerDept; }
    public String getRunnerDept() { return runnerDept; } public void setRunnerDept(String runnerDept) { this.runnerDept = runnerDept; }
}
