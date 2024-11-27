package org.example.emsfinal.HomePages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.emsfinal.SignInPage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EmployeeHome implements HomeScreen {

    private double ptoBalance = 40.0;
    private String username = "Jane Doe";

    public EmployeeHome() {
        this.username = username;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Dashboard");

        Label welcomeLabel = new Label("Welcome " + username + " to the Employee Dashboard!");

        Button ptoButton = new Button("PTO");
        ptoButton.setOnAction(e -> openPTOWindow());
        Button viewReviewsButton = new Button("View Employee Reviews");
        viewReviewsButton.setOnAction(e -> viewEmployeeReviews());
        Button payrollButton = new Button("Payroll");
        payrollButton.setOnAction(e -> openPayrollWindow());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(welcomeLabel, 0, 0);
        grid.add(ptoButton, 0, 1);
        grid.add(viewReviewsButton, 0, 2);
        grid.add(payrollButton, 0, 3);
        grid.add(logoutButton, 0, 4);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void viewEmployeeReviews() {
        Stage reviewStage = new Stage();
        reviewStage.setTitle("Employee Reviews");

        TextArea reviewTextArea = new TextArea();
        reviewTextArea.setEditable(false);

        String filePath = "EmployeeReviews.json";
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);

            JSONArray employeeReviews;
            if (obj instanceof JSONObject) {
                JSONObject rootObject = (JSONObject) obj;
                employeeReviews = (JSONArray) rootObject.get("EmployeeReviews");
            } else if (obj instanceof JSONArray) {
                employeeReviews = (JSONArray) obj;
            } else {
                throw new IllegalArgumentException("Invalid JSON structure");
            }

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

        ScrollPane scrollPane = new ScrollPane(reviewTextArea);
        Scene scene = new Scene(scrollPane, 600, 400);
        reviewStage.setScene(scene);
        reviewStage.show();
    }

    public void openPTOWindow() {
        Stage ptoStage = new Stage();
        ptoStage.setTitle("PTO Management");

        Label messageLabel = new Label();


        Button viewPTOButton = new Button("View PTO Balance");
        viewPTOButton.setOnAction(e -> messageLabel.setText("PTO Balance: " + ptoBalance + " hours"));


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


        Scene scene = new Scene(grid, 400, 300);
        ptoStage.setScene(scene);
        ptoStage.show();
    }

    public void openPayrollWindow(String userRole) {
        Stage payrollStage = new Stage();
        payrollStage.setTitle("Payroll");

        Label welcomeLabel = new Label("Welcome to the Payroll System");

        // Button to enter hours worked
        Button enterHoursButton = new Button("Enter Hours Worked");
        enterHoursButton.setOnAction(e -> recordHoursWindow());

        // Button to approve payroll
        Button approvePayrollButton = new Button("Approve Payroll");
        approvePayrollButton.setOnAction(e -> approvePayroll());

        // Button to calculate and send out payslips
        Button payslipButton = new Button("Payslip Calculation/Disbursal");
        payslipButton.setOnAction(e -> calcPayslips("ApprovedPayroll.json"));

        // Make the "Payslip Calculation/Disbursal" button invisible if the user is not an HRManager
        if ("HRManager".equalsIgnoreCase(userRole)) {
            payslipButton.setVisible(false); // Hides the button from view
        }

        // Disable the "Approve Payroll" button if the user is an Employee
        if ("Employee".equalsIgnoreCase(userRole)) {
            approvePayrollButton.setVisible(false);
        }

        VBox initialLayout = new VBox(10, welcomeLabel, enterHoursButton, approvePayrollButton, payslipButton);
        initialLayout.setAlignment(Pos.CENTER);
        initialLayout.setPadding(new Insets(10));

        Scene initialScene = new Scene(initialLayout, 400, 200);
        payrollStage.setScene(initialScene);
        payrollStage.show();
    }

    // Overloaded version for default role
    public void openPayrollWindow() {
        openPayrollWindow("Employee");
    }


    private void recordHoursWindow() {
        Stage recordStage = new Stage();
        recordStage.setTitle("Record Hours");

        // UI elements
        Label employeeIdLabel = new Label("Enter Your Employee ID:");
        TextField employeeIdField = new TextField();

        Label hoursLabel = new Label("Enter Hours Worked This Week:");
        TextField hoursField = new TextField();

        Label roleLabel = new Label("Select Your Role:");
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Employee", "Manager", "HRManager");
        roleDropdown.setPromptText("Select Role");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String employeeId = employeeIdField.getText();
            String hours = hoursField.getText();
            String role = roleDropdown.getValue();

            if (employeeId.isEmpty() || hours.isEmpty() || role == null) {
                System.out.println("All fields must be filled.");
            } else {
                try {
                    double reportedHours = Double.parseDouble(hours);
                    recordHours(employeeId, reportedHours, role);
                    System.out.println("Hours recorded successfully!");
                    recordStage.close();
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid hours format.");
                }
            }
        });

        VBox layout = new VBox(10, employeeIdLabel, employeeIdField, hoursLabel, hoursField, roleLabel, roleDropdown, submitButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        recordStage.setScene(scene);
        recordStage.show();
    }

    private void recordHours(String employeeId, double hours, String role) {
        String filePath = "PayrollHours.json";

        JSONArray payrollData = new JSONArray();

        // Load existing data from file
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                payrollData = (JSONArray) obj;
            }
        } catch (IOException | ParseException ex) {
            System.out.println("No existing payroll data found. Creating a new file.");
        }

        // Add new entry
        JSONObject entry = new JSONObject();
        entry.put("Employee ID", employeeId);
        entry.put("Hours Reported", hours);
        entry.put("Role", role);
        payrollData.add(entry);

        // Write back to file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(payrollData.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing to payroll file: " + e.getMessage());
        }
    }

    private void approvePayroll() {
        Stage approvalStage = new Stage();
        approvalStage.setTitle("Approve/Deny Payroll Submissions");

        Label instructionLabel = new Label("Review Payroll Submissions");

        VBox approvalLayout = new VBox(10);
        approvalLayout.setAlignment(Pos.CENTER);
        approvalLayout.setPadding(new Insets(10));
        approvalLayout.getChildren().add(instructionLabel);

        JSONArray payrollSubmissions = loadPayrollSubmissions("PayrollHours.json");

        if (payrollSubmissions == null || payrollSubmissions.isEmpty()) {
            Label noSubmissionsLabel = new Label("No payroll submissions found.");
            approvalLayout.getChildren().add(noSubmissionsLabel);
        } else {
            JSONArray approvedPayrolls = new JSONArray();
            for (int i = 0; i < payrollSubmissions.size(); i++) {
                JSONObject submission = (JSONObject) payrollSubmissions.get(i);
                String employeeId = (String) submission.get("Employee ID");
                double hours = (double) submission.get("Hours Reported");
                String role = (String) submission.get("Role");

                Label submissionLabel = new Label("Employee ID: " + employeeId + ", Hours: " + hours + ", Role: " + role);
                Button approveButton = new Button("Approve");
                Button denyButton = new Button("Deny");

                // Approve action
                approveButton.setOnAction(e -> {
                    approvedPayrolls.add(submission);
                    removeSubmissionFromFile(submission, "PayrollHours.json");
                    approvalLayout.getChildren().remove(submissionLabel);
                    approvalLayout.getChildren().remove(approveButton);
                    approvalLayout.getChildren().remove(denyButton);
                });

                // Deny action
                denyButton.setOnAction(e -> {
                    removeSubmissionFromFile(submission, "PayrollHours.json");
                    approvalLayout.getChildren().remove(submissionLabel);
                    approvalLayout.getChildren().remove(approveButton);
                    approvalLayout.getChildren().remove(denyButton);
                });

                approvalLayout.getChildren().addAll(submissionLabel, approveButton, denyButton);
            }

            // Save approved payrolls
            Button saveButton = new Button("Save Approved Payrolls");
            saveButton.setOnAction(e -> {
                saveApprovedPayrolls("ApprovedPayroll.json", approvedPayrolls);
                approvalStage.close();
            });

            approvalLayout.getChildren().add(saveButton);
        }

        Scene scene = new Scene(approvalLayout, 500, 500); // Adjust size for additional information
        approvalStage.setScene(scene);
        approvalStage.show();
    }

    private void removeSubmissionFromFile(JSONObject submission, String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);

            if (obj instanceof JSONArray) {
                JSONArray submissions = (JSONArray) obj;
                submissions.remove(submission);

                // Write updated array back to file
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(submissions.toJSONString());
                    writer.flush();
                    System.out.println("Submission removed from file.");
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error updating payroll submissions: " + e.getMessage());
        }
    }

    private void calcPayslips(String approvedPayrollFile) {
        Stage payslipStage = new Stage();
        payslipStage.setTitle("Payslip Approval");

        // Scrollable text area to display approved payslips
        TextArea payslipTextArea = new TextArea();
        payslipTextArea.setEditable(false); // Make it read-only

        // Load approved payroll submissions
        JSONArray approvedPayrolls = loadApprovedPayrolls(approvedPayrollFile);

        if (approvedPayrolls == null || approvedPayrolls.isEmpty()) {
            payslipTextArea.setText("No submissions found in ApprovedPayroll.json.");
        } else {
            // UI to approve or deny each submission
            VBox approvalLayout = new VBox(10);
            approvalLayout.setPadding(new Insets(10));
            approvalLayout.setAlignment(Pos.CENTER);

            StringBuilder approvedContent = new StringBuilder(); // To accumulate approved payslips

            for (int i = 0; i < approvedPayrolls.size(); i++) {
                JSONObject submission = (JSONObject) approvedPayrolls.get(i);
                String employeeId = (String) submission.get("Employee ID");
                double hours = (double) submission.get("Hours Reported");
                String role = (String) submission.get("Role");

                double payRate = 0.0;
                switch (role) {
                    case "Employee":
                        payRate = 20.0;
                        break;
                    case "Manager":
                        payRate = 30.0;
                        break;
                    case "HRManager":
                        payRate = 40.0;
                        break;
                    default:
                        continue; // Skip unknown roles
                }

                double totalPay = hours * payRate;

                // Display submission details
                Label submissionLabel = new Label("Employee ID: " + employeeId + ", Role: " + role +
                        ", Hours: " + hours + ", Pay: $" + totalPay);
                Button approveButton = new Button("Approve");
                Button denyButton = new Button("Deny");

                // Approve action
                double finalPayRate = payRate;
                approveButton.setOnAction(e -> {
                    // Append to approved content
                    approvedContent.append("--- Payslip ---\n")
                            .append("Employee ID: ").append(employeeId).append("\n")
                            .append("Role: ").append(role).append("\n")
                            .append("Hours Worked: ").append(hours).append("\n")
                            .append("Pay Rate: $").append(finalPayRate).append(" per hour\n")
                            .append("Total Pay: $").append(totalPay).append("\n")
                            .append("----------------\n\n");

                    // Refresh the TextArea with updated content
                    payslipTextArea.setText(approvedContent.toString());

                    // Remove the submission from the file
                    removeSubmissionFromFile(submission, approvedPayrollFile);

                    // Remove UI elements for this submission
                    approvalLayout.getChildren().removeAll(submissionLabel, approveButton, denyButton);
                });

                // Deny action
                denyButton.setOnAction(e -> {
                    // Remove the submission from the file
                    removeSubmissionFromFile(submission, approvedPayrollFile);

                    // Remove UI elements for this submission
                    approvalLayout.getChildren().removeAll(submissionLabel, approveButton, denyButton);
                });

                // Add UI elements for this submission
                approvalLayout.getChildren().addAll(submissionLabel, approveButton, denyButton);
            }

            // Add the TextArea to display approved payslips
            VBox rootLayout = new VBox(10, approvalLayout, new ScrollPane(payslipTextArea));
            rootLayout.setPadding(new Insets(10));
            rootLayout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(rootLayout, 600, 400);
            payslipStage.setScene(scene);
            payslipStage.show();
        }
    }


    private JSONArray loadApprovedPayrolls(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);

            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading approved payrolls: " + e.getMessage());
        }
        return new JSONArray();
    }

    // Method to send the generated payslip to the employee. Currently does not function
    private void sendPayslip(String employeeId, String role, double hours, double payRate, double totalPay) {
        System.out.println("Payslip sent to email of Employee ID: " + employeeId);
    }

    private JSONArray loadPayrollSubmissions(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error loading payroll submissions: " + e.getMessage());
        }
        return new JSONArray();
    }

    private void saveApprovedPayrolls(String filePath, JSONArray approvedPayrolls) {
        JSONArray existingApprovedPayrolls = new JSONArray();

        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);

            if (obj instanceof JSONArray) {
                existingApprovedPayrolls = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.out.println("No existing approved payrolls found. Starting fresh.");
        }

        existingApprovedPayrolls.addAll(approvedPayrolls);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(existingApprovedPayrolls.toJSONString());
            writer.flush();
            System.out.println("Approved payrolls saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving approved payrolls: " + e.getMessage());
        }
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

    void logout(Stage primaryStage) {

        SignInPage signInPage = new SignInPage(); // Assuming you have a SignInPage class
        signInPage.start(primaryStage); // Restart the SignInPage in the same stage
    }
}
