package com.example.emsapp.Roles;

import com.example.emsapp.EmployeePTO;
import com.example.emsapp.PayrollManager;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class EmployeeRole {

    public void submitPTORequest(double balance) {
        EmployeePTO employeePTO = new EmployeePTO(balance);

        // Assuming EmployeePTO has methods to capture and retrieve PTO request data
        String ptoType = employeePTO.getPTOType(); // e.g., Vacation, Sick Leave, etc.
        LocalDate startDate = employeePTO.getStartDate();
        LocalDate endDate = employeePTO.getEndDate();
        int ptoHours = employeePTO.calculatePtoHours(startDate, endDate);

        // Check if enough PTO balance is available
        if (ptoHours > balance) {
            System.out.println("Insufficient PTO balance.");
            return;
        }

        // Construct PTO request entry
        String ptoRequest = "PTO Request - Type: " + ptoType + ", From: " + startDate + " To: " + endDate + ", Hours: " + ptoHours;

        // Write request to file (assuming EmployeePTORequests.txt as storage)
        try (FileWriter writer = new FileWriter("EmployeePTORequests.txt", true)) {
            writer.write(ptoRequest + "\n");
            System.out.println("PTO request submitted successfully.");
        } catch (IOException e) {
            System.out.println("Error submitting PTO request: " + e.getMessage());
        }
    }

    public void viewPayroll() {
        // Assuming payroll records are stored in a text file named EmployeePayroll.txt
        PayrollManager payrollManager = new PayrollManager();

    }
}
