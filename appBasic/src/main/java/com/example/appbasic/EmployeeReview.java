package com.example.appbasic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeReview extends Application {

    private List<String> reviews = new ArrayList<>();
    private ListView<String> reviewListView = new ListView<>();
    private int reviewCounter = 1;  // Counter for generating review IDs
    private File selectedFile;  // To hold the user's selected file
    private Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "app.properties"; // Properties file name

    @Override
    public void start(Stage primaryStage) {
        // Prompt user to choose a file upon application start
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Reviews File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            properties.setProperty("filePath", selectedFile.getAbsolutePath());
            saveProperties(); // Save the file path to properties
            showAlert("Loaded file: " + selectedFile.getAbsolutePath());
            loadReviewsFromFile(selectedFile); // Load existing reviews
        } else {
            showAlert("No file selected. The application will exit.");
            return; // Exit the application if no file is selected
        }

        // Create UI elements
        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");

        TextField employeeNameField = new TextField();
        employeeNameField.setPromptText("Enter Employee Name");

        ComboBox<Integer> ratingComboBox = new ComboBox<>();
        for (int i = 1; i <= 5; i++) {
            ratingComboBox.getItems().add(i);
        }
        ratingComboBox.setPromptText("Select Rating (1-5)");

        TextArea commentsArea = new TextArea();
        commentsArea.setPromptText("Enter Review Comments");
        commentsArea.setWrapText(true); // Enable text wrapping for the comments area

        Button submitButton = new Button("Submit Review");
        Button fileButton = new Button("Choose New File Location");

        // Set up action for the file button
        fileButton.setOnAction(event -> {
            FileChooser fileChooser1 = new FileChooser();
            fileChooser1.setTitle("Choose Reviews File");
            fileChooser1.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            selectedFile = fileChooser1.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                properties.setProperty("filePath", selectedFile.getAbsolutePath());
                saveProperties(); // Save the file path to properties
                showAlert("Loaded file: " + selectedFile.getAbsolutePath());
                loadReviewsFromFile(selectedFile); // Load existing reviews
            }
        });

        // Set up action for the submit button
        submitButton.setOnAction(event -> {
            String employeeId = employeeIdField.getText();
            String employeeName = employeeNameField.getText();
            Integer rating = ratingComboBox.getValue();
            String comments = commentsArea.getText();

            if (employeeId.isEmpty() || employeeName.isEmpty() || rating == null || comments.isEmpty()) {
                showAlert("Please fill in all fields.");
            } else {
                // Get the current date and time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                // Generate a unique review ID
                String reviewId = "R" + reviewCounter++;

                // Create the review string with ID, date, and time
                String review = String.format("(Submitted on: %s) - Review ID: %s | ID: %s | Review for %s: Rating %d - %s",
                        formattedDateTime, reviewId, employeeId, employeeName, rating, comments);
                reviews.add(review);
                reviewListView.getItems().add(review); // Update ListView

                // Write to file if a file is selected
                if (selectedFile != null) {
                    writeReviewToFile(review);
                }

                // Clear fields after submission
                employeeIdField.clear();
                employeeNameField.clear();
                ratingComboBox.getSelectionModel().clearSelection();
                commentsArea.clear();
            }
        });

        // Layout the UI
        VBox layout = new VBox(10, fileButton, employeeIdField, employeeNameField, ratingComboBox, commentsArea, submitButton, reviewListView);
        layout.setPrefWidth(400);
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Employee Review Submission");
        primaryStage.show();
    }

    private void loadReviewsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                reviews.add(line);
                reviewListView.getItems().add(line); // Populate ListView with existing reviews
            }
        } catch (IOException e) {
            showAlert("Error reading from file: " + e.getMessage());
        }
    }

    private void writeReviewToFile(String review) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile, true))) {
            writer.write(review);
            writer.newLine();
        } catch (IOException e) {
            showAlert("Error writing to file: " + e.getMessage());
        }
    }

    private void loadProperties() {
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            // Handle file not found or error loading properties
        }
    }

    private void saveProperties() {
        try (FileOutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
            properties.store(output, null);
        } catch (IOException e) {
            showAlert("Error saving properties: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
