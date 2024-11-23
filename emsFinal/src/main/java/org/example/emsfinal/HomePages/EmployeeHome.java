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

public class EmployeeHome {

    // Employee data (for demonstration purposes)
    private double ptoBalance = 40.0; // 40 hours of PTO remaining
    private String username = "Jane Doe"; // Employee's username

    // Constructor to pass the username
    public EmployeeHome() {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Dashboard");

        // Create UI elements
        Label welcomeLabel = new Label("Welcome " + username + " to the Employee Dashboard!");
        Label messageLabel = new Label();

        // View PTO button and functionality
        Button viewPTOButton = new Button("View PTO Balance");
        viewPTOButton.setOnAction(e -> messageLabel.setText("PTO Balance: " + ptoBalance + " hours"));

        // Request PTO functionality with date selection
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

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(welcomeLabel, 0, 0, 2, 1);
        grid.add(viewPTOButton, 0, 1);
        grid.add(requestPTOLabel, 0, 2);
        grid.add(startDatePicker, 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(requestPTOButton, 0, 4, 2, 1);
        grid.add(messageLabel, 0, 5, 2, 1);

        // Set scene and show stage
        Scene scene = new Scene(grid, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Saves the PTO request to a JSON file, ensuring the request is appended to the existing array.
     */
    private void savePTORequestToFile(String username, LocalDate startDate, LocalDate endDate, double totalRequestedHours) {
        String filePath = "PTORequests.json";
        JSONParser parser = new JSONParser();
        JSONArray ptoRequests;

        // Read existing JSON data from the file
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            ptoRequests = (JSONArray) obj;
        } catch (Exception e) {
            // If the file doesn't exist or is empty, create a new array
            ptoRequests = new JSONArray();
        }

        // Create the new PTO request
        JSONObject ptoRequest = new JSONObject();
        ptoRequest.put("Username", username);
        ptoRequest.put("Date Range", startDate + " to " + endDate);
        ptoRequest.put("Total Hours Requested", totalRequestedHours);

        // Add the new request to the array
        ptoRequests.add(ptoRequest);

        // Write the updated array back to the file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(ptoRequests.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }
}
