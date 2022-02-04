package com.ehealthsystem.doctor;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.appointment.ScheduleLoader;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.mail.SendEmail;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.ReminderTime;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.activation.UnsupportedDataTypeException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

public class FoundDoctorFullController extends ScheduleLoader {

    @FXML
    Label doctorLabel;

    @FXML
    Label addressLabel;

    @FXML
    Label specializationsLabel;

    @FXML
    Label dateLabel;

    @FXML
    DatePicker datePicker;

    @FXML
    ComboBox reminderComboBox;

    @FXML
    Button makeAppointmentButton;

    @FXML
    private WebView mapWebView = new WebView();

    private DoctorDistance doctor = new DoctorDistance();
    private String userGeoData;
    private String doctorGeoData;

    /**
     * sets reminder times, loads the map, doctor data and the doctors schedule
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     * @throws SQLException
     */
    public void start() throws IOException, InterruptedException, ApiException, SQLException {
        reminderComboBox.getItems().setAll(ReminderTime.values());
        reminderComboBox.setValue(ReminderTime.THREE_DAYS); //no need for validation if default value is valid
        loadGMap();
        loadDoctorData();

        datePicker.setValue(LocalDate.now());
        Session.appointment.setDate(datePicker.getValue());
        while (!loadSchedule()) { //Display first day with free times.
            datePicker.setValue(datePicker.getValue().plusDays(1));
        }
    }


    /**
     * set the doctor selected in the scene before
     * @param doctor
     */
    public void setDoctor(DoctorDistance doctor) {
        this.doctor = doctor;
    }

    /**
     * set the user's geodata for map
     * @param userGeoData
     */
    public void setUserGeoData(String userGeoData) {
        // parse address if it contains 'ß'
        String uGD = userGeoData.replaceAll("ß", "ss");
        this.userGeoData = uGD;
    }

    /**
     * set the doctor geodata for map
     * @param doctorGeoData
     */
    public void setDoctorGeoData(String doctorGeoData) {
        // parse address if it contains 'ß'
        String dGD = doctorGeoData.replaceAll("ß", "ss");
        this.doctorGeoData = dGD;
    }

    /**
     * DEPRECATED: replaced by interactive route display: loadGMap()
     * Load a static map image using OpenStreetMap data into the webview
     */
    private void loadOsmMap() throws IOException, InterruptedException, ApiException {
        LatLng user = Session.getUserGeo().geometry.location;
        LatLng[] bounds = getBoundsForImage(user, doctor.getDoctor().getLocation());
        WebEngine e = mapWebView.getEngine();
        e.load("https://maps.geoapify.com/v1/staticmap?style=osm-carto&width=" +
                //mapWebView: can't get the current size (width, height) because too complicated
                mapWebView.getPrefWidth() +
                "&height=" +
                mapWebView.getPrefHeight() +
                "&area=rect:" +
                bounds[0].lng + "," + bounds[0].lat +
                "," +
                bounds[1].lng + "," + bounds[1].lat +
                "&marker=" +
                "lonlat:" +
                user.lng +
                "," +
                user.lat +
                ";type:material;color:red;icon:home;icontype:awesome" +
                "|" +
                "lonlat:" +
                doctor.getDoctor().getLocation().lng +
                "," +
                doctor.getDoctor().getLocation().lat +
                ";type:material;color:red;icon:plus-square;icontype:awesome" +
                "&apiKey=00cdf1a2d7324db996a8706d774a9469");
    }

    /**
     * Algorithm to determine bounds to display a map image with two locations as markers
     * Adds spacing so that the map markers are not directly at the edge but within the displayed area/frame
     * Adds spacing on top of the map because marker is represented by a bubble above the actual location
     * @param l1 coordinates 1
     * @param l2 coordinates 2
     * @return
     */
    private LatLng[] getBoundsForImage(LatLng l1, LatLng l2) {
        //lat = y, lon = x
        LatLng topLeftBound = new LatLng(Math.max(l1.lat, l2.lat), Math.min(l1.lng, l2.lng));
        LatLng bottomRightBound = new LatLng(Math.min(l1.lat, l2.lat), Math.max(l1.lng, l2.lng));

        double latSpacing = Math.abs(topLeftBound.lat - bottomRightBound.lat)/10/2;
        double lngSpacing = Math.abs(topLeftBound.lng - bottomRightBound.lng)/10/2;
        topLeftBound.lat += 3*latSpacing; //factor in more spacing so that the top marker can be seen
        topLeftBound.lng -= lngSpacing;
        bottomRightBound.lat -= latSpacing;
        bottomRightBound.lng += lngSpacing;
        return new LatLng[]{topLeftBound, bottomRightBound};
    }

