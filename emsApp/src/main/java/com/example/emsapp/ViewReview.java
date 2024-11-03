package com.example.emsapp;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ViewReview extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Employee Review");

        // TextArea to display the review content with enhanced readability
        TextArea reviewDisplay = new TextArea();
        reviewDisplay.setEditable(false);
        reviewDisplay.setWrapText(true);
        reviewDisplay.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16; -fx-text-fill: #333; -fx-padding: 10;");
        reviewDisplay.setPrefHeight(250);  // Increase height for more visible text

        // Button to open the file chooser
        Button chooseFileButton = new Button("Choose Review File");
        chooseFileButton.setStyle("-fx-font-size: 14; -fx-padding: 5 10;");  // Increase font size and padding for button

        // File chooser configuration
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Employee Review File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        // Button action to open file chooser and read file content
        chooseFileButton.setOnAction(event -> {
            File reviewFile = fileChooser.showOpenDialog(stage);
            if (reviewFile != null) {
                try {
                    // Read all lines from the file and separate entries with "---" and spacing
                    List<String> lines = Files.readAllLines(Path.of(reviewFile.getAbsolutePath()));
                    StringBuilder reviewContent = new StringBuilder();
                    for (String line : lines) {
                        reviewContent.append(line).append("\n");
                        // Add separator line between entries
                        reviewContent.append("------------------------------------------------\n\n");
                    }
                    reviewDisplay.setText(reviewContent.toString());
                } catch (IOException e) {
                    reviewDisplay.setText("Error reading review file: " + e.getMessage());
                }
            } else {
                reviewDisplay.setText("No review file selected.");
            }
        });

        // Set up the layout and scene with spacing adjustments
        VBox layout = new VBox(15);  // Increase spacing between elements for readability
        layout.getChildren().addAll(chooseFileButton, reviewDisplay);
        Scene scene = new Scene(layout, 500, 400);  // Increase overall window size

        stage.setScene(scene);
        stage.show();
    }
}
