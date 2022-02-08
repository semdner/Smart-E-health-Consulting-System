package com.ehealthsystem.appointment;

import com.ehealthsystem.mail.SendEmail;
import com.ehealthsystem.reminder.ReminderScheduler;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for manager an ready made appointment
 */
public class AppointmentMadeController {

    /**
     * all attributes with a fx:id in the view
     */
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
    WebView mapWebView;

    @FXML
    Button editAppointmentButton;

    @FXML
    Button cancelAppointmentButton;

    /**
     * contains the laodedAppointment
     */
    Appointment loadedAppointment;

    /**
     * contains the doctor geodata for loading the map
     */
    String doctorGeoData;

    /**
     * first method called when scene is switched
     * @param appointment the selected Appointment is passed
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public void start(Appointment appointment) throws SQLException, IOException, InterruptedException, ApiException {
        loadedAppointment = appointment;
        loadAppointment();
        if (loadedAppointment.isInThePast()) { //past appointment
            editAppointmentButton.setDisable(true);
            cancelAppointmentButton.setDisable(true);
        }
        loadDoctor();
        loadGMap();
    }

    /**
     * load the appointment information
     */
    private void loadAppointment() {
        dateLabel.setText(loadedAppointment.getDate().format(Session.dateFormatter));
        System.out.println(dateLabel.getText() + " " + loadedAppointment.getDate());
        timeLabel.setText(loadedAppointment.getTime().format(Session.timeFormatter));
        healthProblemLabel.setText(loadedAppointment.getHealthProblemDescription());
    }

    /**
     * load the doctors informaiton
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    private void loadDoctor() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
        doctorGeoData = loadedAppointment.getDoctor().getFormattedAddressWithPlaceName();
        addressLabel.setText(doctorGeoData);
        specializationsLabel.setText(StringEnumerator.enumerate(loadedAppointment.getDoctor().getSpecializations()));
        healthProblemLabel.setText(loadedAppointment.getHealthProblemDescription());
    }

    /**
     * load google maps into with webengine
     */
    private void loadGMap() {
        WebEngine engine = mapWebView.getEngine();

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> { //https://stackoverflow.com/a/30178571
            if (newState == Worker.State.SUCCEEDED) {
                // new page has loaded, process:
                try {
                    engine.executeScript("setData(%s,%s,\"%s\",\"%s\",15)".formatted(loadedAppointment.getDoctor().getLocation().lat, loadedAppointment.getDoctor().getLocation().lng, Session.user.getFormattedAddress(), doctorGeoData));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        engine.load(getClass().getResource("/com/ehealthsystem/map/map.html").toString());
    }

    /**
     * Switch to edit appointment scene when button is pressed
     * @param event the event that is triggered the method call
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     * @throws ApiException
     */
    public void handleEditAppointmentButton(ActionEvent event) throws IOException, SQLException, InterruptedException, ApiException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ehealthsystem/appointment/appointmentShift-view.fxml"));
        Parent root = loader.load();

        AppointmentShiftController controller = loader.getController();
        controller.start(loadedAppointment);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root);
        stage.setTitle("Shift Appointment");
        stage.setScene(primaryScene);
    }

    /**
     * Cancel the selected appointment
     * @param event the event that is triggered the method call
     * @throws SQLException
     * @throws IOException
     * @throws MessagingException
     */
    public void handleCancelButton(ActionEvent event) throws SQLException, IOException, MessagingException {
        loadedAppointment.delete();

        if (loadedAppointment.getMinutesBeforeReminder() != 0) //has a reminder
            ReminderScheduler.deleteReminder(loadedAppointment);

        SendEmail.sendMail(
                Session.user.getMail(),
                "Confirmation of appointment cancellation: Doctor: %s @ %s %s".formatted(
                        loadedAppointment.getDoctor().getLastName(),
                        loadedAppointment.getDate().format(Session.dateFormatter),
                        loadedAppointment.getTime().format(Session.timeFormatter)
                ),
                "This is to confirm that your appointment with the doctor %s was cancelled.".formatted(loadedAppointment.getDoctor().getLastName()),false
        );
        SendEmail.sendMail(
                Session.user.getMail(),
                "Your patient has cancelled an appointment",
                "This is to inform you that your appointment with the patient %s %s on %s at %s has been cancelled".formatted(
                        Session.user.getFirstName(),
                        Session.user.getLastName(),
                        loadedAppointment.getDate().format(Session.dateFormatter),
                        loadedAppointment.getTime().format(Session.timeFormatter)
                ), false
        );

        SceneSwitch.switchTo(event,"primary/primary-view.fxml", "E-Health-System");
    }

    /**
     * return to primary view without any changes made
     * @param event the event that is triggered the method call
     * @throws IOException
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "primary/primary-view.fxml", "E-Health System");
    }
}
