module com.example.flightmanagementproject {
    requires javafx.controls;
    requires javafx.fxml;
            
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.jfoenix;
    requires itextpdf;
    requires java.desktop;

    opens com.example.flightmanagementproject to javafx.fxml;
    opens com.example.flightmanagementproject.controllers to javafx.fxml;
    opens com.example.flightmanagementproject.controllers.admin to javafx.fxml;
    opens com.example.flightmanagementproject.controllers.user;

    exports com.example.flightmanagementproject.controllers;
    exports com.example.flightmanagementproject.controllers.admin;
    exports com.example.flightmanagementproject.controllers.user;

    exports com.example.flightmanagementproject.models;
    exports com.example.flightmanagementproject.auth;
    opens com.example.flightmanagementproject.auth to javafx.fxml;
}