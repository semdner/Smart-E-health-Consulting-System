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

/**
 * Class for dynamically adding the doctor information when they are in the set search distance
 */
public class AppointmentController {

    /**
     * attributes with a fx:id of the view
     */
    @FXML
    Label dateLabel;

    @FXML
    Label timeLabel;

    @FXML
    Label doctorLabel;

    @FXML
    Button showMoreButton;

    Appointment loadedAppointment;

    /**
     * first method that is called when loading the scene.
     * Used to set the doctors date
     * @param appointment the appointment that currently made
     * @throws SQLException
     */
    public void setData(Appointment appointment) throws SQLException {
        loadedAppointment = appointment; // used later for passing to the next scene;
        dateLabel.setText(loadedAppointment.getDate().format(Session.dateFormatter));
        timeLabel.setText(loadedAppointment.getTime().format(Session.timeFormatter));
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
    }

    /**
     * Event Handler to switch to the doctor scene to make an appointment
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     * @throws ApiException
     */
    public void handleShowMoreButton(ActionEvent event) throws IOException, SQLException, InterruptedException, ApiException {
        SceneSwitch.loadAppointmentMadeView(event, loadedAppointment);
    }
}
