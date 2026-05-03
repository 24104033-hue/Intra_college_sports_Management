package com.college.sports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Event {
    @Id
    private Integer id;
    private String name;
    private String type;
    private String category;
    private Integer winPts;
    private Integer runPts;
    private Integer maxPlayers;
    private boolean resultDeclared = false;

    public Event() {}
    public Event(Integer id, String name, String type, String category, Integer winPts, Integer runPts, Integer maxPlayers, boolean resultDeclared) {
        this.id = id; this.name = name; this.type = type; this.category = category; this.winPts = winPts; this.runPts = runPts; this.maxPlayers = maxPlayers; this.resultDeclared = resultDeclared;
    }
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getType() { return type; } public void setType(String type) { this.type = type; }
    public String getCategory() { return category; } public void setCategory(String category) { this.category = category; }
    public Integer getWinPts() { return winPts; } public void setWinPts(Integer winPts) { this.winPts = winPts; }
    public Integer getRunPts() { return runPts; } public void setRunPts(Integer runPts) { this.runPts = runPts; }
    public Integer getMaxPlayers() { return maxPlayers; } public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }
    public boolean isResultDeclared() { return resultDeclared; } public void setResultDeclared(boolean resultDeclared) { this.resultDeclared = resultDeclared; }
}
