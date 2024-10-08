package com.example.appbasic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EmployeeReview extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    final Button button = new Button ("Send");
    final Label notification = new Label ();
    final TextField subject = new TextField("");
    final TextArea text = new TextArea ("");

    String address = " ";

    @Override public void start(Stage stage) {
        stage.setTitle("ComboBoxSample");
        Scene scene = new Scene(new Group(), 500, 270);

        final ComboBox emailComboBox = new ComboBox();
        emailComboBox.getItems().addAll(
                "jacob.smith@example.com",
                "isabella.johnson@example.com",
                "ethan.williams@example.com",
                "emma.jones@example.com",
                "michael.brown@example.com"
        );

        final ComboBox priorityComboBox = new ComboBox();
        priorityComboBox.getItems().addAll(
                "1. Exceptional",
                "2. Very Good",
                "3. Acceptable",
                "4. Needs Improvement",
                "5. Unacceptable"
        );

        priorityComboBox.setValue("Select...");

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Select Employee: "), 0, 0);
        grid.add(emailComboBox, 1, 0);
        grid.add(new Label("Rating: "), 2, 0);
        grid.add(priorityComboBox, 3, 0);
        grid.add(new Label("Notes: "), 0, 1);
        grid.add(text, 0, 2, 4, 1);
        grid.add(button, 0, 3);
        grid.add (notification, 1, 3, 3, 1);

        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);
        stage.setScene(scene);
        stage.show();
    }
}
