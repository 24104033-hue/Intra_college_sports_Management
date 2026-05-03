package intracollegesportsmanagementsystem.department;

import intracollegesportsmanagementsystem.model.Event;
import intracollegesportsmanagementsystem.util.DataStore;
import java.util.*;

public class DepartmentLogin {
    public static void start() {
        DataStore.sc.nextLine();
        System.out.print("Dept: ");
        String d = DataStore.sc.nextLine();
        System.out.print("Password: ");
        String p = DataStore.sc.nextLine();

        if (!p.equals(d + "123")) {
            System.out.println("Invalid");
            return;
        }

        while (true) {
            System.out.println("\n1.Register Event\n2.View Points\n3.Logout");
            int ch = DataStore.sc.nextInt();

            switch (ch) {
                case 1:
                    registerEvent(d);
                    break;
                case 2:
                    System.out.println(d + " -> " + DataStore.points.getOrDefault(d, 0));
                    break;
                case 3:
                    return;
            }
        }
    }

    private static void registerEvent(String dept) {
        System.out.println("Available Events:");
        for (Event e : DataStore.events.values()) {
            if (!e.resultDeclared) { // Only show events that haven't had results declared
                System.out.println(e.id + " " + e.name + " (" + e.type + ")");
            }
        }

        System.out.print("Enter Event ID: ");
        int id = DataStore.sc.nextInt();
        DataStore.sc.nextLine();

        if (!DataStore.events.containsKey(id)) {
            System.out.println("Invalid");
            return;
        }

        Event event = DataStore.events.get(id);
        if (event.resultDeclared) {
            System.out.println("Result already declared for this event");
            return;
        }

        DataStore.registrations.putIfAbsent(id, new ArrayList<>());
        if (DataStore.registrations.get(id).contains(dept)) {
            System.out.println("Already registered");
            return;
        }
        DataStore.registrations.get(id).add(dept);

        DataStore.players.putIfAbsent(id, new HashMap<>());
        DataStore.players.get(id).put(dept, new ArrayList<>());

        int numPlayers = event.type.equals("Group") ? event.maxPlayers : 1;
        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("Enter details for Player " + i + ":");
            System.out.print("Player Name: ");
            String name = DataStore.sc.nextLine();
            System.out.print("Roll No: ");
            String roll = DataStore.sc.nextLine();
            System.out.print("Year: ");
            String year = DataStore.sc.nextLine();

            DataStore.players.get(id).get(dept).add(new String[]{name, roll, year});
            DataStore.write("register.txt", id + "|" + dept + "|" + DataStore.encode(name) + "|" + roll + "|" + year);
        }

        System.out.println("Registered");
    }
}
