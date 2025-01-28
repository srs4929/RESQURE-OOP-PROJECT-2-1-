module com.example.disaster {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires jdk.compiler;
    requires java.sql;

    opens com.example.disaster to javafx.fxml;
    exports com.example.disaster;
}