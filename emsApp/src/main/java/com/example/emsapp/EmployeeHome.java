package com.example.emsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmployeeHome extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Employee Portal");

            // Load the FXML file with the absolute path within resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/emsapp/EmployeeHome.fxml"));
            Parent root = loader.load();

            // Set the scene with the loaded FXML root and apply styling
            Scene scene = new Scene(root, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
