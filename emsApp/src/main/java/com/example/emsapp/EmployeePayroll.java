package com.example.emsapp;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
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

        // Button to generate and display payslips
        Button generatePayslipButton = new Button("Generate Payslip");

        // Button to save payslip file
        Button savePayslipButton = new Button("Save Payslip File");

        // TextArea to display results
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // FileChooser for selecting Excel and save files
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        final File[] selectedFile = {null};
        final File[] saveFile = {null};

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
                    resultArea.setText("Error generating payslips.");
                    e.printStackTrace();
                }
            } else {
                resultArea.setText("Please select a valid Excel file first.");
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
                resultArea.setText("Please select a valid Excel file first.");
            }
        });

        // VBox layout
        VBox root = new VBox(10);
        root.getChildren().addAll(instructionLabel, selectFileButton, selectedFileLabel, generatePayslipButton, savePayslipButton, resultArea);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to get the cell value as a string
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue()); // Convert to int if numeric
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    // Modified method to read the Excel file and generate payslips
    private String generatePayslipsFromExcel(File excelFile) throws IOException {
        StringBuilder payslipData = new StringBuilder();

        FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        payslipData.append("Employee Payslips:\n");

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getRowNum() == 0) continue;

            Cell idCell = row.getCell(0);
            Cell nameCell = row.getCell(1);
            Cell hoursWorkedCell = row.getCell(2);
            Cell overtimeCell = row.getCell(3);
            Cell payRateCell = row.getCell(4);

            if (idCell != null && nameCell != null && hoursWorkedCell != null && overtimeCell != null && payRateCell != null) {
                String employeeID = getCellValueAsString(idCell);
                String employeeName = nameCell.getStringCellValue();
                double hoursWorked = hoursWorkedCell.getNumericCellValue();
                double overtime = overtimeCell.getNumericCellValue();
                double payRate = payRateCell.getNumericCellValue();

                double employeeSalary = (hoursWorked * payRate) + (overtime * payRate * 1.5);

                payslipData.append("Employee ID: ").append(employeeID)
                        .append(", Name: ").append(employeeName)
                        .append(", Salary: $").append(employeeSalary)
                        .append("\nPayslip generated for ").append(employeeName).append("\n\n");
            }
        }

        fis.close();
        workbook.close();

        return payslipData.toString();
    }

    // New method to save generated payslips to a file
    private void generatePayslipsToFile(File excelFile, File saveFile) throws IOException {
        FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row.getRowNum() == 0) continue;

                Cell idCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell hoursWorkedCell = row.getCell(2);
                Cell overtimeCell = row.getCell(3);
                Cell payRateCell = row.getCell(4);

                if (idCell != null && nameCell != null && hoursWorkedCell != null && overtimeCell != null && payRateCell != null) {
                    String employeeID = getCellValueAsString(idCell);
                    String employeeName = nameCell.getStringCellValue();
                    double hoursWorked = hoursWorkedCell.getNumericCellValue();
                    double overtime = overtimeCell.getNumericCellValue();
                    double payRate = payRateCell.getNumericCellValue();

                    double employeeSalary = (hoursWorked * payRate) + (overtime * payRate * 1.5);

                    writer.write("Employee ID: " + employeeID + ", Name: " + employeeName + ", Pay: $" + employeeSalary + "\n");
                }
            }
        }

        fis.close();
        workbook.close();
    }
}
