package intracollegesportsmanagementsystem.util;

import intracollegesportsmanagementsystem.model.Event;
import intracollegesportsmanagementsystem.model.Captain;
import java.util.*;
import java.io.*;

public class DataStore {
    public static Scanner sc = new Scanner(System.in);
    public static Map<Integer, Event> events = new HashMap<>();
    public static Map<String, Integer> points = new HashMap<>();
    public static Map<String, Captain> captains = new HashMap<>();
    public static Map<Integer, List<String>> registrations = new HashMap<>();
    public static Map<Integer, List<String>> fixtures = new HashMap<>();
    public static Map<Integer, Map<String, List<String[]>>> players = new HashMap<>(); // eventId -> dept -> list of [name, roll, year]
    public static String[] depts = {"CSE", "IT", "AIDS", "ECE", "EEE", "CIVIL", "MECH"};

    static {
        init();
    }

    public static void init() {
        ensureFile("events.txt");
        ensureFile("schedule.txt");
        ensureFile("fixtures.txt");
        ensureFile("winner.txt");
        ensureFile("register.txt");
        ensureFile("points.txt");
        ensureFile("captains.txt");

        loadPoints();
        loadEvents();
        loadCaptains();
        loadRegistrations();
        loadFixtures();
        loadPlayers();
        ensureDefaultPoints();
    }

    private static void ensureFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    private static List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException ignored) {
        }
        return lines;
    }

    public static String encode(String value) {
        return value == null ? "" : value.replace("|", " ");
    }

    public static void write(String file, String data) {
        ensureFile(file);
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(data + "\n");
        } catch (IOException ignored) {
        }
    }

    public static void savePoints() {
        ensureFile("points.txt");
        try (FileWriter fw = new FileWriter("points.txt")) {
            for (Map.Entry<String, Integer> entry : points.entrySet()) {
                fw.write(entry.getKey() + "|" + entry.getValue() + "\n");
            }
        } catch (IOException ignored) {
        }
    }

    public static void printFinalResult() {
        String winDept = "";
        int max = -1;
        for (Map.Entry<String, Integer> entry : points.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                winDept = entry.getKey();
            }
        }
        System.out.println("Overall Winner: " + winDept);
    }

    private static void ensureDefaultPoints() {
        for (String d : depts) {
            points.putIfAbsent(d, 0);
        }
    }

    private static void loadPoints() {
        for (String line : readLines("points.txt")) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 2) {
                try {
                    points.put(parts[0], Integer.parseInt(parts[1]));
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    private static void loadEvents() {
        for (String line : readLines("events.txt")) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 7) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String type = parts[2];
                    String category = parts[3];
                    int winPts = Integer.parseInt(parts[4]);
                    int runPts = Integer.parseInt(parts[5]);
                    int maxPlayers = Integer.parseInt(parts[6]);
                    Event event = new Event(id, name, type, category, winPts, runPts, maxPlayers);
                    events.put(id, event);
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    private static void loadCaptains() {
        for (String line : readLines("captains.txt")) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 3) {
                try {
                    int year = Integer.parseInt(parts[2]);
                    captains.put(parts[0], new Captain(parts[1], parts[0], year));
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    private static void loadRegistrations() {
        for (String line : readLines("register.txt")) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 5) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    registrations.putIfAbsent(id, new ArrayList<>());
                    registrations.get(id).add(parts[1]);
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    private static void loadPlayers() {
        for (String line : readLines("register.txt")) {
            String[] parts;
            if (line.contains("|")) {
                parts = line.split("\\|", -1);
            } else {
                // Old format: Event ID: 1, Dept: cse, Name: Krishna, Roll No: 21010, Year: 2
                String[] temp = line.split(", ");
                parts = new String[5];
                for (String t : temp) {
                    if (t.startsWith("Event ID: ")) parts[0] = t.substring(10);
                    else if (t.startsWith("Dept: ")) parts[1] = t.substring(6);
                    else if (t.startsWith("Name: ")) parts[2] = t.substring(6);
                    else if (t.startsWith("Roll No: ")) parts[3] = t.substring(9);
                    else if (t.startsWith("Year: ")) parts[4] = t.substring(6);
                }
            }
            if (parts.length >= 5 && parts[0] != null) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    String dept = parts[1];
                    String name = parts[2];
                    String roll = parts[3];
                    String year = parts[4];
                    players.putIfAbsent(id, new HashMap<>());
                    players.get(id).putIfAbsent(dept, new ArrayList<>());
                    players.get(id).get(dept).add(new String[]{name, roll, year});
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    private static void loadFixtures() {
        for (String line : readLines("fixtures.txt")) {
            String[] parts;
            if (line.contains("|")) {
                parts = line.split("\\|", -1);
            } else {
                // Old format: Event ID: 1, Match: CSE vs EEE
                String[] temp = line.split(", ");
                parts = new String[2];
                for (String t : temp) {
                    if (t.startsWith("Event ID: ")) parts[0] = t.substring(10);
                    else if (t.startsWith("Match: ")) parts[1] = t.substring(7);
                }
            }
            if (parts.length >= 2 && parts[0] != null && parts[1] != null) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    fixtures.putIfAbsent(id, new ArrayList<>());
                    fixtures.get(id).add(parts[1]);
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }
}
