package intracollegesportsmanagementsystem.admin;

import intracollegesportsmanagementsystem.model.Event;
import intracollegesportsmanagementsystem.model.Captain;
import intracollegesportsmanagementsystem.util.DataStore;

public class AdminLogin {
    public static void start() {
        if (!authenticate()) {
            System.out.println("Invalid");
            return;
        }

        while (true) {
            System.out.println("\n1.View Events\n2.View Captains\n3.View Final Result\n4.Logout");
            int ch = DataStore.sc.nextInt();

            switch (ch) {
                case 1:
                    viewEvents();
                    break;
                case 2:
                    viewCaptains();
                    break;
                case 3:
                    DataStore.printFinalResult();
                    break;
                case 4:
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
        return u.equals("admin") && p.equals("admin123");
    }

    private static void viewEvents() {
        for (Event e : DataStore.events.values()) {
            System.out.println("Event ID: " + e.id + ", Name: " + e.name + ", Type: " + e.type + ", Category: " + e.category);
        }
    }

    private static void viewCaptains() {
        for (Captain c : DataStore.captains.values()) {
            System.out.println("Dept: " + c.dept + ", Name: " + c.name + ", Year: " + c.year);
        }
    }
}
