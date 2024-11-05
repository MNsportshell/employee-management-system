package com.example.emsapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollManager extends Application {

    private List<String> payrollEntries;
    private int currentEntryIndex = 0;
    private TextArea entryArea;
    private ListView<String> entryListView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Payroll Approval Manager");

        Label instructionLabel = new Label("Open the Excel file containing payroll data to review.");

        entryArea = new TextArea();
        entryArea.setEditable(false);
        entryArea.setWrapText(true);

        entryListView = new ListView<>();

        Button approveButton = new Button("Approve");
        Button denyButton = new Button("Deny");
        approveButton.setDisable(true);
        denyButton.setDisable(true);

        approveButton.setOnAction(event -> updateEntryStatus("Approved"));
        denyButton.setOnAction(event -> updateEntryStatus("Denied"));

        Button openFileButton = new Button("Open Payroll Data File");
        openFileButton.setOnAction(event -> openFile(primaryStage, approveButton, denyButton));

        Button saveFileButton = new Button("Save Results to Text File");
        saveFileButton.setOnAction(event -> saveToFile(primaryStage));
        saveFileButton.setDisable(true);  // Initially disable until entries are loaded

        VBox layout = new VBox(10);
        layout.getChildren().addAll(instructionLabel, openFileButton, entryArea, approveButton, denyButton, entryListView, saveFileButton);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFile(Stage stage, Button approveButton, Button denyButton) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Payroll Data File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            loadEntries(file);
            if (!payrollEntries.isEmpty()) {
                currentEntryIndex = 0;
                displayCurrentEntry();
                updateListView();
                approveButton.setDisable(false);
                denyButton.setDisable(false);
                ((Button) ((VBox) stage.getScene().getRoot()).getChildren().get(6)).setDisable(false);  // Enable Save button
            }
        }
    }

    private void loadEntries(File file) {
        payrollEntries = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);  // Use the first sheet
            Row headerRow = sheet.getRow(0);  // First row for column names
            List<String> columnNames = new ArrayList<>();

            // Extract column names from the header row
            for (Cell cell : headerRow) {
                cell.setCellType(CellType.STRING);  // Convert header cells to string for consistency
                columnNames.add(cell.getStringCellValue());
            }

            // Iterate through each row starting from the second row
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                StringBuilder entryBuilder = new StringBuilder();
                for (int colIndex = 0; colIndex < columnNames.size(); colIndex++) {
                    Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);  // Convert all cells to string for simplicity
                    String cellValue = cell.getStringCellValue();

                    // Append column name and cell value to entry
                    entryBuilder.append(columnNames.get(colIndex)).append(": ").append(cellValue).append(" ");
                }
                payrollEntries.add(entryBuilder.toString().trim());  // Add formatted entry
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayCurrentEntry() {
        if (currentEntryIndex < payrollEntries.size()) {
            entryArea.setText(payrollEntries.get(currentEntryIndex));
        } else {
            entryArea.setText("All payroll entries have been reviewed.");
        }
    }

    private void updateEntryStatus(String status) {
        if (currentEntryIndex < payrollEntries.size()) {
            String entry = payrollEntries.get(currentEntryIndex);
            if (entry.startsWith("Approved:") || entry.startsWith("Denied:")) {
                entry = entry.substring(entry.indexOf(":") + 1).trim();
            }

            String updatedEntry = status + ": " + entry;
            payrollEntries.set(currentEntryIndex, updatedEntry);

            currentEntryIndex++;
            displayCurrentEntry();
            updateListView();
        } else {
            entryArea.setText("All payroll entries have been reviewed.");
        }
    }

    private void updateListView() {
        entryListView.getItems().clear();
        entryListView.getItems().addAll(payrollEntries);
    }

    private void saveToFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Payroll Data to Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File saveFile = fileChooser.showSaveDialog(stage);

        if (saveFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
                for (String entry : payrollEntries) {
                    writer.write(entry);
                    writer.newLine();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "File saved successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving file.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
