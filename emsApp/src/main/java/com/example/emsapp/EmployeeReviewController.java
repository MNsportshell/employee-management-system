package com.example.emsapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeeReviewController {

    private List<String> reviews = new ArrayList<>();
    private File selectedFile;
    private Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "app.properties";
    private int reviewCounter = 1;

    @FXML private TextField employeeIdField;
    @FXML private TextField employeeNameField;
    @FXML private TextField employeeEmailField;
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextArea commentsArea;
    @FXML private ListView<String> reviewListView;

    @FXML
    private void initialize() {
        for (int i = 1; i <= 5; i++) {
            ratingComboBox.getItems().add(i);
        }
    }

    @FXML
    private void handleFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Reviews File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            loadReviewsFromFile(selectedFile);
        }
    }

    @FXML
    private void handleSubmitButtonAction() {
        String employeeId = employeeIdField.getText();
        String employeeName = employeeNameField.getText();
        String employeeEmail = employeeEmailField.getText();
        Integer rating = ratingComboBox.getValue();
        String comments = commentsArea.getText();

        if (employeeId.isEmpty() || employeeName.isEmpty() || employeeEmail.isEmpty() || rating == null || comments.isEmpty()) {
            showAlert("Please fill in all fields.");
        } else {
            String review = createReviewString(employeeId, employeeName, employeeEmail, rating, comments);
            reviews.add(review);
            reviewListView.getItems().add(review);
            if (selectedFile != null) writeReviewToFile(review);
            clearFields();
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        String selectedReview = reviewListView.getSelectionModel().getSelectedItem();
        if (selectedReview != null) {
            reviews.remove(selectedReview);
            reviewListView.getItems().remove(selectedReview);
            saveReviewsToFile();
        } else {
            showAlert("Please select a review to delete.");
        }
    }

    private String createReviewString(String id, String name, String email, int rating, String comments) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("Review ID: R%d | ID: %s | Name: %s | Email: %s | Rating %d - %s (Submitted on: %s)",
                reviewCounter++, id, name, email, rating, comments, formattedDate);
    }

    private void writeReviewToFile(String review) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile, true))) {
            writer.write(review);
            writer.newLine();
        } catch (IOException e) {
            showAlert("Error writing to file: " + e.getMessage());
        }
    }

    private void loadReviewsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reviews.clear();
            reviewListView.getItems().clear();
            while ((line = reader.readLine()) != null) {
                reviews.add(line);
                reviewListView.getItems().add(line);
            }
        } catch (IOException e) {
            showAlert("Error reading from file: " + e.getMessage());
        }
    }

    private void saveReviewsToFile() {
        if (selectedFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                for (String review : reviews) {
                    writer.write(review);
                    writer.newLine();
                }
            } catch (IOException e) {
                showAlert("Error saving reviews to file: " + e.getMessage());
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        employeeIdField.clear();
        employeeNameField.clear();
        employeeEmailField.clear();
        ratingComboBox.getSelectionModel().clearSelection();
        commentsArea.clear();
    }
}
