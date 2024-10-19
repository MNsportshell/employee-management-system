package com.example.appbasic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EmployeePTO extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("PTO Request Form");
        Scene scene = new Scene(new Group(), 500, 350);

        // ComboBox for employee selection
        final ComboBox<String> employeeComboBox = new ComboBox<>();
        employeeComboBox.getItems().addAll(
                "jacob.smith@example.com",
                "isabella.johnson@example.com",
                "ethan.williams@example.com",
                "emma.jones@example.com",
                "michael.brown@example.com"
        );
        employeeComboBox.setPromptText("Select Employee");

        // ComboBox for PTO type
        final ComboBox<String> ptoTypeComboBox = new ComboBox<>();
        ptoTypeComboBox.getItems().addAll(
                "Vacation",
                "Sick Leave",
                "Personal Leave",
                "Other"
        );
        ptoTypeComboBox.setPromptText("Select PTO Type");

        // Date pickers for start and end dates
        final DatePicker startDatePicker = new DatePicker();
        final DatePicker endDatePicker = new DatePicker();

        // Button to submit PTO request
        final Button submitButton = new Button("Submit Request");
        final Label notification = new Label();

        // Add action to the submit button
        submitButton.setOnAction(e -> {
            String selectedEmployee = employeeComboBox.getValue();
            String selectedPtoType = ptoTypeComboBox.getValue();
            String startDate = (startDatePicker.getValue() != null) ? startDatePicker.getValue().toString() : "N/A";
            String endDate = (endDatePicker.getValue() != null) ? endDatePicker.getValue().toString() : "N/A";

            if (selectedEmployee == null || selectedPtoType == null) {
                notification.setText("Please complete all fields.");
            } else {
                notification.setText("PTO request submitted for " + selectedEmployee +
                        " (" + selectedPtoType + ") from " + startDate + " to " + endDate);
            }
        });

        // Layout for the PTO form
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Add form elements to the grid
        grid.add(new Label("Employee: "), 0, 0);
        grid.add(employeeComboBox, 1, 0);
        grid.add(new Label("PTO Type: "), 0, 1);
        grid.add(ptoTypeComboBox, 1, 1);
        grid.add(new Label("Start Date: "), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("End Date: "), 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(submitButton, 0, 4);
        grid.add(notification, 1, 4);

        // Add the grid to the scene
        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        stage.setScene(scene);
        stage.show();
    }
}
