package com.example.emsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class EmployeeReview extends Application {
    @Override
    public void start(Stage primaryStage) {

        try {
            primaryStage.setTitle("Employee Portal");

            // Debug check for FXML path
            URL fxmlUrl = getClass().getResource("/com/example/emsapp/EmployeeReview.fxml");
            System.out.println("FXML URL: " + fxmlUrl); // Should not be null

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
