package org.example.emsfinal.HomePages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.emsfinal.SignInPage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HRManagerHome extends ManagerHome {

    private double ptoBalance = 40.0; // Default PTO balance for manager
    public String username; // Manager's username

    // Constructor to pass the logged-in username
    public HRManagerHome() {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manager Dashboard");

        // Welcome message with username
        Label welcomeLabel = new Label("Welcome " + username + " to the Manager Dashboard!");
        Label messageLabel = new Label();

        // PTO Button to open new window
        Button ptoButton = new Button("PTO");
        ptoButton.setOnAction(e -> openPTOWindow());

        // PTO Request Review Button to open new window
        Button reviewRequestsButton = new Button("Review PTO Requests");
        reviewRequestsButton.setOnAction(e -> openRequestReviewWindow());

        // User Management
        Button userManagementButton = new Button("User Management");
        userManagementButton.setOnAction(e -> openUserManagementWindow());

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        // Approve/Deny PTO functionality
        Label approvePTOLabel = new Label("Review PTO Requests:");
        TextArea requestList = new TextArea();
        requestList.setEditable(false);
        Button loadRequestsButton = new Button("Load PTO Requests");
        Button approveRequestButton = new Button("Approve");
        Button denyRequestButton = new Button("Deny");

        // Submit Employee Review button
        Button employeeReviewButton = new Button("View/Submit Employee Reviews");
        employeeReviewButton.setOnAction(e -> openEmployeeReviewWindow());

        loadRequestsButton.setOnAction(e -> loadPTORequests(requestList));
        approveRequestButton.setOnAction(e -> handleRequest(requestList, true, messageLabel));
        denyRequestButton.setOnAction(e -> handleRequest(requestList, false, messageLabel));

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(welcomeLabel, 0, 0, 2, 1);
        grid.add(ptoButton, 0, 1);
        grid.add(reviewRequestsButton, 0, 2);
        grid.add(employeeReviewButton, 0, 3);
        grid.add(userManagementButton, 0,4);
        grid.add(logoutButton, 0, 5);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openUserManagementWindow() {
        Stage userStage = new Stage();
        userStage.setTitle("User Management");

        // Labels and fields for user data
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();

        Label roleLabel = new Label("Role:");
        TextField roleField = new TextField();

        Label employeeIdLabel = new Label("Employee ID:");
        TextField employeeIdField = new TextField();

        Label messageLabel = new Label();

        // ComboBox to select a user
        ComboBox<String> userComboBox = new ComboBox<>();
        userComboBox.setPromptText("Select a user to edit");
        userComboBox.setOnAction(e -> {
            String selectedUser = userComboBox.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                populateUserFields(selectedUser, usernameField, passwordField, roleField, employeeIdField);
            }
        });

        // Buttons for managing users
        Button addUserButton = new Button("Add User");
        Button editUserButton = new Button("Edit User");
        Button loadUsersButton = new Button("Load Users");

        // Add user functionality
        addUserButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();
            String employeeId = employeeIdField.getText();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty() || employeeId.isEmpty()) {
                messageLabel.setText("Error: All fields must be filled.");
            } else {
                addUserToFile(username, password, role, employeeId);
                messageLabel.setText("User added successfully!");
                loadUsers(userComboBox);
            }
        });

        // Edit user functionality
        editUserButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();
            String employeeId = employeeIdField.getText();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty() || employeeId.isEmpty()) {
                messageLabel.setText("Error: All fields must be filled.");
            } else {
                editUserInFile(username, password, role, employeeId);
                messageLabel.setText("User edited successfully!");
                loadUsers(userComboBox);
            }
        });

        // Load users functionality
        loadUsersButton.setOnAction(e -> loadUsers(userComboBox));

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(userComboBox, 0, 0, 2, 1);

        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);

        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);

        grid.add(roleLabel, 0, 3);
        grid.add(roleField, 1, 3);

        grid.add(employeeIdLabel, 0, 4);
        grid.add(employeeIdField, 1, 4);

        grid.add(addUserButton, 0, 5);
        grid.add(editUserButton, 1, 5);
        grid.add(loadUsersButton, 0, 6, 2, 1);

        grid.add(messageLabel, 0, 7, 2, 1);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 600, 500);
        userStage.setScene(scene);
        userStage.show();
    }

    private void populateUserFields(String selectedUser, TextField usernameField, TextField passwordField, TextField roleField, TextField employeeIdField) {
        String filePath = "Users.json";
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray users = (JSONArray) obj;

            for (Object userObj : users) {
                JSONObject user = (JSONObject) userObj;
                if (user.get("Username").equals(selectedUser)) {
                    usernameField.setText((String) user.get("Username"));
                    passwordField.setText((String) user.get("Password"));
                    roleField.setText((String) user.get("Role"));
                    employeeIdField.setText((String) user.get("Employee ID"));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading user details: " + e.getMessage());
        }
    }


    private void addUserToFile(String username, String password, String role, String employeeId) {
        String filePath = "Users.json";
        JSONParser parser = new JSONParser();
        JSONArray users;

        // Load existing users from the file
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            users = (JSONArray) obj;
        } catch (Exception e) {
            users = new JSONArray();
        }

        // Create the new user object
        JSONObject newUser = new JSONObject();
        newUser.put("Username", username);
        newUser.put("Password", password);
        newUser.put("Role", role);
        newUser.put("Employee ID", employeeId);

        // Add the user to the list
        users.add(newUser);

        // Save back to the file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(users.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to Users.json: " + e.getMessage());
        }
    }

    private void editUserInFile(String username, String password, String role, String employeeId) {
        String filePath = "Users.json";
        JSONParser parser = new JSONParser();
        JSONArray users;

        // Load existing users from the file
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            users = (JSONArray) obj;
        } catch (Exception e) {
            users = new JSONArray();
        }

        // Edit the user if it exists
        boolean userFound = false;
        for (Object obj : users) {
            JSONObject user = (JSONObject) obj;
            if (user.get("Username").equals(username)) {
                user.put("Password", password);
                user.put("Role", role);
                user.put("Employee ID", employeeId);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("User not found.");
        }

        // Save back to the file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(users.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to Users.json: " + e.getMessage());
        }
    }

    private boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }



    private void loadUsers(ComboBox<String> userComboBox) {
        String filePath = "Users.json";
        JSONParser parser = new JSONParser();


        if (!fileExists(filePath)) {
            System.out.println("Users.json file not found!");
            return;
        }

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray users = (JSONArray) obj;

            userComboBox.getItems().clear();
            for (Object userObj : users) {
                JSONObject user = (JSONObject) userObj;
                String username = (String) user.get("Username");
                System.out.println("Loaded username: " + username); // Debugging
                userComboBox.getItems().add(username);
            }
            System.out.println("Total users loaded: " + userComboBox.getItems().size());
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
}