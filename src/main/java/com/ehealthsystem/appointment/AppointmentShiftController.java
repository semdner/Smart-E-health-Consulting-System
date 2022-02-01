package com.ehealthsystem.appointment;

import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AppointmentShiftController {

    @FXML
    Label doctorLabel;

    @FXML
    Label addressLabel;

    @FXML
    Label specializationsLabel;

    @FXML
    Label dateLabel;

    @FXML
    Label timeLabel;

    @FXML
    Label healthProblemLabel;

    @FXML
    DatePicker datePicker;

    @FXML
    Label selectedDateLabel;

    @FXML
    GridPane scheduleGridPane;


    static Appointment selectedAppointment;

    public void start(Appointment appointment) throws SQLException, IOException, InterruptedException, ApiException {
        selectedAppointment = appointment;
        setCurrentAppointment();
    }

    private void setCurrentAppointment() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText(selectedAppointment.getDoctor().getLastName());
        addressLabel.setText(GeoCoder.geocode(selectedAppointment.getDoctor().getFormattedAddress()).formattedAddress);
        specializationsLabel.setText(StringEnumerator.enumerate(selectedAppointment.getDoctor().getSpecializations()));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLabel.setText(selectedAppointment.getDate().format(dateFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeLabel.setText(selectedAppointment.getTime().format(timeFormatter));
    }

}
