package com.example.appbasic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignIn extends Application {

    // Dummy credentials for Employee, Manager, and Payroll
    private static final String EMPLOYEE_USERNAME = "employee001";
    private static final String EMPLOYEE_PASSWORD = "password";

    private static final String MANAGER_USERNAME = "manager001";
    private static final String MANAGER_PASSWORD = "password";

    private static final String PAYROLL_USERNAME = "payroll001";
    private static final String PAYROLL_PASSWORD = "password";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Form");

        // Create labels for username and password fields
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        // Create text input fields for username and password
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Create a label to display the login result
        Label resultLabel = new Label();

        // Create a "Login" button
        Button loginButton = new Button("Login");

        // Set an action for the "Login" button to validate credentials
        loginButton.setOnAction(event -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            if (enteredUsername.equals(EMPLOYEE_USERNAME) && enteredPassword.equals(EMPLOYEE_PASSWORD)) {
                resultLabel.setText("Employee login successful!");
                // Redirect to EmployeePTO
                redirectToPTO(primaryStage);

            } else if (enteredUsername.equals(MANAGER_USERNAME) && enteredPassword.equals(MANAGER_PASSWORD)) {
                resultLabel.setText("Manager login successful!");
                // Redirect to EmployeeReview
                redirectToReview(primaryStage);

            } else if (enteredUsername.equals(PAYROLL_USERNAME) && enteredPassword.equals(PAYROLL_PASSWORD)) {
                resultLabel.setText("Payroll login successful!");
                // Redirect to EmployeePayroll (to be implemented)
                redirectToPayroll(primaryStage);

            } else {
                resultLabel.setText("Login failed. Please check your credentials.");
            }
        });

        // Create a layout (VBox) to arrange the elements
        VBox root = new VBox(10);
        root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, resultLabel);

        // Create the scene and set it in the stage
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);

        // Set the title of the window
        primaryStage.setTitle("Login Form App");

        // Show the window
        primaryStage.show();
    }

    // Method to redirect to EmployeePTO
    private void redirectToPTO(Stage primaryStage) {
        EmployeePTO employeePTO = new EmployeePTO();
        try {
            employeePTO.start(primaryStage); // Redirect to PTO request screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to redirect to EmployeeReview for Manager
    private void redirectToReview(Stage primaryStage) {
        EmployeeReview employeeReview = new EmployeeReview();
        try {
            employeeReview.start(primaryStage); // Redirect to Employee Review screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to redirect to EmployeePayroll (Placeholder)
    private void redirectToPayroll(Stage primaryStage) {
        EmployeePayroll employeePayroll = new EmployeePayroll();
        try {
            employeePayroll.start(primaryStage); // Redirect to Payroll screen (to be implemented)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
