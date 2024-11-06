package com.example.emsapp.Roles;

import com.example.emsapp.EmployeeReview;
import com.example.emsapp.PTOManager;
import com.example.emsapp.PayrollManager;
import javafx.stage.Stage;

public class ManagerRole extends EmployeeRole {

    public void approveDenyPTO() {
        PTOManager ptoManager = new PTOManager();

        // Open the PTO Manager interface for approving/denying requests
        Stage ptoStage = new Stage();
        try {
            ptoManager.start(ptoStage); // Assumes PTOManager has a start method for GUI
            System.out.println("PTO Manager interface opened for approval/denial of PTO requests.");
        } catch (Exception e) {
            System.out.println("Error launching PTO Manager: " + e.getMessage());
        }
    }

    public void accessEmployeeReviews() {
        EmployeeReview employeeReview = new EmployeeReview();

        // Open the Employee Review interface
        Stage reviewStage = new Stage();
        try {
            employeeReview.start(reviewStage); // Assumes EmployeeReview has a start method for GUI
            System.out.println("Employee Review interface opened.");
        } catch (Exception e) {
            System.out.println("Error launching Employee Review: " + e.getMessage());
        }
    }

    public void accessPayrollRecords() {
        PayrollManager payrollManager = new PayrollManager();

        // Open the Payroll Manager interface to access payroll records
        Stage payrollStage = new Stage();
        try {
            payrollManager.start(payrollStage); // Assumes PayrollManager has a start method for GUI
            System.out.println("Payroll Manager interface opened for viewing payroll records.");
        } catch (Exception e) {
            System.out.println("Error launching Payroll Manager: " + e.getMessage());
        }
    }
}
