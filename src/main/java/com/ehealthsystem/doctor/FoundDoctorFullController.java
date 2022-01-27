package com.ehealthsystem.doctor;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FoundDoctorFullController {

    @FXML
    Label doctorLabel;

    @FXML
    Label addressLabel;

    @FXML
    Label specializationLabel;

    @FXML
    Label dateLabel;

    @FXML
    GridPane scheduleGridPane;

    @FXML
    Label errorLabel;

    @FXML
    ComboBox reminderComboBox;

    @FXML
    Button makeAppointmentButton;

    @FXML
    private WebView mapWebView = new WebView();

    private DoctorDistance doctor = new DoctorDistance();
    private String userGeoData;
    private String doctorGeoData;
    private ArrayList<Label> timeLabelList = new ArrayList<>();
    private LocalTime selectedTime;

    /**
     * sets reminder times, loads the map, doctor data and the doctors schedule
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     * @throws SQLException
     */
    public void start() throws IOException, InterruptedException, ApiException, SQLException {
        String[] choices = {"10 minutes", "1 hour", "3 days", "1 week"};
        reminderComboBox.getItems().addAll(choices);
        loadGMap();
        loadDoctorData();
        loadSchedule();
    }


    /**
     * set the doctor selected in the scene before
     * @param doctor
     */
    public void setDoctor(DoctorDistance doctor) {
        this.doctor = doctor;
    }

    /**
     * set the users geodata for map
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
     * Load a static map image using OpenStreetMap data into the webview
     */
    private void loadOsmMap() {
        LatLng user = Session.userGeo.geometry.location;
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
    private void loadDoctorData() {
        doctorLabel.setText("Dr. " + doctor.getDoctor().getFirstName() + " " + doctor.getDoctor().getLastName());
        addressLabel.setText(doctorGeoData);
    }

    /**
     * dynamically load the doctors schedule
     * @throws SQLException
     */
    private void loadSchedule() throws SQLException {
        ArrayList<DoctorTimeSlot> doctorTimeSlotList = Database.loadDoctorAppointments(doctor.getDoctor(), Session.appointment.getDate());
        dateLabel.setText(Session.appointment.getDate().toString());
        int column = 0;
        int row = 1;
        for(int i = 0; i< doctorTimeSlotList.size(); i++, column++) {
            //Prepare UI
            Label time = new Label(doctorTimeSlotList.get(i).getTime().toString());
            Button timeButton = new Button();
            if(doctorTimeSlotList.get(i).getFree()) {
                handleTimeButton(time, timeButton);
                setStyle(time, timeButton);
            } else {
                setStyle(time);
            }

            if(i % 5 == 0 && i != 0) {
                //Go to next row
                column = 0;
                row++;
            }

            //Add to UI
            scheduleGridPane.add(time, column, row);
            if(doctorTimeSlotList.get(i).getFree()) {
                scheduleGridPane.add(timeButton, column, row);
            }
        }
    }

    private void handleTimeButton(Label time, Button timeButton) {
        timeLabelList.add(time);
        String timeStr = time.getText();
        // dynamically add the eventhandler for the buttons
        timeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (int i = 0; i<timeLabelList.size(); i++) {
                    timeLabelList.get(i).setTextFill(Color.web("#000000"));
                }
                time.setTextFill(Color.web("#FF0000"));
                DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("H:mm");
                selectedTime = LocalTime.parse(timeStr, TimeFormatter);
            }
        });
    }

    /**
     * set the style for the not free appointment labels
     * @param time
     */
    private void setStyle(Label time) {
        time.setStyle("-fx-font-size: 15px;");
        time.setTextFill(Color.web("#999999"));
    }

    /**
     * set style for the free appointment labels and the buttons
     * @param time
     * @param timeButton
     */
    private void setStyle(Label time, Button timeButton) {
        time.setStyle("-fx-font-size: 15px;");
        timeButton.setStyle("-fx-opacity: 0%");
        timeButton.setPrefWidth(100);
    }

    /**
     * method to load to previous scene
     * @param event
     * @throws IOException
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "appointment/appointmentFound-view.fxml", "Make appointment");;
    }

    /**
     * method to handle the event when trying to make an appointment
     * @param event
     * @throws IOException
     */
    public void handleMakeAppointmentButton(ActionEvent event) throws IOException {
        if(selectedTime != null) {
            Session.appointment.setTime(selectedTime);
            SceneSwitch.switchTo(event, "appointment/appointmentFound-view.fxml", "Make appointment");
        } else {
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
}
