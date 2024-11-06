package com.example.emsapp.Roles;

import com.example.emsapp.EmployeePayroll;
import com.example.emsapp.EmployeeReview;
import com.example.emsapp.PayrollManager;
import javafx.stage.Stage;

public class HRManagerRole extends ManagerRole {

    public void generatePayslips() {
        EmployeePayroll payroll = new EmployeePayroll();

        // Open the Employee Payroll interface for generating payslips
        Stage payrollStage = new Stage();
        try {
            payroll.start(payrollStage); // Assumes EmployeePayroll has a start method for GUI
            System.out.println("Employee Payroll interface opened for generating payslips.");
        } catch (Exception e) {
            System.out.println("Error launching Employee Payroll for payslip generation: " + e.getMessage());
        }
    }

    public void approvePayroll() {
        PayrollManager payrollManager = new PayrollManager();

        // Open Payroll Manager interface for approving payroll
        Stage payrollApprovalStage = new Stage();
        try {
            payrollManager.start(payrollApprovalStage); // Assumes PayrollManager has a start method for GUI
            System.out.println("Payroll Manager interface opened for approving payroll.");
        } catch (Exception e) {
            System.out.println("Error launching Payroll Manager for payroll approval: " + e.getMessage());
        }
    }

    public void addEmployeeReview() {
        EmployeeReview employeeReview = new EmployeeReview();

        // Open Employee Review interface for adding or managing reviews
        Stage reviewStage = new Stage();
        try {
            employeeReview.start(reviewStage); // Assumes EmployeeReview has a start method for GUI
            System.out.println("Employee Review interface opened for adding/managing employee reviews.");
        } catch (Exception e) {
            System.out.println("Error launching Employee Review for adding/managing reviews: " + e.getMessage());
        }
    }
}
