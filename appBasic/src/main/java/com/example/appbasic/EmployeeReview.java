package com.example.appbasic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeReview extends Application {

    private List<String> reviews = new ArrayList<>();
    private ListView<String> reviewListView;
    private int reviewCounter = 1;  // Counter for generating review IDs

    @Override
    public void start(Stage primaryStage) {
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

        Button submitButton = new Button("Submit Review");
        reviewListView = new ListView<>();

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
                String review = String.format("Review ID: %s | ID: %s | Review for %s: Rating %d - %s (Submitted on: %s)",
                        reviewId, employeeId, employeeName, rating, comments, formattedDateTime);
                reviews.add(review);
                reviewListView.getItems().add(review); // Update ListView

                // Clear fields after submission
                employeeIdField.clear();
                employeeNameField.clear();
                ratingComboBox.getSelectionModel().clearSelection();
                commentsArea.clear();
            }
        });

        // Layout the UI
        VBox layout = new VBox(10, employeeIdField, employeeNameField, ratingComboBox, commentsArea, submitButton, reviewListView);
        layout.setPrefWidth(400);
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Employee Review Submission");
        primaryStage.show();
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
