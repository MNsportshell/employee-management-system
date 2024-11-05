package com.example.emsapp;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeePayroll extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Payroll System");

        // Label for file selection
        Label instructionLabel = new Label("Select Text file:");

        // Button to open file chooser
        Button selectFileButton = new Button("Select Text File");
        Label selectedFileLabel = new Label("No file selected");

        // Button to generate and display payslips
        Button generatePayslipButton = new Button("Generate Payslip");

        // Button to save payslip file
        Button savePayslipButton = new Button("Save Payslip File");

        // TextArea to display results
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // FileChooser for selecting text and save files
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        final File[] selectedFile = {null};
        final File[] saveFile = {null};

        // Action for selecting a text file
        selectFileButton.setOnAction(event -> {
            selectedFile[0] = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile[0] != null) {
                selectedFileLabel.setText("Selected file: " + selectedFile[0].getName());
            } else {
                selectedFileLabel.setText("No file selected");
            }
        });

        // Action for generating payslips
        generatePayslipButton.setOnAction(event -> {
            if (selectedFile[0] != null) {
                try {
                    String payslipData = generatePayslipsFromText(selectedFile[0]);
                    resultArea.setText(payslipData);
                } catch (IOException e) {
                    resultArea.setText("Error generating payslips.");
                    e.printStackTrace();
                }
            } else {
                resultArea.setText("Please select a valid text file first.");
            }
        });

        // Action for saving payslip data to a file
        savePayslipButton.setOnAction(event -> {
            if (selectedFile[0] != null) {
                FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.setTitle("Save Payslip File");
                saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                saveFile[0] = saveFileChooser.showSaveDialog(primaryStage);

                if (saveFile[0] != null) {
                    try {
                        generatePayslipsToFile(selectedFile[0], saveFile[0]);
                        resultArea.setText("Payslips saved to: " + saveFile[0].getAbsolutePath());
                    } catch (IOException e) {
                        resultArea.setText("Error saving payslips to file.");
                        e.printStackTrace();
                    }
                } else {
                    resultArea.setText("No save location selected.");
                }
            } else {
                resultArea.setText("Please select a valid text file first.");
            }
        });

        // VBox layout
        VBox root = new VBox(10);
        root.getChildren().addAll(instructionLabel, selectFileButton, selectedFileLabel, generatePayslipButton, savePayslipButton, resultArea);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private String generatePayslipsFromText(File textFile) throws IOException {
        StringBuilder payslipData = new StringBuilder();
        payslipData.append("Employee Payslips:\n");

        // Define a regex pattern to extract each field
        Pattern pattern = Pattern.compile(
                "Employee ID: (\\S+)\\s+" +
                        "Employee Name: (\\S+ \\S+)\\s+" +
                        "Hours Worked: (\\d+(\\.\\d+)?)\\s+" +
                        "Overtime Hours: (\\d+(\\.\\d+)?)\\s+" +
                        "Hourly Pay Rate: (\\d+(\\.\\d+)?)\\s+" +
                        "Employee Email: (\\S+)"
        );

        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Remove the "Approved:" or "Denied:" prefix
                line = line.replaceFirst("^(Approved|Denied):\\s*", "");

                // Match the line against the pattern
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    // Extract matched groups
                    String employeeID = matcher.group(1);
                    String employeeName = matcher.group(2);
                    double hoursWorked = Double.parseDouble(matcher.group(3));
                    double overtime = Double.parseDouble(matcher.group(5));
                    double payRate = Double.parseDouble(matcher.group(7));
                    String employeeEmail = matcher.group(9);

                    // Calculate salary
                    double employeeSalary = (hoursWorked * payRate) + (overtime * payRate * 1.5);

                    payslipData.append("Employee ID: ").append(employeeID)
                            .append(", Name: ").append(employeeName)
                            .append(", Email: ").append(employeeEmail)
                            .append(", Salary: $").append(employeeSalary)
                            .append("\nPayslip generated for ").append(employeeName).append("\n\n");
                } else {
                    System.out.println("Warning: Incorrect format for line: " + line);
                }
            }
        }

        return payslipData.toString();
    }




    private void generatePayslipsToFile(File textFile, File saveFile) throws IOException {
        // Define the same regex pattern used in generatePayslipsFromText
        Pattern pattern = Pattern.compile(
                "Employee ID: (\\S+)\\s+" +
                        "Employee Name: (\\S+ \\S+)\\s+" +
                        "Hours Worked: (\\d+(\\.\\d+)?)\\s+" +
                        "Overtime Hours: (\\d+(\\.\\d+)?)\\s+" +
                        "Hourly Pay Rate: (\\d+(\\.\\d+)?)\\s+" +
                        "Employee Email: (\\S+)"
        );

        try (BufferedReader reader = new BufferedReader(new FileReader(textFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Remove the "Approved:" or "Denied:" prefix
                line = line.replaceFirst("^(Approved|Denied):\\s*", "");

                // Match the line against the pattern
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    // Extract matched groups
                    String employeeID = matcher.group(1);
                    String employeeName = matcher.group(2);
                    double hoursWorked = Double.parseDouble(matcher.group(3));
                    double overtime = Double.parseDouble(matcher.group(5));
                    double payRate = Double.parseDouble(matcher.group(7));
                    String employeeEmail = matcher.group(9);

                    // Calculate salary
                    double employeeSalary = (hoursWorked * payRate) + (overtime * payRate * 1.5);

                    // Write formatted payslip information to the file
                    writer.write("Employee ID: " + employeeID + ", Name: " + employeeName +
                            ", Email: " + employeeEmail + ", Pay: $" + employeeSalary + "\n");
                } else {
                    System.out.println("Warning: Incorrect format for line: " + line);
                }
            }
        }
    }

}
