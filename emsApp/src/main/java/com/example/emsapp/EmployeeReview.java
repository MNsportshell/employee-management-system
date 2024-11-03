package com.example.emsapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeeReview extends Application {

    private List<String> reviews = new ArrayList<>();
    private ListView<String> reviewListView = new ListView<>();
    private int reviewCounter = 1;  // Counter for generating review IDs
    private File selectedFile;  // To hold the user's selected file
    private Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "app.properties"; // Properties file name
    private boolean isEditing = false; // Flag to indicate if an edit is in progress


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

    private void loadReviewsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reviews.clear();
            reviewListView.getItems().clear();

            while ((line = reader.readLine()) != null) {
                reviews.add(line);
                reviewListView.getItems().add(line); // Populate ListView with each review
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

    private void saveReviewsToFile() {
        if (selectedFile == null) {
            showAlert("No file selected to save reviews.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
            for (String review : reviews) {
                writer.write(review);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert("Error saving reviews to file: " + e.getMessage());
        }
    }



    public void start(Stage primaryStage) {
        // Create UI elements
        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");

        TextField employeeNameField = new TextField();
        employeeNameField.setPromptText("Enter Employee Name");

        TextField employeeEmailField = new TextField();
        employeeEmailField.setPromptText("Enter Employee Email");

        ComboBox<Integer> ratingComboBox = new ComboBox<>();
        for (int i = 1; i <= 5; i++) {
            ratingComboBox.getItems().add(i);
        }
        ratingComboBox.setPromptText("Select Rating (1-5)");

        TextArea commentsArea = new TextArea();
        commentsArea.setPromptText("Enter Review Comments");
        commentsArea.setWrapText(true); // Enable text wrapping for the comments area

        Button submitButton = new Button("Submit Review");
        Button editButton = new Button("Edit Selected Review");
        Button fileButton = new Button("Choose File");
        Button sendButton = new Button("Send Review to Employee");

        // Set up action for the file button
        fileButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Reviews File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                properties.setProperty("filePath", selectedFile.getAbsolutePath());
                saveProperties(); // Save the file path to properties
                reviews.clear();
                reviewListView.getItems().clear(); // Clear ListView before loading
                loadReviewsFromFile(selectedFile); // Load existing reviews
                showAlert("Loaded file: " + selectedFile.getAbsolutePath());
            }
        });

        // Set up action for the submit button
        submitButton.setOnAction(event -> {
            String employeeId = employeeIdField.getText();
            String employeeName = employeeNameField.getText();
            String employeeEmail = employeeEmailField.getText();
            Integer rating = ratingComboBox.getValue();
            String comments = commentsArea.getText();

            if (employeeId.isEmpty() || employeeName.isEmpty() || employeeEmail.isEmpty() || rating == null || comments.isEmpty()) {
                showAlert("Please fill in all fields.");
            } else {
                // Get the current date and time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                // Generate a unique review ID
                String reviewId = "R" + reviewCounter++;

                // Create the review string with ID, date, and time
                String review = String.format("Review ID: %s | ID: %s | Name: %s | Email: %s | Rating %d - %s (Submitted on: %s)",
                        reviewId, employeeId, employeeName, employeeEmail, rating, comments, formattedDateTime);
                reviews.add(review);
                reviewListView.getItems().add(review); // Update ListView

                // Write to file if a file is selected
                if (selectedFile != null) {
                    writeReviewToFile(review);
                } else {
                    showAlert("Please select a file before submitting a review.");
                }

                // Clear fields after submission
                employeeIdField.clear();
                employeeNameField.clear();
                employeeEmailField.clear();
                ratingComboBox.getSelectionModel().clearSelection();
                commentsArea.clear();
            }
        });

        // Set up action for the edit button
        editButton.setOnAction(event -> {
            isEditing = true; // Mark as editing to prevent submitButton reset
            String selectedReview = reviewListView.getSelectionModel().getSelectedItem();
            if (selectedReview != null) {
                int selectedIndex = reviewListView.getSelectionModel().getSelectedIndex();

                // Split into main content and submission date
                String[] mainParts = selectedReview.split(" \\(Submitted on: ");
                if (mainParts.length < 2) {
                    showAlert("Error: Selected review format is invalid.");
                    isEditing = false;
                    return;
                }

                String mainContent = mainParts[0].trim();
                String submissionDate = mainParts[1].replace(")", "").trim();

                // Further split main content by " | " for other fields
                String[] parts = mainContent.split(" \\| ");
                if (parts.length < 5) {  // Adjusted to check the correct number of parts
                    showAlert("Error: Selected review format is invalid.");
                    isEditing = false;
                    return;
                }

                try {
                    // Extract Review ID, which is always first
                    String reviewId = parts[0].split(": ")[1].trim();
                    String employeeId = parts[1].split(": ")[1].trim();
                    String employeeName = parts[2].split(": ")[1].trim();
                    String employeeEmail = parts[3].split(": ")[1].trim();
                    int rating = Integer.parseInt(parts[4].split(" ")[1].trim());

                    // Extract comments from the final part (up to "-")
                    String comments = parts[4].split("- ")[1].trim();

                    // Populate form fields with extracted values (omitting Review ID and Submission Date)
                    employeeIdField.setText(employeeId);
                    employeeNameField.setText(employeeName);
                    employeeEmailField.setText(employeeEmail);
                    ratingComboBox.setValue(rating);
                    commentsArea.setText(comments);

                    // Set up submit button action for updating the review
                    submitButton.setOnAction(event1 -> {
                        if (employeeIdField.getText().isEmpty() || employeeNameField.getText().isEmpty() ||
                                employeeEmailField.getText().isEmpty() || ratingComboBox.getValue() == null || commentsArea.getText().isEmpty()) {
                            showAlert("Please fill in all fields.");
                        } else {
                            // Reconstruct the updated review, re-including Review ID and Submission Date
                            String updatedReview = String.format(
                                    "Review ID: %s | ID: %s | Name: %s | Email: %s | Rating %d - %s (Submitted on: %s)",
                                    reviewId, employeeIdField.getText(), employeeNameField.getText(),
                                    employeeEmailField.getText(), ratingComboBox.getValue(),
                                    commentsArea.getText(), submissionDate);

                            // Update the review in the list and save to file
                            reviews.set(selectedIndex, updatedReview);
                            reviewListView.getItems().set(selectedIndex, updatedReview);
                            saveReviewsToFile();

                            // Clear fields after updating
                            employeeIdField.clear();
                            employeeNameField.clear();
                            employeeEmailField.clear();
                            ratingComboBox.getSelectionModel().clearSelection();
                            commentsArea.clear();
                        }
                        isEditing = false; // Reset editing flag after saving
                    });
                } catch (Exception e) {
                    showAlert("Error parsing review: " + e.getMessage());
                    isEditing = false;
                }
            } else {
                showAlert("Please select a review to edit.");
            }
        });


        // Set up action for the send button
        sendButton.setOnAction(event -> {
            String selectedReview = reviewListView.getSelectionModel().getSelectedItem();
            if (selectedReview != null) {
                String[] parts = selectedReview.split(" \\| ");
                String email = parts[3].split(": ")[1]; // Extract email

                sendEmail(email, "Employee Review", selectedReview);
            } else {
                showAlert("Please select a review to send.");
            }
        });

        // Layout the UI
        VBox layout = new VBox(10, fileButton, employeeIdField, employeeNameField, employeeEmailField, ratingComboBox, commentsArea, submitButton, editButton, sendButton, reviewListView);
        layout.setPrefWidth(400);
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Employee Review Submission");
        primaryStage.show();
    }

    private void sendEmail(String to, String subject, String content) {
        String from = "your-email@example.com"; // Your email
        String host = "smtp.example.com"; // Your SMTP host

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            showAlert("Review sent to " + to);
        } catch (MessagingException mex) {
            showAlert("Error: Unable to send email.");
            mex.printStackTrace();
        }
    }

}
