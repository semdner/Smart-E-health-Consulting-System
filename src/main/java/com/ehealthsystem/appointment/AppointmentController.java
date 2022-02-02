package com.ehealthsystem.appointment;

import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.google.maps.errors.ApiException;
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
        loadedAppointment = appointment; // used later for passing to the next scene;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Session.datePatternUI);
        dateLabel.setText(loadedAppointment.getDate().format(dateTimeFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Session.timePatternUI);
        timeLabel.setText(loadedAppointment.getTime().format(timeFormatter));
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
    }

    public void handleShowMoreButton(ActionEvent event) throws IOException, SQLException, InterruptedException, ApiException {
        SceneSwitch.loadAppointmentMadeView(event, loadedAppointment);
    }
}
