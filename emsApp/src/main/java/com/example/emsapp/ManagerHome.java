package com.example.emsapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ManagerHome extends Application {

    // Sample data
    private double managerPTOBalance = 20.0;
    private List<String> ptoRequests = new ArrayList<>();
    private List<String> employeeReviews = new ArrayList<>();
    private List<String> payrollRecords = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manager Home");

        // UI components
        Label titleLabel = new Label("Manager Dashboard");
        Button submitPTOButton = new Button("Submit PTO Request");
        Button approveDenyPTOButton = new Button("Approve/Deny PTO Requests");
        Button employeeReviewsButton = new Button("Employee Reviews");
        Button payrollButton = new Button("Employee Payroll");
        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);

        // Submit PTO request (for the manager)
        submitPTOButton.setOnAction(event -> {
            EmployeePTO employeePTO = new EmployeePTO(managerPTOBalance); // assuming EmployeePTO class takes PTO balance as parameter
            Stage ptoStage = new Stage();
            employeePTO.start(ptoStage);
        });

        // Approve/Deny PTO Requests
        approveDenyPTOButton.setOnAction(event -> {
                    PTOManager PTOManager = new PTOManager(); // assuming EmployeePTO class takes PTO balance as parameter
                    Stage ptoStage = new Stage();
                    PTOManager.start(ptoStage);
        });


        // Employee Reviews
        employeeReviewsButton.setOnAction(event -> {
            EmployeeReview employeeReview = new EmployeeReview();
            Stage empReview = new Stage();
            try {
                employeeReview.start(empReview);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Employee Payroll
        payrollButton.setOnAction(event -> {
            PayrollManager payrollManager = new PayrollManager();
            Stage payrollMgr = new Stage();
            payrollManager.start(payrollMgr);
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(titleLabel, submitPTOButton, approveDenyPTOButton, employeeReviewsButton, payrollButton, displayArea);

        // Set scene and show stage
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

