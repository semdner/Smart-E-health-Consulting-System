module Smart.E.health.Consulting.System {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires com.google.common;
    requires google.maps.services;
    requires com.google.gson;
    requires java.mail;
    requires org.json;
    requires activation;
    requires layout;
    requires kernel;
    requires io;
    requires java.desktop;
    requires javafx.base;

    opens com.ehealthsystem.appointment to javafx.fxml;
    opens com.ehealthsystem.login to javafx.fxml;
    opens com.ehealthsystem.primary to javafx.fxml;
    opens com.ehealthsystem.registration to javafx.fxml;
    opens com.ehealthsystem.doctor to javafx.fxml;
    opens com.ehealthsystem.admin to javafx.fxml, javafx.base;
    opens com.ehealthsystem.healthInformation to javafx.fxml, javafx.base;

    exports com.ehealthsystem;
}
