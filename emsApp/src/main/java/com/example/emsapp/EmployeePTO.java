package com.example.emsapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class EmployeePTO extends Application {

    private static double empPTOBalance = 60.0;
    private double availablePTOBalance = empPTOBalance;
    private File selectedDirectory;  // Directory to save PTO requests

    public EmployeePTO() {
        // Default constructor logic (if needed)
    }

    public EmployeePTO(double availablePTOBalance) {
        this.availablePTOBalance = availablePTOBalance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("PTO Request Form");
        Scene scene = new Scene(new Group(), 600, 500);

        // Text fields for Employee ID and Name
        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");
        TextField employeeNameField = new TextField();
        employeeNameField.setPromptText("Enter Employee Name");

        // PTO type selection ComboBox
        ComboBox<String> ptoTypeComboBox = new ComboBox<>();
        ptoTypeComboBox.getItems().addAll("Vacation", "Sick Leave", "Personal Leave", "Other");
        ptoTypeComboBox.setPromptText("Select PTO Type");

        // Date pickers for start and end dates
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        // Buttons
        Button chooseDirectoryButton = new Button("Choose Directory to Save Requests");
        Button submitButton = new Button("Submit Request");
        Label notification = new Label();

        // Directory chooser to select a folder for saving requests
        chooseDirectoryButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Directory to Save PTO Requests");
            selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                notification.setText("Directory selected: " + selectedDirectory.getPath());
            } else {
                notification.setText("No directory selected.");
            }
        });

        // Submit button action to save the PTO request in the selected directory
        submitButton.setOnAction(e -> {
            String employeeId = employeeIdField.getText();
            String employeeName = employeeNameField.getText();
            String selectedPtoType = ptoTypeComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (!employeeId.isEmpty() && !employeeName.isEmpty() && selectedPtoType != null
                    && startDate != null && endDate != null && selectedDirectory != null) {

                int ptoHours = calculatePtoHours(startDate, endDate);
                String record = "PTO request for ID: " + employeeId + ", Name: " + employeeName +
                        " (" + selectedPtoType + ") from " + startDate + " to " + endDate +
                        " (" + ptoHours + " hours)\n";

                // Save the request as a new file in the selected directory
                String fileName = "PTO_Request_" + employeeId + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt";
                File requestFile = new File(selectedDirectory, fileName);

                try (FileWriter writer = new FileWriter(requestFile)) {
                    writer.write(record);
                    notification.setText("PTO request submitted and saved to " + requestFile.getPath());
                } catch (IOException ex) {
                    notification.setText("Error saving request: " + ex.getMessage());
                }
            } else {
                notification.setText("Please complete all fields and select a directory before submitting.");
            }
        });

        // Set up the layout
        GridPane grid = new GridPane();
        grid.setVgap(10.0);
        grid.setHgap(10.0);
        grid.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        grid.add(new Label("Employee ID: "), 0, 0);
        grid.add(employeeIdField, 1, 0);
        grid.add(new Label("Employee Name: "), 0, 1);
        grid.add(employeeNameField, 1, 1);
        grid.add(new Label("PTO Type: "), 0, 2);
        grid.add(ptoTypeComboBox, 1, 2);
        grid.add(new Label("Start Date: "), 0, 3);
        grid.add(startDatePicker, 1, 3);
        grid.add(new Label("End Date: "), 0, 4);
        grid.add(endDatePicker, 1, 4);
        grid.add(chooseDirectoryButton, 0, 5);
        grid.add(submitButton, 0, 6);
        grid.add(notification, 1, 6);

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        stage.setScene(scene);
        stage.show();
    }

    // Calculate PTO hours by counting weekdays (Mon-Fri) only
    private int calculatePtoHours(LocalDate startDate, LocalDate endDate) {
        int ptoHours = 0;

        // Iterate over the dates, adding 8 hours for each weekday
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                ptoHours += 8;
            }
            date = date.plusDays(1);
        }

        return ptoHours;
    }
}
