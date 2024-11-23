package org.example.emsfinal;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.emsfinal.HomePages.EmployeeHome;
import org.example.emsfinal.HomePages.HRManagerHome;
import org.example.emsfinal.HomePages.ManagerHome;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;

public class SignInPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Company Sign-In");

        // Create UI elements
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Sign In");
        Label messageLabel = new Label();

        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(messageLabel, 1, 3);

        // Button action
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            String role = validateCredentials(username, password);
            if (role != null) {
                redirectToRolePage(primaryStage, role);
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        // Set scene and stage
        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Validates the username and password against the credentials.json file
     * Returns the user's role if credentials are valid, otherwise null.
     */
    private String validateCredentials(String username, String password) {
        JSONParser parser = new JSONParser();
        try (InputStream inputStream = getClass().getResourceAsStream("/credentials.json");
             InputStreamReader reader = new InputStreamReader(inputStream)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray employees = (JSONArray) jsonObject.get("employees");

            for (Object obj : employees) {
                JSONObject employee = (JSONObject) obj;
                String storedUsername = (String) employee.get("username");
                String storedPassword = (String) employee.get("password");
                String role = (String) employee.get("role");

                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    return role;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
        return null;
    }

    /**
     * Redirects the user to a specific page based on their role
     */
    private void redirectToRolePage(Stage stage, String role) {
        switch (role) {
            case "Employee":
                new EmployeeHome().start(stage);
                break;
            case "Manager":
                new ManagerHome().start(stage);
                break;
            case "HRManager":
                new HRManagerHome().start(stage);
                break;
            default:
                Label roleLabel = new Label("Role not recognized.");
                Scene scene = new Scene(roleLabel, 400, 200);
                stage.setScene(scene);
                stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
