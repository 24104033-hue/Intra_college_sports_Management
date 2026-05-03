package intracollegesportsmanagementsystem;

import intracollegesportsmanagementsystem.admin.AdminLogin;
import intracollegesportsmanagementsystem.coordinator.CoordinatorLogin;
import intracollegesportsmanagementsystem.department.DepartmentLogin;
import intracollegesportsmanagementsystem.util.DataStore;

public class IntracollegeSportsManagementSystem {
    public static void main(String[] args) {
        for (String d : DataStore.depts) {
            DataStore.points.put(d, 0);
        }

        while (true) {
            System.out.println("\n1.Admin Login\n2.Coordinator Login\n3.Department Login\n4.Exit");
            int ch = DataStore.sc.nextInt();

            switch (ch) {
                case 1:
                    AdminLogin.start();
                    break;
                case 2:
                    CoordinatorLogin.start();
                    break;
                case 3:
                    DepartmentLogin.start();
                    break;
                case 4:
                    System.exit(0);
                    break;
            }
        }
    }
}
