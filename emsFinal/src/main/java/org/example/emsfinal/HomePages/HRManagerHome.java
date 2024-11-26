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

//    // Method to open PTO window
//    public void openPTOWindow() {
//        Stage ptoStage = new Stage();
//        ptoStage.setTitle("PTO Management");
//
//        Label messageLabel = new Label();
//
//        // View PTO functionality
//        Button viewPTOButton = new Button("View PTO Balance");
//        viewPTOButton.setOnAction(e -> messageLabel.setText("PTO Balance: " + ptoBalance + " hours"));
//
//        // Request PTO functionality with date selection
//        Label requestPTOLabel = new Label("Submit PTO Request:");
//        DatePicker startDatePicker = new DatePicker();
//        startDatePicker.setPromptText("Start Date");
//        DatePicker endDatePicker = new DatePicker();
//        endDatePicker.setPromptText("End Date");
//
//        Button requestPTOButton = new Button("Request PTO");
//        requestPTOButton.setOnAction(e -> {
//            LocalDate startDate = startDatePicker.getValue();
//            LocalDate endDate = endDatePicker.getValue();
//
//            if (startDate == null || endDate == null) {
//                messageLabel.setText("Error: Please select both start and end dates.");
//            } else if (endDate.isBefore(startDate)) {
//                messageLabel.setText("Error: End date cannot be before start date.");
//            } else {
//                long days = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Include both start and end dates
//                double totalRequestedHours = days * 8; // Assume 8 hours per day
//
//                if (totalRequestedHours > ptoBalance) {
//                    messageLabel.setText("Error: Not enough PTO balance.");
//                } else {
//                    ptoBalance -= totalRequestedHours;
//                    savePTORequestToFile(username, startDate, endDate, totalRequestedHours);
//                    messageLabel.setText("PTO request submitted for " + days + " days (" +
//                            totalRequestedHours + " hours). Remaining balance: " + ptoBalance + " hours.");
//                }
//            }
//        });
//
//        // Layout
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setVgap(10);
//        grid.setHgap(10);
//
//        grid.add(viewPTOButton, 0, 0);
//        grid.add(requestPTOLabel, 0, 1);
//        grid.add(startDatePicker, 0, 2);
//        grid.add(endDatePicker, 1, 2);
//        grid.add(requestPTOButton, 0, 3, 2, 1);
//        grid.add(messageLabel, 0, 4, 2, 1);
//
//        // Set scene and show stage
//        Scene scene = new Scene(grid, 400, 300);
//        ptoStage.setScene(scene);
//        ptoStage.show();
//    }

    // Method to open PTO Requests Review window
//    private void openRequestReviewWindow() {
//        Stage reviewStage = new Stage();
//        reviewStage.setTitle("PTO Requests Review");
//
//        Label messageLabel = new Label();
//        TextArea requestList = new TextArea();
//        requestList.setEditable(false);
//
//        // Buttons for loading, approving, and denying requests
//        Button loadRequestsButton = new Button("Load PTO Requests");
//        Button approveRequestButton = new Button("Approve");
//        Button denyRequestButton = new Button("Deny");
//
//        loadRequestsButton.setOnAction(e -> loadPTORequests(requestList));
//        approveRequestButton.setOnAction(e -> handleRequest(requestList, true, messageLabel));
//        denyRequestButton.setOnAction(e -> handleRequest(requestList, false, messageLabel));
//
//        // Layout
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setVgap(10);
//        grid.setHgap(10);
//
//        grid.add(new Label("Review PTO Requests:"), 0, 0, 2, 1);
//        grid.add(requestList, 0, 1, 2, 1);
//        grid.add(loadRequestsButton, 0, 2);
//        grid.add(approveRequestButton, 0, 3);
//        grid.add(denyRequestButton, 1, 3);
//        grid.add(messageLabel, 0, 4, 2, 1);
//
//        // Set scene and show stage
//        Scene scene = new Scene(grid, 500, 400);
//        reviewStage.setScene(scene);
//        reviewStage.show();
//    }

