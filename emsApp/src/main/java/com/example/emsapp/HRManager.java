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

public class HRManager extends Application {

    private List<String> entries; // Holds entries from the file
    private int currentEntryIndex = 0; // Index to track the current entry
    private File currentFile; // Reference to the current file
    private ListView<String> entryListView; // ListView to display entries with status

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HR Manager Portal");

        // Label for instructions
        Label instructionLabel = new Label("Select an option to view Employee PTO requests or Payroll data.");

        // TextArea to display current entry for review
        TextArea entryArea = new TextArea();
        entryArea.setEditable(false);
        entryArea.setWrapText(true);

        // ListView to display all entries with statuses
        entryListView = new ListView<>();

        // Buttons to approve or deny the current entry
        Button approveButton = new Button("Approve");
        Button denyButton = new Button("Deny");

        approveButton.setDisable(true);
        denyButton.setDisable(true);

        approveButton.setOnAction(event -> updateEntryStatus("Approved", entryArea));
        denyButton.setOnAction(event -> updateEntryStatus("Denied", entryArea));

        // Button to open Employee PTO requests file
        Button openPTOButton = new Button("Open Employee PTO Requests");
        openPTOButton.setOnAction(event -> openFile(primaryStage, entryArea, "PTO Requests", approveButton, denyButton));

        // Button to open Employee Payroll file
        Button openPayrollButton = new Button("Open Employee Payroll Data");
        openPayrollButton.setOnAction(event -> openFile(primaryStage, entryArea, "Payroll Data", approveButton, denyButton));

        // Layout setup
        VBox layout = new VBox(10);
        layout.getChildren().addAll(instructionLabel, openPTOButton, openPayrollButton, entryArea, approveButton, denyButton, entryListView);

        // Scene setup
        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to open a file and load entries into the list
    private void openFile(Stage stage, TextArea entryArea, String fileType, Button approveButton, Button denyButton) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open " + fileType + " File");

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        currentFile = fileChooser.showOpenDialog(stage);
        if (currentFile != null) {
            loadEntries(currentFile);
            if (!entries.isEmpty()) {
                currentEntryIndex = 0;
                displayCurrentEntry(entryArea);
                updateListView(); // Update the ListView with loaded entries
                approveButton.setDisable(false);
                denyButton.setDisable(false);
            }
        }
    }

    // Method to load entries from the file into the entries list
    private void loadEntries(File file) {
        entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                entries.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to display the current entry in the TextArea
    private void displayCurrentEntry(TextArea entryArea) {
        if (currentEntryIndex < entries.size()) {
            entryArea.setText(entries.get(currentEntryIndex));
        } else {
            entryArea.setText("No more entries to review.");
        }
    }

    // Method to update the current entry with approval or denial status
    private void updateEntryStatus(String status, TextArea entryArea) {
        if (currentEntryIndex < entries.size()) {
            String currentEntry = entries.get(currentEntryIndex);

            // Check if the entry already has a status
            if (currentEntry.startsWith("Approved:") || currentEntry.startsWith("Denied:")) {
                // Remove the existing status prefix
                currentEntry = currentEntry.substring(currentEntry.indexOf(":") + 1).trim();
            }

            // Update the entry with the new status
            String updatedEntry = status + ": " + currentEntry;
            entries.set(currentEntryIndex, updatedEntry);
            writeUpdatedEntriesToFile(); // Save changes to the file
            updateListView(); // Update the ListView with new statuses

            currentEntryIndex++; // Move to the next entry
            displayCurrentEntry(entryArea); // Display the next entry
        } else {
            entryArea.setText("All entries have been reviewed.");
        }
    }

    // Method to update the ListView with entries and their statuses
    private void updateListView() {
        entryListView.getItems().clear();
        entryListView.getItems().addAll(entries);
    }

    // Method to write updated entries back to the file
    private void writeUpdatedEntriesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            for (String entry : entries) {
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



