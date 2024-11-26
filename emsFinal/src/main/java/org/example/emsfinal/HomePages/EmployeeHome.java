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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EmployeeHome {

    private double ptoBalance = 40.0; // 40 hours of PTO remaining
    private String username = "Jane Doe"; // Employee's username

    public EmployeeHome() {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Dashboard");

        // Create UI elements
        Label welcomeLabel = new Label("Welcome " + username + " to the Employee Dashboard!");

        // PTO Button
        Button ptoButton = new Button("PTO");
        ptoButton.setOnAction(e -> openPTOWindow());
        Button viewReviewsButton = new Button("View Employee Reviews");
        viewReviewsButton.setOnAction(e -> viewEmployeeReviews());

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(welcomeLabel, 0, 0);
        grid.add(ptoButton, 0, 1);
        grid.add(viewReviewsButton, 0, 2);
        grid.add(logoutButton, 0, 3);

        // Set scene and show stage
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void viewEmployeeReviews() {
        Stage reviewStage = new Stage();
        reviewStage.setTitle("Employee Reviews");

        // TextArea to display the reviews
        TextArea reviewTextArea = new TextArea();
        reviewTextArea.setEditable(false);

        // Load the reviews from the JSON file
        String filePath = "EmployeeReviews.json";
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);

            // Check if the root is an object or an array
            JSONArray employeeReviews;
            if (obj instanceof JSONObject) {
                JSONObject rootObject = (JSONObject) obj;
                employeeReviews = (JSONArray) rootObject.get("EmployeeReviews");
            } else if (obj instanceof JSONArray) {
                employeeReviews = (JSONArray) obj;
            } else {
                throw new IllegalArgumentException("Invalid JSON structure");
            }

            // Format and display the reviews
            StringBuilder reviewsContent = new StringBuilder();
            for (Object reviewObj : employeeReviews) {
                JSONObject review = (JSONObject) reviewObj;
                reviewsContent.append("Employee Name: ").append(review.get("Employee Name")).append("\n");
                reviewsContent.append("Employee ID: ").append(review.get("Employee ID")).append("\n");
                reviewsContent.append("Review Rating: ").append(review.get("Review Rating")).append("\n");
                reviewsContent.append("Review Notes: ").append(review.get("Review Notes")).append("\n");
                reviewsContent.append("\n-----------------------------------\n\n");
            }
            reviewTextArea.setText(reviewsContent.toString());
        } catch (Exception e) {
            reviewTextArea.setText("Error loading reviews: " + e.getMessage());
        }

        // Layout
        ScrollPane scrollPane = new ScrollPane(reviewTextArea);
        Scene scene = new Scene(scrollPane, 600, 400);
        reviewStage.setScene(scene);
        reviewStage.show();
    }



    private void openPTOWindow() {
        Stage ptoStage = new Stage();
        ptoStage.setTitle("PTO Management");

        Label messageLabel = new Label();

        // View PTO functionality
        Button viewPTOButton = new Button("View PTO Balance");
        viewPTOButton.setOnAction(e -> messageLabel.setText("PTO Balance: " + ptoBalance + " hours"));

        // Request PTO functionality with date selection
        Label requestPTOLabel = new Label("Submit PTO Request:");
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
                    messageLabel.setText("PTO request submitted for " + days + " days (" +
                            totalRequestedHours + " hours). Remaining balance: " + ptoBalance + " hours.");
                }
            }
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(viewPTOButton, 0, 0);
        grid.add(requestPTOLabel, 0, 1);
        grid.add(startDatePicker, 0, 2);
        grid.add(endDatePicker, 1, 2);
        grid.add(requestPTOButton, 0, 3, 2, 1);
        grid.add(messageLabel, 0, 4, 2, 1);

        // Set scene and show stage
        Scene scene = new Scene(grid, 400, 300);
        ptoStage.setScene(scene);
        ptoStage.show();
    }

    private void savePTORequestToFile(String username, LocalDate startDate, LocalDate endDate, double totalRequestedHours) {
        String filePath = "PTORequests.json";
        JSONParser parser = new JSONParser();
        JSONArray ptoRequests;

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            ptoRequests = (JSONArray) obj;
        } catch (Exception e) {
            ptoRequests = new JSONArray();
        }

        JSONObject ptoRequest = new JSONObject();
        ptoRequest.put("Username", username);
        ptoRequest.put("Date Range", startDate + " to " + endDate);
        ptoRequest.put("Total Hours Requested", totalRequestedHours);

        ptoRequests.add(ptoRequest);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(ptoRequests.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    private void logout(Stage primaryStage) {
        // Start the SignInPage
        SignInPage signInPage = new SignInPage(); // Assuming you have a SignInPage class
        signInPage.start(primaryStage); // Restart the SignInPage in the same stage
    }
}
