package com.example.emsapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ManagerPTO extends Application {
    private File selectedFile;
    private TextArea fileDisplayArea;

    public ManagerPTO() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("PTO Request Form");
        Scene scene = new Scene(new Group(), 600, 550);

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

        // Buttons and labels
        Button fileChooserButton = new Button("Choose File to Save");
        Button submitButton = new Button("Submit Request");
        Button openFileButton = new Button("Open PTO Requests File");
        Label notification = new Label();

        // TextArea to display the file contents
        fileDisplayArea = new TextArea();
        fileDisplayArea.setPrefHeight(200);
        fileDisplayArea.setPrefWidth(500);

        // File chooser setup to select the file to save
        fileChooserButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File to Save PTO Requests");
            selectedFile = fileChooser.showSaveDialog(stage);
            if (selectedFile != null) {
                notification.setText("File selected: " + selectedFile.getPath());
            }
        });

        // Submit button action to record the PTO request in the selected file
        submitButton.setOnAction(e -> {
            String employeeId = employeeIdField.getText();
            String employeeName = employeeNameField.getText();
            String selectedPtoType = ptoTypeComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (!employeeId.isEmpty() && !employeeName.isEmpty() && selectedPtoType != null && selectedFile != null
                    && startDate != null && endDate != null) {

                int ptoHours = calculatePtoHours(startDate, endDate);
                String record = "PTO request for ID: " + employeeId + ", Name: " + employeeName +
                        " (" + selectedPtoType + ") from " + startDate + " to " + endDate +
                        " (" + ptoHours + " hours)\n";

                // Write the request to the selected file
                try (FileWriter writer = new FileWriter(selectedFile, true)) {
                    writer.write(record);
                    notification.setText("PTO request submitted and recorded.");
                } catch (IOException ex) {
                    notification.setText("Error writing to file: " + ex.getMessage());
                }
            } else {
                notification.setText("Please complete all fields and select a file.");
            }
        });

        // Open file button action to display PTO requests from the file
        openFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open PTO Requests File");
            File fileToOpen = fileChooser.showOpenDialog(stage);
            if (fileToOpen != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
                    StringBuilder fileContent = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }
                    fileDisplayArea.setText(fileContent.toString());
                    notification.setText("File loaded: " + fileToOpen.getPath());
                } catch (IOException ex) {
                    notification.setText("Error reading file: " + ex.getMessage());
                }
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
        grid.add(fileChooserButton, 0, 5);
        grid.add(submitButton, 0, 6);
        grid.add(openFileButton, 1, 5);
        grid.add(notification, 1, 6);
        grid.add(fileDisplayArea, 0, 7, 2, 1);  // Span the TextArea across two columns

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




