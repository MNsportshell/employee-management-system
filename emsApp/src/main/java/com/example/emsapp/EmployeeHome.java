package com.example.emsapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeHome extends Application {

    // Sample employee data
    private String employeeName = "John Doe";
    private double availablePTOBalance = 60.0; // Example of 15 days available
    private String lastReview = "Great performance. Keep up the good work!";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Portal");

        // Creating UI components
        Label nameLabel = new Label("Employee: " + employeeName);
        Button checkPTOButton = new Button("Check Available PTO Balance");
        Button submitPTOButton = new Button("Submit PTO Request");
        Button checkReviewButton = new Button("Check Employee Review");
        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);

        // Action to check PTO balance
        checkPTOButton.setOnAction(event -> {
            displayArea.setText("Available PTO Balance: " + availablePTOBalance + " hours");
        });

        // Action to submit PTO request by opening EmployeePTO stage
        submitPTOButton.setOnAction(event -> {
            EmployeePTO employeePTO = new EmployeePTO(availablePTOBalance);
            Stage ptoStage = new Stage();
            employeePTO.start(ptoStage);
        });

        // Action to check employee review by opening ViewReview stage
        checkReviewButton.setOnAction(event -> {
            ViewReview viewReview = new ViewReview();
            Stage reviewStage = new Stage();
            viewReview.start(reviewStage);
        });

        // Layout configuration
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(nameLabel, checkPTOButton, submitPTOButton, checkReviewButton, displayArea);

        // Set scene and show stage
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
