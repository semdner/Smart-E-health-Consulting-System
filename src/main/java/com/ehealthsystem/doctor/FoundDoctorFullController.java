package com.ehealthsystem.doctor;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

import java.io.IOException;
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
    private WebView mapWebView;

    private DoctorDistance doctor = new DoctorDistance();
    private String userGeoData;
    private String doctorGeoData;
    private ArrayList<Label> timeLabelList = new ArrayList<>();
    private LocalTime selectedTime;

    public void start() throws IOException, InterruptedException, ApiException, SQLException {
        loadPage(userGeoData, doctorGeoData);
        loadDoctorData();
        loadSchedule();
    }


    public void setDoctor(DoctorDistance doctor) {
        this.doctor = doctor;
    }

    public void setUserGeoData(String userGeoData) {
        this.userGeoData = userGeoData;
    }

    public void setDoctorGeoData(String doctorGeoData) {
        this.doctorGeoData = doctorGeoData;
    }

    private void loadPage(String userGeoData, String doctorGeoData) {

    }

    private void loadDoctorData() {
        doctorLabel.setText("Dr. " + doctor.getDoctor().getFirstName() + " " + doctor.getDoctor().getLastName());
        addressLabel.setText(doctorGeoData);
    }

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

            if(i % 2 == 0 && i != 0) {
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

    private void setStyle(Label time) {
        time.setStyle("-fx-font-size: 15px;");
        time.setTextFill(Color.web("#999999"));
    }

    private void setStyle(Label time, Button timeButton) {
        time.setStyle("-fx-font-size: 15px;");
        timeButton.setStyle("-fx-opacity: 0%");
        timeButton.setPrefWidth(100);
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "appointment/appointmentFound-view.fxml", "Make appointment");;
    }

    public void handleSelectButton(ActionEvent event) throws IOException {
        if(selectedTime != null) {
            Session.appointment.setTime(selectedTime);
            SceneSwitch.switchTo(event, "appointment/appointmentFound-view.fxml", "Make appointment");
        } else {
            errorLabel.setVisible(true);
        }
    }
}
