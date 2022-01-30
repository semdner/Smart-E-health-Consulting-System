package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.Doctor;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AppointmentController {

    @FXML
    Label dateLabel;

    @FXML
    Label timeLabel;

    @FXML
    Label doctorLabel;

    @FXML
    Button showMoreButton;

    Appointment loadedAppointment;

    public void setData(Appointment appointment) throws SQLException {
        loadedAppointment = appointment; // used later for loading for passing to the next scene;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLabel.setText(loadedAppointment.getDate().format(dateTimeFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeLabel.setText(loadedAppointment.getTime().format(timeFormatter));
        Doctor doctor = loadedAppointment.getDoctor();
        doctorLabel.setText(doctor.getLastName());
    }

    public void handleShowMoreButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "appointment/appointmentMade-view.fxml", "Appointment");
    }
}
