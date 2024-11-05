package com.example.emsapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeReviewController {

    private List<String> reviews = new ArrayList<>();
    private File selectedFile;
    private int reviewCounter = 1;
    private int editIndex = -1; // Track the index of the review being edited

    @FXML private TextField employeeIdField;
    @FXML private TextField employeeNameField;
    @FXML private TextField employeeEmailField;
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextArea commentsArea;
    @FXML private ListView<String> reviewListView;

    @FXML
    private void initialize() {
        // Set up rating ComboBox
        for (int i = 1; i <= 5; i++) {
            ratingComboBox.getItems().add(i);
        }

        // Enable text wrapping in ListView
        reviewListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                private final Label label = new Label();

                {
                    label.setWrapText(true); // Enable text wrapping
                    label.setMaxWidth(350); // Set max width to wrap within ListView bounds
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        label.setText(item);
                        setGraphic(label);
                    }
                }
            };
            return cell;
        });
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

            if (editIndex >= 0) { // If we're editing an existing review
                reviews.set(editIndex, review);
                reviewListView.getItems().set(editIndex, review);
                editIndex = -1; // Reset after editing
            } else {
                reviews.add(review);
                reviewListView.getItems().add(review);
                if (selectedFile != null) writeReviewToFile(review);
            }

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

    @FXML
    private void handleEditButtonAction() {
        String selectedReview = reviewListView.getSelectionModel().getSelectedItem();
        if (selectedReview != null) {
            editIndex = reviewListView.getSelectionModel().getSelectedIndex(); // Track the index for editing
            populateFieldsForEditing(selectedReview);
        } else {
            showAlert("Please select a review to edit.");
        }
    }

    private void populateFieldsForEditing(String review) {
        // Assuming review format: "Review ID: R<id> | ID: <employeeId> | Name: <name> | Email: <email> | Rating <rating> - <comments> (Submitted on: <date>)"
        String[] parts = review.split(" \\| ");
        if (parts.length < 5) { // Ensure there are enough parts
            showAlert("The selected review format is invalid.");
            return;
        }
        employeeIdField.setText(parts[1].split(": ")[1]);
        employeeNameField.setText(parts[2].split(": ")[1]);
        employeeEmailField.setText(parts[3].split(": ")[1]);
        ratingComboBox.setValue(Integer.parseInt(parts[4].split(" ")[1]));
        commentsArea.setText(parts[4].split(" - ")[1].split(" \\(Submitted")[0]);
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
