package com.example.emsapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PTOManager extends Application {

    private List<File> ptoRequestFiles;
    private int currentRequestIndex = 0;
    private TextArea requestArea;
    private ListView<String> requestListView;
    private StringBuilder decisionLog = new StringBuilder();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PTO Approval Manager");

        Label instructionLabel = new Label("Open the folder containing PTO requests to review.");

        requestArea = new TextArea();
        requestArea.setEditable(false);
        requestArea.setWrapText(true);

        requestListView = new ListView<>();

        Button approveButton = new Button("Approve");
        Button denyButton = new Button("Deny");
        approveButton.setDisable(true);
        denyButton.setDisable(true);

        approveButton.setOnAction(event -> updateRequestStatus("Approved"));
        denyButton.setOnAction(event -> updateRequestStatus("Denied"));

        Button openFolderButton = new Button("Open PTO Requests Folder");
        openFolderButton.setOnAction(event -> openFolder(primaryStage, approveButton, denyButton));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(instructionLabel, openFolderButton, requestArea, approveButton, denyButton, requestListView);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFolder(Stage stage, Button approveButton, Button denyButton) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open PTO Requests Folder");

        File directory = directoryChooser.showDialog(stage);
        if (directory != null && directory.isDirectory()) {
            ptoRequestFiles = List.of(directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt")));

            if (!ptoRequestFiles.isEmpty()) {
                currentRequestIndex = 0;
                displayCurrentRequest();
                updateListView();
                approveButton.setDisable(false);
                denyButton.setDisable(false);
            } else {
                requestArea.setText("No PTO requests found in the selected folder.");
            }
        }
    }

    private void displayCurrentRequest() {
        if (currentRequestIndex < ptoRequestFiles.size()) {
            File requestFile = ptoRequestFiles.get(currentRequestIndex);
            try {
                String requestContent = Files.readString(requestFile.toPath());
                requestArea.setText(requestContent);
            } catch (IOException e) {
                e.printStackTrace();
                requestArea.setText("Error reading PTO request file: " + requestFile.getName());
            }
        } else {
            requestArea.setText("All PTO requests have been reviewed.");
            saveDecisionLog();
        }
    }

    private void updateRequestStatus(String status) {
        if (currentRequestIndex < ptoRequestFiles.size()) {
            File requestFile = ptoRequestFiles.get(currentRequestIndex);
            String requestContent;
            try {
                requestContent = Files.readString(requestFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Add decision and content to the log
            decisionLog.append(status).append(": ").append(requestFile.getName()).append("\n")
                    .append("Request Content:\n").append(requestContent).append("\n\n");

            // Delete file if approved
            if (status.equals("Approved")) {
                try {
                    Files.delete(requestFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    requestArea.setText("Error deleting approved PTO request file: " + requestFile.getName());
                }
            }

            currentRequestIndex++;
            displayCurrentRequest();
            updateListView();
        }
    }

    private void updateListView() {
        requestListView.getItems().clear();
        for (int i = currentRequestIndex; i < ptoRequestFiles.size(); i++) {
            requestListView.getItems().add(ptoRequestFiles.get(i).getName());
        }
    }

    private void saveDecisionLog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PTO Decisions Log");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File saveFile = fileChooser.showSaveDialog(null);

        if (saveFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
                writer.write(decisionLog.toString());
                requestArea.setText("PTO decisions saved to: " + saveFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                requestArea.setText("Error saving decisions log: " + e.getMessage());
            }
        } else {
            requestArea.setText("Save operation canceled.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
