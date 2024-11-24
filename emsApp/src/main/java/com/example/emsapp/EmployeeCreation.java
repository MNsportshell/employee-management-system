package com.example.emsapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeCreation extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Employee Account");

        // Labels and input fields
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        Label roleLabel = new Label("Assign Role:");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Manager");
        roleComboBox.setPromptText("Select Role");

        Button createButton = new Button("Create Employee");

        // Display area for output
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        // Create button action
        createButton.setOnAction(event -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String role = roleComboBox.getValue();

            if (firstName.isEmpty() || lastName.isEmpty() || role == null) {
                outputArea.setText("Please fill out all fields.");
            } else {
                outputArea.setText("Employee Account Created:\n"
                        + "Name: " + firstName + " " + lastName + "\n"
                        + "Role: " + role);
                firstNameField.clear();
                lastNameField.clear();
                roleComboBox.getSelectionModel().clearSelection();
            }
        });

        // Layout configuration
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(firstNameLabel, firstNameField, lastNameLabel, lastNameField, roleLabel, roleComboBox, createButton, outputArea);

        // Scene and stage setup
        Scene scene = new Scene(layout, 350, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
