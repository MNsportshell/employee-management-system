module com.example.emsapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javax.mail.api;

    opens com.example.emsapp to javafx.fxml;
    exports com.example.emsapp;
}