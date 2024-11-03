package com.example.emsapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollManager extends Application {

    private List<String> payrollEntries;
    private int currentEntryIndex = 0;
    private File currentFile;
    private TextArea entryArea;
    private ListView<String> entryListView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Payroll Approval Manager");

        Label instructionLabel = new Label("Open the file containing payroll data to review.");

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

        VBox layout = new VBox(10);
        layout.getChildren().addAll(instructionLabel, openFileButton, entryArea, approveButton, denyButton, entryListView);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFile(Stage stage, Button approveButton, Button denyButton) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Payroll Data File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        currentFile = fileChooser.showOpenDialog(stage);
        if (currentFile != null) {
            loadEntries(currentFile);
            if (!payrollEntries.isEmpty()) {
                currentEntryIndex = 0;
                displayCurrentEntry();
                updateListView();
                approveButton.setDisable(false);
                denyButton.setDisable(false);
            }
        }
    }

    private void loadEntries(File file) {
        payrollEntries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                payrollEntries.add(line);
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
            writeUpdatedEntriesToFile();

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

    private void writeUpdatedEntriesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            for (String entry : payrollEntries) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

