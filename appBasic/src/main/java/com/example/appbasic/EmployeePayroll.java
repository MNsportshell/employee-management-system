package com.example.appbasic;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class EmployeePayroll extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Payroll System");

        // Label for file selection
        Label instructionLabel = new Label("Select Excel file:");

        // Button to open file chooser
        Button selectFileButton = new Button("Select Excel File");
        Label selectedFileLabel = new Label("No file selected");

        // Button to generate payslips
        Button generatePayslipButton = new Button("Generate Payslip");

        // Submit and Email Payslip button
        Button submitAndEmailButton = new Button("Submit & Email");

        // TextArea to display results
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // FileChooser to select Excel file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        final File[] selectedFile = {null};

        // Action for selecting an Excel file
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
                    String payslipData = generatePayslipsFromExcel(selectedFile[0]);
                    resultArea.setText(payslipData);
                } catch (IOException e) {
                    resultArea.setText("Error");
                    e.printStackTrace();
                }
            } else {
                resultArea.setText("select valid file");
            }
        });

        // Action for submitting and emailing payslips
        submitAndEmailButton.setOnAction(event -> {
            if (selectedFile[0] != null) {
                try {
                    String emailResult = generatePayslipsAndEmail(selectedFile[0]);
                    resultArea.setText(emailResult);
                } catch (IOException e) {
                    resultArea.setText("Error sending emails.");
                    e.printStackTrace();
                }
            } else {
                resultArea.setText("Please select a valid Excel file first.");
            }
        });

        // Vbox layout
        VBox root = new VBox(10);
        root.getChildren().addAll(instructionLabel, selectFileButton, selectedFileLabel, generatePayslipButton, resultArea, submitAndEmailButton);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // read the Excel file and generate payslips
    private String generatePayslipsFromExcel(File excelFile) throws IOException {
        StringBuilder payslipData = new StringBuilder();

        FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        payslipData.append("Employee Payslips:\n");

        // Reading Excel file and generating payslip for each employee
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getRowNum() == 0) continue;

            // Reading employee data
            Cell nameCell = row.getCell(0);
            Cell hoursWorkedCell = row.getCell(1);
            Cell overtimeCell = row.getCell(2);

            if (nameCell != null && hoursWorkedCell != null && overtimeCell != null) {
                String employeeName = nameCell.getStringCellValue();
                double hoursWorked = hoursWorkedCell.getNumericCellValue();
                double overtime = overtimeCell.getNumericCellValue();

                // Calculate salary using the formula
                double employeeSalary = (hoursWorked * 20) + (overtime * 22);

                // Generating payslip data for each employee
                payslipData.append("Employee: ").append(employeeName)
                        .append(", Salary: $").append(employeeSalary)
                        .append("\nPayslip generated for ").append(employeeName).append("\n\n");
            }
        }

        fis.close();
        workbook.close();

        return payslipData.toString();
    }

    // Method to read the Excel file, payslip and email
    private String generatePayslipsAndEmail(File excelFile) throws IOException {
        StringBuilder resultData = new StringBuilder();

        FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        resultData.append("Employee Payslips and Emails:\n");

        // Reading Excel file and generating payslip for each employee
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getRowNum() == 0) continue;

            // Reading employee data
            Cell nameCell = row.getCell(0);
            Cell hoursWorkedCell = row.getCell(1);
            Cell overtimeCell = row.getCell(2);
            Cell emailCell = row.getCell(3);

            if (nameCell != null && hoursWorkedCell != null && overtimeCell != null && emailCell != null) {
                String employeeName = nameCell.getStringCellValue();
                double hoursWorked = hoursWorkedCell.getNumericCellValue();
                double overtime = overtimeCell.getNumericCellValue();
                String employeeEmail = emailCell.getStringCellValue();

                // Calculate the salary
                double salary = (hoursWorked * 20) + (overtime * 22);

                // Generating payslip data
                String payslip = "Employee: " + employeeName + "\n" +
                        "Hours Worked: " + hoursWorked + "\n" +
                        "Overtime: " + overtime + "\n" +
                        "Salary: $" + salary + "\n";

                resultData.append(payslip).append("\n");

                // Send the payslip via email
                sendEmail(employeeEmail, "Your Payslip", payslip);
            }
        }

        fis.close();
        workbook.close();

        return resultData.toString();
    }

    // Sending email
    private void sendEmail(String to, String subject, String body) {
        // demo for sending email
        System.out.println("Email sent to: " + to + "\nSubject: " + subject + "\nBody:\n" + body);
    }
}
