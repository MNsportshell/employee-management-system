package com.example.emsapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HRManagerHome extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HR Manager Home");

        // Title label
        Label titleLabel = new Label("HR Manager Dashboard");

        // Buttons for different actions
        Button submitPTOButton = new Button("Submit PTO Request");
        Button approvePTOButton = new Button("Approve/Deny PTO Requests");
        Button employeeReviewsButton = new Button("View/Add Employee Reviews");
        Button payrollButton = new Button("Approve/Deny Payroll");
        Button generatePayslipsButton = new Button("Generate Payslips"); // New button for generating payslips

        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setWrapText(true);

        // Submit PTO Request action
        submitPTOButton.setOnAction(event -> {
            EmployeePTO employeePTO = new EmployeePTO(60.0);
            Stage ptoStage = new Stage();
            employeePTO.start(ptoStage);
        });

        // Approve/Deny PTO Requests action
        approvePTOButton.setOnAction(event -> {
            PTOManager ptoApprovalManager = new PTOManager();
            Stage ptoStage = new Stage();
            ptoApprovalManager.start(ptoStage);
        });

        // View/Add Employee Reviews action
        employeeReviewsButton.setOnAction(event -> {
            EmployeeReview employeeReview = new EmployeeReview();
            Stage empReview = new Stage();
            try {
                employeeReview.start(empReview);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Employee Payroll action
        payrollButton.setOnAction(event -> {
            EmployeePayroll payrollApprovalManager = new EmployeePayroll();
            Stage payrollApprovalStage = new Stage();
            payrollApprovalManager.start(payrollApprovalStage);
        });

        // Generate Payslips action
        generatePayslipsButton.setOnAction(event -> {
            EmployeePayroll employeePayroll = new EmployeePayroll();
            Stage payrollStage = new Stage();
            employeePayroll.start(payrollStage);
        });

        // Layout configuration
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(titleLabel, submitPTOButton, approvePTOButton, employeeReviewsButton, payrollButton, generatePayslipsButton, displayArea);

        // Scene and stage setup
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
