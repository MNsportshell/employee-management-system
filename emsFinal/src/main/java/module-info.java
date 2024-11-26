module org.example.emsfinal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires json.simple;
    requires java.desktop;

    opens org.example.emsfinal to javafx.fxml;
    exports org.example.emsfinal;
}