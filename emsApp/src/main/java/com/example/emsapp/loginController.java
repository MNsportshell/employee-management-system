package com.example.emsapp;

import com.example.emsapp.Roles.EmployeeRole;
import com.example.emsapp.Roles.HRManagerRole;
import com.example.emsapp.Roles.ManagerRole;

import java.util.ArrayList;
import java.util.List;

public class loginController {

    private static List<User> users = new ArrayList<>();

    static {
        // Predefined users with roles
        users.add(new User("emp", "pw", "Employee"));
        users.add(new User("mgr", "pw", "Manager"));
        users.add(new User("hr", "pw", "HRManager"));
        users.add(new User("user1", "pw", "Employee"));


        // Add additional hardcoded users here as needed
    }

    // Hardcoded User class as previously defined
    public static class User {
        private String username;
        private String password;
        private String role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }

    public static User authenticateUser(String username, String password) {
        // Iterate through hardcoded users to authenticate
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        System.out.println("Authentication failed: Invalid username or password.");
        return null;
    }

    public static Object assignRole(User user) {
        String role = user.getRole();

        switch (role) {
            case "Employee":
                return new EmployeeRole();
            case "Manager":
                return new ManagerRole();
            case "HRManager":
                return new HRManagerRole();
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    public static Object login(String username, String password) {
        User user = authenticateUser(username, password);
        if (user != null) {
            return assignRole(user);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // Example login attempts
        Object role1 = login("emp", "pw");
        Object role2 = login("mgr", "pw");
        Object role3 = login("hr", "pw");


    }
}
