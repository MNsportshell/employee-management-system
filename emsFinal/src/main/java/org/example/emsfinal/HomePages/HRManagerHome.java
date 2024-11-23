package org.example.emsfinal.HomePages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HRManagerHome extends ManagerHome {

    private double ptoBalance = 40.0;

    public HRManagerHome() {
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HR Manager Dashboard");

        // Welcome message with username
        Label welcomeLabel = new Label("Welcome " + username + " to the HR Manager Dashboard!");
        Label messageLabel = new Label();

        // Buttons and functionalities inherited from ManagerHome
        Button viewPTOButton = new Button("View PTO Balance");
        viewPTOButton.setOnAction(e -> messageLabel.setText("PTO Balance: " + ptoBalance + " hours"));

        Label requestPTOLabel = new Label("Select PTO dates:");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        Button requestPTOButton = new Button("Request PTO");
        requestPTOButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null || endDate == null) {
                messageLabel.setText("Error: Please select both start and end dates.");
            } else if (endDate.isBefore(startDate)) {
                messageLabel.setText("Error: End date cannot be before start date.");
            } else {
                long days = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Include both start and end dates
                double totalRequestedHours = days * 8; // Assume 8 hours per day

                if (totalRequestedHours > ptoBalance) {
                    messageLabel.setText("Error: Not enough PTO balance.");
                } else {
                    ptoBalance -= totalRequestedHours;
                    savePTORequestToFile(username, startDate, endDate, totalRequestedHours);
                    messageLabel.setText("PTO request approved for " + days + " days (" +
                            totalRequestedHours + " hours). Remaining balance: " + ptoBalance + " hours.");
                }
            }
        });

        Label approvePTOLabel = new Label("Review PTO Requests:");
        TextArea requestList = new TextArea();
        requestList.setEditable(false);
        Button loadRequestsButton = new Button("Load PTO Requests");
        Button approveRequestButton = new Button("Approve");
        Button denyRequestButton = new Button("Deny");

        loadRequestsButton.setOnAction(e -> loadPTORequests(requestList));
        approveRequestButton.setOnAction(e -> handleRequest(requestList, true, messageLabel));
        denyRequestButton.setOnAction(e -> handleRequest(requestList, false, messageLabel));

        // New HR Manager functionality: Create User
        Label createUserLabel = new Label("Create New User:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button createUserButton = new Button("Create User");

        createUserButton.setOnAction(e -> {
            String newUsername = usernameField.getText();
            String newPassword = passwordField.getText();
            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                messageLabel.setText("Error: Username or password cannot be empty.");
            } else {
                boolean success = createNewUser(newUsername, newPassword);
                if (success) {
                    messageLabel.setText("User created successfully: " + newUsername);
                    usernameField.clear();
                    passwordField.clear();
                } else {
                    messageLabel.setText("Error: Failed to create user.");
                }
            }
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        // Add ManagerHome functionalities
        grid.add(welcomeLabel, 0, 0, 2, 1);
        grid.add(viewPTOButton, 0, 1);
        grid.add(requestPTOLabel, 0, 2);
        grid.add(startDatePicker, 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(requestPTOButton, 0, 4);
        grid.add(approvePTOLabel, 0, 5);
        grid.add(requestList, 0, 6, 2, 1);
        grid.add(loadRequestsButton, 0, 7);
        grid.add(approveRequestButton, 0, 8);
        grid.add(denyRequestButton, 1, 8);

        // Add HR-specific functionalities
        grid.add(createUserLabel, 0, 9);
        grid.add(usernameField, 0, 10);
        grid.add(passwordField, 1, 10);
        grid.add(createUserButton, 0, 11);

        grid.add(messageLabel, 0, 12, 2, 1);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void handlePTORequest(TextArea requestList, boolean approve, Label messageLabel) {
        String[] requests = requestList.getText().split("\n");
        if (requests.length == 0 || requests[0].isEmpty()) {
            messageLabel.setText("No requests to process.");
            return;
        }

        // Parse the first request for simplicity
        String selectedRequest = requests[0];
        String[] requestDetails = selectedRequest.split(", ");
        if (approve) {
            String username = requestDetails[0].split(": ")[1];
            String dateRange = requestDetails[1].split(": ")[1];
            double totalHours = Double.parseDouble(requestDetails[2].split(": ")[1]);

            JSONObject approvedRequest = new JSONObject();
            approvedRequest.put("Username", username);
            approvedRequest.put("Date Range", dateRange);
            approvedRequest.put("Total Hours Requested", totalHours);

            writeApprovedRequestToFile(approvedRequest);
            messageLabel.setText("Approved: " + selectedRequest);
        } else {
            messageLabel.setText("Denied: " + selectedRequest);
        }

        // Remove the processed request from the JSON file
        removeRequestFromFile(selectedRequest);

        // Reload the request list
        loadPTORequests(requestList);
    }

    /**
     * Creates a new user and saves it to a JSON file.
     */
    private boolean createNewUser(String username, String password) {
        String filePath = "Users.json";
        JSONParser parser = new JSONParser();
        JSONArray users;

        // Read existing users from the file
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            users = (JSONArray) obj;
        } catch (Exception e) {
            // If the file doesn't exist or is empty, create a new array
            users = new JSONArray();
        }

        // Check if the username already exists
        for (Object obj : users) {
            JSONObject user = (JSONObject) obj;
            if (user.get("Username").equals(username)) {
                return false; // Username already exists
            }
        }

        // Create a new user object
        JSONObject newUser = new JSONObject();
        newUser.put("Username", username);
        newUser.put("Password", password);

        // Add the new user to the array
        users.add(newUser);

        // Write the updated array back to the file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(users.toJSONString());
            writer.flush();
            return true; // Successfully added
        } catch (IOException e) {
            System.out.println("Error writing to Users.json: " + e.getMessage());
            return false;
        }
    }
}
