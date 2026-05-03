package intracollegesportsmanagementsystem.coordinator;

import intracollegesportsmanagementsystem.model.Event;
import intracollegesportsmanagementsystem.model.Captain;
import intracollegesportsmanagementsystem.util.DataStore;
import java.util.*;

public class CoordinatorLogin {
    public static void start() {
        if (!authenticate()) {
            System.out.println("Invalid!");
            return;
        }

        while (true) {
            System.out.println("\n1.Add Event\n2.Add Schedule\n3.Create Fixtures\n4.View Fixtures\n5.Add Result\n6.View Points\n7.Add Captain\n8.Final Result\n9.Logout");
            int ch = DataStore.sc.nextInt();

            switch (ch) {
                case 1:
                    addEvent();
                    break;
                case 2:
                    addSchedule();
                    break;
                case 3:
                    createFixtures();
                    break;
                case 4:
                    viewFixtures();
                    break;
                case 5:
                    addResult();
                    break;
                case 6:
                    viewPoints();
                    break;
                case 7:
                    addCaptain();
                    break;
                case 8:
                    DataStore.printFinalResult();
                    break;
                case 9:
                    return;
            }
        }
    }

    private static boolean authenticate() {
        DataStore.sc.nextLine();
        System.out.print("Username: ");
        String u = DataStore.sc.nextLine();
        System.out.print("Password: ");
        String p = DataStore.sc.nextLine();
        return u.equals("coord") && p.equals("coord123");
    }

    private static void addEvent() {
        System.out.print("Event ID: ");
        int id = DataStore.sc.nextInt();
        DataStore.sc.nextLine();

        System.out.print("Name: ");
        String name = DataStore.sc.nextLine();

        System.out.print("Type(Group/Individual): ");
        String type = DataStore.sc.nextLine();

        System.out.print("Category(Men/Women): ");
        String category = DataStore.sc.nextLine();

        System.out.print("Winner Points: ");
        int w = DataStore.sc.nextInt();

        System.out.print("Runner Points: ");
        int r = DataStore.sc.nextInt();

        System.out.print("Max Players per Team: ");
        int maxPlayers = DataStore.sc.nextInt();

        Event e = new Event(id, name, type, category, w, r, maxPlayers);
        DataStore.events.put(id, e);
        DataStore.write("events.txt","Event Id: " + id + ", Name: " + DataStore.encode(name) + ", Type: " + DataStore.encode(type) + ", Category: " + DataStore.encode(category) + ", Winner Points: " + w + ", Runner Points: " + r + ", Max Players: " + maxPlayers);
        System.out.println("Event Added");
    }

    private static void addSchedule() {
        System.out.print("Event ID: ");
        int id = DataStore.sc.nextInt();
        DataStore.sc.nextLine();

        if (!DataStore.events.containsKey(id)) {
            System.out.println("Invalid Event");
            return;
        }

        System.out.print("Date: ");
        String date = DataStore.sc.nextLine();
        System.out.print("Time: ");
        String time = DataStore.sc.nextLine();
        System.out.print("Venue: ");
        String venue = DataStore.sc.nextLine();

        DataStore.write("schedule.txt", "Event ID: " + id + ", Date: " + DataStore.encode(date) + ", Time: " + DataStore.encode(time) + ", Venue: " + DataStore.encode(venue));
        System.out.println("Schedule Added");
    }

    private static void createFixtures() {
        System.out.print("Event ID: ");
        int id = DataStore.sc.nextInt();
        DataStore.sc.nextLine();

        // Validate event exists
        if (!DataStore.events.containsKey(id)) {
            System.out.println("Invalid Event");
            return;
        }

        Event event = DataStore.events.get(id);

        // Check if result is already declared for this event
        if (event.resultDeclared) {
            System.out.println("Cannot create fixtures - Result already declared for this event");
            return;
        }

        // Check if registrations exist for this event ID
        if (!DataStore.registrations.containsKey(id)) {
            System.out.println("No registrations for this event");
            return;
        }

        List<String> registeredDepts = DataStore.registrations.get(id);
        if (registeredDepts.isEmpty()) {
            System.out.println("No departments registered for event ID: " + id);
            return;
        }

        System.out.println("Event: " + event.name + " (Event ID: " + id + ")");
        System.out.println("Registered Departments for this event:");
        for (String dept : registeredDepts) {
            System.out.println("- " + dept);
        }

        System.out.print("Enter number of matches to create: ");
        int numMatches = DataStore.sc.nextInt();
        DataStore.sc.nextLine();

        List<String> f = new ArrayList<>();

        for (int i = 1; i <= numMatches; i++) {
            System.out.println("Match " + i + ":");
            System.out.print("Enter first department: ");
            String dept1 = DataStore.sc.nextLine().toUpperCase();
            System.out.print("Enter second department: ");
            String dept2 = DataStore.sc.nextLine().toUpperCase();

            if (!registeredDepts.contains(dept1) || !registeredDepts.contains(dept2)) {
                System.out.println("One or both departments are not registered for event ID " + id + ". Skipping this match.");
                i--; // Retry this match
                continue;
            }

            if (dept1.equals(dept2)) {
                System.out.println("Cannot have a department play against itself. Skipping this match.");
                i--; // Retry this match
                continue;
            }

            String match = dept1 + " vs " + dept2;
            f.add(match);
            DataStore.write("fixtures.txt", id + "|" + DataStore.encode(match));
        }

        DataStore.fixtures.put(id, f);
        System.out.println("Fixtures Created for Event ID: " + id);
    }

