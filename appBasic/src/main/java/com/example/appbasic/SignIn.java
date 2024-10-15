package com.example.appbasic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignIn extends Application {

    private static final String EMPLOYEE_USERNAME = "employee001";
    private static final String EMPLOYEE_PASSWORD = "password";
    private static final String MANAGER_USERNAME = "manager001";
    private static final String MANAGER_PASSWORD = "managerPass";
    private static final String PAYROLL_USERNAME = "payroll001";
    private static final String PAYROLL_PASSWORD = "payrollPass";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(SignIn.class.getResource("SignIn.fxml"));
        primaryStage.setTitle("Login Form");

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label resultLabel = new Label();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(event -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();
            if (enteredUsername.equals(EMPLOYEE_USERNAME) && enteredPassword.equals(EMPLOYEE_PASSWORD)) {
                resultLabel.setText("Employee login successful!");
                // Open employee interface
            } else if (enteredUsername.equals(MANAGER_USERNAME) && enteredPassword.equals(MANAGER_PASSWORD)) {
                resultLabel.setText("Manager login successful!");
                // Open manager interface
            } else if (enteredUsername.equals(PAYROLL_USERNAME) && enteredPassword.equals(PAYROLL_PASSWORD)) {
                resultLabel.setText("Payroll manager login successful!");
                // Open payroll interface
            } else {
                resultLabel.setText("Login failed. Please check your credentials.");
            }
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, resultLabel);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