//    private void openEmployeeReviewWindow() {
//        Stage reviewStage = new Stage();
//        reviewStage.setTitle("Employee Reviews");
//
//        // Layout for the main buttons
//        GridPane mainGrid = new GridPane();
//        mainGrid.setAlignment(Pos.CENTER);
//        mainGrid.setVgap(10);
//        mainGrid.setHgap(10);
//
//        // Buttons for "View Employee Reviews" and "Submit Employee Reviews"
//        Button viewReviewsButton = new Button("View Employee Reviews");
//        Button submitReviewButton = new Button("Submit Employee Review");
//
//        // Action for "View Employee Reviews"
//        viewReviewsButton.setOnAction(e -> {
//            Stage viewStage = new Stage();
//            viewStage.setTitle("View Employee Reviews");
//
//            TextArea reviewList = new TextArea();
//            reviewList.setEditable(false);
//            loadEmployeeReviews(reviewList);
//
//            GridPane viewGrid = new GridPane();
//            viewGrid.setAlignment(Pos.CENTER);
//            viewGrid.setVgap(10);
//            viewGrid.setHgap(10);
//            viewGrid.add(new Label("Employee Reviews:"), 0, 0);
//            viewGrid.add(reviewList, 0, 1);
//
//            Scene viewScene = new Scene(viewGrid, 500, 400);
//            viewStage.setScene(viewScene);
//            viewStage.show();
//        });
//
//        // Action for "Submit Employee Reviews"
//        submitReviewButton.setOnAction(e -> {
//            Stage submitStage = new Stage();
//            submitStage.setTitle("Submit Employee Review");
//
//            // Fields for the employee review
//            Label nameLabel = new Label("Employee Name:");
//            TextField nameField = new TextField();
//
//            Label idLabel = new Label("Employee ID:");
//            TextField idField = new TextField();
//
//            Label ratingLabel = new Label("Review Rating (1-5):");
//            TextField ratingField = new TextField();
//
//            Label notesLabel = new Label("Review Notes:");
//            TextArea notesField = new TextArea();
//
//            Label messageLabel = new Label();
//
//            // Submit button to save the review
//            Button submitButton = new Button("Submit Review");
//            submitButton.setOnAction(ev -> {
//                String name = nameField.getText();
//                String id = idField.getText();
//                String ratingText = ratingField.getText();
//                String notes = notesField.getText();
//
//                if (name.isEmpty() || id.isEmpty() || ratingText.isEmpty() || notes.isEmpty()) {
//                    messageLabel.setText("Error: All fields must be filled.");
//                } else {
//                    try {
//                        int rating = Integer.parseInt(ratingText);
//                        if (rating < 1 || rating > 5) {
//                            messageLabel.setText("Error: Rating must be between 1 and 5.");
//                        } else {
//                            saveEmployeeReviewToFile(name, id, rating, notes);
//                            messageLabel.setText("Review submitted successfully!");
//                        }
//                    } catch (NumberFormatException ex) {
//                        messageLabel.setText("Error: Rating must be a number between 1 and 5.");
//                    }
//                }
//            });
//
//            GridPane submitGrid = new GridPane();
//            submitGrid.setAlignment(Pos.CENTER);
//            submitGrid.setVgap(10);
//            submitGrid.setHgap(10);
//
//            submitGrid.add(nameLabel, 0, 0);
//            submitGrid.add(nameField, 1, 0);
//
//            submitGrid.add(idLabel, 0, 1);
//            submitGrid.add(idField, 1, 1);
//
//            submitGrid.add(ratingLabel, 0, 2);
//            submitGrid.add(ratingField, 1, 2);
//
//            submitGrid.add(notesLabel, 0, 3);
//            submitGrid.add(notesField, 1, 3, 2, 1);
//
//            submitGrid.add(submitButton, 0, 4);
//            submitGrid.add(messageLabel, 1, 4, 2, 1);
//
//            Scene submitScene = new Scene(submitGrid, 500, 400);
//            submitStage.setScene(submitScene);
//            submitStage.show();
//        });
//
//        // Add buttons to the main layout
//        mainGrid.add(viewReviewsButton, 0, 0);
//        mainGrid.add(submitReviewButton, 0, 1);
//
//        // Set scene and show stage
//        Scene mainScene = new Scene(mainGrid, 300, 200);
//        reviewStage.setScene(mainScene);
//        reviewStage.show();
//    }

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

        userComboBox.getItems().clear();
        userComboBox.getItems().addAll("john_doe", "jane_smith");
        System.out.println("Hardcoded users added to ComboBox.");


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


