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
    private double availablePTOBalance;
    private File selectedDirectory;

    public EmployeePTO() {
        this.availablePTOBalance = empPTOBalance;
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

        // UI Elements
        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");
        TextField employeeNameField = new TextField();
        employeeNameField.setPromptText("Enter Employee Name");

        ComboBox<String> ptoTypeComboBox = new ComboBox<>();
        ptoTypeComboBox.getItems().addAll("Vacation", "Sick Leave", "Personal Leave", "Other");
        ptoTypeComboBox.setPromptText("Select PTO Type");

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        Button chooseDirectoryButton = new Button("Choose Directory to Save Requests");
        Button submitButton = new Button("Submit Request");
        Label notification = new Label();

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

        submitButton.setOnAction(e -> {
            String employeeId = employeeIdField.getText();
            String employeeName = employeeNameField.getText();
            String selectedPtoType = ptoTypeComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (!employeeId.isEmpty() && !employeeName.isEmpty() && selectedPtoType != null
                    && startDate != null && endDate != null && selectedDirectory != null) {

                int ptoHours = calculatePtoHours(startDate, endDate);

                // Check if sufficient balance is available
                if (ptoHours > availablePTOBalance) {
                    notification.setText("Insufficient PTO balance.");
                    return;
                }

                String record = String.format("PTO request for ID: %s, Name: %s (%s) from %s to %s (%d hours)%n",
                        employeeId, employeeName, selectedPtoType, startDate, endDate, ptoHours);

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

        // Layout setup
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

    public int calculatePtoHours(LocalDate startDate, LocalDate endDate) {
        int ptoHours = 0;
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

    public String getPTOType() {
        return "Vacation"; // placeholder
    }

    public LocalDate getStartDate() {
        return LocalDate.now(); // placeholder
    }

    public LocalDate getEndDate() {
        return LocalDate.now().plusDays(5); // placeholder
    }
}
