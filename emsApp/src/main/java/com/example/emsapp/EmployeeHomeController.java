package com.example.emsapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EmployeeHomeController {

    @FXML
    private Label nameLabel;

    @FXML
    private Button checkPTOButton;

    @FXML
    private Button submitPTOButton;

    @FXML
    private Button checkReviewButton;

    @FXML
    private TextArea displayArea;

    // Sample employee data
    private String employeeName = "John Doe";
    private double availablePTOBalance = 60.0; // Example of 15 days available
    private String lastReview = "Great performance. Keep up the good work!";

    public void initialize() {
        // Set the employee's name
        nameLabel.setText("Employee: " + employeeName);

        // Add action listeners to buttons
        checkPTOButton.setOnAction(event -> handleCheckPTOAction());
        submitPTOButton.setOnAction(event -> handleSubmitPTOAction());
        checkReviewButton.setOnAction(event -> handleCheckReviewAction());
    }

    @FXML
    private void handleCheckPTOAction() {
        displayArea.setText("Available PTO Balance: " + availablePTOBalance + " hours");
    }

    @FXML
    private void handleSubmitPTOAction() {
        EmployeePTO employeePTO = new EmployeePTO(availablePTOBalance);
        Stage ptoStage = new Stage();
        employeePTO.start(ptoStage);
    }

    @FXML
    private void handleCheckReviewAction() {
        ViewReview viewReview = new ViewReview();
        Stage reviewStage = new Stage();
        viewReview.start(reviewStage);
    }
}
