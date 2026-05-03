package intracollegesportsmanagementsystem.model;

public class Event {
    public int id;
    public String name;
    public String type;
    public String category;
    public int winPts;
    public int runPts;
    public int maxPlayers; // Number of players per team
    public boolean resultDeclared = false;

    public Event(int id, String name, String type, String category, int winPts, int runPts, int maxPlayers) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.winPts = winPts;
        this.runPts = runPts;
        this.maxPlayers = maxPlayers;
    }
}
