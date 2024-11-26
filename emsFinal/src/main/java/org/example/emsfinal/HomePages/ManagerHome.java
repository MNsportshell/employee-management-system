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

public class ManagerHome extends EmployeeHome {

    private double ptoBalance = 40.0; // Default PTO balance for manager
    public String username; // Manager's username

    public ManagerHome() {
        super();
        this.username = username; // Set the username for the manager
    }

    @Override
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
        grid.add(logoutButton, 0, 4);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Employee Reviews
    void openRequestReviewWindow() {
        Stage reviewStage = new Stage();
        reviewStage.setTitle("PTO Requests Review");

        Label messageLabel = new Label();

        // ComboBox for selecting a request
        ComboBox<String> requestComboBox = new ComboBox<>();
        requestComboBox.setPromptText("Select a request to review");

        // Buttons for approving and denying requests
        Button loadRequestsButton = new Button("Load PTO Requests");
        Button approveRequestButton = new Button("Approve");
        Button denyRequestButton = new Button("Deny");

        // Load requests into ComboBox
        loadRequestsButton.setOnAction(e -> loadPTORequestsIntoComboBox(requestComboBox));

        // Approve the selected request
        approveRequestButton.setOnAction(e -> {
            String selectedRequest = requestComboBox.getSelectionModel().getSelectedItem();
            if (selectedRequest == null) {
                messageLabel.setText("Error: No request selected.");
            } else {
                handleSelectedRequest(selectedRequest, true, messageLabel);
                loadPTORequestsIntoComboBox(requestComboBox); // Reload updated requests
            }
        });

        // Deny the selected request
        denyRequestButton.setOnAction(e -> {
            String selectedRequest = requestComboBox.getSelectionModel().getSelectedItem();
            if (selectedRequest == null) {
                messageLabel.setText("Error: No request selected.");
            } else {
                handleSelectedRequest(selectedRequest, false, messageLabel);
                loadPTORequestsIntoComboBox(requestComboBox); // Reload updated requests
            }
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Review PTO Requests:"), 0, 0, 2, 1);
        grid.add(requestComboBox, 0, 1, 2, 1);
        grid.add(loadRequestsButton, 0, 2);
        grid.add(approveRequestButton, 0, 3);
        grid.add(denyRequestButton, 1, 3);
        grid.add(messageLabel, 0, 4, 2, 1);

        // Set scene and show stage
        Scene scene = new Scene(grid, 500, 400);
        reviewStage.setScene(scene);
        reviewStage.show();
    }


    void openEmployeeReviewWindow() {
        Stage reviewStage = new Stage();
        reviewStage.setTitle("Employee Reviews");

        // Layout for the main buttons
        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);

        // Buttons for "View Employee Reviews" and "Submit Employee Reviews"
        Button viewReviewsButton = new Button("View Employee Reviews");
        Button submitReviewButton = new Button("Submit Employee Review");

        // Action for "View Employee Reviews"
        viewReviewsButton.setOnAction(e -> {
            Stage viewStage = new Stage();
            viewStage.setTitle("View Employee Reviews");

            TextArea reviewList = new TextArea();
            reviewList.setEditable(false);
            loadEmployeeReviews(reviewList);

            GridPane viewGrid = new GridPane();
            viewGrid.setAlignment(Pos.CENTER);
            viewGrid.setVgap(10);
            viewGrid.setHgap(10);
            viewGrid.add(new Label("Employee Reviews:"), 0, 0);
            viewGrid.add(reviewList, 0, 1);

            Scene viewScene = new Scene(viewGrid, 500, 400);
            viewStage.setScene(viewScene);
            viewStage.show();
        });

        // Action for "Submit Employee Reviews"
        submitReviewButton.setOnAction(e -> {
            Stage submitStage = new Stage();
            submitStage.setTitle("Submit Employee Review");

            // Fields for the employee review
            Label nameLabel = new Label("Employee Name:");
            TextField nameField = new TextField();

            Label idLabel = new Label("Employee ID:");
            TextField idField = new TextField();

            Label ratingLabel = new Label("Review Rating (1-5):");
            TextField ratingField = new TextField();

            Label notesLabel = new Label("Review Notes:");
            TextArea notesField = new TextArea();

            Label messageLabel = new Label();

            // Submit button to save the review
            Button submitButton = new Button("Submit Review");
            submitButton.setOnAction(ev -> {
                String name = nameField.getText();
                String id = idField.getText();
                String ratingText = ratingField.getText();
                String notes = notesField.getText();

                if (name.isEmpty() || id.isEmpty() || ratingText.isEmpty() || notes.isEmpty()) {
                    messageLabel.setText("Error: All fields must be filled.");
                } else {
                    try {
                        int rating = Integer.parseInt(ratingText);
                        if (rating < 1 || rating > 5) {
                            messageLabel.setText("Error: Rating must be between 1 and 5.");
                        } else {
                            saveEmployeeReviewToFile(name, id, rating, notes);
                            messageLabel.setText("Review submitted successfully!");
                        }
                    } catch (NumberFormatException ex) {
                        messageLabel.setText("Error: Rating must be a number between 1 and 5.");
                    }
                }
            });

            GridPane submitGrid = new GridPane();
            submitGrid.setAlignment(Pos.CENTER);
            submitGrid.setVgap(10);
            submitGrid.setHgap(10);

            submitGrid.add(nameLabel, 0, 0);
            submitGrid.add(nameField, 1, 0);

            submitGrid.add(idLabel, 0, 1);
            submitGrid.add(idField, 1, 1);

            submitGrid.add(ratingLabel, 0, 2);
            submitGrid.add(ratingField, 1, 2);

            submitGrid.add(notesLabel, 0, 3);
            submitGrid.add(notesField, 1, 3, 2, 1);

            submitGrid.add(submitButton, 0, 4);
            submitGrid.add(messageLabel, 1, 4, 2, 1);

            Scene submitScene = new Scene(submitGrid, 500, 400);
            submitStage.setScene(submitScene);
            submitStage.show();
        });

        // Add buttons to the main layout
        mainGrid.add(viewReviewsButton, 0, 0);
        mainGrid.add(submitReviewButton, 0, 1);

        // Set scene and show stage
        Scene mainScene = new Scene(mainGrid, 300, 200);
        reviewStage.setScene(mainScene);
        reviewStage.show();
    }

    private void loadPTORequestsIntoComboBox(ComboBox<String> requestComboBox) {
        String filePath = "PTORequests.json";
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray ptoRequests = (JSONArray) obj;

            requestComboBox.getItems().clear();
            for (int i = 0; i < ptoRequests.size(); i++) {
                JSONObject request = (JSONObject) ptoRequests.get(i);
                String requestString = "User: " + request.get("Username") + ", Dates: " + request.get("Date Range") +
                        ", Hours: " + request.get("Total Hours Requested");
                requestComboBox.getItems().add(requestString);
            }
        } catch (Exception e) {
            System.out.println("Error loading PTO requests: " + e.getMessage());
        }
    }

