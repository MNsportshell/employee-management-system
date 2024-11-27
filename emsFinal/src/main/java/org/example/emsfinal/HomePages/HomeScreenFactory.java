package org.example.emsfinal.HomePages;

import javafx.stage.Stage;

public class HomeScreenFactory {
    public static HomeScreen createHomeScreen(String role) {

        // Switch case for the different roles for login
        switch (role.toLowerCase()) {
            case "employee":
                return new EmployeeHome();
            case "manager":
                return new ManagerHome();
            case "hrmanager":
                return new HRManagerHome();
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
