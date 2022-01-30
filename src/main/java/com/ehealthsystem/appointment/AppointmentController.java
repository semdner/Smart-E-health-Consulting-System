package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

    public void setData(Appointment appointment) throws SQLException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLabel.setText(appointment.getDate().format(dateTimeFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeLabel.setText(appointment.getTime().format(timeFormatter));
        doctorLabel.setText(Database.loadDoctorFromId(appointment.getDoctor()));
    }

    public void handleShowMoreButton()  {

    }
}