    private static void viewFixtures() {
        System.out.print("Event ID: ");
        int id = DataStore.sc.nextInt();

        // Validate event exists
        if (!DataStore.events.containsKey(id)) {
            System.out.println("Invalid Event");
            return;
        }

        if (!DataStore.fixtures.containsKey(id)) {
            System.out.println("No Fixtures for Event ID: " + id);
            return;
        }

        Event event = DataStore.events.get(id);
        System.out.println("\nFixtures for Event ID: " + id + " - " + event.name);
        System.out.println("================================");
        for (String s : DataStore.fixtures.get(id)) {
            System.out.println(s);
        }
        System.out.println("================================");
    }

    private static void addResult() {
        System.out.print("Event ID: ");
        int id = DataStore.sc.nextInt();
        DataStore.sc.nextLine();

        if (!DataStore.events.containsKey(id)) {
            System.out.println("Invalid");
            return;
        }

        Event e = DataStore.events.get(id);
        if (e.resultDeclared) {
            System.out.println("Already declared");
            return;
        }

        System.out.print("Winner Dept: ");
        String w = DataStore.sc.nextLine();
        System.out.print("Runner Dept: ");
        String r = DataStore.sc.nextLine();

        DataStore.points.put(w, DataStore.points.getOrDefault(w, 0) + e.winPts);
        DataStore.points.put(r, DataStore.points.getOrDefault(r, 0) + e.runPts);
        e.resultDeclared = true;

        // Write detailed result to winner.txt
        StringBuilder result = new StringBuilder();
        result.append("Event ID: ").append(id).append(", Event Name: ").append(DataStore.encode(e.name))
              .append(", Category: ").append(DataStore.encode(e.category)).append(", Winner: ").append(DataStore.encode(w))
              .append(", Runner: ").append(DataStore.encode(r)).append("\n");

        // Add winner team players
        if (DataStore.players.containsKey(id) && DataStore.players.get(id).containsKey(w)) {
            result.append("Winner Team Players:\n");
            for (String[] player : DataStore.players.get(id).get(w)) {
                result.append("Name: ").append(DataStore.encode(player[0])).append(", Roll No: ").append(player[1])
                      .append(", Year: ").append(player[2]).append("\n");
            }
        }

        // Add runner team players
        if (DataStore.players.containsKey(id) && DataStore.players.get(id).containsKey(r)) {
            result.append("Runner Team Players:\n");
            for (String[] player : DataStore.players.get(id).get(r)) {
                result.append("Name: ").append(DataStore.encode(player[0])).append(", Roll No: ").append(player[1])
                      .append(", Year: ").append(player[2]).append("\n");
            }
        }

        DataStore.write("winner.txt", result.toString().trim());
        DataStore.savePoints();
        System.out.println("Result Added");
    }

    private static void viewPoints() {
        for (Map.Entry<String, Integer> entry : DataStore.points.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    private static void addCaptain() {
        DataStore.sc.nextLine();
        System.out.print("Dept: ");
        String d = DataStore.sc.nextLine();

        if (DataStore.captains.containsKey(d)) {
            System.out.println("Already assigned");
            return;
        }

        System.out.print("Name: ");
        String n = DataStore.sc.nextLine();
        System.out.print("Year: ");
        int y = DataStore.sc.nextInt();

        DataStore.captains.put(d, new Captain(n, d, y));
        DataStore.write("captains.txt", "Dept: " + d + ", Name: " + DataStore.encode(n) + ", Year: " + y);
        System.out.println("Captain Added");
    }
}