    private void loadEmployeeReviews(TextArea reviewList) {
        String filePath = "EmployeeReviews.json";
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray reviews = (JSONArray) obj;

            StringBuilder displayText = new StringBuilder();
            for (Object reviewObj : reviews) {
                JSONObject review = (JSONObject) reviewObj;
                displayText.append("Name: ").append(review.get("Employee Name")).append("\n");
                displayText.append("ID: ").append(review.get("Employee ID")).append("\n");
                displayText.append("Rating: ").append(review.get("Review Rating")).append("\n");
                displayText.append("Notes: ").append(review.get("Review Notes")).append("\n");
                displayText.append("-----\n");
            }

            reviewList.setText(displayText.toString());
        } catch (Exception e) {
            reviewList.setText("Error loading employee reviews: " + e.getMessage());
        }
    }

    private void saveEmployeeReviewToFile(String name, String id, int rating, String notes) {
        String filePath = "EmployeeReviews.json";
        JSONParser parser = new JSONParser();
        JSONArray reviews;

        // Read existing JSON data from the file
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            reviews = (JSONArray) obj;
        } catch (Exception e) {
            // If the file doesn't exist or is empty, create a new array
            reviews = new JSONArray();
        }

        // Create the new review
        JSONObject review = new JSONObject();
        review.put("Employee Name", name);
        review.put("Employee ID", id);
        review.put("Review Rating", rating);
        review.put("Review Notes", notes);

        // Add the new review to the array
        reviews.add(review);

        // Write the updated array back to the file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(reviews.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to EmployeeReviews.json: " + e.getMessage());
        }
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

    private void handleSelectedRequest(String selectedRequest, boolean approve, Label messageLabel) {
        String[] requestDetails = selectedRequest.split(", ");
        String username = requestDetails[0].split(": ")[1];
        String dateRange = requestDetails[1].split(": ")[1];
        double totalHours = Double.parseDouble(requestDetails[2].split(": ")[1]);

        JSONObject processedRequest = new JSONObject();
        processedRequest.put("Username", username);
        processedRequest.put("Date Range", dateRange);
        processedRequest.put("Total Hours Requested", totalHours);

        if (approve) {
            writeApprovedRequestToFile(processedRequest);
            messageLabel.setText("Approved: " + selectedRequest);
        } else {
            writeDeniedRequestToFile(processedRequest);
            messageLabel.setText("Denied: " + selectedRequest);
        }

        // Remove the processed request from the JSON file
        removeRequestFromFile(selectedRequest);
    }

    private void writeDeniedRequestToFile(JSONObject deniedRequest) {
        String filePath = "DeniedPTO.json";
        JSONParser parser = new JSONParser();
        JSONArray deniedRequests;

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            deniedRequests = (JSONArray) obj;
        } catch (Exception e) {
            deniedRequests = new JSONArray();
        }

        deniedRequests.add(deniedRequest);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(deniedRequests.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to DeniedPTO.json: " + e.getMessage());
        }
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
