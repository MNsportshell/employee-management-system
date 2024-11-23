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

public class ManagerHome {

    private double ptoBalance = 40.0; // Default PTO balance for manager
    public String username; // Manager's username

    // Constructor to pass the logged-in username
    public ManagerHome() {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manager Dashboard");

        // Welcome message with username
        Label welcomeLabel = new Label("Welcome " + username + " to the Manager Dashboard!");
        Label messageLabel = new Label();

        // View PTO button
        Button viewPTOButton = new Button("View PTO Balance");
        viewPTOButton.setOnAction(e -> messageLabel.setText("PTO Balance: " + ptoBalance + " hours"));

        // Request PTO functionality
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

        // Approve/Deny PTO functionality
        Label approvePTOLabel = new Label("Review PTO Requests:");
        TextArea requestList = new TextArea();
        requestList.setEditable(false);
        Button loadRequestsButton = new Button("Load PTO Requests");
        Button approveRequestButton = new Button("Approve");
        Button denyRequestButton = new Button("Deny");

        loadRequestsButton.setOnAction(e -> loadPTORequests(requestList));
        approveRequestButton.setOnAction(e -> handleRequest(requestList, true, messageLabel));
        denyRequestButton.setOnAction(e -> handleRequest(requestList, false, messageLabel));

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
        grid.add(requestPTOButton, 0, 4);
        grid.add(approvePTOLabel, 0, 5);
        grid.add(requestList, 0, 6, 2, 1);
        grid.add(loadRequestsButton, 0, 7);
        grid.add(approveRequestButton, 0, 8);
        grid.add(denyRequestButton, 1, 8);
        grid.add(messageLabel, 0, 9, 2, 1);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void savePTORequestToFile(String username, LocalDate startDate, LocalDate endDate, double totalRequestedHours) {
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

    public void loadPTORequests(TextArea requestList) {
        String filePath = "PTORequests.json";
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray ptoRequests = (JSONArray) obj;

            StringBuilder displayText = new StringBuilder();
            for (int i = 0; i < ptoRequests.size(); i++) {
                JSONObject request = (JSONObject) ptoRequests.get(i);
                displayText.append("Request #").append(i + 1).append(": ");
                displayText.append("User: ").append(request.get("Username")).append(", ");
                displayText.append("Dates: ").append(request.get("Date Range")).append(", ");
                displayText.append("Hours: ").append(request.get("Total Hours Requested")).append("\n");
            }

            requestList.setText(displayText.toString());
        } catch (Exception e) {
            requestList.setText("Error loading PTO requests: " + e.getMessage());
        }
    }

    public void handleRequest(TextArea requestList, boolean approve, Label messageLabel) {
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

    public void removeRequestFromFile(String selectedRequest) {
        String filePath = "PTORequests.json";
        JSONParser parser = new JSONParser();
        JSONArray ptoRequests;

        try (FileReader reader = new FileReader(filePath)) {
            // Read existing requests from the file
            Object obj = parser.parse(reader);
            ptoRequests = (JSONArray) obj;

            // Find and remove the matching request
            for (int i = 0; i < ptoRequests.size(); i++) {
                JSONObject request = (JSONObject) ptoRequests.get(i);

                String username = (String) request.get("Username");
                String dateRange = (String) request.get("Date Range");
                double totalHours = (double) request.get("Total Hours Requested");

                // Match the request string to the JSON object
                String jsonString = "User: " + username + ", Dates: " + dateRange + ", Hours: " + totalHours;
                if (selectedRequest.contains(jsonString)) {
                    ptoRequests.remove(i);
                    break;
                }
            }

            // Write the updated list back to the file
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(ptoRequests.toJSONString());
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("Error while removing request from file: " + e.getMessage());
        }
    }


    public void writeApprovedRequestToFile(JSONObject approvedRequest) {
        String filePath = "ApprovedPTO.json";
        JSONParser parser = new JSONParser();
        JSONArray approvedRequests;

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            approvedRequests = (JSONArray) obj;
        } catch (Exception e) {
            approvedRequests = new JSONArray();
        }

        approvedRequests.add(approvedRequest);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(approvedRequests.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to ApprovedPTO.json: " + e.getMessage());
        }
    }
}