//    private void loadEmployeeReviews(TextArea reviewList) {
//        String filePath = "EmployeeReviews.json";
//        JSONParser parser = new JSONParser();
//
//        try (FileReader reader = new FileReader(filePath)) {
//            Object obj = parser.parse(reader);
//            JSONArray reviews = (JSONArray) obj;
//
//            StringBuilder displayText = new StringBuilder();
//            for (Object reviewObj : reviews) {
//                JSONObject review = (JSONObject) reviewObj;
//                displayText.append("Name: ").append(review.get("Employee Name")).append("\n");
//                displayText.append("ID: ").append(review.get("Employee ID")).append("\n");
//                displayText.append("Rating: ").append(review.get("Review Rating")).append("\n");
//                displayText.append("Notes: ").append(review.get("Review Notes")).append("\n");
//                displayText.append("-----\n");
//            }
//
//            reviewList.setText(displayText.toString());
//        } catch (Exception e) {
//            reviewList.setText("Error loading employee reviews: " + e.getMessage());
//        }
//    }


//    private void saveEmployeeReviewToFile(String name, String id, int rating, String notes) {
//        String filePath = "EmployeeReviews.json";
//        JSONParser parser = new JSONParser();
//        JSONArray reviews;
//
//        // Read existing JSON data from the file
//        try (FileReader reader = new FileReader(filePath)) {
//            Object obj = parser.parse(reader);
//            reviews = (JSONArray) obj;
//        } catch (Exception e) {
//            // If the file doesn't exist or is empty, create a new array
//            reviews = new JSONArray();
//        }
//
//        // Create the new review
//        JSONObject review = new JSONObject();
//        review.put("Employee Name", name);
//        review.put("Employee ID", id);
//        review.put("Review Rating", rating);
//        review.put("Review Notes", notes);
//
//        // Add the new review to the array
//        reviews.add(review);
//
//        // Write the updated array back to the file
//        try (FileWriter file = new FileWriter(filePath)) {
//            file.write(reviews.toJSONString());
//            file.flush();
//        } catch (IOException e) {
//            System.out.println("Error writing to EmployeeReviews.json: " + e.getMessage());
//        }
//    }