    /**
     * load the doctor data into the Labels
     */
    private void loadDoctorData() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText("Dr. " + doctor.getDoctor().getFirstName() + " " + doctor.getDoctor().getLastName());
        doctorGeoData = doctor.getDoctor().getFormattedAddressWithPlaceName();
        addressLabel.setText(doctorGeoData);
        specializationsLabel.setText(StringEnumerator.enumerate(doctor.getDoctor().getSpecializations()));
    }

    /**
     * dynamically load the doctors schedule
     * @throws SQLException
     */
    private boolean loadSchedule() throws SQLException, UnsupportedDataTypeException {
        return loadSchedule(datePicker.getValue(), doctor.getDoctor(), dateLabel, makeAppointmentButton);
    }

    /**
     * method to load to previous scene
     * @param event
     * @throws IOException
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "appointment/appointmentFound-view.fxml", "Make appointment");
    }

    /**
     * method to handle the event when trying to make an appointment
     * @param event
     * @throws IOException
     */
    public void handleMakeAppointmentButton(ActionEvent event) throws IOException, SQLException, MessagingException, InterruptedException, ApiException {
        if(selectedTime != null) {
            errorLabel.setVisible(false);
            ReminderTime reminderChoice = ((ReminderTime) reminderComboBox.getValue());
            Session.appointment.setTime(selectedTime);
            Appointment appointment = new Appointment(
                    true,
                    0,
                    Session.user.getUsername(),
                    doctor.getDoctor().getId(),
                    Session.appointment.getHealthProblem(),
                    Session.appointment.getDate(),
                    Session.appointment.getTime(),
                    reminderChoice.getMinutes(), //no need for validation of combobox selection since default value is valid
                    0
            );
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Session.datePatternUI);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Session.timePatternUI);
            String date = appointment.getDate().format(dateFormatter);
            String time = appointment.getTime().format(timeFormatter);
            SendEmail.sendMail(
                    Session.user.getMail(),
                    "Appointment confirmation: Dr. %s @ %s %s".formatted(
                            appointment.getDoctor().getLastName(),
                            date,
                            time
                    ),
                    "You successfully made an appointment with Dr. %s on %s at %s. Their address is %s.".formatted(appointment.getDoctor().getLastName(), time, date, doctorGeoData)
                    + (reminderChoice.getMinutes() != 0 ? " A reminder email will be sent to you %s ahead.".formatted(reminderChoice.getLabel()) : "")
            );
            SceneSwitch.switchTo(event, "primary/primary-view.fxml", "E-Health System");
        } else {
            errorLabel.setText("no time selected");
            errorLabel.setVisible(true);
        }
    }

    /**
     * loads the map into the scene via webengine and webview
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public void loadGMap() throws IOException, InterruptedException, ApiException {
        WebEngine engine = mapWebView.getEngine();

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> { //https://stackoverflow.com/a/30178571
            if (newState == Worker.State.SUCCEEDED) {
                // new page has loaded, process:
                LatLng docLoc = doctor.getDoctor().getLocation();
                engine.executeScript("setData(%s,%s,\"%s\",\"%s\",15)".formatted(docLoc.lat, docLoc.lng, userGeoData, doctorGeoData));
            }
        });

        engine.load(getClass().getResource("/com/ehealthsystem/map/map.html").toString());
    }

    public void handleDateChoice(ActionEvent event) throws SQLException, UnsupportedDataTypeException {
        if (datePicker.getValue().isBefore(LocalDate.now())) {
            datePicker.setValue(LocalDate.now());
            return;
        }
        Session.appointment.setDate(datePicker.getValue());
        loadSchedule();
    }
}
