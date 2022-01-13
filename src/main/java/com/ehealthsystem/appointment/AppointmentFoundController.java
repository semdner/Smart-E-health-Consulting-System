package com.ehealthsystem.appointment;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentFoundController implements Initializable {

    @FXML
    ComboBox reminderComboBox;

    static Appointment newAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] choices = {"10 minutes", "1 hour", "3 days", "1 week"};
        reminderComboBox.getItems().addAll(choices);
    }

    public static void setAppointment(Appointment passedAppointment) {
        newAppointment = passedAppointment;
    }
}