//    public void savePTORequestToFile(String username, LocalDate startDate, LocalDate endDate, double totalRequestedHours) {
//        String filePath = "PTORequests.json";
//        JSONParser parser = new JSONParser();
//        JSONArray ptoRequests;
//
//        // Read existing JSON data from the file
//        try (FileReader reader = new FileReader(filePath)) {
//            Object obj = parser.parse(reader);
//            ptoRequests = (JSONArray) obj;
//        } catch (Exception e) {
//            // If the file doesn't exist or is empty, create a new array
//            ptoRequests = new JSONArray();
//        }
//
//        // Create the new PTO request
//        JSONObject ptoRequest = new JSONObject();
//        ptoRequest.put("Username", username);
//        ptoRequest.put("Date Range", startDate + " to " + endDate);
//        ptoRequest.put("Total Hours Requested", totalRequestedHours);
//
//        // Add the new request to the array
//        ptoRequests.add(ptoRequest);
//
//        // Write the updated array back to the file
//        try (FileWriter file = new FileWriter(filePath)) {
//            file.write(ptoRequests.toJSONString());
//            file.flush();
//        } catch (IOException e) {
//            System.out.println("Error writing to JSON file: " + e.getMessage());
//        }
//    }
//
//    public void loadPTORequests(TextArea requestList) {
//        String filePath = "PTORequests.json";
//        JSONParser parser = new JSONParser();
//
//        try (FileReader reader = new FileReader(filePath)) {
//            Object obj = parser.parse(reader);
//            JSONArray ptoRequests = (JSONArray) obj;
//
//            StringBuilder displayText = new StringBuilder();
//            for (int i = 0; i < ptoRequests.size(); i++) {
//                JSONObject request = (JSONObject) ptoRequests.get(i);
//                displayText.append("Request #").append(i + 1).append(": ");
//                displayText.append("User: ").append(request.get("Username")).append(", ");
//                displayText.append("Dates: ").append(request.get("Date Range")).append(", ");
//                displayText.append("Hours: ").append(request.get("Total Hours Requested")).append("\n");
//            }
//
//            requestList.setText(displayText.toString());
//        } catch (Exception e) {
//            requestList.setText("Error loading PTO requests: " + e.getMessage());
//        }
//    }
//
//    public void handleRequest(TextArea requestList, boolean approve, Label messageLabel) {
//        String[] requests = requestList.getText().split("\n");
//        if (requests.length == 0 || requests[0].isEmpty()) {
//            messageLabel.setText("No requests to process.");
//            return;
//        }
//
//        // Parse the first request for simplicity
//        String selectedRequest = requests[0];
//        String[] requestDetails = selectedRequest.split(", ");
//        if (approve) {
//            String username = requestDetails[0].split(": ")[1];
//            String dateRange = requestDetails[1].split(": ")[1];
//            double totalHours = Double.parseDouble(requestDetails[2].split(": ")[1]);
//
//            JSONObject approvedRequest = new JSONObject();
//            approvedRequest.put("Username", username);
//            approvedRequest.put("Date Range", dateRange);
//            approvedRequest.put("Total Hours Requested", totalHours);
//
//            writeApprovedRequestToFile(approvedRequest);
//            messageLabel.setText("Approved: " + selectedRequest);
//        } else {
//            messageLabel.setText("Denied: " + selectedRequest);
//        }
//
//        // Remove the processed request from the JSON file
//        removeRequestFromFile(selectedRequest);
//
//        // Reload the request list
//        loadPTORequests(requestList);
//    }
//
//    public void removeRequestFromFile(String selectedRequest) {
//        String filePath = "PTORequests.json";
//        JSONParser parser = new JSONParser();
//        JSONArray ptoRequests;
//
//        try (FileReader reader = new FileReader(filePath)) {
//            // Read existing requests from the file
//            Object obj = parser.parse(reader);
//            ptoRequests = (JSONArray) obj;
//
//            // Find and remove the matching request
//            for (int i = 0; i < ptoRequests.size(); i++) {
//                JSONObject request = (JSONObject) ptoRequests.get(i);
//
//                String username = (String) request.get("Username");
//                String dateRange = (String) request.get("Date Range");
//                double totalHours = (double) request.get("Total Hours Requested");
//
//                // Match the request string to the JSON object
//                String jsonString = "User: " + username + ", Dates: " + dateRange + ", Hours: " + totalHours;
//                if (selectedRequest.contains(jsonString)) {
//                    ptoRequests.remove(i);
//                    break;
//                }
//            }
//
//            // Write the updated list back to the file
//            try (FileWriter writer = new FileWriter(filePath)) {
//                writer.write(ptoRequests.toJSONString());
//                writer.flush();
//            }
//        } catch (Exception e) {
//            System.out.println("Error while removing request from file: " + e.getMessage());
//        }
//    }
//
//
//    public void writeApprovedRequestToFile(JSONObject approvedRequest) {
//        String filePath = "ApprovedPTO.json";
//        JSONParser parser = new JSONParser();
//        JSONArray approvedRequests;
//
//        try (FileReader reader = new FileReader(filePath)) {
//            Object obj = parser.parse(reader);
//            approvedRequests = (JSONArray) obj;
//        } catch (Exception e) {
//            approvedRequests = new JSONArray();
//        }
//
//        approvedRequests.add(approvedRequest);
//
//        try (FileWriter file = new FileWriter(filePath)) {
//            file.write(approvedRequests.toJSONString());
//            file.flush();
//        } catch (IOException e) {
//            System.out.println("Error writing to ApprovedPTO.json: " + e.getMessage());
//        }
//    }

//    private void logout(Stage primaryStage) {
//        // Start the SignInPage
//        SignInPage signInPage = new SignInPage(); // Assuming you have a SignInPage class
//        signInPage.start(primaryStage); // Restart the SignInPage in the same stage
//    }
}