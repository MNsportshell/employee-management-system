package com.example.emsapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignIn extends Application {

    // Dummy credentials for Employee, Manager, Payroll, and HR Manager
    private static final String EMPLOYEE_USERNAME = "emp";
    private static final String EMPLOYEE_PASSWORD = "pw";

    private static final String MANAGER_USERNAME = "mgr";
    private static final String MANAGER_PASSWORD = "pw";

    private static final String HR_USERNAME = "hr";
    private static final String HR_PASSWORD = "pw";

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
                redirectToManager(primaryStage);

            } else if (enteredUsername.equals(HR_USERNAME) && enteredPassword.equals(HR_PASSWORD)) {
                resultLabel.setText("HR Manager login successful!");
                // Redirect to HRManager
                redirectToHRManager(primaryStage);

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
        EmployeeHome employeeHome = new EmployeeHome();
        try {
            employeeHome.start(primaryStage); // Redirect to PTO request screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to redirect to EmployeeReview for Manager
    private void redirectToManager(Stage primaryStage) {
        ManagerHome managerHome = new ManagerHome();
        try {
            managerHome.start(primaryStage); // Redirect to Employee Review screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to redirect to HRManager
    private void redirectToHRManager(Stage primaryStage) {
        HRManagerHome hrManagerHome = new HRManagerHome();
        try {
            hrManagerHome.start(primaryStage); // Redirect to HR Manager screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


