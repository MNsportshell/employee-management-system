module com.example.appbasic {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.appbasic to javafx.fxml;
    exports com.example.appbasic;
}