package com.example.appbasic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MenuSample extends Application {

    // launch the application
    public void start(Stage s)
    {
        // set title for the stage
        s.setTitle("creating MenuBar");

        // create a menu
        Menu m = new Menu("Menu");

        // create menuitems
        MenuItem m1 = new MenuItem("View PTO");
        MenuItem m2 = new MenuItem("View Schedule");
        MenuItem m3 = new MenuItem("View Paystubs");

        // add menu items to menu
        m.getItems().add(m1);
        m.getItems().add(m2);
        m.getItems().add(m3);

        // label to display events
        Label l = new Label("\t\t\t\t"
                + "no menu item selected");

        // create events for menu items
        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                l.setText("\t\t\t\t" + ((MenuItem)e.getSource()).getText() +
                        " selected");
            }
        };

        // add event
        m1.setOnAction(event);
        m2.setOnAction(event);
        m3.setOnAction(event);

        // create a menubar
        MenuBar mb = new MenuBar();

        // add menu to menubar
        mb.getMenus().add(m);

        // create a VBox
        VBox vb = new VBox(mb, l);

        // create a scene
        Scene sc = new Scene(vb, 500, 300);

        // set the scene
        s.setScene(sc);

        s.show();
    }

    public static void main(String args[])
    {
        // launch the application
        launch(args);
    }
}
