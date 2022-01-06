module Smart.E.health.Consulting.System {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires com.google.common;
    requires java.mail;

    opens com.ehealthsystem.appointment to javafx.fxml;
    opens com.ehealthsystem.login to javafx.fxml;
    opens com.ehealthsystem.primary to javafx.fxml;
    opens com.ehealthsystem.registration to javafx.fxml;
    opens com.ehealthsystem.appointment.edit to javafx.fxml;

    exports com.ehealthsystem;
}