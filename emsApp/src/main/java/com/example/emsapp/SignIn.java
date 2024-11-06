package com.example.emsapp;

import com.example.emsapp.Roles.EmployeeRole;
import com.example.emsapp.Roles.ManagerRole;
import com.example.emsapp.Roles.HRManagerRole;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignIn extends Application {

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

            // Use loginController to authenticate and assign the role
            Object role = loginController.login(enteredUsername, enteredPassword);

            if (role instanceof EmployeeRole) {
                resultLabel.setText("Employee login successful!");
                redirectToEmployeeHome(primaryStage);
            } else if (role instanceof ManagerRole) {
                resultLabel.setText("Manager login successful!");
                redirectToManagerHome(primaryStage);
            } else if (role instanceof HRManagerRole) {
                resultLabel.setText("HR Manager login successful!");
                redirectToHRManagerHome(primaryStage);
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

        // Show the window
        primaryStage.show();
    }

    // Method to redirect to EmployeeHome
    private void redirectToEmployeeHome(Stage primaryStage) {
        EmployeeHome employeeHome = new EmployeeHome();
        try {
            employeeHome.start(primaryStage); // Redirect to Employee Home screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to redirect to ManagerHome
    private void redirectToManagerHome(Stage primaryStage) {
        ManagerHome managerHome = new ManagerHome();
        try {
            managerHome.start(primaryStage); // Redirect to Manager Home screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to redirect to HRManagerHome
    private void redirectToHRManagerHome(Stage primaryStage) {
        HRManagerHome hrManagerHome = new HRManagerHome();
        try {
            hrManagerHome.start(primaryStage); // Redirect to HR Manager Home screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
