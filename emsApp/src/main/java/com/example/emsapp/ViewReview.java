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

public class ViewReview extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Employee Review");

        // TextArea to display the review content
        TextArea reviewDisplay = new TextArea();
        reviewDisplay.setEditable(false);

        // Button to open the file chooser
        Button chooseFileButton = new Button("Choose Review File");

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
                    String reviewContent = Files.readString(Path.of(reviewFile.getAbsolutePath()));
                    reviewDisplay.setText(reviewContent);
                } catch (IOException e) {
                    reviewDisplay.setText("Error reading review file: " + e.getMessage());
                }
            } else {
                reviewDisplay.setText("No review file selected.");
            }
        });

        // Set up the layout and scene
        VBox layout = new VBox(10);
        layout.getChildren().addAll(chooseFileButton, reviewDisplay);
        Scene scene = new Scene(layout, 400, 300);

        stage.setScene(scene);
        stage.show();
    }
}
