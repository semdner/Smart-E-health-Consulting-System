package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AppointmentMadeController {

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

    Appointment loadedAppointment;
    String doctorGeoData;

    public void start(Appointment appointment) throws SQLException, IOException, InterruptedException, ApiException {
        loadedAppointment = appointment;
        loadAppointment();
        loadDoctor();
        loadGMap();
    }

    private void loadAppointment() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLabel.setText(loadedAppointment.getDate().format(dateTimeFormatter));
        System.out.println(dateLabel.getText() + " " + loadedAppointment.getDate());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeLabel.setText(loadedAppointment.getTime().format(timeFormatter));
        healthProblemLabel.setText(loadedAppointment.getHealthProblemDescription());
    }

    private void loadDoctor() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
        doctorGeoData = loadDoctorGeoData();
        addressLabel.setText(doctorGeoData);
        specializationsLabel.setText(StringEnumerator.enumerate(loadedAppointment.getDoctor().getSpecializations()));
    }

    private String loadDoctorGeoData() throws SQLException, IOException, InterruptedException, ApiException {
        GeocodingResult doctorGeoData = GeoCoder.geocode(loadedAppointment.getDoctor().getStreet() + ", " + loadedAppointment.getDoctor().getNumber(), loadedAppointment.getDoctor().getZip());
        return doctorGeoData.formattedAddress.toString();
    }

    private void loadGMap() {
        WebEngine engine = mapWebView.getEngine();

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> { //https://stackoverflow.com/a/30178571
            if (newState == Worker.State.SUCCEEDED) {
                // new page has loaded, process:
                try {
                    String userFormattedAddress = GeoCoder.geocode(Session.user.getStreet() + ", " + Session.user.getHouseNo(), Session.user.getZipCode()).formattedAddress;
                    engine.executeScript("setData(%s,%s,\"%s\",\"%s\",15)".formatted(loadedAppointment.getDoctor().getLocation().lat, loadedAppointment.getDoctor().getLocation().lng, userFormattedAddress, doctorGeoData));
                } catch (SQLException | InterruptedException | IOException | ApiException e) {
                    e.printStackTrace();
                }
            }
        });

        engine.load(getClass().getResource("/com/ehealthsystem/map/map.html").toString());
    }

    public void handleShiftAppointmentButton() {

    }

    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "primary/primary-view.fxml", "E-Health System");
    }
}
